package com.eliasasskali.tfg.android.data.repository

import com.eliasasskali.tfg.android.ui.features.chat.OnErrorChat
import com.eliasasskali.tfg.android.ui.features.chat.OnSuccessChat
import com.eliasasskali.tfg.android.ui.features.chats.OnErrorChats
import com.eliasasskali.tfg.android.ui.features.chats.OnSuccessChats
import com.eliasasskali.tfg.model.*
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class ChatsRepositoryImpl : ChatsRepository {
    private val db = Firebase.firestore
    private val chatsRef = db.collection("Chats")

    override fun getChat(chatId: String) = callbackFlow {
        val chat = chatsRef
            .document(chatId)

        val snapshotListener = chat.addSnapshotListener { value, error ->
            val response = if (error == null) {
                OnSuccessChat(value)
            } else {
                OnErrorChat(error)
            }
            this.trySend(response).isSuccess
        }

        awaitClose {
            snapshotListener.remove()
        }
    }

    override fun getUserChats(chatIds: List<String>) = callbackFlow {
        val chats = chatsRef
            .whereIn(FieldPath.documentId(), chatIds)

        val snapshotListener = chats.addSnapshotListener { value, error ->
            val response = if (error == null) {
                OnSuccessChats(value)
            } else {
                OnErrorChats(error)
            }
            this.trySend(response).isSuccess
        }

        awaitClose {
            snapshotListener.remove()
        }
    }

    override suspend fun sendMessage(message: Message, chatId: String): Either<DomainError, Success> {
        return try {
            chatsRef
                .document(chatId)
                .update("messages", FieldValue.arrayUnion(message.toModel()))
                .await()
            Either.Right(Success)
        } catch (e: Exception) {
            println(e.localizedMessage)
            Either.Left(DomainError.ErrorNotHandled("Error sending message."))
        }
    }
}