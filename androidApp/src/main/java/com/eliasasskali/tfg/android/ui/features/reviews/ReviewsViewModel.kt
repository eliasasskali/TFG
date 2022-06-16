package com.eliasasskali.tfg.android.ui.features.reviews

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.eliasasskali.tfg.android.core.ui.RootViewModel
import com.eliasasskali.tfg.android.data.repository.reviews.ReviewsRepository
import com.eliasasskali.tfg.data.preferences.Preferences
import com.eliasasskali.tfg.model.Review
import com.eliasasskali.tfg.model.ReviewDto
import com.eliasasskali.tfg.ui.error.ErrorHandler
import com.eliasasskali.tfg.ui.executor.Executor
import kotlinx.coroutines.launch

class ReviewsViewModel(
    private val repository: ReviewsRepository,
    private val preferences: Preferences,
    executor: Executor,
    errorHandler: ErrorHandler
) : RootViewModel(executor, errorHandler) {

    val state: MutableState<ReviewsState> = mutableStateOf(ReviewsState())
    var reviews = repository.getReviews(state.value.clubId).cachedIn(viewModelScope)

    fun initReviewsScreen(clubId: String) {
        state.value = state.value.copy(clubId = clubId)
        reviews = repository.getReviews(state.value.clubId).cachedIn(viewModelScope)
        setStep(ReviewsSteps.ShowReviews)
    }

    fun setStep(step: ReviewsSteps) {
        state.value = state.value.copy(step = step)
    }

    fun setNewRating(newRating: Int) {
        state.value = state.value.copy(newRating = newRating)
    }

    fun setNewComment(newComment: String) {
        state.value = state.value.copy(newComment = newComment)
    }

    fun isClub() = preferences.isClub()

    fun postReview() {
        val review = ReviewDto(
            userId = preferences.getLoggedUid(),
            clubId = state.value.clubId,
            date = System.currentTimeMillis(),
            comment = state.value.newComment,
            rating = state.value.newRating
        )

        viewModelScope.launch {
            execute {
                repository.postReview(review)
            }.fold(
                error = {
                    setStep(
                        ReviewsSteps.Error(
                            error = errorHandler.convert(it),
                            onRetry = { postReview() }
                        )
                    )
                },
                success = {
                    state.value = state.value.copy(newComment = "", newRating = 0)
                    setStep(ReviewsSteps.IsLoading)
                    initReviewsScreen(state.value.clubId)
                }
            )
        }
    }

    fun hasNoReviewsInClub(reviews: List<Review>) : Boolean {
        return reviews.none { review ->
            review.userId == preferences.getLoggedUid()
        }
    }
}