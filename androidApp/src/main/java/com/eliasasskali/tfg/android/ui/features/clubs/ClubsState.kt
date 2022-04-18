package com.eliasasskali.tfg.android.ui.features.clubs

import android.location.Location
import androidx.paging.PagingData
import com.eliasasskali.tfg.model.Club
import com.eliasasskali.tfg.model.DomainError
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

data class ClubsState(
    val error: String = "",
    val isLoading: Boolean = false,
    val searchString: String = "",
    val data: Flow<PagingData<Club>> = flowOf(),
    val step: ClubListSteps = ClubListSteps.ShowClubs,
    val sportFilters: List<String> = listOf(),
    val filterLocation: Location = Location(""),
    val filterLocationRadius: Int = 500
)

sealed class ClubListSteps {
    object IsLoading : ClubListSteps()
    object ShowClubs : ClubListSteps()
    object ShowFilterBySports : ClubListSteps()
    object ShowFilterByLocation : ClubListSteps()
    class Error(val error: DomainError) : ClubListSteps()
}