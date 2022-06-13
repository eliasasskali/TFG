package com.eliasasskali.tfg.android.ui.features.clubProfile

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.eliasasskali.tfg.android.core.ui.RootViewModel
import com.eliasasskali.tfg.android.data.repository.authentication.AuthRepository
import com.eliasasskali.tfg.data.preferences.Preferences
import com.eliasasskali.tfg.model.Club
import com.eliasasskali.tfg.ui.error.ErrorHandler
import com.eliasasskali.tfg.ui.executor.Executor
import com.google.gson.Gson
import kotlinx.coroutines.launch

class ClubProfileViewModel(
    private val preferences: Preferences,
    val authRepository: AuthRepository,
    executor: Executor,
    errorHandler: ErrorHandler
) : RootViewModel(executor, errorHandler) {
    val state: MutableState<ClubProfileState> = mutableStateOf(ClubProfileState())

    fun setStep(step: ClubProfileSteps) {
        state.value = state.value.copy(step = step)
    }

    fun initClubProfile() {
        val club = Gson().fromJson(preferences.getProfileJson(), Club::class.java)
        state.value = state.value.copy(club = club)
        setStep(ClubProfileSteps.ShowClubProfile)
    }

    fun logOut(onLoggedOut: () -> Unit) {
        viewModelScope.launch {
            execute {
                authRepository.logOut()
            }.fold(
                error = {
                    // TODO: Handle error
                },
                success = {
                    onLoggedOut()
                }
            )
        }
    }
}