plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.compose.compiler)
//    id("kotlin-kapt")
    id("com.google.gms.google-services")
    id("com.google.devtools.ksp")

}

android {
    namespace = "com.example.test"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.test"
        minSdk = 24
        //noinspection OldTargetApi
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
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

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "2.0.0"

    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
            excludes += "/META-INF/DEPENDENCIES"
        }
    }

}
composeCompiler {
    reportsDestination = layout.buildDirectory.dir("compose_compiler")

}
dependencies {
    // Core libraries
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)

    // Firebase
    implementation(libs.firebase.messaging)
    implementation(libs.google.firebase.firestore.ktx)
    implementation(libs.firebase.storage.ktx)
    implementation(libs.firebase.auth.ktx)
    implementation(libs.firebase.database)
    implementation(libs.firebase.analytics)
    implementation(platform(libs.firebase.bom))

    // UI & Animation
    implementation(libs.lottie.compose)
    implementation(libs.androidx.material.icons.extended)

    // Networking & Serialization
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.play.services.measurement.api)

    // Navigation & Lifecycle
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.lifecycle.runtime.ktx)

    // Dependency Injection
    implementation(libs.hilt.android)
    implementation(libs.firebase.storage)
    // Test dependencies
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    implementation ("androidx.compose.runtime:runtime-livedata:1.7.5")
    implementation( "androidx.compose.runtime:runtime:1.7.5")
    // Other dependencies
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-play-services:1.7.3")
    implementation("com.google.auth:google-auth-library-oauth2-http:1.18.0")
    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.karumi:dexter:6.2.3")
    implementation(libs.androidx.material)
    implementation(libs.ui)

    // Firebase
    implementation ("com.google.firebase:firebase-appcheck-safetynet:16.0.1")
    implementation ("com.google.firebase:firebase-appcheck-playintegrity:16.0.1")
    implementation("com.google.auth:google-auth-library-oauth2-http:1.18.0")
    implementation("com.google.firebase:firebase-firestore:25.1.1")

    // Cloudinary
    implementation ("com.cloudinary:cloudinary-android:3.0.2")

    // Coil
    implementation("io.coil-kt.coil3:coil-compose:3.0.4")
    implementation ("io.coil-kt:coil-compose:2.4.0")

    // Credential Saving
    implementation ("androidx.security:security-crypto:1.1.0-alpha06")
    implementation("androidx.credentials:credentials-play-services-auth:1.3.0")

    implementation ("com.google.accompanist:accompanist-swiperefresh:0.28.0")

}
