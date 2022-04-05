package com.eliasasskali.tfg.android.data.repository

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.eliasasskali.tfg.model.*
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.Query
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import java.io.File

class ClubAthleteRepository(
    private val queryClubsByName: Query
) {
    suspend fun getClubsFromFirestore() : Either<DomainError, List<Club>> {
        return try {
            Either.Right(queryClubsByName.get().await().map { document ->
                document.toObject(ClubDto::class.java).toModel(document.id)
            })
        } catch (e: FirebaseFirestoreException) {
            Either.Left(DomainError.ErrorNotHandled(e.toString()))
        }
    }

    suspend fun getClubById(clubId: String) : Either<DomainError, Club?> {
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

    suspend fun getUserById(userId: String) : Either<DomainError, Athlete?> {
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

    suspend fun getClubImages(clubId: String) : Either<DomainError, List<Bitmap?>> {
        val storageRef = FirebaseStorage.getInstance().reference.child("clubImages/$clubId-0")

        val localFile = File.createTempFile("tempImage", "png")
        return try {
            storageRef.getFile(localFile)
                .await()

            Either.Right(listOf(BitmapFactory.decodeFile(localFile.absolutePath)))
        } catch (e: Exception) {
            Either.Left(DomainError.ErrorNotHandled(e.toString()))
        }
    }
}