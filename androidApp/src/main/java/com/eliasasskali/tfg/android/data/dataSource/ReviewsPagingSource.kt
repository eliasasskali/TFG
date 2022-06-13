package com.eliasasskali.tfg.android.data.dataSource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.eliasasskali.tfg.model.Review
import com.eliasasskali.tfg.model.ReviewDto
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.tasks.await

// Network Data Source
class ReviewsPagingSource(
    private val clubId: String,
    private val queryPosts: Query
) : PagingSource<QuerySnapshot, Review>() {
    override fun getRefreshKey(state: PagingState<QuerySnapshot, Review>): QuerySnapshot? = null

    override suspend fun load(params: LoadParams<QuerySnapshot>): LoadResult<QuerySnapshot, Review> {
        return try {
            val currentPage = params.key ?: queryPosts
                .whereEqualTo("clubId", clubId)
                .get()
                .await()

            val lastVisibleReview = currentPage.documents[currentPage.size() - 1]

            val nextPage = queryPosts
                .whereEqualTo("clubId", clubId)
                .startAfter(lastVisibleReview)
                .get()
                .await()

            val data = currentPage.map { document ->
                document.toObject(ReviewDto::class.java).toModel()
            }

            LoadResult.Page(
                data = data,
                prevKey = null,
                nextKey = nextPage
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}