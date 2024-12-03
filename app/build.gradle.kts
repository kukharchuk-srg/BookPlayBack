plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.google.gms.google.services)
    alias(libs.plugins.google.firebase.crashlytics)
    alias(libs.plugins.compose.compiler)
}

android {
    namespace = "ua.kuk.books"
    compileSdk = 35

    defaultConfig {
        applicationId = "ua.kuk.books"
        minSdk = 23
        targetSdk = 35
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
            proguardFile(getDefaultProguardFile("proguard-android-optimize.txt"))
            proguardFile(file("proguard/"))
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
        freeCompilerArgs += listOf(
            "-P",
            "plugin:androidx.compose.compiler.plugins.kotlin:metricsDestination=${projectDir.absolutePath}/build/reports",
            "-P",
            "plugin:androidx.compose.compiler.plugins.kotlin:reportsDestination=${projectDir.absolutePath}/build/reports",
        )
    }
    buildFeatures {
        compose = true
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation(projects.feature.bookPlayback)
    implementation(projects.core.common)
    implementation(projects.core.data)
    implementation(projects.core.uiKit)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.lifecycle.runtime.ktx)

    implementation(libs.koin)
    implementation(libs.koin.compose)

}