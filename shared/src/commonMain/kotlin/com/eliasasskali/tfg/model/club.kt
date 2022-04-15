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
    val images: List<String> = listOf(),
    val keywords: List<String> = listOf()
) {
    fun toModel() : Club {
        val location = Location("")
        location.latitude = this.location.latitude
        location.longitude = this.location.longitude

        return Club(
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
            images = this.images,
            keywords = generateKeywords(this.name)
        )
    }
}

data class ClubLocation(
    val longitude: Double = 0.0,
    val latitude: Double = 0.0
)

// Generate all possible keyword from string: Used for searching
fun generateKeywords(name: String): List<String> {
    val keywords = mutableListOf<String>()
    for (i in name.indices) {
        for (j in (i+1)..name.length) {
            keywords.add(name.slice(i until j))
        }
    }
    return keywords
}