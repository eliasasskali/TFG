package com.eliasasskali.tfg.android.ui.features.chat

import com.eliasasskali.tfg.model.DomainError

data class ChatState(
    val chatId: String = "",
    val newMessage: String = "",
    val step: ChatSteps = ChatSteps.IsLoading,
)

sealed class ChatSteps {
    object IsLoading : ChatSteps()
    object ShowChat : ChatSteps()
    class Error(val error: DomainError) : ChatSteps()
}