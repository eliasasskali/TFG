package com.eliasasskali.tfg.android.ui.features.clubDetail

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eliasasskali.tfg.android.data.repository.ChatsRepository
import com.eliasasskali.tfg.android.data.repository.ClubAthleteRepository
import com.eliasasskali.tfg.data.preferences.Preferences
import com.eliasasskali.tfg.model.Athlete
import com.eliasasskali.tfg.model.DomainError
import com.eliasasskali.tfg.model.Either
import com.eliasasskali.tfg.ui.executor.Executor
import com.google.gson.Gson
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ClubDetailViewModel(
    private val repository: ClubAthleteRepository,
    private val chatsRepository: ChatsRepository,
    private val executor: Executor,
    private val preferences: Preferences
) : ViewModel() {

    val state: MutableState<ClubDetailState> = mutableStateOf(ClubDetailState())
    private suspend fun <T> execute(f: suspend () -> Either<DomainError, T>): Either<DomainError, T> =
        withContext(executor.bg) { f() }

    fun initClubDetailScreen(clubId: String) {
        val athlete = Gson().fromJson(preferences.getProfileJson(), Athlete::class.java)
        viewModelScope.launch {
            execute {
                repository.getClubById(clubId)
            }.fold(
                error = {

                },
                success = {
                    it?.let { club ->
                        state.value = state.value.copy(club = club, athlete = athlete)
                    }
                }
            )
        }
    }

    fun isClubOwner(clubId: String) {
        viewModelScope.launch {
            execute {
                repository.isClubOwner(clubId)
            }.fold(
                error = {},
                success = {
                    state.value = state.value.copy(isClubOwner = it)
                }
            )
        }
    }

    fun followClub(clubId: String) {
        viewModelScope.launch {
            execute {
                repository.followClub(clubId)
            }.fold(
                error = {},
                success = {
                    saveAthletePreferences()
                }
            )
        }
    }

    fun unFollowClub(clubId: String) {
        viewModelScope.launch {
            execute {
                repository.unFollowClub(clubId)
            }.fold(
                error = {},
                success = {
                    saveAthletePreferences()
                }
            )
        }
    }

    fun saveAthletePreferences() {
        viewModelScope.launch {
            execute {
                repository.saveAthletePreferences()
            }.fold(
                error = {},
                success = {
                    initClubDetailScreen(state.value.club.id)
                }
            )
        }
    }

    fun getOrCreateChatWithClub(onChatFoundOrCreated: (chatId: String) -> Unit) {
        viewModelScope.launch {
            execute {
                chatsRepository.getOrCreateChatWithClub(
                    athleteId = preferences.getLoggedUid(),
                    clubId = state.value.club.id,
                    athleteName = state.value.athlete.name,
                    clubName = state.value.club.name
                )
            }.fold(
                error = {

                },
                success = {
                    onChatFoundOrCreated(it)
                }
            )
        }
    }
}