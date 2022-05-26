package com.eliasasskali.tfg.model

data class ChatDto(
    val user1: String = "",
    val user2: String = "",
    val user1Name: String = "",
    val user2Name: String = "",
    val messages: List<Message> = listOf(),
) {
    fun toModel(chatId: String) : Chat {
        return Chat(
            chatId = chatId,
            user1 = this.user1,
            user2 = this.user2,
            user1Name = this.user1Name,
            user2Name = this.user2Name,
            messages = this.messages
        )
    }
}

data class Chat(
    val chatId: String = "",
    val user1: String = "",
    val user2: String = "",
    val user1Name: String = "",
    val user2Name: String = "",
    val messages: List<Message> = listOf(),
) {
    fun toModel() : ChatDto {
        return ChatDto(
            user1 = this.user1,
            user2 = this.user2,
            user1Name = this.user1Name,
            user2Name = this.user2Name,
            messages = this.messages
        )
    }
}