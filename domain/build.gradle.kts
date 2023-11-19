import com.bitucketsrepos.buildsrc.Configs
import com.bitucketsrepos.buildsrc.Dependencies

plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("com.google.dagger.hilt.android")
    kotlin("kapt")
}

android {
    namespace = "com.ahmedpro.domain"
    compileSdk = Configs.compileSdk

    defaultConfig {
        minSdk = Configs.minSdk

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {

    implementation(Dependencies.Androidx.coreKTX)
    implementation(Dependencies.Androidx.appCompat)
    implementation(Dependencies.Kotlin.stdlib)

    // Coroutines
    implementation(Dependencies.Coroutines.coroutineAndroid)

    // Hilt
    implementation(Dependencies.Hilt.hilt)
    kapt(Dependencies.Hilt.hiltCompiler)

    // Network
    implementation(Dependencies.Retrofit.retrofit)
    implementation(Dependencies.Retrofit.okhttp)
    implementation(Dependencies.Retrofit.okhttpLoggingInterceptor)
    implementation(Dependencies.Retrofit.gsonConverter)
    implementation(Dependencies.Retrofit.gson)

    // Datastore
    implementation(Dependencies.Androidx.dataStore)

    // Test
    testImplementation(Dependencies.Testing.jUnit)
    androidTestImplementation(Dependencies.Testing.androidxJUnit)
    androidTestImplementation(Dependencies.Testing.espresso)
}

kapt {
    correctErrorTypes = true
}