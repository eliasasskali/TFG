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
import com.eliasasskali.tfg.android.ui.features.bottomNavBar.BottomNavBar
import com.eliasasskali.tfg.android.ui.features.chat.ChatScreen
import com.eliasasskali.tfg.android.ui.features.chat.ChatViewModel
import com.eliasasskali.tfg.android.ui.features.chats.ChatsScreen
import com.eliasasskali.tfg.android.ui.features.chats.ChatsViewModel
import com.eliasasskali.tfg.android.ui.features.clubDetail.ClubDetailScreen
import com.eliasasskali.tfg.android.ui.features.clubProfile.ClubProfileViewModel
import com.eliasasskali.tfg.android.ui.features.editClubProfile.EditClubProfileScreen
import com.eliasasskali.tfg.android.ui.features.editClubProfile.EditClubProfileViewModel
import com.eliasasskali.tfg.android.ui.features.post.PostScreen
import com.eliasasskali.tfg.android.ui.features.post.PostViewModel
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
fun ClubNavigation(
    navController: NavHostController,
    context: Activity,
) {
    NavHost(
        navController = navController,
        startDestination = HomeRoutesClub.Home.routeName
    ) {

        composable(route = HomeRoutesClub.Home.routeName) {
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
                        navController.navigate(HomeRoutesClub.PostDetail.routeName.plus("/${post.postId}"))
                    },
                    onCreatePostClicked = {
                        navController.navigate(HomeRoutesClub.Post.routeName) {
                            navController.graph.startDestinationRoute?.let { screen_route ->
                                popUpTo(screen_route) {
                                    saveState = true
                                }
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    onFindClubsClicked = {}
                )
            }
        }

        composable(route = HomeRoutesClub.PostDetail.routeName.plus("/{${HomeRoutesClub.POST_ID}}")) { entry ->
            val viewModel: PostDetailViewModel = get()
            val postId = entry.arguments?.getString(HomeRoutesClub.POST_ID)
            LaunchedEffect(Unit) {
                if (postId != null) {
                    viewModel.initPostDetailScreen(postId)
                }
            }

            PostDetailScreen(
                viewModel = viewModel,
                onPostDeleted = {
                    navController.navigate(HomeRoutesClub.Home.routeName)
                },
                onBackClicked = {
                    navController.popBackStack()
                }
            )
        }

        composable(route = HomeRoutesClub.Chats.routeName) {
            val scaffoldState = rememberScaffoldState()

            NavDrawerScaffold(
                scaffoldState = scaffoldState,
                scope = rememberCoroutineScope(),
                navController = navController
            ) { paddingValues ->
                val viewModel: ChatsViewModel = get()
                ChatsScreen(
                    viewModel = viewModel,
                    paddingValues = paddingValues,
                    onChatClicked = { chat ->
                        navController.navigate(HomeRoutesClub.ChatDetail.routeName.plus("/${chat.chatId}"))
                    }
                )
            }
        }

        composable(route = HomeRoutesClub.ChatDetail.routeName.plus("/{${HomeRoutesClub.CHAT_ID}}")) { entry ->
            val chatId = entry.arguments?.getString(HomeRoutesClub.CHAT_ID) as String
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
                    onPostCreated = {
                        navController.navigate(HomeRoutesClub.Home.routeName) {
                            navController.graph.startDestinationRoute?.let { screen_route ->
                                popUpTo(screen_route) {
                                    saveState = true
                                }
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    paddingValues = paddingValues
                )
            }
        }

        composable(route = HomeRoutesClub.Profile.routeName) {
            val viewModel: ClubProfileViewModel = get()
            LaunchedEffect(Unit) {
                viewModel.initClubProfile()
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
                    0 ->
                        ClubDetailScreen(
                            club = viewModel.state.value.club,
                            isClubOwner = true,
                            onBackClicked = { navController.popBackStack() },
                            onEditButtonClick = {
                                navController.navigate(HomeRoutesClub.EditClubProfile.routeName)
                            },
                            onLogOutButtonClick = {
                                viewModel.logOut {
                                    goToLogin(context)
                                }
                            }
                        )
                    1 -> {
                        val postsViewModel: PostsViewModel = get()

                        LaunchedEffect(Unit) {
                            postsViewModel.initPostsScreen(listOf(viewModel.state.value.club.id))
                        }
                        PostsScreen(
                            viewModel = postsViewModel,
                            paddingValues = PaddingValues(0.dp),
                            onPostClicked = { post ->
                                navController.navigate(HomeRoutesClub.PostDetail.routeName.plus("/${post.postId}"))
                            },
                            onCreatePostClicked = {
                                navController.navigate(HomeRoutesClub.Post.routeName) {
                                    navController.graph.startDestinationRoute?.let { screen_route ->
                                        popUpTo(screen_route) {
                                            saveState = true
                                        }
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            onFindClubsClicked = {}
                        )
                    }
                    2 -> {
                        val reviewsViewModel: ReviewsViewModel = get()

                        LaunchedEffect(Unit) {
                            reviewsViewModel.initReviewsScreen(viewModel.state.value.club.id)
                        }

                        ReviewsScreen(
                            viewModel = reviewsViewModel,
                            paddingValues = PaddingValues(0.dp)
                        )
                    }
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
        bottomBar = { BottomNavBar(navController = navController, isClub = true) },
        drawerBackgroundColor = MaterialTheme.colors.background
    ) { innerPadding ->
        content(innerPadding)
    }
}