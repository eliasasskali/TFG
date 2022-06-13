package com.eliasasskali.tfg.android.ui.features.editClubProfile

import android.graphics.Bitmap
import com.eliasasskali.tfg.model.Club
import com.eliasasskali.tfg.model.ClubLocation
import com.eliasasskali.tfg.model.DomainError

data class EditClubProfileState(
    val club: Club = Club(),
    val error: String = "",
    val step: EditClubProfileSteps = EditClubProfileSteps.IsLoading,
    val name: String = "",
    val services: Set<String> = setOf(),
    val contactEmail: String = "",
    val contactPhone: String = "",
    val description: String = "",
    val address: String = "",
    val location: ClubLocation = ClubLocation(0.0, 0.0),
    val images: List<String> = listOf(),
    val bitmapImages: ArrayList<Bitmap> = arrayListOf(),
    val previousBitmapImages: ArrayList<Bitmap> = arrayListOf()
)

sealed class EditClubProfileSteps {
    object IsLoading : EditClubProfileSteps()
    object ShowEditClub : EditClubProfileSteps()
    object ShowEditLocation : EditClubProfileSteps()
    class Error(val error: String, val onRetry: () -> Unit) : EditClubProfileSteps()
}