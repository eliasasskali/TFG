package com.eliasasskali.tfg.android.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.google.firebase.firestore.FirebaseFirestore

class ClubsRepositoryImpl(
    private val source: FirestorePagingSource,
    private val config: PagingConfig
) : ClubsRepository {
    override fun getClubs(searchString: String) = Pager(
        config = config
    ) {
        if (searchString.isBlank()) source
        else FirestorePagingSource(
            queryClubs = FirebaseFirestore.getInstance().collection("Clubs"),
            searchString = searchString
        )
    }.flow
}