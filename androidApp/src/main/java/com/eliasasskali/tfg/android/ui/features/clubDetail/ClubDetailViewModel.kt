package com.eliasasskali.tfg.android.ui.features.clubDetail

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eliasasskali.tfg.android.data.repository.ClubAthleteRepository
import com.eliasasskali.tfg.data.preferences.Preferences
import com.eliasasskali.tfg.model.Athlete
import com.eliasasskali.tfg.model.Club
import com.eliasasskali.tfg.model.DomainError
import com.eliasasskali.tfg.model.Either
import com.eliasasskali.tfg.ui.executor.Executor
import com.google.gson.Gson
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ClubDetailViewModel(
    private val repository: ClubAthleteRepository,
    private val executor: Executor,
    private val preferences: Preferences
) : ViewModel() {

    val clubState: MutableState<ClubDetailState> = mutableStateOf(ClubDetailState())
    private suspend fun <T> execute(f: suspend () -> Either<DomainError, T>): Either<DomainError, T> =
        withContext(executor.bg) { f() }

    fun initClubDetailScreen(club: Club) {
        val athlete = Gson().fromJson(preferences.getProfileJson(), Athlete::class.java)
        clubState.value = clubState.value.copy(club = club, athlete = athlete)
    }

    fun isClubOwner(clubId: String) {
        viewModelScope.launch {
            execute {
                repository.isClubOwner(clubId)
            }.fold(
                error = {},
                success = {
                    clubState.value = clubState.value.copy(isClubOwner = it)
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
                    initClubDetailScreen(clubState.value.club)
                }
            )
        }
    }
}