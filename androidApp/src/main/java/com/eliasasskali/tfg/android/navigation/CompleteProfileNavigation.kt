package com.eliasasskali.tfg.android.navigation

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.eliasasskali.tfg.android.data.repository.authentication.AuthRepository
import com.eliasasskali.tfg.android.ui.features.completeProfile.*
import com.eliasasskali.tfg.android.ui.features.completeProfile.completeProfileClub.CompleteClubProfileScreenFirst
import com.eliasasskali.tfg.android.ui.features.completeProfile.completeProfileClub.CompleteClubProfileScreenSecond
import com.eliasasskali.tfg.android.ui.features.completeProfile.completeProfileClub.CompleteProfileMapScreen
import kotlinx.coroutines.runBlocking
import org.koin.androidx.compose.getViewModel

@Composable
fun CompleteProfileNavigation(
    activity: Activity,
    authRepository: AuthRepository,
    navController: NavHostController
) {
    val viewModel: CompleteProfileViewModel = getViewModel()

    fun onProfileCompleted() = runBlocking {
        authRepository.isProfileCompleted(
            onTrue = {
                goToHome(context = activity)
            },
            onFalse = {
                goToCompleteProfile(context = activity)
            }
        )
    }

    NavHost(
        navController = navController,
        startDestination = CompleteProfileRoutes.ChooseUserType.routeName
    ) {
        composable(route = CompleteProfileRoutes.ChooseUserType.routeName) {
            ChooseProfileTypeScreen(
                athleteButtonClick = {
                    navController.navigate(CompleteProfileRoutes.AthleteCompleteProfile.routeName)
                },
                clubButtonClick = {
                    viewModel.state.value = viewModel.state.value.copy(isClub = true)
                    navController.navigate(CompleteProfileRoutes.ClubCompleteProfileFirst.routeName)
                }
            )
        }

        composable(route = CompleteProfileRoutes.AthleteCompleteProfile.routeName) {
            CompleteAthleteProfileScreen(
                onContinueButtonClick = {
                    viewModel.completeProfile(onCompleteProfileSuccess = { onProfileCompleted() })
                },
                viewModel = viewModel,
                onBackClicked = {
                    viewModel.resetState()
                    navController.popBackStack()
                }
            )
        }

        composable(route = CompleteProfileRoutes.ClubCompleteProfileFirst.routeName) {
            CompleteClubProfileScreenFirst(
                viewModel = viewModel,
                onContinueButtonClick = {
                    navController.navigate(CompleteProfileRoutes.ClubCompleteProfileSecond.routeName)
                },
                onBackClicked = {
                    viewModel.resetState()
                    navController.popBackStack()
                }
            )
        }

        composable(route = CompleteProfileRoutes.ClubCompleteProfileSecond.routeName) {
            CompleteClubProfileScreenSecond(
                viewModel = viewModel,
                onContinueButtonClick = {
                    navController.navigate(CompleteProfileRoutes.ClubCompleteProfileMap.routeName)
                },
                onBackClicked = {
                    navController.popBackStack()
                }
            )
        }

        composable(route = CompleteProfileRoutes.ClubCompleteProfileMap.routeName) {
            CompleteProfileMapScreen(
                onContinueButtonClick = {
                    viewModel.completeProfile(onCompleteProfileSuccess = { onProfileCompleted() })
                },
                viewModel = viewModel,
                onBackClicked = { navController.popBackStack() }
            )
        }
    }
}