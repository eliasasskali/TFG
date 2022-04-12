package com.eliasasskali.tfg.android.ui.features.clubs

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.Scaffold
import androidx.navigation.compose.rememberNavController
import com.eliasasskali.tfg.android.navigation.HomeNavigation
import com.eliasasskali.tfg.android.ui.features.bottomNavBar.BottomNavBar
import com.eliasasskali.tfg.android.ui.theme.AppTheme

class HomeActivity : AppCompatActivity() {
    companion object {
        fun intent(context: Context): Intent = Intent(context, HomeActivity::class.java)
    }

    @SuppressLint("UnusedMaterialScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                val navController = rememberNavController()
                Scaffold(
                    bottomBar = { BottomNavBar(navController = navController) }
                ) {
                    HomeNavigation(navController)
                }
            }
        }
    }
}