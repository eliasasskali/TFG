package com.eliasasskali.tfg.android.ui.features.clubDetail

import com.eliasasskali.tfg.model.Club
import com.eliasasskali.tfg.model.DomainError

data class ClubDetailState(
    val club: Club = Club(),
    val isClubOwner: Boolean = false,
    val distanceToClub: String = "",
    val step: ClubDetailSteps = ClubDetailSteps.IsLoading
)

sealed class ClubDetailSteps {
    object IsLoading : ClubDetailSteps()
    object ShowClubDetail : ClubDetailSteps()
    class Error(val error: DomainError) : ClubDetailSteps()
}