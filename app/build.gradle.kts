plugins {
    kotlin("kapt")
    kotlin("plugin.serialization") version libs.versions.kotlinPlugin
    alias(libs.plugins.android.app)
    alias(libs.plugins.kotlin)
    alias(libs.plugins.dagger.hilt)
}

android {
    namespace = "com.example.productsenuygun"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.productsenuygun"
        minSdk = 24
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
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation(libs.bundles.android)
    implementation(libs.bundles.compose)
    implementation(libs.coil)
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)
    implementation(libs.hilt.navigation)
    implementation(libs.kotlinx.serialization)
    implementation(libs.okhttp)
    implementation(libs.bundles.retrofit)

    testImplementation(libs.junit)
    androidTestImplementation(libs.junit.ext)
    androidTestImplementation(platform(libs.compose.bom))
    debugImplementation(libs.compose.ui.tooling)
}

kapt {
    correctErrorTypes = true
}