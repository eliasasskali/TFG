package com.eliasasskali.tfg.android.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.eliasasskali.tfg.android.ui.features.clubs.ClubsViewModel
import com.eliasasskali.tfg.android.ui.features.clubs.HomeScreen
import org.koin.androidx.compose.get

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
                    /*navController.navigate(
                        HomeRoutesClub.ClubDetail.routeName.plus(
                            "/$clubId"
                        )
                    )*/
                }
            )
        }
    }
}