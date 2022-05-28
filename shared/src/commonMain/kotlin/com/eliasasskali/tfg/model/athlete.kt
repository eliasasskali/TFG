package com.eliasasskali.tfg.model

data class AthleteDto(
    val name: String = "",
    val interests: List<String>? = emptyList(),
    val following: List<String> = emptyList(),
    val chats: List<String> = listOf()
) {
    fun toModel() : Athlete {
        return Athlete(
            name = this.name,
            interests = this.interests,
            following = this.following,
            chats = this.chats
        )
    }
}

data class Athlete(
    val name: String = "",
    val interests: List<String>? = emptyList(),
    val following: List<String> = emptyList(),
    val chats: List<String> = listOf()
) {
    fun toModel() : AthleteDto {
        return AthleteDto(
            name = this.name,
            interests = this.interests,
            following = this.following,
            chats = this.chats
        )
    }
}