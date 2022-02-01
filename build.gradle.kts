buildscript {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
    dependencies {
        classpath(BuildPlugins.kotlin)
        classpath(BuildPlugins.android)
        classpath(BuildPlugins.googleServices)
        classpath(BuildPlugins.crashlytics)
        classpath(Dependencies.Android.secrets)
    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}