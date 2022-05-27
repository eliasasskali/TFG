package com.eliasasskali.tfg.android.ui.features.splash


import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.ExperimentalComposeUiApi
import com.eliasasskali.tfg.android.navigation.SplashNavigation
import com.eliasasskali.tfg.android.ui.theme.AppTheme

class SplashActivity : AppCompatActivity() {

    companion object {
        fun intent(context: Context): Intent = Intent(context, SplashActivity::class.java)
    }

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