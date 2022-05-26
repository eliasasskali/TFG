package com.eliasasskali.tfg.android.ui.features.chats

import android.os.Build
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.eliasasskali.tfg.android.core.ui.RootViewModel
import com.eliasasskali.tfg.android.data.repository.ChatsRepository
import com.eliasasskali.tfg.data.preferences.Preferences
import com.eliasasskali.tfg.model.Athlete
import com.eliasasskali.tfg.model.Chat
import com.eliasasskali.tfg.model.Club
import com.eliasasskali.tfg.ui.error.ErrorHandler
import com.eliasasskali.tfg.ui.executor.Executor
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import java.text.SimpleDateFormat
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*

class ChatsViewModel(
    private val repository: ChatsRepository,
    private val preferences: Preferences,
    executor: Executor,
    errorHandler: ErrorHandler
) : RootViewModel(executor, errorHandler) {

    val state: MutableState<ChatsState> = mutableStateOf(ChatsState())

    fun getChatsMutable() : Flow<ChatsResponse> {
        val chatIds = if (preferences.isClub()) {
            Gson().fromJson(preferences.getProfileJson(), Club::class.java).chats
        } else {
            Gson().fromJson(preferences.getProfileJson(), Athlete::class.java).chats
        }
        return repository.getUserChats(chatIds = chatIds)
    }

    fun setStep(step: ChatsSteps) {
        state.value = state.value.copy(step = step)
    }

    fun getOtherUserName(chat: Chat): String {
        return if (chat.user1 == preferences.getLoggedUid()) {
            chat.user2Name
        } else {
            chat.user1Name
        }
    }

    fun getLastMessageDateTime(chat: Chat): String {
        return getDateTime(chat.messages.last().sentOn)
    }

    private fun getDateTime(timeStamp: Long): String {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            java.time.Instant.ofEpochMilli(timeStamp)
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime()
                .format(DateTimeFormatter.ofPattern("dd-MMM-yyyy, HH:mm"))
                .toString()
        } else {
            try {
                val sdf = SimpleDateFormat("dd-MMM-yyyy, HH:mm")
                val netDate = Date(timeStamp * 1000)
                sdf.format(netDate)
            } catch (e: Exception) {
                e.toString()
            }
        }
    }
}