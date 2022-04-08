plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("dagger.hilt.android.plugin")
    kotlin("kapt")
}

android {
    compileSdk = 31

    defaultConfig {
        minSdk = 23
        targetSdk = 31

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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    // Core
    implementation (Deps.coreKtx)
    implementation (Deps.appCompat)
    implementation (Deps.materialDesign)
    implementation (Deps.viewBinding)
    implementation (Deps.coroutines)

    // Hilt
    implementation(Deps.hilt)
    kapt(Deps.hiltCompiler)

    // Tests
    testImplementation (Deps.junit)
    androidTestImplementation (Deps.extJunit)
    androidTestImplementation (Deps.espresso)
}