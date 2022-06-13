package com.eliasasskali.tfg.android.ui.features.athleteProfile

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.eliasasskali.tfg.android.core.ui.RootViewModel
import com.eliasasskali.tfg.android.data.repository.authentication.AuthRepository
import com.eliasasskali.tfg.android.data.repository.clubAthlete.ClubAthleteRepositoryImpl
import com.eliasasskali.tfg.data.preferences.Preferences
import com.eliasasskali.tfg.model.Athlete
import com.eliasasskali.tfg.ui.error.ErrorHandler
import com.eliasasskali.tfg.ui.executor.Executor
import com.google.gson.Gson
import kotlinx.coroutines.launch


class AthleteProfileViewModel(
    private val preferences: Preferences,
    private val authRepository: AuthRepository,
    private val repository: ClubAthleteRepositoryImpl,
    executor: Executor,
    errorHandler: ErrorHandler
) : RootViewModel(executor, errorHandler) {

    val state: MutableState<AthleteProfileState> = mutableStateOf(AthleteProfileState())

    fun initAthleteProfileScreen() {
        setStep(AthleteProfileSteps.IsLoading)
        val athlete = Gson().fromJson(preferences.getProfileJson(), Athlete::class.java)
        state.value = state.value.copy(athlete = athlete)
        getFollowingClubNames(athlete.following)
        setStep(AthleteProfileSteps.ShowAthleteProfile)
    }

    fun setStep(step: AthleteProfileSteps) {
        state.value = state.value.copy(step = step)
    }

    fun logOut(onLoggedOut: () -> Unit) {
        viewModelScope.launch {
            execute {
                authRepository.logOut()
            }.fold(
                error = {
                    setStep(
                        AthleteProfileSteps.Error(
                            error = errorHandler.convert(it),
                            onRetry = { logOut(onLoggedOut) }
                        )
                    )
                },
                success = {
                    onLoggedOut()
                }
            )
        }
    }

    private fun getFollowingClubNames(following: List<String>) {
        viewModelScope.launch {
            execute {
                repository.getFollowingClubNames(following)
            }.fold(
                error = {
                    setStep(
                        AthleteProfileSteps.Error(
                            error = errorHandler.convert(it),
                            onRetry = { getFollowingClubNames(following) }
                        )
                    )
                },
                success = { followingClubs ->
                    state.value = state.value.copy(followingClubs = followingClubs)
                }
            )
        }
    }

    fun unFollowClub(clubId: String) {
        viewModelScope.launch {
            execute {
                repository.unFollowClub(clubId)
            }.fold(
                error = {
                    setStep(
                        AthleteProfileSteps.Error(
                            error = errorHandler.convert(it),
                            onRetry = { unFollowClub(clubId) }
                        )
                    )
                },
                success = {
                    saveAthletePreferences()
                }
            )
        }
    }

    private fun saveAthletePreferences() {
        viewModelScope.launch {
            execute {
                repository.saveAthletePreferences()
            }.fold(
                error = {
                    setStep(
                        AthleteProfileSteps.Error(
                            error = errorHandler.convert(it),
                            onRetry = { saveAthletePreferences() }
                        )
                    )
                },
                success = {
                    initAthleteProfileScreen()
                }
            )
        }
    }
}