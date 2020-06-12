import at.sunilson.buildsrc.Dependencies

plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("android.extensions")
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
    implementation(Dependencies.kotlinStdLib)
    implementation(Dependencies.coreKtx)
    implementation(Dependencies.appcompat)
    implementation(Dependencies.lifecycleViewModel)
    implementation(Dependencies.lifecycleRuntime)
    implementation(Dependencies.lifecycleLiveData)
    implementation(Dependencies.constraintlayout)
    implementation(Dependencies.material)
}