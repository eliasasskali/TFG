package com.eliasasskali.tfg.model

import android.location.Location

data class ClubDto(
    val name: String,
    val contactEmail: String?,
    val contactPhone: String?,
    val description: String?,
    val address: String?,
    val location: Location?,
    val services: List<String>?
)