plugins {
    // Android / Kotlin
    alias(libs.plugins.android.library)

    // DI
    id("com.google.dagger.hilt.android")

    // Serialization
    id("org.jetbrains.kotlin.plugin.serialization")

    // Codegen (Hilt + Room)
    id("com.google.devtools.ksp")
}

android {
    namespace = "co.pacastrillon.boldtest.data"
    compileSdk {
        version = release(36)
    }

    defaultConfig {
        minSdk = 26

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
    implementation(project(":domain"))

    // =======================
    // AndroidX base / Material
    // =======================
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)

    // =======================
    // DI (Hilt)
    // =======================
    implementation(libs.hilt.android)
    ksp(libs.hilt.android.compiler)

    // =======================
    // Persistence (Room)
    // =======================
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    implementation(libs.androidx.room.paging)
    ksp(libs.androidx.room.compiler)
    testImplementation(libs.androidx.room.testing)

    // =======================
    // Networking
    // =======================
    implementation(libs.retrofit)
    implementation(libs.okhttp)
    implementation(libs.logging.interceptor)

    // =======================
    // Serialization
    // =======================
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.converter.kotlinx.serialization)

    // =======================
    // Testing
    // =======================
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    testImplementation(libs.mockito.core)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.mockwebserver)
    testImplementation(libs.mockito.kotlin)
}