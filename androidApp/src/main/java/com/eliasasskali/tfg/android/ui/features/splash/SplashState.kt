package com.eliasasskali.tfg.android.ui.features.splash

data class SplashState(
    val screen: Screen,
)

enum class Screen {
    LOGIN,
    HOME,
    COMPLETE_PROFILE
}