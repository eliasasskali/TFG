package com.eliasasskali.tfg.android.ui.features.chats

import com.eliasasskali.tfg.model.DomainError

data class ChatsState(
    val step: ChatsSteps = ChatsSteps.ShowChats,
)

sealed class ChatsSteps {
    object IsLoading : ChatsSteps()
    object ShowChats : ChatsSteps()
    class Error(val error: DomainError) : ChatsSteps()
}