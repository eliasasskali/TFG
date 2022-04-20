package com.eliasasskali.tfg.android.ui.features.clubDetail

import com.eliasasskali.tfg.model.Club

data class ClubDetailState(
    val club: Club = Club(),
    val isClubOwner: Boolean = false,
    val distanceToClub: String = ""
)