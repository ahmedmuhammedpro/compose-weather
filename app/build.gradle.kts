import com.bitucketsrepos.buildsrc.Configs
import com.bitucketsrepos.buildsrc.Dependencies

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.dagger.hilt.android")
    id("kotlin-parcelize")
    kotlin("kapt")
}

android {
    namespace = "com.ahmedpro.weathercompose"
    compileSdk = Configs.compileSdk

    defaultConfig {
        applicationId = "com.ahmedpro.weathercompose"
        minSdk = Configs.minSdk
        targetSdk = Configs.targetSdk
        versionCode = Configs.versionCode
        versionName = Configs.versionName

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.4"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation(project(":domain"))

    implementation(Dependencies.Androidx.coreKTX)
    implementation(Dependencies.Kotlin.stdlib)
    implementation(Dependencies.Androidx.lifecycleRuntimeKTX)
    implementation(Dependencies.Location.gmsLocation)

    // Compose
    implementation(Dependencies.Compose.activityCompose)
    implementation(platform(Dependencies.Compose.composeBom))
    implementation(Dependencies.Compose.composeUi)
    implementation(Dependencies.Compose.composeUiGraphics)
    implementation(Dependencies.Compose.composeUiToolingPreview)
    implementation(Dependencies.Compose.composeMaterial)
    implementation(Dependencies.Compose.composeMaterial3)
    implementation(Dependencies.Compose.compseNavigation)

    // Coil
    implementation(Dependencies.Compose.coil)

    // Coroutines
    implementation(Dependencies.Coroutines.coroutineAndroid)

    // Hilt
    implementation(Dependencies.Hilt.hilt)
    kapt(Dependencies.Hilt.hiltCompiler)

    // Lottie
    implementation(Dependencies.Lottie.lottieAnimation)

    // ThreeTenBP
    implementation(Dependencies.Common.threeTenBP)
    // Test
    testImplementation(Dependencies.Testing.jUnit)
    androidTestImplementation(Dependencies.Testing.androidxJUnit)
    androidTestImplementation(Dependencies.Testing.espresso)
    androidTestImplementation(platform(Dependencies.Testing.composeTestBom))
    androidTestImplementation(Dependencies.Testing.composeUITestJunit)
    debugImplementation(Dependencies.Testing.composeUITooling)
    debugImplementation(Dependencies.Testing.composeUITestManifest)
}

kapt { correctErrorTypes = true }