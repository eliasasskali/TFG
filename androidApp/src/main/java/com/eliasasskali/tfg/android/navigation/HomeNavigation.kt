package com.eliasasskali.tfg.android.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.eliasasskali.tfg.android.ui.features.clubDetail.ClubDetailScreen
import com.eliasasskali.tfg.android.ui.features.clubDetail.ClubDetailViewModel
import com.eliasasskali.tfg.android.ui.features.clubs.ClubsViewModel
import com.eliasasskali.tfg.android.ui.features.clubs.HomeScreen
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
            val dataOrException = viewModel.state.value.data
            HomeScreen(
                dataOrException,
                viewModel,
                onClubClicked = { clubId ->
                    navController.navigate(
                        HomeRoutesClub.ClubDetail.routeName.plus(
                            "/$clubId"
                        )
                    )
                }
            )
        }

        composable(route = HomeRoutesClub.ClubDetail.routeName.plus("/{${HomeRoutesClub.ARG_PAYMENTMEAN_ID}}")) { entry ->
            val viewModel = getViewModel<ClubDetailViewModel>()
            val clubId = entry.arguments?.getString(HomeRoutesClub.ARG_PAYMENTMEAN_ID)
            if (clubId != null) {
                LaunchedEffect(Unit) {
                    viewModel.initClubDetailScreen(clubId)
                }
            }
            ClubDetailScreen(
                clubDetailState = viewModel.clubState.value,
                onBackClicked = { navController.popBackStack() }
            )
        }
    }
}