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
        testInstrumentationRunner = "com.monitoringtendepay.HiltTestRunner"
        javaCompileOptions.annotationProcessorOptions.arguments["dagger.hilt.disableCrossCompilationRootValidation"] = "true"
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
    implementation(libs.bundles.vico)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.activity.ktx)

    // Hilt dependencies
    implementation(libs.dagger.hilt)
    implementation(libs.androidx.swiperefreshlayout)
    implementation(libs.androidx.cardview)
    testImplementation(libs.junit.junit)
    testImplementation(libs.junit.junit)
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

//    androidTestImplementation ("androidx.test.ext:junit:1.1.5")
//    androidTestImplementation ("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation("androidx.test.espresso:espresso-intents:3.6.1")
    androidTestImplementation("androidx.test.espresso:espresso-contrib:3.6.1")
    androidTestImplementation("androidx.test.espresso:espresso-idling-resource:3.6.1")
    androidTestImplementation("androidx.test:rules:1.6.1")
    androidTestImplementation("androidx.test:runner:1.6.1")

    // Hilt for instrumentation tests
    androidTestImplementation("com.google.dagger:hilt-android-testing:2.49")
    kaptAndroidTest("com.google.dagger:hilt-compiler:2.49")

    // For launching fragments
    debugImplementation("androidx.fragment:fragment-testing:1.8.2")

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

    testImplementation("com.squareup.okhttp3:mockwebserver:4.11.0")

    implementation("androidx.room:room-runtime:2.6.1")
    kapt("androidx.room:room-compiler:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")
}

// Allow references to generated code
kapt {
    correctErrorTypes = true
}