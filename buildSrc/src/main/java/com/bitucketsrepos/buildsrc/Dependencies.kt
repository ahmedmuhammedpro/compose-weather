package com.bitucketsrepos.buildsrc

object Dependencies {
    const val kotlinVersion = "1.9.20"

    object Kotlin {
        const val stdlib = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:$kotlinVersion"
        const val reflect = "org.jetbrains.kotlin:kotlin-reflect:$kotlinVersion"
        const val extensions = "org.jetbrains.kotlin:kotlin-android-extensions:$kotlinVersion"
        const val serialization = "org.jetbrains.kotlin:kotlin-serialization:$kotlinVersion"
    }

    object Location {
        const val gmsLocation = "com.google.android.gms:play-services-location:21.0.1"
    }

    object Lottie {
        const val lottieAnimation = "com.airbnb.android:lottie-compose:6.0.0"
    }

    object Androidx {
        const val coreKTX = "androidx.core:core-ktx:1.12.0"
        const val lifecycleRuntimeKTX = "androidx.lifecycle:lifecycle-runtime-ktx:2.6.2"
        const val appCompat = "androidx.appcompat:appcompat:1.6.1"
        const val dataStore = "androidx.datastore:datastore-preferences:1.0.0"
    }

    object Compose {
        const val activityCompose = "androidx.activity:activity-compose:1.8.1"
        const val composeBom = "androidx.compose:compose-bom:2023.10.01"
        const val composeUi = "androidx.compose.ui:ui"
        const val composeUiGraphics = "androidx.compose.ui:ui-graphics"
        const val composeUiToolingPreview = "androidx.compose.ui:ui-tooling-preview"
        const val composeMaterial = "androidx.compose.material:material"
        const val composeMaterial3 = "androidx.compose.material3:material3"
        const val compseNavigation = "androidx.hilt:hilt-navigation-compose:1.1.0"
        const val coil = "io.coil-kt:coil-compose:2.5.0"
    }

    object Coroutines {
        const val coroutineAndroid = "org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3"
    }

    object Retrofit {
        private const val retrofitVersion = "2.9.0"
        private const val okhttpVersion = "4.12.0"
        const val retrofit = "com.squareup.retrofit2:retrofit:$retrofitVersion"
        const val gsonConverter = "com.squareup.retrofit2:converter-gson:$retrofitVersion"
        const val converterScalars = "com.squareup.retrofit2:converter-scalars:$retrofitVersion"
        const val okhttp = "com.squareup.okhttp3:okhttp:$okhttpVersion"
        const val okhttpLoggingInterceptor = "com.squareup.okhttp3:logging-interceptor:$okhttpVersion"
        const val okhttpMock = "com.squareup.okhttp3:mockwebserver:$okhttpVersion"
        const val gson = "com.google.code.gson:gson:2.10.1"
    }

    object Hilt {
        private const val hiltVersion = "2.48.1"
        const val hilt = "com.google.dagger:hilt-android:$hiltVersion"
        const val hiltCompiler = "com.google.dagger:hilt-android-compiler:$hiltVersion"
    }

    object Common {
        const val threeTenBP = "com.jakewharton.threetenabp:threetenabp:1.3.0"
    }

    object Testing {
        const val jUnit = "junit:junit:4.13.2"
        const val androidxJUnit = "androidx.test.ext:junit:1.1.5"
        const val espresso = "androidx.test.espresso:espresso-core:3.5.1"
        const val composeTestBom = "androidx.compose:compose-bom:2023.10.01"
        const val composeUITestJunit = "androidx.compose.ui:ui-test-junit4"
        const val composeUITooling = "androidx.compose.ui:ui-tooling"
        const val composeUITestManifest = "androidx.compose.ui:ui-test-manifest"

    }
}