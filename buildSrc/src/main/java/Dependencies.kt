object BuildConfig {
    const val minSdkVersion = 21
    const val targetSdkVersion = 29
    const val compileSdkVersion = 29
    const val buildToolsVersion = "29.0.3"
}

object Versions {
    const val retrofit = "2.9.0"
    const val okhttpLogging = "3.9.0"
    const val appcompat = "1.3.0"
    const val coreKtx = "1.3.0"
    const val constraintlayout = "2.0.4"
    const val paging = "3.0.0-alpha02"
    const val timber = "4.7.1"
    const val kotlin = "1.3.72"
    const val koin = "2.1.5"
    const val work = "2.2.0"
    const val room = "2.3.0-alpha01"
    const val persistence = "1.1.1"
    const val cardview = "1.0.0"
    const val recyclerview = "1.2.0-alpha06"
    const val fragment = "1.3.0-alpha06"
    const val anko = "0.10.8"
    const val swiperefreshlayout = "1.0.0"
    const val junit = "4.13.1"
    const val junitExt = "1.1.1"
    const val testRunner = "1.3.0"
    const val testCore = "1.1.0"
    const val espressoCore = "3.3.0"
    const val jDatabinding = "1.0.1"
    const val progressview = "1.0.0"
    const val runtime = "0.11.0"
    const val hit = "2.28-alpha"
    const val hitViewModule = "1.0.0-alpha01"
    const val appStartup = "1.0.0-alpha01"
    const val material = "1.2.1"
    const val lifeCycle = "2.3.0-rc01"
    const val multidex = "2.0.1"
    const val zxing = "3.3.3"
    const val eventBus = "3.2.0"
    const val leakcanary = "1.6.2"
    const val gson = "2.8.5"
    const val glide = "4.10.0"
    const val rxjava = "3.0.0"
    const val greendao = "3.2.2"
    const val rxandroid = "3.0.0"
    const val autosize = "1.1.2"
    const val bugly = "latest.release"
    const val spinKit = "1.4.0"
    const val timePickerDialog = "1.0.1"
    const val filePicker = "2.2.5"
    const val mpAndroidChart = "3.0.3"
    const val mpAndroidChartUtil = "1.0.3"
    const val viewpager2 = "1.1.0-alpha01"
}

object AndroidX {
    val appcompat = "androidx.appcompat:appcompat:${Versions.appcompat}"
    val coreKtx = "androidx.core:core-ktx:${Versions.coreKtx}"
    val constraintlayout =
        "androidx.constraintlayout:constraintlayout:${Versions.constraintlayout}"
    val pagingRuntime = "androidx.paging:paging-runtime:${Versions.paging}"

    // alternatively - without Android dependencies for tests
    val pagingCommon = "androidx.paging:paging-common:${Versions.paging}"

    // optional - RxJava2 support
    val pagingRxJava = "androidx.paging:paging-rxjava2:${Versions.paging}"

    // optional - Guava ListenableFuture support
    val pagingGuava = "androidx.paging:paging-guava:${Versions.paging}"
    val workRuntime = "androidx.work:work-runtime:${Versions.work}"
    val workTesting = "androidx.work:work-testing:${Versions.work}"
    val cardview = "androidx.cardview:cardview:${Versions.cardview}"
    val recyclerview = "androidx.recyclerview:recyclerview:${Versions.recyclerview}"
    val swiperefreshlayout =
        "androidx.swiperefreshlayout:swiperefreshlayout:${Versions.swiperefreshlayout}"

    val appStartup = "androidx.startup:startup-runtime:${Versions.appStartup}"

    const val lifeCycleExtension = "androidx.lifecycle:lifecycle-extensions:${Versions.lifeCycle}"
    const val lifecycleRuntime = "androidx.lifecycle:lifecycle-runtime:${Versions.lifeCycle}"
    const val lifeCycleKT = "androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.lifeCycle}"
    const val lifeCycleProcess = "androidx.lifecycle:lifecycle-process:${Versions.lifeCycle}"
    const val multidex = "androidx.multidex:multidex:${Versions.multidex}"
    const val viewpager2 = "androidx.viewpager2:viewpager2:${Versions.viewpager2}"
}

object Android {
    const val meteria = "com.google.android.material:material:${Versions.material}"
    const val zxing = "com.google.zxing:core:${Versions.zxing}"
}

object Glide {
    const val runtime = "com.github.bumptech.glide:glide:${Versions.glide}"
    const val okhttp3Integration =
        "com.github.bumptech.glide:okhttp3-integration:${Versions.glide}@aar"
    const val annotations = "com.github.bumptech.glide:annotations:${Versions.glide}"
    const val compiler = "com.github.bumptech.glide:compiler:${Versions.glide}"
}

object Hilt {
    val daggerRuntime = "com.google.dagger:hilt-android:${Versions.hit}"
    val daggerCompiler = "com.google.dagger:hilt-android-compiler:${Versions.hit}"
    val viewModule = "androidx.hilt:hilt-lifecycle-viewmodel:${Versions.hitViewModule}"
    val compiler = "androidx.hilt:hilt-compiler:${Versions.hitViewModule}"
}

object EventBus {
    const val eventBus = "org.greenrobot:eventbus:${Versions.eventBus}"
}

object Coil {
    val runtime = "io.coil-kt:coil:${Versions.runtime}"
}

object Leakcanary {
    const val open = "com.squareup.leakcanary:leakcanary-android:${Versions.leakcanary}"
    const val close = "com.squareup.leakcanary:leakcanary-android-no-op:${Versions.leakcanary}"
}

object SpinKit {
    const val runtime = "com.github.ybq:Android-SpinKit:${Versions.spinKit}"
}

object TimePickerDialog {
    const val timePickerDialog =
        "com.jzxiang.pickerview:TimePickerDialog:${Versions.timePickerDialog}"
}

object Rxandroid {
    const val rxjava3 = "io.reactivex.rxjava3:rxjava:${Versions.rxjava}"
    const val rxandroid = "io.reactivex.rxjava3:rxandroid:${Versions.rxandroid}"
}

object Room {
    val runtime = "androidx.room:room-runtime:${Versions.room}"
    val compiler = "androidx.room:room-compiler:${Versions.room}"
    val persistence = "android.arch.persistence.room:compiler:${Versions.persistence}"
    val ktx = "androidx.room:room-ktx:${Versions.room}"
    val rxjava2 = "androidx.room:room-rxjava2:${Versions.room}"
    val testing = "androidx.room:room-testing:${Versions.room}"
}

object GreenDao {
    val greendao = "org.greenrobot:greendao:${Versions.greendao}"
}

object Fragment {
    val runtime = "androidx.fragment:fragment:${Versions.fragment}"
    val runtimeKtx = "androidx.fragment:fragment-ktx:${Versions.fragment}"
    val testing = "androidx.fragment:fragment-testing:${Versions.fragment}"
}

object AutoSize {
    const val runtime = "me.jessyan:autosize:${Versions.autosize}"
}

object Bugly {
    const val upgrade = "com.tencent.bugly:crashreport_upgrade:${Versions.bugly}"
    const val nativecrashreport = "com.tencent.bugly:nativecrashreport:${Versions.bugly}"
}

object Kt {
    val stdlibJdk7 = "org.jetbrains.kotlin:kotlin-stdlib-jdk7:${Versions.kotlin}"
    val stdlibJdk8 = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:${Versions.kotlin}"
    val test = "org.jetbrains.kotlin:kotlin-test-junit:${Versions.kotlin}"
    val plugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.kotlin}"
}

object Koin {
    val core = "org.koin:koin-core:${Versions.koin}"
    val androidCore = "org.koin:koin-android:${Versions.koin}"
    val viewmodel = "org.koin:koin-androidx-viewmodel:${Versions.koin}"
    val androidScope = "org.koin:koin-android-scope:$${Versions.koin}"
}

object Anko {
    val common = "org.jetbrains.anko:anko-common:${Versions.anko}"
    val sqlite = "org.jetbrains.anko:anko-sqlite:${Versions.anko}"
    val coroutines = "org.jetbrains.anko:anko-coroutines:${Versions.anko}"
    val design = "org.jetbrains.anko:anko-design:${Versions.anko}" // For SnackBars
}

object Retrofit {
    val runtime = "com.squareup.retrofit2:retrofit:${Versions.retrofit}"
    val gson = "com.squareup.retrofit2:converter-gson:${Versions.retrofit}"
    val mock = "com.squareup.retrofit2:retrofit-mock:${Versions.retrofit}"
    val logging = "com.squareup.okhttp3:logging-interceptor:${Versions.okhttpLogging}"
    val adapter = "com.squareup.retrofit2:adapter-rxjava3:${Versions.retrofit}"
}

object Gson {
    const val runtime = "com.google.code.gson:gson:${Versions.gson}"
}

object Depend {

    val junit = "junit:junit:${Versions.junit}"
    val androidTestJunit = "androidx.test.ext:junit:${Versions.junitExt}"
    val anndroidTestRunner = "androidx.test:runner:${Versions.testRunner}"
    val espressoCore = "androidx.test.espresso:espresso-core:${Versions.espressoCore}"
    val androidTestCore = "androidx.test:core:${Versions.testCore}"
    val jDatabinding = "com.hi-dhl:jdatabinding:${Versions.jDatabinding}"
    val progressview = "com.hi-dhl:progressview:${Versions.progressview}"
    val timber = "com.jakewharton.timber:timber:${Versions.timber}"
}

object FilePicker {
    const val runtime = "com.droidninja:filepicker:${Versions.filePicker}"
}

object AndroidChart {
    const val mpAndroidChart = "com.github.PhilJay:MPAndroidChart:${Versions.mpAndroidChart}"
    const val mpAndroidChartUtils =
        "com.teaanddogdog:mpandroidchartutil:${Versions.mpAndroidChartUtil}"
}
