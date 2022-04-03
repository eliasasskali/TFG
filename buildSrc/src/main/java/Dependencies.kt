/**
 * To define plugins
 */
object BuildPlugins {
    val kotlin by lazy { "org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.kotlin}" }
    val android by lazy { "com.android.tools.build:gradle:${Versions.gradlePlugin}" }
    val googleServices by lazy { "com.google.gms:google-services:${Versions.googleServices}" }
    val crashlytics by lazy { "com.google.firebase:firebase-crashlytics-gradle:${Versions.crashlyticsPlugin}" }
}

/**
 * To define dependencies
 */
object Dependencies {
    object Root {
        const val android = "com.android.tools.build:gradle:7.0.2"
        const val google = "com.google.gms:google-services:4.3.10"
    }

    object Main {
        const val coroutines = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.coroutines}"
    }
    object Android {
        const val material = "com.google.android.material:material:${Versions.material}"
        const val appCompat = "androidx.appcompat:appcompat:${Versions.appcompat}"
        const val constraintLayout = "androidx.constraintlayout:constraintlayout:${Versions.constraintLayout}"
        const val kotlinxCoroutines = "org.jetbrains.kotlinx:kotlinx-coroutines-play-services:${Versions.kotlinxCoroutines}"
        const val coroutines = "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.coroutinesAndroid}"

        // Google Maps
        const val secrets = "com.google.android.libraries.mapsplatform.secrets-gradle-plugin:secrets-gradle-plugin:${Versions.secrets}"
        const val maps = "com.google.android.libraries.maps:maps:${Versions.maps}"
        const val mapsKtx = "com.google.maps.android:maps-v3-ktx:${Versions.mapsKtx}"
        const val playServicesMaps = "com.google.android.gms:play-services-maps:${Versions.playServicesMaps}"
        const val playServicesLocation = "com.google.android.gms:play-services-location:${Versions.playServiceslocation}"
        const val volley = "com.android.volley:volley:${Versions.volley}"

    }

    object Compose {
        const val ui = "androidx.compose.ui:ui:${Versions.compose}"
        const val uiGraphics = "androidx.compose.ui:ui-graphics:${Versions.compose}"
        const val uiTooling = "androidx.compose.ui:ui-tooling:${Versions.compose}"
        const val foundationLayout = "androidx.compose.foundation:foundation-layout:${Versions.compose}"
        const val materialCompose = "androidx.compose.material:material:${Versions.compose}"
        const val navigation = "androidx.navigation:navigation-compose:${Versions.navCompose}"

        const val accompanistPager = "com.google.accompanist:accompanist-pager:${Versions.accompanistPager}"
    }

    object Firebase {
        const val firebaseBoM = "com.google.firebase:firebase-bom:${Versions.firebaseBoM}"
        const val analytics = "com.google.firebase:firebase-analytics-ktx"
        const val crashlytics = "com.google.firebase:firebase-crashlytics-ktx"
        const val auth = "com.google.firebase:firebase-auth-ktx"
        const val database = "com.google.firebase:firebase-database"
        const val firestore = "com.google.firebase:firebase-firestore-ktx"
        const val storage = "com.google.firebase:firebase-storage"
        const val authUi = "com.firebaseui:firebase-ui-auth:${Versions.authUi}"
    }

    object DI {
        const val koinCore = "io.insert-koin:koin-core:${Versions.koin}"
        const val koinAndroid = "io.insert-koin:koin-android:${Versions.koin}"
        const val koinExt = "io.insert-koin:koin-android-ext:${Versions.koin}"
        const val koinCompose = "io.insert-koin:koin-androidx-compose:${Versions.koin}"
    }
}