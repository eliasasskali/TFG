package com.eliasasskali.tfg.android.data.repository.clubAthlete

import android.net.Uri
import com.eliasasskali.tfg.data.preferences.Preferences
import com.eliasasskali.tfg.model.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.gson.Gson
import kotlinx.coroutines.tasks.await

class ClubAthleteRepositoryImpl(
    private val preferences: Preferences,
) : ClubAthleteRepository {
    override fun isClubOwner(clubId: String): Either<DomainError, Boolean> {
        try {
            FirebaseAuth.getInstance().currentUser?.let { user ->
                return Either.Right(user.uid == clubId)
            }
            return Either.Right(false)
        } catch (e: Exception) {
            // TODO: Change error
            return Either.Left(DomainError.ServiceError)
        }
    }

    override suspend fun getClubById(clubId: String): Either<DomainError, Club?> {
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
            Either.Left(DomainError.ServiceError)
        }
    }

    override suspend fun getAthleteById(userId: String): Either<DomainError, Athlete?> {
        return try {
            Either.Right(
                FirebaseFirestore.getInstance().collection("Athletes")
                    .document(userId)
                    .get()
                    .await()
                    .toObject(AthleteDto::class.java)
                    ?.toModel()
            )
        } catch (e: FirebaseFirestoreException) {
            Either.Left(DomainError.ServiceError)
        }
    }

    override suspend fun uploadImages(clubImages: List<Uri>) : Either<DomainError, Success> {
        return try {
            if (clubImages.isNotEmpty()) {
                val storage = FirebaseStorage.getInstance()
                val userId = FirebaseAuth.getInstance().currentUser?.uid

                userId?.let { uid ->
                    clubImages.mapIndexed { index, uri ->
                        val storageReference = storage.reference.child("clubImages/$uid/$index")
                        val url = storageReference.putFile(uri)
                            .await()
                            .storage
                            .downloadUrl
                            .await()

                        FirebaseFirestore.getInstance().collection("Clubs")
                            .document(uid)
                            .update("images", FieldValue.arrayUnion(url.toString()))
                    }
                }
            }
            Either.Right(Success)
        } catch (e: Exception) {
            Either.Left(DomainError.UploadImagesError)
        }
    }

    override fun deleteClubImages(clubId: String, numberOfImages: Int) {
        for (index in 0..numberOfImages) {
            val storage = FirebaseStorage.getInstance()
            storage.reference.child("clubImages/$clubId/$index").delete()
        }
        FirebaseFirestore.getInstance().collection("Clubs")
            .document(clubId)
            .update("images", FieldValue.delete())
    }

    override suspend fun uploadPost(title: String, content: String) : Either<DomainError, Success> {
        val uid = Firebase.auth.currentUser?.uid
        val db = Firebase.firestore
        uid?.let {
            val clubName = db.collection("Clubs")
                .document(uid)
                .get()
                .await()
                .getString("name")

            val post = Post(
                title = title,
                content = content,
                date = System.currentTimeMillis(),
                clubName = clubName ?: ""
            )

            return try {
                db.collection("Posts").add(post.toModel(uid)).await()
                Either.Right(Success)
            } catch (e: Exception) {
                Either.Left(DomainError.CreatePostError)
            }
        }
        return Either.Left(DomainError.CreatePostError)
    }

    override suspend fun followClub(clubId: String) : Either<DomainError, Success> {
        val uid = Firebase.auth.currentUser?.uid
        val db =  Firebase.firestore
        return try {
            uid?.let {
                db.collection("Athletes")
                    .document(uid)
                    .update("following", FieldValue.arrayUnion(clubId))
                    .await()
            }
            Either.Right(Success)
        } catch (e: Exception) {
            Either.Left(DomainError.FollowError)
        }
    }

    override suspend fun unFollowClub(clubId: String) : Either<DomainError, Success> {
        val uid = Firebase.auth.currentUser?.uid
        val db =  Firebase.firestore
        return try {
            uid?.let {
                db.collection("Athletes")
                    .document(uid)
                    .update("following", FieldValue.arrayRemove(clubId))
                    .await()
            }
            Either.Right(Success)
        } catch (e: Exception) {
            Either.Left(DomainError.UnfollowError)
        }
    }

    override suspend fun saveAthletePreferences() : Either<DomainError, Success> {
        val uid = Firebase.auth.currentUser?.uid
        return try {
            uid?.let {
                getAthleteById(uid).fold(
                    error = {},
                    success = { athlete ->
                        athlete?.let {
                            preferences.saveProfileJson(Gson().toJson(it))
                        }
                    }
                )
            }
            Either.Right(Success)
        } catch (e: Exception) {
            Either.Left(DomainError.ServiceError)
        }
    }

    override suspend fun getFollowingClubNames(following: List<String>): Either<DomainError, List<Pair<String, String>>> {
        val clubsRef = FirebaseFirestore.getInstance().collection("Clubs")
        return try {
            Either.Right(
                following.map { clubId ->
                    Pair(
                        clubId,
                        clubsRef
                            .document(clubId)
                            .get()
                            .await()
                            .get("name")
                            .toString()
                    )
                }
            )
        } catch (e: Exception) {
            Either.Left(DomainError.GetFollowedClubsError)
        }
    }

    override suspend fun updateAthleteProfile(
        athlete: Athlete,
        newAthleteName: String,
        newInterests: List<String>
    ) : Either<DomainError, Success> {
        return try {
            val athleteReference = FirebaseFirestore
                .getInstance()
                .collection("Athletes")
                .document(preferences.getLoggedUid())

            if (athlete.name != newAthleteName) {
                athleteReference
                    .update(
                        "name", newAthleteName,
                    )
                    .await()
            }

            if (athlete.interests != newInterests) {
                athleteReference
                    .update(
                        "interests", newInterests
                    )
                    .await()
            }

            Either.Right(Success)
        } catch (e: Exception) {
            Either.Left(DomainError.UpdateProfileError)
        }
    }
}