package com.eliasasskali.tfg.android.ui.features.completeProfile

import android.location.Location
import android.net.Uri

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
    val location: Location = Location(""), //TODO: own location class
    val clubImages: List<Uri> = emptyList()
)