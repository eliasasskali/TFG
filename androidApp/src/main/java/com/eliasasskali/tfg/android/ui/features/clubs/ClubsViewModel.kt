package com.eliasasskali.tfg.android.ui.features.clubs

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.eliasasskali.tfg.android.core.ui.RootViewModel
import com.eliasasskali.tfg.android.data.repository.ClubsRepository
import com.eliasasskali.tfg.ui.error.ErrorHandler
import com.eliasasskali.tfg.ui.executor.Executor
import kotlinx.coroutines.launch

class ClubsViewModel(
    private val repository: ClubsRepository,
    executor: Executor,
    errorHandler: ErrorHandler
) : RootViewModel(executor, errorHandler) {

    val state: MutableState<ClubsState> = mutableStateOf(ClubsState())
    var clubs = repository.getClubs(state.value.searchString).cachedIn(viewModelScope)

    fun setError(error: String) {
        state.value = state.value.copy(error = error)
    }

    fun setSearchString(searchString: String) {
        state.value = state.value.copy(searchString = searchString)
        clubs = repository.getClubs(state.value.searchString, state.value.sportFilters).cachedIn(viewModelScope)
    }

    fun setSportFilters(sports: List<String>) {
        state.value = state.value.copy(sportFilters = sports)
        clubs = repository.getClubs(state.value.searchString, state.value.sportFilters).cachedIn(viewModelScope)
    }

    fun setStep(step: ClubListSteps) {
        state.value = state.value.copy(step = step)
    }

    fun setIsLoading(isLoading: Boolean) {
        state.value = state.value.copy(isLoading = isLoading)
    }
}