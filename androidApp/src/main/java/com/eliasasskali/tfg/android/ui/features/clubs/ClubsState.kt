package com.eliasasskali.tfg.android.ui.features.clubs

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.input.TextFieldValue
import androidx.paging.PagingData
import com.eliasasskali.tfg.model.Club
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

data class ClubsState(
    val error: String = "",
    val isLoading: Boolean = false,
    val searchString: String = "",
    val data: Flow<PagingData<Club>> = flowOf()
)