package com.eliasasskali.tfg.android.data.repository

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.eliasasskali.tfg.model.Post
import com.eliasasskali.tfg.model.PostDto
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.tasks.await

// Network Data Source
class PostsPagingSource(
    private val clubIds: List<String>,
    private val queryPosts: Query
) : PagingSource<QuerySnapshot, Post>() {
    override fun getRefreshKey(state: PagingState<QuerySnapshot, Post>): QuerySnapshot? = null

    override suspend fun load(params: LoadParams<QuerySnapshot>): LoadResult<QuerySnapshot, Post> {
        return try {
            val currentPage = params.key ?: queryPosts
                .whereIn("clubId", clubIds)
                .get()
                .await()

            val lastVisibleClub = currentPage.documents[currentPage.size() - 1]

            val nextPage = queryPosts
                .whereIn("clubId", clubIds)
                .startAfter(lastVisibleClub)
                .get()
                .await()

            val data = currentPage.map { document ->
                document.toObject(PostDto::class.java).toModel(document.id)
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