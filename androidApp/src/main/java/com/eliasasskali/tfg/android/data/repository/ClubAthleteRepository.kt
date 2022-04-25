package com.eliasasskali.tfg.android.data.repository

import android.net.Uri
import com.eliasasskali.tfg.model.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.Query
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await

class ClubAthleteRepository(
    private val queryClubsByName: Query
) {
    suspend fun getClubsFromFirestore(): Either<DomainError, List<Club>> {
        return try {
            Either.Right(queryClubsByName.get().await().map { document ->
                document.toObject(ClubDto::class.java).toModel(document.id)
            })
        } catch (e: FirebaseFirestoreException) {
            Either.Left(DomainError.ErrorNotHandled(e.toString()))
        }
    }

    fun isClubOwner(clubId: String): Either<DomainError, Boolean> {
        try {
            FirebaseAuth.getInstance().currentUser?.let { user ->
                return Either.Right(user.uid == clubId)
            }
            return Either.Right(false)
        } catch (e: Exception) {
            // TODO: Change error
            return Either.Left(DomainError.ErrorNotHandled("Error"))
        }
    }

    suspend fun getClubById(clubId: String): Either<DomainError, Club?> {
        return try {
            Either.Right(
                FirebaseFirestore.getInstance().collection("Clubs")
                    .document(clubId)
                    .get()
                    .await()
                    .toObject(ClubDto::class.java)
                    ?.toModel(clubId)
            )
        } catch (e: FirebaseFirestoreException) {
            Either.Left(DomainError.ErrorNotHandled(e.toString()))
        }
    }

    suspend fun getUserById(userId: String): Either<DomainError, Athlete?> {
        return try {
            Either.Right(
                FirebaseFirestore.getInstance().collection("Users")
                    .document(userId)
                    .get()
                    .await()
                    .toObject(AthleteDto::class.java)
                    ?.toModel()
            )
        } catch (e: FirebaseFirestoreException) {
            Either.Left(DomainError.ErrorNotHandled(e.toString()))
        }
    }

    fun uploadImages(clubImages: List<Uri>) {
        if (clubImages.isNotEmpty()) {
            val storage = FirebaseStorage.getInstance()
            val userId = FirebaseAuth.getInstance().currentUser?.uid

            userId?.let { uid ->
                clubImages.mapIndexed { index, uri ->
                    val storageReference = storage.reference.child("clubImages/$uid/$index")
                    storageReference.putFile(uri)
                        .addOnSuccessListener {
                            it.storage.downloadUrl
                                .addOnSuccessListener { url ->
                                    FirebaseFirestore.getInstance().collection("Clubs")
                                        .document(uid)
                                        .update("images", FieldValue.arrayUnion(url.toString()))
                                }
                        }
                }
            }
        }
    }

    fun deleteClubImages(clubId: String, numberOfImages: Int) {
        for (index in 0..numberOfImages) {
            val storage = FirebaseStorage.getInstance()
            storage.reference.child("clubImages/$clubId/$index").delete()
        }
        FirebaseFirestore.getInstance().collection("Clubs")
            .document(clubId)
            .update("images", FieldValue.delete())
    }
}