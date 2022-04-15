package com.eliasasskali.tfg.android.ui.features.clubs

import androidx.compose.runtime.MutableState
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
    val clubs = repository.getClubs().cachedIn(viewModelScope)

    fun setError(error: String) {
        state.value = state.value.copy(error = error)
    }
}