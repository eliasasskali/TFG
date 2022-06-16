package com.eliasasskali.tfg.android.ui.features.completeProfile

import android.net.Uri
import com.eliasasskali.tfg.model.ClubLocation
import com.eliasasskali.tfg.model.DomainError

data class CompleteProfileState(
    val error: String = "",
    val hasCompletedProfile: Boolean = false,
    val isClub: Boolean = false,
    val name: String = "",
    val services: Set<String> = setOf(),
    val contactEmail: String = "",
    val contactPhone: String = "",
    val description: String = "",
    val address: String = "",
    val location: ClubLocation = ClubLocation(0.0, 0.0),
    val clubImages: List<Uri> = emptyList(),
    val step: CompleteProfileSteps = CompleteProfileSteps.ShowCompleteProfile,
    val schedule: MutableMap<String, String> = mutableMapOf(
        "1" to "08:00-22:00",
        "2" to "08:00-22:00",
        "3" to "08:00-22:00",
        "4" to "08:00-22:00",
        "5" to "08:00-22:00",
        "6" to "08:00-22:00",
        "7" to "08:00-22:00",
    )
)

sealed class CompleteProfileSteps {
    object IsLoading : CompleteProfileSteps()
    object ShowCompleteProfile : CompleteProfileSteps()
    class Error(val error: DomainError) : CompleteProfileSteps()
}