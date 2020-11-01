const val LIFECYCLE_VERSION = "2.3.0-beta01"
const val NAVIGATION_VERSION = "2.3.1"
const val RESULT_VERSION = "3.1.0"
const val ROOM_VERSION = "2.3.0-alpha03"
const val ESPRESSO_VERSION = "3.4.0-alpha02"
const val JUNIT_4_VERSION = "4.13"
const val JUNIT_4_RUNNER_VERSION = "1.1.3-alpha02"
const val JUNIT_VERSION = "5.7.0"
const val HILT_VERSION = "2.29.1-alpha"
const val WORKMANAGER_VERSION = "2.5.0-beta01"
const val MOCKK_VERSION = "1.10.2"
const val COROUTINES_VERSION = "1.4.0"
const val MOSHI_VERSION = "1.11.0"
const val EPOXY_VERSION = "4.1.0"
const val ARCH_CORE_VERSION = "2.1.0"

object Dependencies {
    const val contour = "app.cash.contour:contour:1.0.0"
    const val charts = "com.github.PhilJay:MPAndroidChart:3.1.0"
    const val espresso = "androidx.test.espresso:espresso-core:$ESPRESSO_VERSION"
    const val espressoContrib = "androidx.test.espresso:espresso-contrib:$ESPRESSO_VERSION"
    const val espressoIntent = "androidx.test.espresso:espresso-intents:$ESPRESSO_VERSION"
    const val appcompat = "androidx.appcompat:appcompat:1.2.0"
    const val coreKtx = "androidx.core:core-ktx:1.5.0-alpha04"
    const val kotlinStdLib = "org.jetbrains.kotlin:kotlin-stdlib:1.3.72"
    const val constraintlayout = "androidx.constraintlayout:constraintlayout:2.0.4"
    const val retrofit = "com.squareup.retrofit2:retrofit:2.9.0"
    const val moshi = "com.squareup.moshi:moshi:$MOSHI_VERSION"
    const val moshiAdapters = "com.squareup.moshi:moshi-adapters:$MOSHI_VERSION"
    const val moshiKapt = "com.squareup.moshi:moshi-kotlin-codegen:$MOSHI_VERSION"
    const val retrofitMoshiAdapter = "com.squareup.retrofit2:converter-moshi:2.9.0"
    const val okhttp = "com.squareup.okhttp3:okhttp:4.10.0-RC1"
    const val chucker = "com.github.chuckerteam.chucker:library:3.3.0"
    const val hilt = "com.google.dagger:hilt-android:$HILT_VERSION"
    const val hiltKapt = "com.google.dagger:hilt-android-compiler:$HILT_VERSION"
    const val hiltKaptJetpack = "androidx.hilt:hilt-compiler:1.0.0-alpha02"
    const val hiltViewModel = "androidx.hilt:hilt-lifecycle-viewmodel:1.0.0-alpha02"
    const val hiltWorkManager = "androidx.hilt:hilt-work:1.0.0-alpha02"
    const val lifecycleViewModel = "androidx.lifecycle:lifecycle-viewmodel-ktx:$LIFECYCLE_VERSION"
    const val lifecycleLiveData = "androidx.lifecycle:lifecycle-livedata-ktx:$LIFECYCLE_VERSION"
    const val lifecycleRuntime = "androidx.lifecycle:lifecycle-runtime-ktx:$LIFECYCLE_VERSION"
    const val lifecycleCommonJava8 = "androidx.lifecycle:lifecycle-common-java8:$LIFECYCLE_VERSION"
    const val navigation = "androidx.navigation:navigation-fragment-ktx:$NAVIGATION_VERSION"
    const val navigationUi = "androidx.navigation:navigation-ui-ktx:$NAVIGATION_VERSION"
    const val material = "com.google.android.material:material:1.3.0-alpha02"
    const val result = "com.github.kittinunf.result:result:$RESULT_VERSION"
    const val resultCoroutines = "com.github.kittinunf.result:result-coroutines:$RESULT_VERSION"
    const val coroutines = "org.jetbrains.kotlinx:kotlinx-coroutines-core:$COROUTINES_VERSION"
    const val timber = "com.jakewharton.timber:timber:4.7.1"
    const val room = "androidx.room:room-runtime:$ROOM_VERSION"
    const val roomCoroutines = "androidx.room:room-ktx:$ROOM_VERSION"
    const val roomKapt = "androidx.room:room-compiler:$ROOM_VERSION"
    const val coil = "io.coil-kt:coil:1.0.0"
    const val insetter = "dev.chrisbanes:insetter:0.3.1"
    const val insetterKtx = "dev.chrisbanes:insetter-ktx:0.3.1"
    const val sunilsonExtensions = "com.github.sunilson:android-kotlin-extensions:0.43"
    const val sunilsonViewModel = "com.github.sunilson:Unidirectional-ViewModel:0.74"
    const val maps = "com.google.android.libraries.maps:maps:3.1.0-beta"
    const val mapsUtils = "com.google.maps.android:android-maps-utils-v3:1.3.1"
    const val epoxy = "com.airbnb.android:epoxy:$EPOXY_VERSION"
    const val epoxyKapt = "com.airbnb.android:epoxy-processor:$EPOXY_VERSION"
    const val lottie = "com.airbnb.android:lottie:3.4.4"
    const val jwt = "com.auth0.android:jwtdecode:2.0.0"
    const val workManager = "androidx.work:work-runtime-ktx:$WORKMANAGER_VERSION"
    const val workManagerTesting = "androidx.work:work-testing:$WORKMANAGER_VERSION"
    const val mockk = "io.mockk:mockk:$MOCKK_VERSION"
    const val mockkAndroid = "io.mockk:mockk-android:$MOCKK_VERSION"
    const val fragmentTesting = "androidx.fragment:fragment-testing:1.3.0-beta01"
    const val androidArchRuntimeCore = "androidx.arch.core:core-runtime:$ARCH_CORE_VERSION"
    const val androidArchCommonCore = "androidx.arch.core:core-common:$ARCH_CORE_VERSION"
    const val androidArchTestingCore = "androidx.arch.core:core-testing:$ARCH_CORE_VERSION"
    const val junit = "org.junit.jupiter:junit-jupiter-api:$JUNIT_VERSION"
    const val junit4 = "junit:junit:$JUNIT_4_VERSION"
    const val junit4Engine = "org.junit.vintage:junit-vintage-engine:$JUNIT_VERSION"
    const val junit4Runner = "androidx.test.ext:junit:$JUNIT_4_RUNNER_VERSION"
    const val junitParams = "org.junit.jupiter:junit-jupiter-params:$JUNIT_VERSION"
    const val coroutineTesting = "org.jetbrains.kotlinx:kotlinx-coroutines-test:$COROUTINES_VERSION"
    const val androidTestRules = "androidx.test:rules:1.2.0"
    const val androidTestRunner = "androidx.test:runner:1.2.0"
    const val recyclerviewAnimations = "jp.wasabeef:recyclerview-animators:4.0.1"
    const val hiltAndroidTest = "com.google.dagger:hilt-android-testing:$HILT_VERSION"
    const val hiltAndroidTestCompiler = "com.google.dagger:hilt-android-compiler:$HILT_VERSION"
    const val moshixSealedRuntime = "dev.zacsweers.moshix:moshi-sealed-runtime:0.5.0"
    const val moshixAdapters = "dev.zacsweers.moshix:moshi-adapters:0.6.0"
    const val moshixSealedCodegen = "dev.zacsweers.moshix:moshi-sealed-codegen:0.5.0"
}