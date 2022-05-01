package com.eliasasskali.tfg.android.ui.features.posts

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.eliasasskali.tfg.android.core.ui.RootViewModel
import com.eliasasskali.tfg.android.data.repository.PostsRepository
import com.eliasasskali.tfg.ui.error.ErrorHandler
import com.eliasasskali.tfg.ui.executor.Executor

class PostsViewModel(
    private val repository: PostsRepository,
    executor: Executor,
    errorHandler: ErrorHandler
) : RootViewModel(executor, errorHandler) {

    val state: MutableState<PostsState> = mutableStateOf(PostsState())
    var posts = repository.getPosts(state.value.clubIds).cachedIn(viewModelScope)

    fun initPostsScreen(clubIds: List<String>) {
        state.value = state.value.copy(clubIds = clubIds)
        posts = repository.getPosts(state.value.clubIds).cachedIn(viewModelScope)
        state.value = state.value.copy(step = PostsSteps.ShowPosts)
    }
}