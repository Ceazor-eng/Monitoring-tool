plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    id("org.jetbrains.kotlin.plugin.serialization")
    id("kotlin-kapt")
    id("com.google.dagger.hilt.android")
}

android {
    namespace = "com.monitoringtendepay"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.monitoringtendepay"
        minSdk = 24
        targetSdk = 34
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.activity.ktx)

    // Hilt dependencies
    implementation(libs.dagger.hilt)
    implementation(libs.androidx.swiperefreshlayout)
    // testImplementation(libs.junit.junit)
    kapt(libs.kapt)

    // Kotlin Serialization dependencies
    implementation(libs.serialization.json)

    // network manager with retrofit
    implementation(libs.bundles.networking)

    // network with chucker
    debugImplementation(libs.bundles.networkChucker)
    releaseImplementation(libs.bundles.networkChuckerOp)

    // viewModel
    implementation(libs.bundles.viewModel)

    // bcrypt
    implementation(libs.bundles.hashing)

    // Charts
    implementation(libs.mpandroidchart)

    // pdf generation
    implementation(libs.itext7.core)

    // Test dependencies
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    testImplementation("org.mockito:mockito-core:5.7.0")
    androidTestImplementation("org.mockito:mockito-core:5.7.0")
    testImplementation("org.mockito.kotlin:mockito-kotlin:5.2.1")
    androidTestImplementation("org.mockito.kotlin:mockito-kotlin:5.2.1")

    // Use mockito-android for Android instrumentation tests
    androidTestImplementation("org.mockito:mockito-android:4.4.0")
    androidTestImplementation("androidx.arch.core:core-testing:2.2.0")

    testImplementation("io.mockk:mockk:1.12.0")
    androidTestImplementation("io.mockk:mockk-android:1.12.0")

    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3")
    testImplementation("org.robolectric:robolectric:4.9")

    // truth
    testImplementation("com.google.truth:truth:1.0.1")
}

// Allow references to generated code
kapt {
    correctErrorTypes = true
}