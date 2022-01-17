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

    object Android {
        const val material = "com.google.android.material:material:${Versions.material}"
        const val appCompat = "androidx.appcompat:appcompat:${Versions.appcompat}"
        const val constraintLayout = "androidx.constraintlayout:constraintlayout:${Versions.constraintLayout}"
    }

    object Firebase {
        const val firebaseBoM = "com.google.firebase:firebase-bom:${Versions.firebaseBoM}"
        const val analytics = "com.google.firebase:firebase-analytics-ktx"
        const val crashlytics = "com.google.firebase:firebase-crashlytics-ktx"
        const val auth = "com.google.firebase:firebase-auth-ktx"
    }

    object Compose {
    }

    object Test {
    }

}