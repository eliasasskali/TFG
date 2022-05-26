package com.eliasasskali.tfg.android.data.repository

import androidx.paging.PagingData
import com.eliasasskali.tfg.model.DomainError
import com.eliasasskali.tfg.model.Either
import com.eliasasskali.tfg.model.Post
import com.eliasasskali.tfg.model.Success
import kotlinx.coroutines.flow.Flow

interface PostsRepository {
    fun getPosts(clubIds: List<String> = listOf()): Flow<PagingData<Post>>
    fun editPost(postId: String, newPostTitle: String = "", newPostContent: String = ""): Either<DomainError, Success>
    suspend fun getPost(postId: String): Either<DomainError, Post>
    suspend fun deletePost(postId: String): Either<DomainError, Success>
}