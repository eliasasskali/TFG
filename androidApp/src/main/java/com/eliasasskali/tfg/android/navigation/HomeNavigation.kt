package com.eliasasskali.tfg.android.navigation

import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.eliasasskali.tfg.android.ui.features.bottomNavBar.BottomNavBar
import com.eliasasskali.tfg.android.ui.features.clubDetail.ClubDetailScreen
import com.eliasasskali.tfg.android.ui.features.clubDetail.ClubDetailViewModel
import com.eliasasskali.tfg.android.ui.features.clubProfile.ClubProfileViewModel
import com.eliasasskali.tfg.android.ui.features.clubs.ClubsViewModel
import com.eliasasskali.tfg.android.ui.features.clubs.HomeScreen
import com.eliasasskali.tfg.android.ui.features.editClubProfile.EditClubProfileScreen
import com.eliasasskali.tfg.android.ui.features.editClubProfile.EditClubProfileViewModel
import com.eliasasskali.tfg.android.ui.features.post.PostScreen
import com.eliasasskali.tfg.android.ui.features.post.PostViewModel
import com.eliasasskali.tfg.android.ui.features.postDetail.PostDetailScreen
import com.eliasasskali.tfg.android.ui.features.postDetail.PostDetailViewModel
import com.eliasasskali.tfg.android.ui.features.posts.PostsScreen
import com.eliasasskali.tfg.android.ui.features.posts.PostsViewModel
import com.eliasasskali.tfg.model.Club
import com.eliasasskali.tfg.model.Post
import com.google.firebase.auth.FirebaseAuth
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

            NavDrawerScaffold(
                scaffoldState = scaffoldState,
                scope = rememberCoroutineScope(),
                navController = navController
            ) { paddingValues ->
                val viewModel: PostsViewModel = get()

                LaunchedEffect(Unit) {
                    val uid = FirebaseAuth.getInstance().currentUser?.uid ?: ""
                    viewModel.initPostsScreen(listOf(uid))
                }
                PostsScreen(
                    viewModel = viewModel,
                    paddingValues = paddingValues,
                    onPostClicked = { post ->
                        val jsonPost = Gson().toJson(post)
                        navController.navigate(HomeRoutesClub.PostDetail.routeName.plus("/$jsonPost"))
                    }
                )
            }
        }

        composable(route = HomeRoutesClub.PostDetail.routeName.plus("/{${HomeRoutesClub.JSON_POST}}")) { entry ->
            val scaffoldState = rememberScaffoldState()

            NavDrawerScaffold(
                scaffoldState = scaffoldState,
                scope = rememberCoroutineScope(),
                navController = navController
            ) { paddingValues ->
                val viewModel: PostDetailViewModel = get()
                val jsonPost = entry.arguments?.getString(HomeRoutesClub.JSON_POST)
                val post = Gson().fromJson(jsonPost, Post::class.java)
                LaunchedEffect(Unit) {
                    viewModel.initPostDetailScreen(post)
                }

                PostDetailScreen(
                    viewModel = viewModel,
                    paddingValues = paddingValues,
                    onPostDeleted = {
                        navController.navigate(HomeRoutesClub.Home.routeName)
                    }
                )
            }
        }

        composable(route = HomeRoutesClub.Chats.routeName) {
            val scaffoldState = rememberScaffoldState()

            NavDrawerScaffold(
                scaffoldState = scaffoldState,
                scope = rememberCoroutineScope(),
                navController = navController
            ) { paddingValues ->
                route?.let { it1 -> Text(text = it1) }
            }
        }

        composable(route = HomeRoutesClub.Profile.routeName) {
            val scaffoldState = rememberScaffoldState()

            NavDrawerScaffold(
                scaffoldState = scaffoldState,
                scope = rememberCoroutineScope(),
                navController = navController
            ) { paddingValues ->
                val viewModel: ClubProfileViewModel = get()
                viewModel.initClubProfile()

                ClubDetailScreen(
                    club = viewModel.state.value.club,
                    isClubOwner = true,
                    onBackClicked = { navController.popBackStack() },
                    onEditButtonClick = {
                        navController.navigate(HomeRoutesClub.EditClubProfile.routeName)
                    },
                    paddingValues = paddingValues
                )
            }
        }

        composable(route = HomeRoutesClub.Post.routeName) {
            val scaffoldState = rememberScaffoldState()

            NavDrawerScaffold(
                scaffoldState = scaffoldState,
                scope = rememberCoroutineScope(),
                navController = navController
            ) { paddingValues ->
                val viewModel: PostViewModel = get()
                PostScreen(
                    viewModel = viewModel,
                    onPostCreated = {},
                    paddingValues = paddingValues
                )
            }
        }

        composable(route = HomeRoutesClub.Home.routeName) {
            val scaffoldState = rememberScaffoldState()

            NavDrawerScaffold(
                scaffoldState = scaffoldState,
                scope = rememberCoroutineScope(),
                navController = navController
            ) { paddingValues ->
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
            var club = Club()
            jsonClub?.let { json ->
                club = Gson().fromJson(json, Club::class.java)
                club = club.copy(images = club.images.map {
                    URLEncoder.encode(it, StandardCharsets.UTF_8.toString())
                })
                encodedJsonClub = Gson().toJson(club)
                LaunchedEffect(Unit) {
                    viewModel.initClubDetailScreen(club)
                    viewModel.isClubOwner(club.id)
                }
            }

            var tabIndex by remember { mutableStateOf(0) }
            val tabTitles = listOf("Club Detail", "Posts", "Reviews")
            Column {
                TabRow(selectedTabIndex = tabIndex) {
                    tabTitles.forEachIndexed { index, title ->
                        Tab(selected = tabIndex == index,
                            onClick = { tabIndex = index },
                            text = { Text(text = title) })
                    }
                }
                when (tabIndex) {
                    0 -> ClubDetailScreen(
                        club = viewModel.clubState.value.club,
                        distanceToClub = distanceToClub ?: "Unknown", // TODO: Check distanceToClub
                        isClubOwner = viewModel.clubState.value.isClubOwner,
                        onBackClicked = { navController.popBackStack() },
                        onEditButtonClick = {
                            navController.navigate(HomeRoutesClub.EditClubProfile.routeName)
                        }
                    )
                    1 -> {
                        val postsViewModel: PostsViewModel = get()

                        LaunchedEffect(Unit) {
                            postsViewModel.initPostsScreen(listOf(club.id))
                        }
                        PostsScreen(
                            viewModel = postsViewModel,
                            paddingValues = PaddingValues(0.dp),
                            onPostClicked = { post ->
                                val jsonPost = Gson().toJson(post)
                                navController.navigate(HomeRoutesClub.PostDetail.routeName.plus("/$jsonPost"))
                            }
                        )
                    }
                    2 -> Text("Club Reviews content")
                }
            }


        }

        composable(
            route = HomeRoutesClub.EditClubProfile.routeName
        ) {
            val viewModel = getViewModel<EditClubProfileViewModel>()
            LaunchedEffect(Unit) {
                viewModel.initEditClubDetailScreen(context)
            }

            EditClubProfileScreen(
                viewModel = viewModel,
                onBackClicked = {
                    navController.popBackStack()
                },
                onProfileUpdated = {
                    viewModel.updatePreferencesClub(viewModel.state.value.club.id)
                }
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