plugins {
    id("com.android.application")
    kotlin("android")

    // Google services
    id("com.google.gms.google-services")

    // Crashlytics plugin
    id("com.google.firebase.crashlytics")

    //Secrets to store api keys in local.properties
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")

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

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = Versions.compose
    }
}

dependencies {
    implementation(project(":shared"))
    implementation("com.google.accompanist:accompanist-pager-indicators:0.24.4-alpha")

    with(Dependencies.Android) {
        implementation(material)
        implementation(appCompat)
        implementation(constraintLayout)
        implementation(kotlinxCoroutines)

        implementation(playServicesMaps)
        implementation(maps)
        implementation(mapsKtx)
        implementation(playServicesLocation)
        constraints {
            // Volley is a transitive dependency of maps
            implementation(volley) {
                because("Only volley 1.2.0 or newer are available on maven.google.com")
            }
        }

    }

    with(Dependencies.DI) {
        implementation(koinCore)
        implementation(koinAndroid)
        implementation(koinCompose)
    }

    with(Dependencies.Compose) {
        implementation(ui)
        implementation(uiGraphics)
        implementation(uiTooling)
        implementation(foundationLayout)
        implementation(materialCompose)
        implementation(navigation)
        implementation(accompanistPager)
        implementation(coilCompose)
        implementation(mapsCompose)
        implementation(pagingCompose)
    }

    with(Dependencies.Firebase) {
        // Import firebase BoM (chooses firebase versions automatically)
        implementation(platform(firebaseBoM))

        implementation(analytics)
        implementation(crashlytics)
        implementation(auth)
        implementation(authUi)
        implementation(firestore)
        implementation(storage)
    }
}