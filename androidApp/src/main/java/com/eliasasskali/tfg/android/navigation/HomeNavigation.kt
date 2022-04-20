package com.eliasasskali.tfg.android.navigation

import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.eliasasskali.tfg.android.ui.features.clubDetail.ClubDetailScreen
import com.eliasasskali.tfg.android.ui.features.clubDetail.ClubDetailViewModel
import com.eliasasskali.tfg.android.ui.features.clubs.ClubsViewModel
import com.eliasasskali.tfg.android.ui.features.clubs.HomeScreen
import com.eliasasskali.tfg.model.Club
import com.google.gson.Gson
import org.koin.androidx.compose.get
import org.koin.androidx.compose.getViewModel

@Composable
fun HomeNavigation(
    navController: NavHostController
) {
    val viewModel: ClubsViewModel = get()
    NavHost(
        navController = navController,
        startDestination = HomeRoutesClub.Home.routeName
    ) {
        composable(route = HomeRoutesClub.Home.routeName) {
            Surface {
                HomeScreen(
                    viewModel,
                    onClubClicked = {
                        val jsonClub = Gson().toJson(it)
                        val distanceToClub = if (viewModel.state.value.filterLocation.latitude != 0.0) {
                            viewModel.distanceToClub(it.location, viewModel.state.value.filterLocation)
                        } else {
                            viewModel.distanceToClub(it.location, viewModel.state.value.userLocation)
                        }

                        navController.navigate(HomeRoutesClub.ClubDetail.routeName.plus("/$jsonClub/$distanceToClub"))
                    }
                )
            }
        }

        composable(
            route = HomeRoutesClub.ClubDetail.routeName.plus("/{${HomeRoutesClub.JSON_CLUB}}/{${HomeRoutesClub.DISTANCE_TO_CLUB}}")
        ) { entry ->
            val viewModel = getViewModel<ClubDetailViewModel>()
            val jsonClub = entry.arguments?.getString(HomeRoutesClub.JSON_CLUB)
            val distanceToClub = entry.arguments?.getString(HomeRoutesClub.DISTANCE_TO_CLUB)
            if (jsonClub != null) {
                val club = Gson().fromJson(jsonClub, Club::class.java)
                LaunchedEffect(Unit) {
                    viewModel.initClubDetailScreen(club)
                    viewModel.isClubOwner(club.id)
                    if (distanceToClub != null) {
                        viewModel.setDistanceToClub(distanceToClub)
                    }
                }
            }
            ClubDetailScreen(
                clubDetailState = viewModel.clubState.value,
                onBackClicked = { navController.popBackStack() },
                viewModel = viewModel
            )
        }
    }
}