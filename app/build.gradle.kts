import at.sunilson.buildsrc.Dependencies

plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("android.extensions")
    kotlin("kapt")
}

apply {
    plugin("dagger.hilt.android.plugin")
}

android {
    defaultConfig {
        applicationId = "at.sunilson.zoe"
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
}

dependencies {
    implementation(fileTree("dir" to "libs", "include" to listOf("*.jar")))
    implementation(project(":authentication"))
    implementation(project(":presentationCore"))
    implementation(Dependencies.kotlinStdLib)
    implementation(Dependencies.coreKtx)
    implementation(Dependencies.appcompat)
    implementation(Dependencies.hilt)
    kapt(Dependencies.hiltKapt)
    testImplementation("junit:junit:4.13")
    androidTestImplementation("androidx.test.ext:junit:1.1.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.2.0")
}