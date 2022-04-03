package com.eliasasskali.tfg.android.ui.features.clubs

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.eliasasskali.tfg.android.core.ui.RootViewModel
import com.eliasasskali.tfg.android.data.repository.ClubAthleteRepository
import com.eliasasskali.tfg.ui.error.ErrorHandler
import com.eliasasskali.tfg.ui.executor.Executor
import kotlinx.coroutines.launch

class ClubsViewModel(
    private val repository: ClubAthleteRepository,
    executor: Executor,
    errorHandler: ErrorHandler
) : RootViewModel(executor, errorHandler) {

    val state: MutableState<ClubsState> = mutableStateOf(ClubsState())

    init {
        getClubs()
    }

    private fun getClubs() {
        viewModelScope.launch {
            state.value = state.value.copy(isLoading = true)
            state.value = state.value.copy(data = repository.getClubsFromFirestore())
            state.value = state.value.copy(isLoading = false)
        }
    }
}