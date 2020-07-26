const val LIFECYCLE_VERSION = "2.2.0"
const val NAVIGATION_VERSION = "2.3.0"
const val RESULT_VERSION = "3.0.0"
const val ROOM_VERSION = "2.2.5"

object Dependencies {
    val appcompat = "androidx.appcompat:appcompat:1.1.0"
    val coreKtx = "androidx.core:core-ktx:1.3.0"
    val kotlinStdLib = "org.jetbrains.kotlin:kotlin-stdlib:1.3.72"
    val constraintlayout = "androidx.constraintlayout:constraintlayout:2.0.0-beta7"
    val retrofit = "com.squareup.retrofit2:retrofit:2.9.0"
    val moshi = "com.squareup.moshi:moshi:1.9.3"
    val moshiKapt = "com.squareup.moshi:moshi-kotlin-codegen:1.9.3"
    val retrofitMoshiAdapter = "com.squareup.retrofit2:converter-moshi:2.9.0"
    val okhttp = "com.squareup.okhttp3:okhttp:4.7.2"
    val chucker = "com.github.chuckerteam.chucker:library:3.2.0"
    val hilt = "com.google.dagger:hilt-android:2.28-alpha"
    val hiltKapt = "com.google.dagger:hilt-android-compiler:2.28-alpha"
    val hiltKaptJetpack = "androidx.hilt:hilt-compiler:1.0.0-alpha01"
    val hiltViewModel = "androidx.hilt:hilt-lifecycle-viewmodel:1.0.0-alpha01"
    val hiltWorkManager = "androidx.hilt:hilt-work:1.0.0-alpha01"
    val lifecycleViewModel = "androidx.lifecycle:lifecycle-viewmodel-ktx:$LIFECYCLE_VERSION"
    val lifecycleLiveData = "androidx.lifecycle:lifecycle-livedata-ktx:$LIFECYCLE_VERSION"
    val lifecycleRuntime = "androidx.lifecycle:lifecycle-runtime-ktx:$LIFECYCLE_VERSION"
    val lifecycleCommonJava8 = "androidx.lifecycle:lifecycle-common-java8:$LIFECYCLE_VERSION"
    val navigation = "androidx.navigation:navigation-fragment-ktx:$NAVIGATION_VERSION"
    val navigationUi = "androidx.navigation:navigation-ui-ktx:$NAVIGATION_VERSION"
    val material = "com.google.android.material:material:1.3.0-alpha01"
    val result = "com.github.kittinunf.result:result:$RESULT_VERSION"
    val resultCoroutines = "com.github.kittinunf.result:result-coroutines:$RESULT_VERSION"
    val coroutines = "org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.7"
    val timber = "com.jakewharton.timber:timber:4.7.1"
    val room = "androidx.room:room-runtime:$ROOM_VERSION"
    val roomCoroutines = "androidx.room:room-ktx:$ROOM_VERSION"
    val roomKapt = "androidx.room:room-compiler:$ROOM_VERSION"
    val coil = "io.coil-kt:coil:0.11.0"
    val insetter = "dev.chrisbanes:insetter:0.3.0"
    val insetterKtx = "dev.chrisbanes:insetter-ktx:0.3.0"
    val sunilsonExtensions = "com.github.sunilson:android-kotlin-extensions:0.35"
    val sunilsonViewModel = "com.github.sunilson:Unidirectional-ViewModel:0.5"
    val maps = "com.google.android.libraries.maps:maps:3.1.0-beta"
    val epoxy = "com.airbnb.android:epoxy:3.11.0"
    val epoxyKapt = "com.airbnb.android:epoxy-processor:3.11.0"
    val lottie = "com.airbnb.android:lottie:3.4.1"
    val jwt = "com.auth0.android:jwtdecode:2.0.0"
    val workManager = "androidx.work:work-runtime-ktx:2.4.0"
}