package com.eliasasskali.tfg.android.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig

class ClubsRepositoryImpl(
    private val source: FirestorePagingSource,
    private val config: PagingConfig
) : ClubsRepository {
    override fun getClubs() = Pager(
        config = config
    ) {
        source
    }.flow
}