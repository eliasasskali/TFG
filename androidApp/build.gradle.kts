plugins {
    id("com.android.application")
    kotlin("android")

    // Google services
    id("com.google.gms.google-services")

    // Crashlytics plugin
    id("com.google.firebase.crashlytics")
}

android {
    compileSdk = ConfigData.compileSdkVersion
    defaultConfig {
        applicationId = "com.eliasasskali.tfg.android"
        minSdk = ConfigData.minSdkVersion
        targetSdk = ConfigData.targetSdkVersion
        versionCode = ConfigData.versionCode
        versionName = ConfigData.versionName
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
}

dependencies {
    implementation(project(":shared"))

    with(Dependencies.Android) {
        implementation(material)
        implementation(appCompat)
        implementation(constraintLayout)
    }

    with(Dependencies.Firebase) {
        // Import firebase BoM (chooses firebase versions automatically)
        implementation(platform(firebaseBoM))

        implementation(analytics)
        implementation(crashlytics)
        implementation(auth)
    }

}