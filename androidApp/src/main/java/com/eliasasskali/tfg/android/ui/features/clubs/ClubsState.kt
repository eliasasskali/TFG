package com.eliasasskali.tfg.android.ui.features.clubs

import androidx.paging.PagingData
import com.eliasasskali.tfg.model.Club
import com.eliasasskali.tfg.model.DomainError
import com.eliasasskali.tfg.model.Either
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

data class ClubsState(
    val error: String = "",
    val isLoading: Boolean = false,
    val data: Flow<PagingData<Club>> = flowOf()
)