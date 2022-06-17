package com.eliasasskali.tfg.android.data.repository.reviews

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.eliasasskali.tfg.android.data.dataSource.ReviewsPagingSource
import com.eliasasskali.tfg.android.data.repository.reviews.ReviewsRepository
import com.eliasasskali.tfg.model.*
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class ReviewsRepositoryImpl(
    private val config: PagingConfig
) : ReviewsRepository {
    override fun getReviews(clubId: String) = Pager(
        config = config
    ) {
        ReviewsPagingSource(
            queryPosts = FirebaseFirestore
                .getInstance()
                .collection("Reviews"),
            clubId = clubId
        )
    }.flow

    override suspend fun postReview(reviewDto: ReviewDto): Either<DomainError, Success> {
        val reviewsRef = FirebaseFirestore
            .getInstance()
            .collection("Reviews")

        val clubsRef = FirebaseFirestore
            .getInstance()
            .collection("Clubs")

        return try {
            reviewsRef
                .add(reviewDto)
                .await()

            clubsRef
                .document(reviewDto.clubId)
                .update("ratings", FieldValue.arrayUnion(reviewDto.rating))
                .await()

            Either.Right(Success)
        } catch (e: Exception) {
            Either.Left(DomainError.ErrorPostingReview)
        }
    }
}