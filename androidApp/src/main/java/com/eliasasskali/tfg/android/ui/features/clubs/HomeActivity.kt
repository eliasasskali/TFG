package com.eliasasskali.tfg.android.ui.features.clubs

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.compose.rememberNavController
import com.eliasasskali.tfg.android.navigation.HomeNavigation
import com.eliasasskali.tfg.android.ui.theme.AppTheme

class HomeActivity: AppCompatActivity() {
    companion object {
        fun intent(context: Context): Intent = Intent(context, HomeActivity::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                val navController = rememberNavController()
                HomeNavigation(navController)
            }
        }
    }
}