package com.eliasasskali.tfg.android.ui.features.clubDetail

import com.eliasasskali.tfg.model.Athlete
import com.eliasasskali.tfg.model.Club
import com.eliasasskali.tfg.model.DomainError

data class ClubDetailState(
    val club: Club = Club(),
    val isClubOwner: Boolean = false,
    val distanceToClub: String = "",
    val step: ClubDetailSteps = ClubDetailSteps.IsLoading,
    val athlete: Athlete = Athlete()
) {
    val athleteFollowsClub: Boolean
        get() = (athlete.following.contains(club.id))
}

sealed class ClubDetailSteps {
    object IsLoading : ClubDetailSteps()
    object ShowClubDetail : ClubDetailSteps()
    class Error(val error: DomainError) : ClubDetailSteps()
}