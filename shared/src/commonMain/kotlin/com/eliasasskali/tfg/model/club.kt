package com.eliasasskali.tfg.model

import android.location.Location

data class ClubDto(
    val name: String = "",
    val contactEmail: String? = "",
    val contactPhone: String? = "",
    val description: String? = "",
    val address: String = "",
    val location: ClubLocation = ClubLocation(0.0, 0.0),
    val services: List<String> = listOf(),
    val images: List<String> = listOf()
) {
    fun toModel(id: String) : Club {
        val location = Location("")
        location.latitude = this.location.longitude
        location.longitude = this.location.latitude

        return Club(
            id = id,
            name = this.name,
            contactEmail = this.contactEmail,
            contactPhone = this.contactPhone,
            description = this.description,
            address = this.address,
            location = location,
            services = this.services,
            images = this.images
        )
    }
}

data class Club(
    val id: String = "",
    val name: String = "",
    val contactEmail: String? = "",
    val contactPhone: String? = "",
    val description: String? = "",
    val address: String = "",
    val location: Location = Location(""),
    val services: List<String> = listOf(),
    val images: List<String> = listOf()
) {
    fun toModel() : ClubDto {
        return ClubDto(
            name = this.name,
            contactEmail = this.contactEmail,
            contactPhone = this.contactPhone,
            description = this.description,
            address = this.address,
            location = ClubLocation(this.location.latitude, this.location.longitude),
            services = this.services,
            images = this.images
        )
    }
}

data class ClubLocation(
    val latitude: Double = 0.0,
    val longitude: Double = 0.0
)
