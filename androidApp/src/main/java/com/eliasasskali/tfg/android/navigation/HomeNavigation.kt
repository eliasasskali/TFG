package com.eliasasskali.tfg.android.navigation

import android.content.Context
import androidx.compose.material.Surface
import androidx.compose.material.Text
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
import com.eliasasskali.tfg.android.ui.features.editClubProfile.EditClubProfileScreen
import com.eliasasskali.tfg.android.ui.features.editClubProfile.EditClubProfileViewModel
import com.eliasasskali.tfg.model.Club
import com.google.gson.Gson
import org.koin.androidx.compose.get
import org.koin.androidx.compose.getViewModel
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@Composable
fun HomeNavigation(
    navController: NavHostController,
    context: Context
) {
    NavHost(
        navController = navController,
        startDestination = HomeRoutesClub.Home.routeName
    ) {

        composable(route = HomeRoutesClub.Notifications.routeName) {
            route?.let { it1 -> Text(text = it1) }
        }

        composable(route = HomeRoutesClub.Chats.routeName) {
            route?.let { it1 -> Text(text = it1) }
        }

        composable(route = HomeRoutesClub.Profile.routeName) {
            route?.let { it1 -> Text(text = it1) }
        }

        composable(route = HomeRoutesClub.Post.routeName) {
            route?.let { it1 -> Text(text = it1) }
        }

        composable(route = HomeRoutesClub.Home.routeName) {
            val viewModel: ClubsViewModel = get()
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
            var encodedJsonClub = jsonClub
            if (jsonClub != null) {
                var club = Gson().fromJson(jsonClub, Club::class.java)
                club = club.copy(images = club.images.map {
                    URLEncoder.encode(it, StandardCharsets.UTF_8.toString())
                })
                encodedJsonClub = Gson().toJson(club)
                LaunchedEffect(Unit) {
                    viewModel.initClubDetailScreen(club)
                    viewModel.isClubOwner(club.id)
                }
            }

            ClubDetailScreen(
                club = viewModel.clubState.value.club,
                distanceToClub = distanceToClub ?: "Unknown", // TODO: Check distanceToClub
                isClubOwner = viewModel.clubState.value.isClubOwner,
                onBackClicked = { navController.popBackStack() },
                onEditButtonClick = { // TODO: Go to edit profile screen
                    navController.navigate(HomeRoutesClub.EditClubProfile.routeName.plus("/$encodedJsonClub"))
                }
            )
        }

        composable(
            route = HomeRoutesClub.EditClubProfile.routeName.plus("/{${HomeRoutesClub.JSON_CLUB}}")
        ) { entry ->
            val viewModel = getViewModel<EditClubProfileViewModel>()
            val jsonClub = entry.arguments?.getString(HomeRoutesClub.JSON_CLUB)
            if (jsonClub != null) {
                val club = Gson().fromJson(jsonClub, Club::class.java)
                LaunchedEffect(Unit) {
                    viewModel.initEditClubDetailScreen(club, context)
                }
            }

            EditClubProfileScreen(
                viewModel = viewModel,
                onBackClicked = { navController.popBackStack() }
            )
        }
    }
}