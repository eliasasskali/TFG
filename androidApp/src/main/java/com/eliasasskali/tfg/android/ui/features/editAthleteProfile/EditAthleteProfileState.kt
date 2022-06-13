package com.eliasasskali.tfg.android.ui.features.editAthleteProfile

import com.eliasasskali.tfg.model.Athlete
import com.eliasasskali.tfg.model.DomainError

data class EditAthleteProfileState(
    val athlete: Athlete = Athlete(),
    val name: String = "",
    val interests: Set<String> = setOf(),
    val step: EditAthleteProfileSteps = EditAthleteProfileSteps.IsLoading,
)

sealed class EditAthleteProfileSteps {
    object IsLoading : EditAthleteProfileSteps()
    object ShowEditAthleteProfile : EditAthleteProfileSteps()
    class Error(val error: String, val onRetry: () -> Unit) : EditAthleteProfileSteps()
}