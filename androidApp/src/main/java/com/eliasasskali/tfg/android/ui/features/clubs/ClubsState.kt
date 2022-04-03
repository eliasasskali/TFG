package com.eliasasskali.tfg.android.ui.features.clubs

import com.eliasasskali.tfg.model.Club
import com.eliasasskali.tfg.model.DomainError
import com.eliasasskali.tfg.model.Either

data class ClubsState(
    val error: String = "",
    val isLoading: Boolean = false,
    val data: Either<DomainError, List<Club>> = Either.Right(emptyList())
)