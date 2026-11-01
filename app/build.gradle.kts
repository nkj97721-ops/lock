plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.locknow.app11020448"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.locknow.app11020448"
        minSdk = 26
        targetSdk = 35
        versionCode = 11020448
        versionName = "11020448"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
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
    // 13.1.1 made Shizuku.newProcess inaccessible; 13.1.0 retains the public API used here.
    implementation("dev.rikka.shizuku:api:13.1.0")
    implementation("dev.rikka.shizuku:provider:13.1.0")
}
