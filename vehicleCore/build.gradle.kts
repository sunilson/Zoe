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
    implementation(project(":core"))
    implementation(Dependencies.retrofitMoshiAdapter)
    implementation(Dependencies.retrofit)
    implementation(Dependencies.hilt)
    implementation(Dependencies.room)
    implementation(Dependencies.roomCoroutines)
    kapt(Dependencies.roomKapt)
    kapt(Dependencies.hiltKapt)
}