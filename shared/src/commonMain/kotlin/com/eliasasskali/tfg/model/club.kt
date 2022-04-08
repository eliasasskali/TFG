package com.eliasasskali.tfg.model

import android.graphics.Bitmap
import android.location.Location
import android.net.Uri

data class ClubDto(
    val name: String = "",
    val contactEmail: String? = "",
    val contactPhone: String? = "",
    val description: String? = "",
    val address: String? = "",
    val location: ClubLocation? = null,
    val services: List<String>? = listOf()
) {
    fun toModel(id: String, images: List<Bitmap?>, clubImages: List<Uri>) : Club {
        val location = Location("")
        location.latitude = this.location?.latitude ?: 0.0
        location.longitude = this.location?.longitude ?: 0.0

        return Club(
            name = this.name,
            contactEmail = this.contactEmail,
            contactPhone = this.contactPhone,
            description = this.description,
            address = this.address,
            location = location,
            services = this.services,
            images = images,
            clubImages = clubImages
        )
    }
}

data class Club(
    val name: String = "",
    val contactEmail: String? = "",
    val contactPhone: String? = "",
    val description: String? = "",
    val address: String? = "",
    val location: Location? = null,
    val services: List<String>? = listOf(),
    val images: List<Bitmap?> = listOf(),
    val clubImages: List<Uri> = listOf()
) {
    fun toModel() : ClubDto {
        return ClubDto(
            name = this.name,
            contactEmail = this.contactEmail,
            contactPhone = this.contactPhone,
            description = this.description,
            address = this.address,
            location = ClubLocation(this.location?.latitude ?: 0.0, this.location?.longitude ?: 0.0),
            services = this.services
        )
    }
}

data class ClubLocation(
    val longitude: Double = 0.0,
    val latitude: Double = 0.0
)