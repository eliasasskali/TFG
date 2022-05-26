package com.eliasasskali.tfg.android.ui.features.posts

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.eliasasskali.tfg.android.core.ui.RootViewModel
import com.eliasasskali.tfg.android.data.repository.PostsRepository
import com.eliasasskali.tfg.data.preferences.Preferences
import com.eliasasskali.tfg.model.Athlete
import com.eliasasskali.tfg.model.Club
import com.eliasasskali.tfg.ui.error.ErrorHandler
import com.eliasasskali.tfg.ui.executor.Executor
import com.google.gson.Gson

class PostsViewModel(
    private val repository: PostsRepository,
    private val preferences: Preferences,
    executor: Executor,
    errorHandler: ErrorHandler
) : RootViewModel(executor, errorHandler) {

    val state: MutableState<PostsState> = mutableStateOf(PostsState())
    var posts = repository.getPosts(state.value.clubIds).cachedIn(viewModelScope)

    fun initPostsScreen(clubIds: List<String>) {
        if (clubIds.isNotEmpty()) {
            state.value = state.value.copy(clubIds = clubIds)
            posts = repository.getPosts(state.value.clubIds).cachedIn(viewModelScope)
            state.value = state.value.copy(step = PostsSteps.ShowPosts)
        } else {
            val json = preferences.getProfileJson()
            if (preferences.isClub()) {
                val club = Gson().fromJson(json, Club::class.java)
                state.value = state.value.copy(clubIds = listOf(club.id))
            } else {
                val athlete = Gson().fromJson(json, Athlete::class.java)
                state.value = state.value.copy(clubIds = athlete.following)
            }
            posts = repository.getPosts(state.value.clubIds).cachedIn(viewModelScope)
            state.value = state.value.copy(step = PostsSteps.ShowPosts)
        }
    }
}