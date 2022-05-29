package com.eliasasskali.tfg.model

import java.net.URLEncoder
import java.nio.charset.StandardCharsets

data class ClubDto(
    val name: String = "",
    val contactEmail: String? = "",
    val contactPhone: String? = "",
    val description: String? = "",
    val address: String = "",
    val location: ClubLocation = ClubLocation(0.0, 0.0),
    val services: List<String> = listOf(),
    val images: List<String> = listOf(),
    val keywords: List<String> = listOf(),
    val chats: List<String> = listOf(),
    val ratings: List<Int> = listOf()
) {
    fun toModel(id: String): Club {
        val encodedImages = this.images.map {
            URLEncoder.encode(it, StandardCharsets.UTF_8.toString())
        }

        return Club(
            id = id,
            name = this.name,
            contactEmail = this.contactEmail,
            contactPhone = this.contactPhone,
            description = this.description,
            address = this.address,
            location = this.location,
            services = this.services,
            images = encodedImages,
            chats = this.chats,
            ratings = this.ratings
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
    val location: ClubLocation = ClubLocation(0.0, 0.0),
    val services: List<String> = listOf(),
    val images: List<String> = listOf(),
    val chats: List<String> = listOf(),
    val ratings: List<Int> = listOf()
) {
    fun toModel(): ClubDto {
        return ClubDto(
            name = this.name,
            contactEmail = this.contactEmail,
            contactPhone = this.contactPhone,
            description = this.description,
            address = this.address,
            location = ClubLocation(this.location.latitude, this.location.longitude),
            services = this.services,
            images = this.images,
            keywords = generateKeywords(this.name),
            chats = this.chats,
            ratings = this.ratings
        )
    }
}

data class ClubLocation(
    val latitude: Double = 0.0,
    val longitude: Double = 0.0
)

// Generate all possible keyword from string: Used for searching
fun generateKeywords(name: String): List<String> {
    val keywords = mutableListOf<String>()
    val nameLowercase = name.lowercase()
    for (i in nameLowercase.indices) {
        for (j in (i+1)..nameLowercase.length) {
            keywords.add(nameLowercase.slice(i until j))
        }
    }
    return keywords
}