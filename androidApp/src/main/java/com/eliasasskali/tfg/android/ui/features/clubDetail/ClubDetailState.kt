package com.eliasasskali.tfg.android.ui.features.clubDetail

import android.graphics.Bitmap
import com.eliasasskali.tfg.model.Club

data class ClubDetailState(
    val club: Club = Club(),
    val clubImages: List<Bitmap?> = emptyList()
)