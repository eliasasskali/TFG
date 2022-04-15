package com.eliasasskali.tfg.android.data.repository

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.eliasasskali.tfg.model.Club
import com.eliasasskali.tfg.model.ClubDto
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.tasks.await

// Network Data Source
class FirestorePagingSource(
    private val queryClubs: Query,
    private val searchString: String = "",
) : PagingSource<QuerySnapshot, Club>() {
    override fun getRefreshKey(state: PagingState<QuerySnapshot, Club>): QuerySnapshot? = null

    override suspend fun load(params: LoadParams<QuerySnapshot>): LoadResult<QuerySnapshot, Club> {
        return try {
            val currentPage = params.key ?: (if (searchString.isBlank()) queryClubs.get()
                .await() else queryClubs.whereArrayContains("keywords", searchString.lowercase().trim()).get()
                .await())

            val lastVisibleClub = currentPage.documents[currentPage.size() - 1]

            val nextPage =
                if (searchString.isBlank()) queryClubs.startAfter(lastVisibleClub).get().await()
                else queryClubs
                    .whereArrayContains("keywords", searchString.lowercase().trim())
                    .startAfter(lastVisibleClub)
                    .get()
                    .await()

            LoadResult.Page(
                data = currentPage.map { document ->
                    document.toObject(ClubDto::class.java).toModel(document.id)
                },
                prevKey = null,
                nextKey = nextPage
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}