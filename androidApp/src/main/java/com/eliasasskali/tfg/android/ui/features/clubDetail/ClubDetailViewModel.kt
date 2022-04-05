package com.eliasasskali.tfg.android.ui.features.clubDetail

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eliasasskali.tfg.android.data.repository.ClubAthleteRepository
import com.eliasasskali.tfg.model.Club
import com.eliasasskali.tfg.model.DomainError
import com.eliasasskali.tfg.model.Either
import com.eliasasskali.tfg.ui.executor.Executor
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ClubDetailViewModel(
    private val repository: ClubAthleteRepository,
    private val executor: Executor,
) :
    ViewModel() {

    val clubState: MutableState<ClubDetailState> = mutableStateOf(ClubDetailState())
    private suspend fun <T> execute(f: suspend () -> Either<DomainError, T>): Either<DomainError, T> =
        withContext(executor.bg) { f() }

    fun initClubDetailScreen(clubId: String) {
        viewModelScope.launch {
            execute {
                repository.getClubById(clubId)
            }.fold(
                error = {},
                success = {
                    clubState.value = clubState.value.copy(club = it ?: Club())
                }
            )

            execute {
                repository.getClubImages(clubId)
            }.fold(
                error = {},
                success = {
                    clubState.value = clubState.value.copy(clubImages = it)
                }
            )
        }
    }
}