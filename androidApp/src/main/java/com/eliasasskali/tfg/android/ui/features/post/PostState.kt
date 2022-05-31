package com.eliasasskali.tfg.android.ui.features.post

data class PostState(
    val title: String = "",
    val content: String = "",
    val step: PostSteps = PostSteps.ShowCreatePost
)

sealed class PostSteps {
    object IsLoading : PostSteps()
    object ShowCreatePost : PostSteps()
    object PostCreated : PostSteps()
    class Error(val error: String, val onRetry: () -> Unit) : PostSteps()
}

