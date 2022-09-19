package com.github.kr328.clash.common.network.http.converter;

import android.util.Log;

import androidx.annotation.NonNull;

import com.github.kr328.clash.common.network.http.back.HttpError;
import com.github.kr328.clash.common.network.http.utils.center.BaseResponse;
import com.google.gson.Gson;
import com.google.gson.internal.$Gson$Types;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;

import okhttp3.ResponseBody;

/**
 * Create by Carson on 2021/12/23.
 * 功能描述：json解析相关
 */
final class GsonResponseBodyConverter<T> extends BaseGsonConverter<T> {
    private final Gson gson;

    GsonResponseBodyConverter(Gson gson, Type type) {
        super(type, $Gson$Types.getRawType(type));
        this.gson = gson;
    }

    @SuppressWarnings("unchecked")
    @Override
    public T convert(@NonNull ResponseBody value) throws IOException {
        String cacheStr = value.string();
        try {
            JSONObject jsonObject = new JSONObject(cacheStr);
            Log.e("zzzz", "convert json: "+cacheStr );
            int code;
            String msg;
            if (cacheStr.contains("errorCode")) {
                code = jsonObject.getInt("errorCode");
                msg = jsonObject.getString("errorMsg");
            } else {
                code = jsonObject.getInt("code");
                msg = jsonObject.getString("msg");
            }
            if (code == 0 || code == 200 || code == 1000) {
                if (BaseResponse.class == rawType) {
                    return (T) new BaseResponse(code, msg);
                }
                Object contentData;
                //这样判断能防止服务端忽略data字段导致jsonObject.get("data")方法奔溃
                //且能判断为null或JSONObject#NULL的情况
                if (jsonObject.isNull("data")) {
                    contentData = new Object();
                } else {
                    contentData = jsonObject.get("data");
                }
                //data 基础类型 如{"msg": "xxx","code": xxx,"data": xxx}
                T response = convertBaseType(contentData, rawType);
                if (response != null) {
                    return response;
                }
                response = gson.fromJson(contentData.toString(), type);
                if (response != null) {
                    //防止线上接口修改导致反序列化失败奔溃
                    return response;
                }
            } else {
                throw new HttpError(msg, code, cacheStr);
            }
            throw new HttpError("数据异常", code, cacheStr);
        } catch (JSONException e) {
            throw new HttpError("解析异常", -2, cacheStr);
        }
    }
}
