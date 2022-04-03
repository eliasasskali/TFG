package com.eliasasskali.tfg.android.ui.features.splash


import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.ExperimentalComposeUiApi
import com.eliasasskali.tfg.android.navigation.SplashNavigation
import com.eliasasskali.tfg.android.ui.theme.AppTheme


class SplashActivity : AppCompatActivity() {

    @OptIn(ExperimentalComposeUiApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                SplashNavigation(this@SplashActivity)
            }
        }
    }
}