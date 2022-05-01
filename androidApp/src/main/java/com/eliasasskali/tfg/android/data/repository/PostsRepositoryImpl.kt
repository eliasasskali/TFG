package com.eliasasskali.tfg.android.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.google.firebase.firestore.FirebaseFirestore

class PostsRepositoryImpl(
    private val config: PagingConfig
) : PostsRepository {
    override fun getPosts(clubIds: List<String>) = Pager(
        config = config
    ) {
        PostsPagingSource(
            queryPosts = FirebaseFirestore.getInstance().collection("Posts"),
            clubIds = clubIds
        )
    }.flow
}