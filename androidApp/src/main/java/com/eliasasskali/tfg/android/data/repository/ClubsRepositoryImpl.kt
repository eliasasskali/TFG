package com.eliasasskali.tfg.android.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.google.firebase.firestore.FirebaseFirestore

class ClubsRepositoryImpl(
    private val config: PagingConfig
) : ClubsRepository {
    override fun getClubs(searchString: String, sportsFilters: List<String>) = Pager(
        config = config
    ) {
        FirestorePagingSource(
            queryClubs = FirebaseFirestore.getInstance().collection("Clubs"),
            searchString = searchString,
            sportsFilters = sportsFilters
        )
    }.flow
}