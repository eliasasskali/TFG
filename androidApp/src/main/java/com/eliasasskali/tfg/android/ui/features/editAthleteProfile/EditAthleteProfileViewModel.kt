package com.eliasasskali.tfg.android.ui.features.editAthleteProfile

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.eliasasskali.tfg.android.core.ui.RootViewModel
import com.eliasasskali.tfg.android.data.repository.clubAthlete.ClubAthleteRepository
import com.eliasasskali.tfg.data.preferences.Preferences
import com.eliasasskali.tfg.model.Athlete
import com.eliasasskali.tfg.model.DomainError
import com.eliasasskali.tfg.ui.error.ErrorHandler
import com.eliasasskali.tfg.ui.executor.Executor
import com.google.gson.Gson
import kotlinx.coroutines.launch

class EditAthleteProfileViewModel(
    private val repository: ClubAthleteRepository,
    private val preferences: Preferences,
    executor: Executor,
    errorHandler: ErrorHandler
) : RootViewModel(executor, errorHandler) {
    val state: MutableState<EditAthleteProfileState> = mutableStateOf(EditAthleteProfileState())

    fun setStep(step: EditAthleteProfileSteps) {
        state.value = state.value.copy(step = step)
    }

    fun initEditAthleteProfileScreen() {
        val athlete = Gson().fromJson(preferences.getProfileJson(), Athlete::class.java)
        state.value = state.value.copy(
            athlete = athlete,
            name = athlete.name,
            interests = athlete.interests?.toSet() ?: emptySet()
        )
        setStep(EditAthleteProfileSteps.ShowEditAthleteProfile)
    }

    fun setName(name: String) {
        state.value = state.value.copy(name = name)
    }

    fun setInterests(interests: Set<String>) {
        state.value = state.value.copy(interests = interests)
    }

    fun updateAthleteProfile(onUpdateFinished: () -> Unit) {
        setStep(EditAthleteProfileSteps.IsLoading)
        viewModelScope.launch {
            execute {
                repository.updateAthleteProfile(
                    athlete = state.value.athlete,
                    newAthleteName = state.value.name,
                    newInterests = state.value.interests.toList()
                )
            }.fold(
                error = {
                    setStep(
                        EditAthleteProfileSteps.Error(
                            error = errorHandler.convert(it),
                            onRetry = { updateAthleteProfile(onUpdateFinished) }
                        )
                    )
                },
                success = {
                    updatePreferencesClub(onUpdateFinished)
                }
            )
        }
    }

    private fun updatePreferencesClub(onUpdateFinished: () -> Unit) {
        viewModelScope.launch {
            execute {
                repository.getAthleteById(preferences.getLoggedUid())
            }.fold(
                error = {
                    setStep(
                        EditAthleteProfileSteps.Error(
                            error = errorHandler.convert(it),
                            onRetry = { updatePreferencesClub(onUpdateFinished) }
                        )
                    )
                },
                success = { athlete ->
                    try {
                        athlete?.let {
                            val jsonAthlete = Gson().toJson(it)
                            preferences.saveProfileJson(jsonAthlete)
                            setStep(EditAthleteProfileSteps.ShowEditAthleteProfile)
                            onUpdateFinished()
                        }
                    } catch (e: Exception) {
                        setStep(
                            EditAthleteProfileSteps.Error(
                                error = errorHandler.convert(DomainError.ServiceError),
                                onRetry = { updatePreferencesClub(onUpdateFinished) }
                            )
                        )
                    }
                }
            )
        }
    }
}