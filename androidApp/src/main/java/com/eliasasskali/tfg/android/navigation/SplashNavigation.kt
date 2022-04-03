package com.eliasasskali.tfg.android.navigation

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.eliasasskali.tfg.android.ui.features.loginSignup.MainActivity
import com.eliasasskali.tfg.android.ui.features.splash.Screen
import com.eliasasskali.tfg.android.ui.features.splash.SplashScreen
import com.eliasasskali.tfg.android.ui.features.splash.SplashViewModel
import org.koin.androidx.compose.getViewModel

@ExperimentalComposeUiApi
@Composable
fun SplashNavigation(activity: Activity) {
    val navController = rememberNavController()
    val viewModel = getViewModel<SplashViewModel>()
    NavHost(navController = navController, startDestination = SplashRoutes.Splash.routeName) {
        composable(route = SplashRoutes.Splash.routeName) {
            SplashScreen(
                viewModel.state.value,
                viewModel,
                onSplashComplete = {
                    when (it.screen) {
                        Screen.LOGIN -> goToLogin(context = activity)
                        Screen.HOME -> goToHome(context = activity)
                        Screen.COMPLETE_PROFILE -> goToCompleteProfile(context = activity)
                    }
                }
            )
        }
    }
}

fun goToLogin(context: Activity) {
    context.startActivity(MainActivity.intent(context))
    context.finish()
}
