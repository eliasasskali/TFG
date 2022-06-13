package com.eliasasskali.tfg.android.ui.features.chat

data class ChatState(
    val chatId: String = "",
    val newMessage: String = "",
    val step: ChatSteps = ChatSteps.IsLoading,
)

sealed class ChatSteps {
    object IsLoading : ChatSteps()
    object ShowChat : ChatSteps()
    class Error(val error: String, val onRetry: () -> Unit) : ChatSteps()
}