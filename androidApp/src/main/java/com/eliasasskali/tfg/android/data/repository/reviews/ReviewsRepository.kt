package com.eliasasskali.tfg.android.data.repository.reviews

import androidx.paging.PagingData
import com.eliasasskali.tfg.model.*
import kotlinx.coroutines.flow.Flow

interface ReviewsRepository {
    fun getReviews(clubId: String): Flow<PagingData<Review>>
    suspend fun postReview(reviewDto: ReviewDto): Either<DomainError, Success>
}