plugins {
    // Android / Kotlin
    alias(libs.plugins.android.library)

    // DI
    id("com.google.dagger.hilt.android")

    // Serialization
    id("org.jetbrains.kotlin.plugin.serialization")

    // Codegen
    id("com.google.devtools.ksp")
}

android {
    namespace = "co.pacastrillon.boldtest.domain"
    compileSdk {
        version = release(36)
    }

    defaultConfig {
        minSdk = 24

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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    // =======================
    // Project modules
    // =======================
    implementation(project(":common"))

    // =======================
    // DI (Hilt)
    // =======================
    implementation(libs.hilt.android)
    ksp(libs.hilt.android.compiler)

    // =======================
    // AndroidX base
    // =======================
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)

    // =======================
    // Testing
    // =======================
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    testImplementation("org.mockito:mockito-core:5.11.0")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3")
}