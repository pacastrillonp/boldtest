plugins {
    // Android / Kotlin
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)

    // DI
    id("com.google.dagger.hilt.android")

    // Codegen
    id("com.google.devtools.ksp")
}

android {
    namespace = "co.pacastrillon.boldtest"
    compileSdk {
        version = release(36)
    }

    defaultConfig {
        applicationId = "co.pacastrillon.boldtest"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        compose = true
    }
}

dependencies {
    // =======================
    // Project modules
    // =======================
    implementation(project(":common"))
    implementation(project(":data"))
    implementation(project(":domain"))

    // =======================
    // DI (Hilt)
    // =======================
    implementation(libs.hilt.android)
    implementation(libs.androidx.hilt.navigation.compose)
    ksp(libs.hilt.android.compiler)

    // =======================
    // UI (Compose + Navigation)
    // =======================
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.navigation.compose)

    // AndroidX base
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)

    // =======================
    // Images
    // =======================
    implementation(libs.coil.compose)

    // =======================
    // Testing
    // =======================
    testImplementation(libs.junit)

    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)

    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
}