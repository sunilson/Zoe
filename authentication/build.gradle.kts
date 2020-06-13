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
    implementation(project(":presentationCore"))
    implementation(project(":core"))
    implementation(Dependencies.retrofit)

}