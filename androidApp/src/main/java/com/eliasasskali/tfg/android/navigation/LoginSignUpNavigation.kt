package com.eliasasskali.tfg.android.navigation

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.eliasasskali.tfg.android.data.repository.AuthRepository
import com.eliasasskali.tfg.android.ui.features.clubs.HomeActivity
import com.eliasasskali.tfg.android.ui.features.completeProfile.CompleteProfileActivity
import com.eliasasskali.tfg.android.ui.features.loginSignup.LoginScreen
import com.eliasasskali.tfg.android.ui.features.loginSignup.LoginSignupViewModel
import com.eliasasskali.tfg.android.ui.features.loginSignup.SignUpScreen
import com.eliasasskali.tfg.android.ui.features.splash.SplashActivity
import kotlinx.coroutines.runBlocking
import org.koin.androidx.compose.get
import org.koin.androidx.compose.getViewModel

@Composable
fun LoginSignUpNavigation(
    activity: Activity,
    navController: NavHostController,
    viewModel: LoginSignupViewModel = getViewModel(),
) {
    val startDestination = LoginSignUpRoutes.Login.routeName
    val authRepository: AuthRepository = get()

    fun onUserLogged() = runBlocking {
        authRepository.isProfileCompleted(
            onTrue = {
                //goToHome(context = activity)
                goToSplashScreen(context = activity)
            },
            onFalse = {
                //goToCompleteProfile(context = activity)
                goToSplashScreen(context = activity)
            }
        )
    }

    NavHost(navController = navController, startDestination = startDestination) {
        composable(route = LoginSignUpRoutes.Login.routeName) {
            LoginScreen(
                signUpLoginClick = {
                    viewModel.resetState()
                    navController.navigate(LoginSignUpRoutes.SignUp.routeName)
                },
                viewModel = viewModel,
                onUserLogged = { onUserLogged() }
            )
        }
        composable(route = LoginSignUpRoutes.SignUp.routeName) {
            SignUpScreen(
                viewModel = viewModel,
                onUserLogged = { onUserLogged() },
                onBackClicked = {
                    viewModel.resetState()
                    navController.popBackStack()
                }
            )
        }
    }
}

fun goToCompleteProfile(context: Activity) {
    context.startActivity(CompleteProfileActivity.intent(context))
    context.finish()
}

fun goToHome(context: Activity) {
    context.startActivity(HomeActivity.intent(context))
    context.finish()
}

fun goToSplashScreen(context: Activity) {
    context.startActivity(SplashActivity.intent(context))
    context.finish()
}