package com.eliasasskali.tfg.android.data.repository

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.eliasasskali.tfg.model.Club
import com.eliasasskali.tfg.model.ClubDto
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.tasks.await

// Network Data Source
class FirestorePagingSource(
    private val queryClubs: Query,
    private val searchString: String = "",
    private val sportsFilters: List<String> = listOf()
) : PagingSource<QuerySnapshot, Club>() {
    override fun getRefreshKey(state: PagingState<QuerySnapshot, Club>): QuerySnapshot? = null

    override suspend fun load(params: LoadParams<QuerySnapshot>): LoadResult<QuerySnapshot, Club> {
        return try {
            val currentPage = params.key ?: getCurrentPage()

            val lastVisibleClub = currentPage.documents[currentPage.size() - 1]

            val nextPage = getNextPage(lastVisibleClub)

            val data = currentPage.map { document ->
                document.toObject(ClubDto::class.java).toModel(document.id)
            }

            LoadResult.Page(
                data = if (sportsFilters.isEmpty()) data else data.filter { club ->
                    club.services.intersect(sportsFilters).isNotEmpty()
                },
                prevKey = null,
                nextKey = nextPage
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    private suspend fun getCurrentPage() : QuerySnapshot {
        val currentPage = queryClubs
            .get()
            .await()

        if (searchString.isBlank()) {
            return if (sportsFilters.isEmpty()) {
                currentPage
            } else {
                queryClubs
                    .whereArrayContainsAny("services", sportsFilters)
                    .get()
                    .await()
            }
        } else {
            return if (sportsFilters.isEmpty()) {
                queryClubs
                    .whereArrayContains("keywords", searchString.lowercase().trim())
                    .get()
                    .await()
            } else {
                queryClubs
                    .whereArrayContains("keywords", searchString.lowercase().trim())
                    .get()
                    .await()
            }
        }
    }

    private suspend fun getNextPage(lastVisibleClub: DocumentSnapshot) : QuerySnapshot {
        val nextPage = queryClubs.startAfter(lastVisibleClub).get().await()

        if (searchString.isBlank()) {
            return if (sportsFilters.isEmpty()) {
                nextPage
            } else {
                queryClubs
                    .whereArrayContainsAny("services", sportsFilters)
                    .startAfter(lastVisibleClub)
                    .get()
                    .await()
            }
        } else {
            return if (sportsFilters.isEmpty()) {
                queryClubs
                    .whereArrayContains("keywords", searchString.lowercase().trim())
                    .startAfter(lastVisibleClub)
                    .get()
                    .await()
            } else {
                queryClubs
                    .whereArrayContains("keywords", searchString.lowercase().trim())
                    .startAfter(lastVisibleClub)
                    .get()
                    .await()
            }
        }
    }
}