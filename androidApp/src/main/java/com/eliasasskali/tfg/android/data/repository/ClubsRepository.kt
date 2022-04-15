package com.eliasasskali.tfg.android.data.repository

import androidx.paging.PagingData
import com.eliasasskali.tfg.model.Club
import kotlinx.coroutines.flow.Flow

interface ClubsRepository {
    fun getClubs(searchString: String = ""): Flow<PagingData<Club>>
}