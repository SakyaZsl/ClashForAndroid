plugins {
    kotlin("android")
    id("com.android.library")
}

dependencies {
    compileOnly(project(":hideapi"))

    implementation(libs.kotlin.coroutine)
    implementation(libs.androidx.core)
    implementation(libs.ok.http)
    implementation(libs.gson)
    implementation(libs.retrofit)
}
