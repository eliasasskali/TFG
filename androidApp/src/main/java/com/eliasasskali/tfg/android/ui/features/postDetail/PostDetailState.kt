package com.eliasasskali.tfg.android.ui.features.postDetail

import com.eliasasskali.tfg.model.Post

data class PostDetailState(
    val post: Post = Post(),
    val isPostOwner: Boolean = false,
    val step: PostDetailSteps = PostDetailSteps.IsLoading,
    val newPostTitle: String = "",
    val newPostContent: String = ""
) {
    val hasTitleChanged: Boolean get() = post.title != newPostTitle
    val hasContentChanged: Boolean get() = post.content != newPostContent
}

sealed class PostDetailSteps {
    object IsLoading : PostDetailSteps()
    object ShowPostDetail : PostDetailSteps()
    object ShowEditPost : PostDetailSteps()
    class Error(val error: String, val onRetry: () -> Unit) : PostDetailSteps()
}