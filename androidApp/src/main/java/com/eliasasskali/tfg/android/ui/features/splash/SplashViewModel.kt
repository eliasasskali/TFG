package com.eliasasskali.tfg.android.ui.features.splash

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.eliasasskali.tfg.android.core.ui.RootViewModel
import com.eliasasskali.tfg.android.data.repository.AuthRepository
import com.eliasasskali.tfg.android.data.repository.ClubAthleteRepository
import com.eliasasskali.tfg.ui.error.ErrorHandler
import com.eliasasskali.tfg.ui.executor.Executor
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashViewModel(
    private val authRepository: AuthRepository,
    private val repository: ClubAthleteRepository,
    executor: Executor,
    errorHandler: ErrorHandler,
) : RootViewModel(executor, errorHandler) {

    val state: MutableState<SplashState> = mutableStateOf(SplashState(screen = Screen.LOGIN))

    fun checkUserLoginState(onSplashComplete: (SplashState) -> Unit) {
        viewModelScope.launch {
            execute {
                delay(SPLASH_DELAY_TIME_MILLIS)
                authRepository.isUserAuthenticated()
            }.fold(error = {
                // TODO : Handle Error
                state.value = state.value.copy(screen = Screen.LOGIN)
                onSplashComplete(state.value)
            }, success = {
                if (!it) {
                    // User not logged -> Navigate to login
                    state.value = state.value.copy(screen = Screen.LOGIN)
                    onSplashComplete(state.value)
                } else {
                    // User logged, check if profile is completed
                    // Check if Club or Athlete related to user id exists.
                    viewModelScope.launch {
                        execute {
                            repository.getClubById(authRepository.getCurrentUser()?.uid!!)
                        }.fold(
                            error = {
                                // TODO : Handle error
                                state.value = state.value.copy(screen = Screen.COMPLETE_PROFILE)
                                onSplashComplete(state.value)
                            },
                            success = { club ->
                                if (club != null) {
                                    // CLub Exists -> Navigate to home
                                    state.value = state.value.copy(screen = Screen.HOME)
                                    onSplashComplete(state.value)
                                } else {
                                    // Club Does not Exist -> Check if athlete exists
                                    viewModelScope.launch {
                                        execute {
                                            repository.getUserById(authRepository.getCurrentUser()?.uid!!)
                                        }.fold(
                                            error = {
                                                // TODO : Handle Error
                                                state.value =
                                                    state.value.copy(screen = Screen.COMPLETE_PROFILE)
                                                onSplashComplete(state.value)
                                            },
                                            success = { athlete ->
                                                if (athlete != null) {
                                                    // Athlete exists -> Navigate to home
                                                    state.value =
                                                        state.value.copy(screen = Screen.HOME)
                                                    onSplashComplete(state.value)
                                                } else {
                                                    // Athlete nor Club exists -> Navigate to Complete Profile
                                                    state.value =
                                                        state.value.copy(screen = Screen.COMPLETE_PROFILE)
                                                    onSplashComplete(state.value)
                                                }
                                            }
                                        )
                                    }
                                }
                            }
                        )
                    }
                }
            })
        }
    }

    companion object {
        const val SPLASH_DELAY_TIME_MILLIS = 2000L
    }
}