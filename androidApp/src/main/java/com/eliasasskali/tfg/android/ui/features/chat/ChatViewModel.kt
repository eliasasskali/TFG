package com.eliasasskali.tfg.android.ui.features.chat

import android.os.Build
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.eliasasskali.tfg.android.core.ui.RootViewModel
import com.eliasasskali.tfg.android.data.repository.ChatsRepository
import com.eliasasskali.tfg.data.preferences.Preferences
import com.eliasasskali.tfg.model.Message
import com.eliasasskali.tfg.ui.error.ErrorHandler
import com.eliasasskali.tfg.ui.executor.Executor
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*

class ChatViewModel(
    private val repository: ChatsRepository,
    private val preferences: Preferences,
    executor: Executor,
    errorHandler: ErrorHandler
) : RootViewModel(executor, errorHandler) {

    val state: MutableState<ChatState> = mutableStateOf(ChatState())

    fun getChatMutable() = repository.getChat(state.value.chatId)

    fun initChatScreen(chatId: String) {
        setStep(ChatSteps.IsLoading)
        state.value = state.value.copy(chatId = chatId)
        setStep(ChatSteps.ShowChat)
    }

    fun setStep(step: ChatSteps) {
        state.value = state.value.copy(step = step)
    }

    fun setNewMessage(newMessage: String) {
        state.value = state.value.copy(newMessage = newMessage)
    }

    fun isSender(message: Message): Boolean = preferences.getLoggedUid() == message.senderId

    fun isClub(): Boolean = preferences.isClub()

    fun sendMessage(newMessage: String) {
        val message = Message(
            message = newMessage,
            senderId = preferences.getLoggedUid(),
            sentOn = System.currentTimeMillis()
        )
        viewModelScope.launch {
            execute {
                repository.sendMessage(message, state.value.chatId)
            }.fold(
                success = {
                    setNewMessage("")
                },
                error = {
                    setStep(
                        ChatSteps.Error(
                            error = errorHandler.convert(it),
                            onRetry = { sendMessage(newMessage) }
                        )
                    )
                }
            )
        }
    }

    // TODO: Move to repo
    fun getDateTime(timeStamp: Long): String {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            java.time.Instant.ofEpochMilli(timeStamp)
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime()
                .format(DateTimeFormatter.ofPattern("dd-MMM-yyyy, HH:mm"))
                .toString()
        } else {
            try {
                val sdf = SimpleDateFormat("dd-MMM-yyyy, HH:mm")
                val netDate = Date(timeStamp)
                sdf.format(netDate)
            } catch (e: Exception) {
                e.toString()
            }
        }
    }
}