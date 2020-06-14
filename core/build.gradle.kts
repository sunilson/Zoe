import at.sunilson.buildsrc.Dependencies

plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("android.extensions")
    kotlin("kapt")
    id("dagger.hilt.android.plugin")
}
android {

    buildFeatures.viewBinding = true

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
}

dependencies {
    implementation(fileTree("dir" to "libs", "include" to listOf("*.jar")))
    api(Dependencies.kotlinStdLib)
    api(Dependencies.coreKtx)
    api(Dependencies.appcompat)
    api(Dependencies.coroutines)
    api(Dependencies.result)
    api(Dependencies.resultCoroutines)
    api(Dependencies.kotlinStdLib)
    api(Dependencies.timber)
    api(Dependencies.okhttp)
    implementation(Dependencies.hilt)
    debugImplementation(Dependencies.chucker)
    kapt(Dependencies.hiltKapt)
}