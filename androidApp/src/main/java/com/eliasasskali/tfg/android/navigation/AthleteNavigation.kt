package com.eliasasskali.tfg.android.navigation

import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
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
import com.eliasasskali.tfg.android.ui.features.clubDetail.ClubDetailViewModel
import com.eliasasskali.tfg.android.ui.features.clubs.ClubsViewModel
import com.eliasasskali.tfg.android.ui.features.clubs.ClubsScreen
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
fun AthleteNavigation(
    navController: NavHostController,
    context: Context,
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
                        val jsonPost = Gson().toJson(post)
                        navController.navigate(HomeRoutesAthlete.PostDetail.routeName.plus("/$jsonPost"))
                    }
                )
            }
        }

        composable(route = HomeRoutesAthlete.PostDetail.routeName.plus("/{${HomeRoutesAthlete.JSON_POST}}")) { entry ->
            val scaffoldState = rememberScaffoldState()

            NavDrawerScaffold(
                scaffoldState = scaffoldState,
                scope = rememberCoroutineScope(),
                navController = navController
            ) { paddingValues ->
                val viewModel: PostDetailViewModel = get()
                val jsonPost = entry.arguments?.getString(HomeRoutesAthlete.JSON_POST)
                val post = Gson().fromJson(jsonPost, Post::class.java)
                LaunchedEffect(Unit) {
                    viewModel.initPostDetailScreen(post)
                }

                PostDetailScreen(
                    viewModel = viewModel,
                    paddingValues = paddingValues,
                    onPostDeleted = {
                        navController.navigate(HomeRoutesAthlete.Home.routeName)
                    }
                )
            }
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
                            //val jsonClub = Gson().toJson(it)
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
                        detailState = viewModel.clubState.value,
                        club = viewModel.clubState.value.club,
                        distanceToClub = distanceToClub ?: "Unknown", // TODO: Check distanceToClub
                        isClubOwner = false,
                        onBackClicked = { navController.popBackStack() },
                        onEditButtonClick = {},
                        onFollowButtonClick = { viewModel.followClub(viewModel.clubState.value.club.id) },
                        onUnfollowButtonClick = { viewModel.unFollowClub(viewModel.clubState.value.club.id) }
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
                                val jsonPost = Gson().toJson(post)
                                navController.navigate(HomeRoutesAthlete.PostDetail.routeName.plus("/$jsonPost"))
                            }
                        )
                    }
                    2 -> Text("Club Reviews content: NOT IMPLEMENTED")
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
                ChatsScreen(
                    viewModel = viewModel,
                    paddingValues = paddingValues,
                    onChatClicked = { chat ->
                        navController.navigate(HomeRoutesAthlete.ChatDetail.routeName.plus("/${chat.chatId}"))
                    }
                )
            }
        }

        composable(route = HomeRoutesAthlete.ChatDetail.routeName.plus("/{${HomeRoutesAthlete.CHAT_ID}}")) { entry ->
            val scaffoldState = rememberScaffoldState()
            val chatId = entry.arguments?.getString(HomeRoutesAthlete.CHAT_ID) as String

            NavDrawerScaffold(
                scaffoldState = scaffoldState,
                scope = rememberCoroutineScope(),
                navController = navController
            ) { paddingValues ->
                val viewModel: ChatViewModel = get()
                LaunchedEffect(Unit) {
                    viewModel.initChatScreen(chatId)
                }
                ChatScreen(
                    viewModel = viewModel,
                    paddingValues = paddingValues,
                )
            }
        }

        composable(route = HomeRoutesAthlete.Profile.routeName) {
            val scaffoldState = rememberScaffoldState()

            NavDrawerScaffold(
                scaffoldState = scaffoldState,
                scope = rememberCoroutineScope(),
                navController = navController
            ) { paddingValues ->
                route?.let { it1 -> Text(modifier = Modifier.padding(paddingValues), text = it1) }
            }
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