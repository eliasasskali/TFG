package com.eliasasskali.tfg.model

data class MessageDto(
    val message: String = "",
    val senderId: String = "",
    val sentOn: Long = 0,
) {

    fun toModel(): Message {
        return Message(
            message = this.message,
            senderId = this.senderId,
            sentOn = this.sentOn,
        )
    }
}

data class Message(
    val message: String = "",
    val senderId: String = "",
    val sentOn: Long = 0,
) {
    fun toModel(): MessageDto {
        return MessageDto(
            message = this.message,
            senderId = this.senderId,
            sentOn = this.sentOn,
        )
    }
}