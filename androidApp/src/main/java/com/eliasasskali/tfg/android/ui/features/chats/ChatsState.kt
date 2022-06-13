package com.eliasasskali.tfg.android.ui.features.chats

data class ChatsState(
    val step: ChatsSteps = ChatsSteps.ShowChats,
)

sealed class ChatsSteps {
    object IsLoading : ChatsSteps()
    object ShowChats : ChatsSteps()
    class Error(val error: String, val onRetry: () -> Unit) : ChatsSteps()
}