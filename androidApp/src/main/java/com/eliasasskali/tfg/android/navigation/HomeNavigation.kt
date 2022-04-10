package com.eliasasskali.tfg.android.navigation

import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.eliasasskali.tfg.android.ui.components.TopBar
import com.eliasasskali.tfg.android.ui.features.clubDetail.ClubDetailScreen
import com.eliasasskali.tfg.android.ui.features.clubDetail.ClubDetailViewModel
import com.eliasasskali.tfg.android.ui.features.clubs.ClubsViewModel
import com.eliasasskali.tfg.android.ui.features.clubs.HomeScreen
import com.eliasasskali.tfg.android.ui.features.drawer.Drawer
import kotlinx.coroutines.CoroutineScope
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
            val scaffoldState =
                rememberScaffoldState(rememberDrawerState(initialValue = DrawerValue.Closed))
            val scope = rememberCoroutineScope()
            DrawerMenuScaffold(scaffoldState, scope, navController) {
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
                //onBackClicked = { navController.popBackStack() }
            )
        }
    }
}

@Composable
fun DrawerMenuScaffold(
    scaffoldState: ScaffoldState,
    scope: CoroutineScope,
    navController: NavHostController,
    content: @Composable () -> Unit
) {
    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopBar(scope = scope, scaffoldState = scaffoldState)
        },
        drawerBackgroundColor = MaterialTheme.colors.background,
        drawerContent = {
            Drawer(
                scope = scope,
                scaffoldState = scaffoldState,
                navController = navController
            )
        }) {
        content()
    }
}