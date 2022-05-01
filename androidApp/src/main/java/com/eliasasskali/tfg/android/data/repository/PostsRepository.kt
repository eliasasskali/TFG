package com.eliasasskali.tfg.android.data.repository

import androidx.paging.PagingData
import com.eliasasskali.tfg.model.Post
import kotlinx.coroutines.flow.Flow

interface PostsRepository {
    fun getPosts(clubIds: List<String> = listOf()): Flow<PagingData<Post>>
}