package com.eliasasskali.tfg.android.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.eliasasskali.tfg.model.*
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import kotlinx.coroutines.tasks.await

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

    override fun editPost(postId: String, newPostTitle: String, newPostContent: String): Either<DomainError, Success> {
        return try {
            val postReference = FirebaseFirestore
                .getInstance()
                .collection("Posts")
                .document(postId)

            if (newPostTitle.isNotBlank()) {
                postReference.update("title", newPostTitle)
            }
            if (newPostContent.isNotBlank()) {
                postReference.update("content", newPostContent)
            }
            postReference.update("date", System.currentTimeMillis())

            Either.Right(Success)
        } catch (e: Exception) {
            Either.Left(DomainError.EditPostError)
        }
    }

    override suspend fun getPost(postId: String): Either<DomainError, Post> {
        return try {
            val post = FirebaseFirestore.getInstance().collection("Posts")
                .document(postId)
                .get()
                .await()

            Either.Right(
                post
                    .toObject(PostDto::class.java)
                    ?.toModel(post.id)!!
            )
        } catch (e: Exception) {
            Either.Left(DomainError.GetPostError)
        }
    }

    override suspend fun deletePost(postId: String): Either<DomainError, Success> {
        return try {
            FirebaseFirestore.getInstance().collection("Posts")
                .document(postId)
                .delete()
                .await()

            Either.Right(Success)
        } catch (e: Exception) {
            Either.Left(DomainError.DeletePostError)
        }
    }
}