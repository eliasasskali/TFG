plugins {
    id("com.android.application")
    kotlin("android")

    // Google services
    id("com.google.gms.google-services")

    // Crashlytics plugin
    id("com.google.firebase.crashlytics")
}

android {
    compileSdk = 31
    defaultConfig {
        applicationId = "com.eliasasskali.tfg.android"
        minSdk = 21
        targetSdk = 31
        versionCode = 1
        versionName = "1.0"
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
}

dependencies {
    implementation(project(":shared"))
    implementation("com.google.android.material:material:1.5.0")
    implementation("androidx.appcompat:appcompat:1.4.1")
    implementation("androidx.constraintlayout:constraintlayout:2.1.3")

    // Import firebase BoM (chooses firebase versions automatically)
    implementation(platform("com.google.firebase:firebase-bom:29.0.3"))

    // Dependency for Firebase SDK for Google Analytics, no need to specify version (BoM)
    implementation("com.google.firebase:firebase-analytics-ktx")
    // Firebase Crashlytics dependency
    implementation("com.google.firebase:firebase-crashlytics-ktx")
    // Firebase Authentication dependency
    implementation("com.google.firebase:firebase-auth-ktx")
}