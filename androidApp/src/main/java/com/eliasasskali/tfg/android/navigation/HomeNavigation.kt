package com.eliasasskali.tfg.android.navigation

import android.content.Context
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.eliasasskali.tfg.android.ui.features.bottomNavBar.BottomNavBar
import com.eliasasskali.tfg.android.ui.features.clubDetail.ClubDetailScreen
import com.eliasasskali.tfg.android.ui.features.clubDetail.ClubDetailViewModel
import com.eliasasskali.tfg.android.ui.features.clubProfile.ClubProfileViewModel
import com.eliasasskali.tfg.android.ui.features.clubs.ClubsViewModel
import com.eliasasskali.tfg.android.ui.features.clubs.HomeScreen
import com.eliasasskali.tfg.android.ui.features.editClubProfile.EditClubProfileScreen
import com.eliasasskali.tfg.android.ui.features.editClubProfile.EditClubProfileViewModel
import com.eliasasskali.tfg.model.Club
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import org.koin.androidx.compose.get
import org.koin.androidx.compose.getViewModel
import java.net.URLDecoder
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@Composable
fun HomeNavigation(
    navController: NavHostController,
    context: Context,
) {
    NavHost(
        navController = navController,
        startDestination = HomeRoutesClub.Home.routeName
    ) {

        composable(route = HomeRoutesClub.Notifications.routeName) {
            val scaffoldState = rememberScaffoldState()

            NavDrawerScaffold(scaffoldState = scaffoldState, scope = rememberCoroutineScope(), navController = navController) { paddingValues ->
                route?.let { it1 -> Text(text = it1) }
            }
        }

        composable(route = HomeRoutesClub.Chats.routeName) {
            val scaffoldState = rememberScaffoldState()

            NavDrawerScaffold(scaffoldState = scaffoldState, scope = rememberCoroutineScope(), navController = navController) { paddingValues ->
                route?.let { it1 -> Text(text = it1) }
            }
        }

        composable(route = HomeRoutesClub.Profile.routeName) {
            val scaffoldState = rememberScaffoldState()

            NavDrawerScaffold(scaffoldState = scaffoldState, scope = rememberCoroutineScope(), navController = navController) { paddingValues ->
                val viewModel: ClubProfileViewModel = get()
                LaunchedEffect(Unit) {
                    viewModel.initClubProfile()
                }
                ClubDetailScreen(
                    club = viewModel.state.value.club,
                    isClubOwner = true,
                    onBackClicked = { navController.popBackStack() },
                    onEditButtonClick = {
                        val club =
                            viewModel.state.value.club.copy(images = viewModel.state.value.club.images.map {
                                URLEncoder.encode(it, StandardCharsets.UTF_8.toString())
                            })
                        val encodedJsonClub = Gson().toJson(club)
                        navController.navigate(HomeRoutesClub.EditClubProfile.routeName.plus("/$encodedJsonClub"))
                    },
                    paddingValues = paddingValues
                )
            }
        }

        composable(route = HomeRoutesClub.Post.routeName) {
            val scaffoldState = rememberScaffoldState()

            NavDrawerScaffold(scaffoldState = scaffoldState, scope = rememberCoroutineScope(), navController = navController) { paddingValues ->
                route?.let { it1 -> Text(text = it1) }
            }
        }

        composable(route = HomeRoutesClub.Home.routeName) {
            val scaffoldState = rememberScaffoldState()

            NavDrawerScaffold(scaffoldState = scaffoldState, scope = rememberCoroutineScope(), navController = navController) { paddingValues ->
                val viewModel: ClubsViewModel = get()
                Surface {
                    HomeScreen(
                        viewModel,
                        onClubClicked = {
                            val jsonClub = Gson().toJson(it)
                            val distanceToClub =
                                if (viewModel.state.value.filterLocation.latitude != 0.0) {
                                    viewModel.distanceToClub(
                                        it.location,
                                        viewModel.state.value.filterLocation
                                    )
                                } else {
                                    viewModel.distanceToClub(
                                        it.location,
                                        viewModel.state.value.userLocation
                                    )
                                }

                            navController.navigate(HomeRoutesClub.ClubDetail.routeName.plus("/$jsonClub/$distanceToClub"))
                        },
                        paddingValues = paddingValues
                    )
                }
            }
        }

        composable(
            route = HomeRoutesClub.ClubDetail.routeName.plus("/{${HomeRoutesClub.JSON_CLUB}}/{${HomeRoutesClub.DISTANCE_TO_CLUB}}")
        ) { entry ->
            val viewModel = getViewModel<ClubDetailViewModel>()
            val jsonClub = entry.arguments?.getString(HomeRoutesClub.JSON_CLUB)
            val distanceToClub = entry.arguments?.getString(HomeRoutesClub.DISTANCE_TO_CLUB)
            var encodedJsonClub = ""
            jsonClub?.let { json ->
                var club = Gson().fromJson(json, Club::class.java)
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
                onEditButtonClick = {
                    navController.navigate(HomeRoutesClub.EditClubProfile.routeName.plus("/$encodedJsonClub"))
                }
            )
        }

        composable(
            route = HomeRoutesClub.EditClubProfile.routeName.plus("/{${HomeRoutesClub.EDIT_JSON_CLUB}}")
        ) { entry ->
            val viewModel = getViewModel<EditClubProfileViewModel>()
            val jsonClub = entry.arguments?.getString(HomeRoutesClub.EDIT_JSON_CLUB)
            jsonClub?.let {
                var club = Gson().fromJson(it, Club::class.java)
                club = club.copy(images = club.images.map { encodedUrl ->
                    if (!encodedUrl.contains("//")) {
                        URLDecoder.decode(encodedUrl, StandardCharsets.UTF_8.toString())
                    } else {
                        encodedUrl
                    }
                })
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

@Composable
private fun NavDrawerScaffold(
    scaffoldState: ScaffoldState,
    scope: CoroutineScope,
    navController: NavHostController,
    content: @Composable (PaddingValues) -> Unit
) {
    Scaffold(
        scaffoldState = scaffoldState,
        bottomBar = { BottomNavBar(navController = navController) },
        drawerBackgroundColor = MaterialTheme.colors.background
    ) { innerPadding ->
        content(innerPadding)
    }
}