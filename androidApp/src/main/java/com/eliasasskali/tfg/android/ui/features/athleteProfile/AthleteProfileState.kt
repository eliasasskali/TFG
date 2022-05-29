package com.eliasasskali.tfg.android.ui.features.athleteProfile

import com.eliasasskali.tfg.model.Athlete
import com.eliasasskali.tfg.model.DomainError

data class AthleteProfileState(
    val athlete: Athlete = Athlete(),
    val followingClubs: List<Pair<String, String>> = listOf(),
    val step: AthleteProfileSteps = AthleteProfileSteps.IsLoading,
)

sealed class AthleteProfileSteps {
    object IsLoading : AthleteProfileSteps()
    object ShowAthleteProfile : AthleteProfileSteps()
    class Error(val error: DomainError) : AthleteProfileSteps()
}