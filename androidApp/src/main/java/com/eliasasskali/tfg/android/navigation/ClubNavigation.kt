package com.eliasasskali.tfg.android.navigation

import android.content.Context
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.*
import androidx.compose.runtime.*
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
import com.eliasasskali.tfg.model.Post
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import org.koin.androidx.compose.get
import org.koin.androidx.compose.getViewModel

@Composable
fun ClubNavigation(
    navController: NavHostController,
    context: Context,
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
                        val jsonPost = Gson().toJson(post)
                        navController.navigate(HomeRoutesClub.PostDetail.routeName.plus("/$jsonPost"))
                    },
                    onCreatePostClicked = {
                        //navController.navigate(HomeRoutesClub.Post.routeName)
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

        composable(route = HomeRoutesClub.PostDetail.routeName.plus("/{${HomeRoutesClub.JSON_POST}}")) { entry ->
            val viewModel: PostDetailViewModel = get()
            val jsonPost = entry.arguments?.getString(HomeRoutesClub.JSON_POST)
            val post = Gson().fromJson(jsonPost, Post::class.java)
            LaunchedEffect(Unit) {
                viewModel.initPostDetailScreen(post)
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