package com.eliasasskali.tfg.android.ui.features.clubProfile

import com.eliasasskali.tfg.model.Club
import com.eliasasskali.tfg.model.DomainError

data class ClubProfileState(
    val club: Club = Club(),
    val step: ClubProfileSteps = ClubProfileSteps.IsLoading
)

sealed class ClubProfileSteps {
    object IsLoading : ClubProfileSteps()
    object ShowClubProfile : ClubProfileSteps()
    class Error(val error: String) : ClubProfileSteps()
}