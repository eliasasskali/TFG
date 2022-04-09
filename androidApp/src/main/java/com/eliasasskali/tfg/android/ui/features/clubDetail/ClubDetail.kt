package com.eliasasskali.tfg.android.ui.features.clubDetail

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.eliasasskali.tfg.android.ui.components.ImagePager
import com.eliasasskali.tfg.android.ui.components.ScrollableChipsRow
import com.eliasasskali.tfg.android.ui.theme.AppTheme
import com.eliasasskali.tfg.model.ClubDto
import com.eliasasskali.tfg.model.ClubLocation

@Composable
fun ClubDetailScreen(
    clubDetailState: ClubDetailState,
) {
    Surface(color = Color.LightGray, modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            if (clubDetailState.club.images.isNotEmpty()) {
                ImagePager(imageList = clubDetailState.club.images)
            }
            Spacer(modifier = Modifier.size(8.dp))
            Row(
                modifier = Modifier
                    .padding(12.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "950m from your location",
                    // TODO: Compute distance
                )
                // TODO: Rating View
                Text(
                    text = "⭐⭐⭐⭐⭐"
                )
            }
            Text(
                modifier = Modifier.padding(12.dp),
                text = clubDetailState.club.name,
                style = MaterialTheme.typography.h5
            )
            Spacer(modifier = Modifier.size(8.dp))
            clubDetailState.club.services?.let { ScrollableChipsRow(elements = it) }
            clubDetailState.club.description?.let {
                Text(
                    modifier = Modifier.padding(12.dp),
                    text = it
                )
            }
            ClubDetailMapView(clubDetailState.club)
            if (clubDetailState.club.location.latitude != 0.0 && clubDetailState.club.location.longitude != 0.0) {
                ClubDetailMap(clubDetailState.club)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ClubDetailScreenPreview() {
    val club = ClubDto(
        name = "CN VIC-ETB",
        contactEmail = "vicetb.cat",
        contactPhone = "938853121",
        description = "Club Natació Vic - Estadi Torres i Bages, tal i com el coneixem avui, té una llarga història. El seu origen es remunta a l’any 1955 en què va néixer la idea de construir una zona esportiva per als joves de la ciutat.\n" +
                "\n" +
                "Així doncs, el Bisbat de Vic amb el suport de vàries famílies i personalitats col·laboradores, entre les quals va destacar el Sr. Joan Riera Rius, van tirar endavant el projecte al terreny que més endavant s’anomenaria Estadi Torres i Bages. A aquestes instal·lacions, poc temps després, s’hi van afegir una pista d’atletisme i un camp de futbol.",
        address = "Carrer de Josep Maria Pallàs, 1, 08500 Vic, Barcelona",
        location = ClubLocation(41.92055251450252, 2.2564390268134584),
        services = listOf("Swimming", "Running", "Gym", "Triathlon", "Body Pump", "Spinning", "Core", "Open Waters")
    )
    val clubDetailState = ClubDetailState(club = club.toModel(""))
    AppTheme {
        ClubDetailScreen(clubDetailState)
    }
}