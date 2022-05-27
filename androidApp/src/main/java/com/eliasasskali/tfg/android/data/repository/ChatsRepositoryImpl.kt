package com.eliasasskali.tfg.android.data.repository

import com.eliasasskali.tfg.android.ui.features.chat.OnErrorChat
import com.eliasasskali.tfg.android.ui.features.chat.OnSuccessChat
import com.eliasasskali.tfg.android.ui.features.chats.OnErrorChats
import com.eliasasskali.tfg.android.ui.features.chats.OnSuccessChats
import com.eliasasskali.tfg.data.preferences.Preferences
import com.eliasasskali.tfg.model.*
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class ChatsRepositoryImpl(
    val preferences: Preferences
) : ChatsRepository {
    private val db = Firebase.firestore
    private val chatsRef = db.collection("Chats")
    private val clubsRef = db.collection("Clubs")
    private val athletesRef = db.collection("Athletes")

    override suspend fun getOrCreateChatWithClub(athleteId: String, clubId: String, athleteName: String, clubName: String): Either<DomainError, String> {
        return try {
            Either.Right(
                chatsRef
                    .whereEqualTo("athleteId", athleteId)
                    .whereEqualTo("clubId", clubId)
                    .limit(1)
                    .get()
                    .await()
                    .documents[0]
                    .id
            )
        } catch (e: Exception) {
            return try {
                val newChat = ChatDto(
                    athleteId = athleteId,
                    clubId = clubId,
                    athleteName = athleteName,
                    clubName = clubName
                )

                val chatId = chatsRef
                    .add(newChat)
                    .await()
                    .id

                // Update club chats
                clubsRef
                    .document(clubId)
                    .update("chats", FieldValue.arrayUnion(chatId))
                    .await()

                // Update athlete chats
                athletesRef
                    .document(athleteId)
                    .update("chats", FieldValue.arrayUnion(chatId))
                    .await()

                // Update preferences
                if (preferences.isClub()) {
                    val clubSnapshot =
                        clubsRef.
                        document(clubId)
                            .get()
                            .await()

                    val club = clubSnapshot
                        .toObject(ClubDto::class.java)
                        ?.toModel(clubSnapshot.id)

                    preferences.saveProfileJson(Gson().toJson(club))
                } else {
                    val athleteSnapshot =
                        athletesRef.
                        document(athleteId)
                            .get()
                            .await()

                    val athlete = athleteSnapshot
                        .toObject(AthleteDto::class.java)
                        ?.toModel()

                    preferences.saveProfileJson(Gson().toJson(athlete))
                }


                Either.Right(chatId)
            } catch (e: Exception) {
                Either.Left(DomainError.ErrorNotHandled("Error creating chat."))
            }
        }
    }

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
        val chats = if (preferences.isClub()) {
            chatsRef
                .whereEqualTo("clubId", preferences.getLoggedUid())
        } else {
            chatsRef
                .whereEqualTo("athleteId", preferences.getLoggedUid())
        }

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