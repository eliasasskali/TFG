package com.eliasasskali.tfg.android.ui.features.reviews

import com.eliasasskali.tfg.model.DomainError

data class ReviewsState(
    val clubId: String = "",
    val newRating: Int = 0,
    val newComment: String = "",
    val step: ReviewsSteps = ReviewsSteps.IsLoading,
)

sealed class ReviewsSteps {
    object IsLoading : ReviewsSteps()
    object ShowReviews : ReviewsSteps()
    class Error(val error: String, val onRetry: () -> Unit) : ReviewsSteps()
}