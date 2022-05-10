package com.eliasasskali.tfg.android.ui.features.postDetail

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.eliasasskali.tfg.android.core.ui.RootViewModel
import com.eliasasskali.tfg.android.data.repository.PostsRepository
import com.eliasasskali.tfg.data.preferences.Preferences
import com.eliasasskali.tfg.model.Post
import com.eliasasskali.tfg.ui.error.ErrorHandler
import com.eliasasskali.tfg.ui.executor.Executor
import kotlinx.coroutines.launch

class PostDetailViewModel(
    private val preferences: Preferences,
    private val repository: PostsRepository,
    executor: Executor,
    errorHandler: ErrorHandler,
) : RootViewModel(executor, errorHandler) {

    val state: MutableState<PostDetailState> = mutableStateOf(PostDetailState())

    fun initPostDetailScreen(post: Post) {
        setStep(PostDetailSteps.IsLoading)

        val isPostOwner = (post.clubId == preferences.getLoggedUid())
        state.value = state.value.copy(
            post = post,
            isPostOwner = isPostOwner,
            step = PostDetailSteps.ShowPostDetail,
            newPostTitle = post.title,
            newPostContent = post.content
        )
    }

    fun setStep(step: PostDetailSteps) {
        state.value = state.value.copy(step = step)
    }

    fun setNewPostTitle(newPostTitle: String) {
        state.value = state.value.copy(newPostTitle = newPostTitle)
    }

    fun setNewPostContent(newPostContent: String) {
        state.value = state.value.copy(newPostContent = newPostContent)
    }

    fun editPostContent() {
        setStep(PostDetailSteps.IsLoading)
        viewModelScope.launch {
            execute {
                repository.editPost(
                    postId = state.value.post.postId,
                    newPostTitle = state.value.newPostTitle,
                    newPostContent = state.value.newPostContent
                )
            }.fold(
                error = {

                },
                success = {
                    viewModelScope.launch {
                        execute {
                            repository.getPost(state.value.post.postId)
                        }.fold(
                            error = {

                            },
                            success = { post ->
                                initPostDetailScreen(post = post)
                            }
                        )
                    }
                    setStep(PostDetailSteps.ShowPostDetail)
                }
            )
        }
    }

    fun deletePost(onPostDeleted: () -> Unit) {
        setStep(PostDetailSteps.IsLoading)
        viewModelScope.launch {
            execute {
                repository.deletePost(postId = state.value.post.postId)
            }.fold(
                error = {

                },
                success = {
                    state.value = PostDetailState()
                    onPostDeleted()
                }
            )
        }
    }
}