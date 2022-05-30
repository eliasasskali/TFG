package com.eliasasskali.tfg.android.data.repository.clubs

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.eliasasskali.tfg.android.data.dataSource.ClubsPagingSource
import com.eliasasskali.tfg.android.data.repository.clubs.ClubsRepository
import com.google.firebase.firestore.FirebaseFirestore

class ClubsRepositoryImpl(
    private val config: PagingConfig
) : ClubsRepository {
    override fun getClubs(searchString: String, sportsFilters: List<String>) = Pager(
        config = config
    ) {
        ClubsPagingSource(
            queryClubs = FirebaseFirestore.getInstance().collection("Clubs"),
            searchString = searchString,
            sportsFilters = sportsFilters
        )
    }.flow
}