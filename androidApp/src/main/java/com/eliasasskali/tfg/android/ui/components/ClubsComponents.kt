package com.eliasasskali.tfg.android.ui.components

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.eliasasskali.tfg.android.ui.theme.AppTheme
import com.eliasasskali.tfg.model.Club
import com.eliasasskali.tfg.model.ClubDto
import com.eliasasskali.tfg.model.ClubLocation

@Composable
fun CircularProgressBar(
    isDisplayed: Boolean
) {
    if (isDisplayed) {
        CircularProgressIndicator()
    }
}

@Composable
fun ClubCard(club: Club, onClubClicked: (clubId: String) -> Unit) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .clickable { onClubClicked("AqbBcJrJhPX4pg62aw8yhn1BzL82") } // TODO: id
            .fillMaxWidth(),
        elevation = 2.dp,
        shape = RoundedCornerShape(corner = CornerSize(16.dp))
    ) {
        Row {
            if (club.images.isNotEmpty()) {
                ImageLoader(imageUrl = club.clubImages[0], modifier = Modifier.align(Alignment.CenterVertically))
            }
            Column(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth()
                    .align(Alignment.CenterVertically)
            ) {
                Text(
                    text = club.name,
                    style = MaterialTheme.typography.h1
                )
                Spacer(modifier = Modifier.size(4.dp))
                club.address?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.h2
                    )
                }
                Spacer(modifier = Modifier.size(4.dp))
                club.services?.let {
                    Text(
                        text = it.joinToString(", "),
                        style = MaterialTheme.typography.caption,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}

@Composable
fun ImageLoader(imageUrl: Uri, modifier: Modifier = Modifier) {
    Image(
        painter = rememberAsyncImagePainter(imageUrl),
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = modifier
            .padding(8.dp)
            .size(100.dp)
            .clip(RoundedCornerShape(corner = CornerSize(16.dp)))
    )
}

@Preview(showBackground = true)
@Composable
fun clubCardPreview() {
    val club = ClubDto(
        name = "CN VIC-ETB",
        contactEmail = "vicetb@vicetb.cat",
        contactPhone = "938853121",
        description = "Club Natació Vic - Estadi Torres i Bages, tal i com el coneixem avui, té una llarga història. El seu origen es remunta a l’any 1955 en què va néixer la idea de construir una zona esportiva per als joves de la ciutat.\n" +
                "\n" +
                "Així doncs, el Bisbat de Vic amb el suport de vàries famílies i personalitats col·laboradores, entre les quals va destacar el Sr. Joan Riera Rius, van tirar endavant el projecte al terreny que més endavant s’anomenaria Estadi Torres i Bages. A aquestes instal·lacions, poc temps després, s’hi van afegir una pista d’atletisme i un camp de futbol.",
        address = "Carrer de Josep Maria Pallàs, 1, 08500 Vic, Barcelona",
        location = ClubLocation(41.92055251450252, 2.2564390268134584),
        services = listOf(
            "Swimming",
            "Running",
            "Gym",
            "Triathlon",
            "Body Pump",
            "Spinning",
            "Core",
            "Open Waters"
        )
    )
    AppTheme {
        ClubCard(club = club.toModel("", listOf(), listOf())) {}
    }
}