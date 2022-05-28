package com.eliasasskali.tfg.model

data class ChatDto(
    val athleteId: String = "",
    val clubId: String = "",
    val athleteName: String = "",
    val clubName: String = "",
    val messages: List<Message> = listOf(),
) {
    fun toModel(chatId: String) : Chat {
        return Chat(
            chatId = chatId,
            athleteId = this.athleteId,
            clubId = this.clubId,
            athleteName = this.athleteName,
            clubName = this.clubName,
            messages = this.messages
        )
    }
}

data class Chat(
    val chatId: String = "",
    val athleteId: String = "",
    val clubId: String = "",
    val athleteName: String = "",
    val clubName: String = "",
    val messages: List<Message> = listOf(),
) {
    fun toModel() : ChatDto {
        return ChatDto(
            athleteId = this.athleteId,
            clubId = this.clubId,
            athleteName = this.athleteName,
            clubName = this.clubName,
            messages = this.messages
        )
    }
}