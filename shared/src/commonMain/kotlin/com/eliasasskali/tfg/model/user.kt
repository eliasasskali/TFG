package com.eliasasskali.tfg.model

data class AthleteDto(
    val name: String = "",
    val interests: List<String>? = emptyList(),
    val following: List<String> = emptyList()
) {
    fun toModel() : Athlete {
        return Athlete(
            name = this.name,
            interests = this.interests,
            following = this.following
        )
    }
}

data class Athlete(
    val name: String = "",
    val interests: List<String>? = emptyList(),
    val following: List<String> = emptyList()
) {
    fun toModel() : AthleteDto {
        return AthleteDto(
            name = this.name,
            interests = this.interests,
            following = this.following
        )
    }
}