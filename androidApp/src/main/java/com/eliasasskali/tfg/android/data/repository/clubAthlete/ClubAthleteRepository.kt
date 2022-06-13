package com.eliasasskali.tfg.android.data.repository.clubAthlete

import android.net.Uri
import com.eliasasskali.tfg.model.*

interface ClubAthleteRepository {
    // Club
    fun isClubOwner(clubId: String): Either<DomainError, Boolean>
    suspend fun getClubById(clubId: String): Either<DomainError, Club?>
    suspend fun uploadImages(clubImages: List<Uri>) : Either<DomainError, Success>
    fun deleteClubImages(clubId: String, numberOfImages: Int)
    suspend fun uploadPost(title: String, content: String) : Either<DomainError, Success>

    // Athlete
    suspend fun getAthleteById(userId: String): Either<DomainError, Athlete?>
    suspend fun followClub(clubId: String) : Either<DomainError, Success>
    suspend fun unFollowClub(clubId: String) : Either<DomainError, Success>
    suspend fun saveAthletePreferences() : Either<DomainError, Success>
    suspend fun getFollowingClubNames(
        following: List<String>
    ): Either<DomainError, List<Pair<String, String>>>

    suspend fun updateAthleteProfile(
        athlete: Athlete,
        newAthleteName: String,
        newInterests: List<String>
    ) : Either<DomainError, Success>
}