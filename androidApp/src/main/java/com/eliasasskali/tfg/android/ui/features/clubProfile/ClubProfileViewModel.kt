package com.eliasasskali.tfg.android.ui.features.clubProfile

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.eliasasskali.tfg.android.core.ui.RootViewModel
import com.eliasasskali.tfg.android.data.repository.AuthRepository
import com.eliasasskali.tfg.android.data.repository.ClubAthleteRepository
import com.eliasasskali.tfg.ui.error.ErrorHandler
import com.eliasasskali.tfg.ui.executor.Executor
import kotlinx.coroutines.launch

class ClubProfileViewModel(
    private val repository: ClubAthleteRepository,
    private val authRepository: AuthRepository,
    executor: Executor,
    errorHandler: ErrorHandler
) : RootViewModel(executor, errorHandler) {
    val state: MutableState<ClubProfileState> = mutableStateOf(ClubProfileState())

    fun setStep(step: ClubProfileSteps) {
        state.value = state.value.copy(step = step)
    }

    fun initClubProfile() {
        val clubId = authRepository.getCurrentUser()?.uid
        clubId?.let {
            viewModelScope.launch {
                execute {
                    repository.getClubById(it)
                }.fold(
                    error = {
                        setStep(ClubProfileSteps.Error(errorHandler.convert(it)))
                    },
                    success = { club ->
                        club?.let {
                            state.value = state.value.copy(club = it)
                            setStep(ClubProfileSteps.ShowClubProfile)
                        }
                    }
                )
            }
        }
    }
}