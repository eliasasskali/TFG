package com.eliasasskali.tfg.android.ui.features.posts

data class PostsState(
    val clubIds: List<String> = listOf(),
    val step: PostsSteps = PostsSteps.IsLoading,
)

sealed class PostsSteps {
    object IsLoading : PostsSteps()
    object ShowPosts : PostsSteps()
    class Error(val error: String, val onRetry: () -> Unit) : PostsSteps()
}