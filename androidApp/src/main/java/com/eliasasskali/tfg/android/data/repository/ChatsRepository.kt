package com.eliasasskali.tfg.android.data.repository

import com.eliasasskali.tfg.android.ui.features.chat.ChatResponse
import com.eliasasskali.tfg.android.ui.features.chats.ChatsResponse
import com.eliasasskali.tfg.model.*
import kotlinx.coroutines.flow.Flow

interface ChatsRepository {
    fun getChat(chatId: String): Flow<ChatResponse>
    fun getUserChats(chatIds: List<String>): Flow<ChatsResponse>
    suspend fun sendMessage(message: Message, chatId: String): Either<DomainError, Success>
}