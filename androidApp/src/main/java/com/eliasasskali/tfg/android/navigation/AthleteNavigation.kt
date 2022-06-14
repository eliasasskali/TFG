package com.eliasasskali.tfg.android.navigation

import android.app.Activity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.eliasasskali.tfg.android.ui.features.athleteProfile.AthleteProfileScreen
import com.eliasasskali.tfg.android.ui.features.athleteProfile.AthleteProfileViewModel
import com.eliasasskali.tfg.android.ui.features.bottomNavBar.BottomNavBar
import com.eliasasskali.tfg.android.ui.features.chat.ChatScreen
import com.eliasasskali.tfg.android.ui.features.chat.ChatViewModel
import com.eliasasskali.tfg.android.ui.features.chats.ChatsScreen
import com.eliasasskali.tfg.android.ui.features.chats.ChatsViewModel
import com.eliasasskali.tfg.android.ui.features.clubDetail.ClubDetailScreen
import com.eliasasskali.tfg.android.ui.features.clubDetail.ClubDetailViewModel
import com.eliasasskali.tfg.android.ui.features.clubs.ClubsScreen
import com.eliasasskali.tfg.android.ui.features.clubs.ClubsViewModel
import com.eliasasskali.tfg.android.ui.features.editAthleteProfile.EditAthleteProfileScreen
import com.eliasasskali.tfg.android.ui.features.editAthleteProfile.EditAthleteProfileViewModel
import com.eliasasskali.tfg.android.ui.features.postDetail.PostDetailScreen
import com.eliasasskali.tfg.android.ui.features.postDetail.PostDetailViewModel
import com.eliasasskali.tfg.android.ui.features.posts.PostsScreen
import com.eliasasskali.tfg.android.ui.features.posts.PostsViewModel
import com.eliasasskali.tfg.android.ui.features.reviews.ReviewsScreen
import com.eliasasskali.tfg.android.ui.features.reviews.ReviewsViewModel
import kotlinx.coroutines.CoroutineScope
import org.koin.androidx.compose.get
import org.koin.androidx.compose.getViewModel

@Composable
fun AthleteNavigation(
    navController: NavHostController,
    context: Activity,
) {
    NavHost(
        navController = navController,
        startDestination = HomeRoutesAthlete.Home.routeName
    ) {

        composable(route = HomeRoutesAthlete.Home.routeName) {
            val scaffoldState = rememberScaffoldState()

            NavDrawerScaffold(
                scaffoldState = scaffoldState,
                scope = rememberCoroutineScope(),
                navController = navController
            ) { paddingValues ->
                val viewModel: PostsViewModel = get()

                viewModel.initPostsScreen(listOf())

                PostsScreen(
                    viewModel = viewModel,
                    paddingValues = paddingValues,
                    onPostClicked = { post ->
                        navController.navigate(HomeRoutesAthlete.PostDetail.routeName.plus("/${post.postId}"))
                    },
                    onCreatePostClicked = {},
                    onFindClubsClicked = {
                        navController.navigate(HomeRoutesAthlete.Clubs.routeName) {
                            navController.graph.startDestinationRoute?.let { screen_route ->
                                popUpTo(screen_route) {
                                    saveState = true
                                }
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        }

        composable(route = HomeRoutesAthlete.PostDetail.routeName.plus("/{${HomeRoutesAthlete.POST_ID}}")) { entry ->
            val viewModel: PostDetailViewModel = get()
            val postId = entry.arguments?.getString(HomeRoutesAthlete.POST_ID)
            LaunchedEffect(Unit) {
                if (postId != null) {
                    viewModel.initPostDetailScreen(postId)
                }
            }

            PostDetailScreen(
                viewModel = viewModel,
                onPostDeleted = {
                    navController.navigate(HomeRoutesAthlete.Home.routeName)
                },
                onBackClicked = {
                    navController.popBackStack()
                }
            )
        }

        composable(route = HomeRoutesAthlete.Clubs.routeName) {
            val scaffoldState = rememberScaffoldState()

            NavDrawerScaffold(
                scaffoldState = scaffoldState,
                scope = rememberCoroutineScope(),
                navController = navController
            ) { paddingValues ->
                val viewModel: ClubsViewModel = get()
                Surface {
                    ClubsScreen(
                        viewModel,
                        onClubClicked = {
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
                            val clubId = it.id
                            navController.navigate(HomeRoutesAthlete.ClubDetail.routeName.plus("/$clubId/$distanceToClub"))
                        },
                        paddingValues = paddingValues
                    )
                }
            }
        }

        composable(
            route = HomeRoutesAthlete.ClubDetail.routeName.plus("/{${HomeRoutesAthlete.CLUB_ID}}/{${HomeRoutesAthlete.DISTANCE_TO_CLUB}}")
        ) { entry ->
            val viewModel = getViewModel<ClubDetailViewModel>()
            val clubId = entry.arguments?.getString(HomeRoutesAthlete.CLUB_ID) as String
            val distanceToClub = entry.arguments?.getString(HomeRoutesAthlete.DISTANCE_TO_CLUB)

            LaunchedEffect(Unit) {
                viewModel.initClubDetailScreen(clubId)
                viewModel.isClubOwner(clubId)
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
                        detailState = viewModel.state.value,
                        club = viewModel.state.value.club,
                        distanceToClub = distanceToClub ?: "Unknown", // TODO: Check distanceToClub
                        isClubOwner = false,
                        onBackClicked = { navController.popBackStack() },
                        onEditButtonClick = {},
                        onFollowButtonClick = { viewModel.followClub(viewModel.state.value.club.id) },
                        onUnfollowButtonClick = { viewModel.unFollowClub(viewModel.state.value.club.id) },
                        onChatButtonClick = {
                            viewModel.getOrCreateChatWithClub { chatId ->
                                navController.navigate(HomeRoutesAthlete.ChatDetail.routeName.plus("/${chatId}"))
                            }
                        }
                    )
                    1 -> {
                        val postsViewModel: PostsViewModel = get()

                        LaunchedEffect(Unit) {
                            postsViewModel.initPostsScreen(listOf(clubId))
                        }
                        PostsScreen(
                            viewModel = postsViewModel,
                            paddingValues = PaddingValues(0.dp),
                            onPostClicked = { post ->
                                navController.navigate(HomeRoutesAthlete.PostDetail.routeName.plus("/${post.postId}"))
                            },
                            onCreatePostClicked = {},
                            onFindClubsClicked = {
                                navController.navigate(HomeRoutesAthlete.Clubs.routeName) {
                                    navController.graph.startDestinationRoute?.let { screen_route ->
                                        popUpTo(screen_route) {
                                            saveState = true
                                        }
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        )
                    }
                    2 -> {
                        val reviewsViewModel: ReviewsViewModel = get()

                        LaunchedEffect(Unit) {
                            reviewsViewModel.initReviewsScreen(clubId)
                        }

                        ReviewsScreen(
                            viewModel = reviewsViewModel,
                            paddingValues = PaddingValues(0.dp)
                        )
                    }
                }
            }
        }

        composable(route = HomeRoutesAthlete.Chats.routeName) {
            val scaffoldState = rememberScaffoldState()

            NavDrawerScaffold(
                scaffoldState = scaffoldState,
                scope = rememberCoroutineScope(),
                navController = navController
            ) { paddingValues ->
                val viewModel: ChatsViewModel = get()
                println("chats")
                ChatsScreen(
                    viewModel = viewModel,
                    paddingValues = paddingValues,
                    onChatClicked = { chat ->
                        navController.navigate(HomeRoutesAthlete.ChatDetail.routeName.plus("/${chat.chatId}"))
                    },
                    onFindClubsClicked = {
                        navController.navigate(HomeRoutesAthlete.Clubs.routeName) {
                            navController.graph.startDestinationRoute?.let { screen_route ->
                                popUpTo(screen_route) {
                                    saveState = true
                                }
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        }

        composable(route = HomeRoutesAthlete.ChatDetail.routeName.plus("/{${HomeRoutesAthlete.CHAT_ID}}")) { entry ->
            val chatId = entry.arguments?.getString(HomeRoutesAthlete.CHAT_ID) as String
            val viewModel: ChatViewModel = get()
            LaunchedEffect(Unit) {
                viewModel.initChatScreen(chatId)
            }

            ChatScreen(
                viewModel = viewModel,
                onBackClicked = {
                    navController.popBackStack()
                }
            )
        }

        composable(route = HomeRoutesAthlete.Profile.routeName) {
            val scaffoldState = rememberScaffoldState()

            NavDrawerScaffold(
                scaffoldState = scaffoldState,
                scope = rememberCoroutineScope(),
                navController = navController
            ) { paddingValues ->
                val viewModel: AthleteProfileViewModel = get()
                viewModel.initAthleteProfileScreen()

                AthleteProfileScreen(
                    viewModel = viewModel,
                    paddingValues = paddingValues,
                    onLoggedOut = {
                        goToLogin(context)
                    },
                    onFollowingClubClicked = { clubId ->
                        // TODO: Distance to club
                        val distanceToClub = "TODO"
                        navController.navigate(HomeRoutesAthlete.ClubDetail.routeName.plus("/$clubId/$distanceToClub"))
                    },
                    onFindClubsClicked = {
                        navController.navigate(HomeRoutesAthlete.Clubs.routeName) {
                            navController.graph.startDestinationRoute?.let { screen_route ->
                                popUpTo(screen_route) {
                                    saveState = true
                                }
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    onEditProfileClicked = {
                        navController.navigate(HomeRoutesAthlete.EditAthleteProfile.routeName)
                    }
                )
            }
        }

        composable(
            route = HomeRoutesAthlete.EditAthleteProfile.routeName
        ) {
            val viewModel : EditAthleteProfileViewModel = get()
            LaunchedEffect(Unit) {
                viewModel.initEditAthleteProfileScreen()
            }

            EditAthleteProfileScreen(
                viewModel = viewModel,
                onBackClicked = {
                    navController.popBackStack()
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
        bottomBar = { BottomNavBar(navController = navController, isClub = false) },
        drawerBackgroundColor = MaterialTheme.colors.background
    ) { innerPadding ->
        content(innerPadding)
    }
}