package com.github.kr328.clash

import android.util.Log
import android.util.TimeUtils
import androidx.activity.result.contract.ActivityResultContracts
import com.github.kr328.clash.common.util.intent
import com.github.kr328.clash.common.util.ticker
import com.github.kr328.clash.core.model.FetchStatus
import com.github.kr328.clash.design.MainDesign
import com.github.kr328.clash.design.ui.ToastDuration
import com.github.kr328.clash.service.model.Profile
import com.github.kr328.clash.service.remote.IFetchObserver
import com.github.kr328.clash.store.TipsStore
import com.github.kr328.clash.util.startClashService
import com.github.kr328.clash.util.stopClashService
import com.github.kr328.clash.util.withClash
import com.github.kr328.clash.util.withProfile
import kotlinx.coroutines.*
import kotlinx.coroutines.selects.select
import java.util.concurrent.TimeUnit

class MainActivity : BaseActivity<MainDesign>() {
    companion object{
        const val  Url ="https://service-bs768u4r-1313153981.sh.apigw.tencentcs.com/api/v1/client/subscribe?token=e8b52814b0a8696ee47d4eb4090d2eed"
        const val  Url2 ="https://service-bs768u4r-1313153981.sh.apigw.tencentcs.com/api/v1/client/subscribe?token=e8b52814b0a8696ee47d4eb4090d2eed"
    }
    override suspend fun main() {
        val design = MainDesign(this)
        setContentDesign(design)
        design.initView()

        launch(Dispatchers.IO) {
            showUpdatedTips(design)
        }
//        test(design)

        design.fetch()

        val ticker = ticker(TimeUnit.SECONDS.toMillis(1))

        while (isActive) {
            select<Unit> {
                events.onReceive {
                    when (it) {
                        Event.ActivityStart,
                        Event.ServiceRecreated,
                        Event.ClashStop, Event.ClashStart,
                        Event.ProfileLoaded, Event.ProfileChanged -> design.fetch()
                        else -> Unit
                    }
                }
                design.requests.onReceive {
                    when (it) {
                        MainDesign.Request.ToggleStatus -> {
                            if (clashRunning)
                                stopClashService()
                            else
                                design.startClash()
                        }
                        MainDesign.Request.OpenProxy ->
                            startActivity(ProxyActivity::class.intent)
                        MainDesign.Request.OpenProfiles ->
                            startActivity(ProfilesActivity::class.intent)
                        MainDesign.Request.OpenProviders ->
                            startActivity(ProvidersActivity::class.intent)
                        MainDesign.Request.OpenLogs ->
                            startActivity(LogsActivity::class.intent)
                        MainDesign.Request.OpenSettings ->
                            startActivity(SettingsActivity::class.intent)
                        MainDesign.Request.OpenHelp ->
                            startActivity(HelpActivity::class.intent)
                        MainDesign.Request.OpenAbout ->
                            design.showAbout(queryAppVersionName())
                    }
                }
                if (clashRunning) {
                    ticker.onReceive {
                        design.fetchTraffic()
                    }
                }
            }
        }
    }

    private suspend fun test(design:MainDesign){
        withProfile {
           val uuid= create(Profile.Type.Url,"人工配置"+System.currentTimeMillis(),Url)
            queryByUUID(uuid)?.let {
                Log.e("zzzz", "test:$uuid " )
                commit(uuid) { status -> Log.e("zzzz", "updateStatus: $status") }
                setActive(it)
                design.startClash()
            }
        }
    }


    private suspend fun showUpdatedTips(design: MainDesign) {
        val tips = TipsStore(this)

        if (tips.primaryVersion != TipsStore.CURRENT_PRIMARY_VERSION) {
            tips.primaryVersion = TipsStore.CURRENT_PRIMARY_VERSION

            val pkg = packageManager.getPackageInfo(packageName, 0)

            if (pkg.firstInstallTime != pkg.lastUpdateTime) {
                design.showUpdatedTips()
            }
        }
    }

    private suspend fun MainDesign.fetch() {
        setClashRunning(clashRunning)

        val state = withClash {
            queryTunnelState()
        }
        val providers = withClash {
            queryProviders()
        }

        setMode(state.mode)
        setHasProviders(providers.isNotEmpty())

        withProfile {
            setProfileName(queryActive()?.name)
        }
    }

    private suspend fun MainDesign.fetchTraffic() {
        withClash {
            setForwarded(queryTrafficTotal())
        }
    }

    private suspend fun MainDesign.startClash() {
        val active = withProfile { queryActive() }

        if (active == null || !active.imported) {
            showToast(R.string.no_profile_selected, ToastDuration.Long) {
                setAction(R.string.profiles) {
                    startActivity(ProfilesActivity::class.intent)
                }
            }

            return
        }

        val vpnRequest = startClashService()

        try {
            if (vpnRequest != null) {
                val result = startActivityForResult(
                    ActivityResultContracts.StartActivityForResult(),
                    vpnRequest
                )

                if (result.resultCode == RESULT_OK)
                    startClashService()
            }
        } catch (e: Exception) {
            design?.showToast(R.string.unable_to_start_vpn, ToastDuration.Long)
        }
    }

    private suspend fun queryAppVersionName(): String {
        return withContext(Dispatchers.IO) {
            packageManager.getPackageInfo(packageName, 0).versionName
        }
    }
}