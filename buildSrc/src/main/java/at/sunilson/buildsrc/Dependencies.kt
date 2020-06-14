package at.sunilson.buildsrc

const val LIFECYCLE_VERSION = "2.2.0"
const val NAVIGATION_VERSION = "2.3.0-rc01"
const val RESULT_VERSION = "3.0.0"

object Dependencies {
    const val appcompat = "androidx.appcompat:appcompat:1.1.0"
    const val coreKtx = "androidx.core:core-ktx:1.3.0"
    const val kotlinStdLib = "org.jetbrains.kotlin:kotlin-stdlib:1.3.72"
    const val constraintlayout = "androidx.constraintlayout:constraintlayout:2.0.0-beta6"
    const val retrofit = "com.squareup.retrofit2:retrofit:2.9.0"
    const val hilt = "com.google.dagger:hilt-android:2.28-alpha"
    const val hiltKapt = "com.google.dagger:hilt-android-compiler:2.28-alpha"
    const val hiltKaptJetpack = "androidx.hilt:hilt-compiler:1.0.0-alpha01"
    const val hiltViewModel = "androidx.hilt:hilt-lifecycle-viewmodel:1.0.0-alpha01"
    const val lifecycleViewModel = "androidx.lifecycle:lifecycle-viewmodel-ktx:$LIFECYCLE_VERSION"
    const val lifecycleLiveData = "androidx.lifecycle:lifecycle-livedata-ktx:$LIFECYCLE_VERSION"
    const val lifecycleRuntime = "androidx.lifecycle:lifecycle-runtime-ktx:$LIFECYCLE_VERSION"
    const val lifecycleCommonJava8 = "androidx.lifecycle:lifecycle-common-java8:$LIFECYCLE_VERSION"
    const val navigation = "androidx.navigation:navigation-fragment-ktx:$NAVIGATION_VERSION"
    const val navigationUi = "androidx.navigation:navigation-ui-ktx:$NAVIGATION_VERSION"
    const val material = "com.google.android.material:material:1.3.0-alpha01"
    const val result = "com.github.kittinunf.result:result:$RESULT_VERSION"
    const val resultCoroutines = "com.github.kittinunf.result:result-coroutines:$RESULT_VERSION"
    const val coroutines = "org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.7"
}