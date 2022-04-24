package com.eliasasskali.tfg.android.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.eliasasskali.tfg.android.R
import com.eliasasskali.tfg.android.ui.theme.AppTheme
import com.eliasasskali.tfg.model.Club
import com.eliasasskali.tfg.model.ClubDto
import com.eliasasskali.tfg.model.ClubLocation
import java.net.URLDecoder
import java.nio.charset.StandardCharsets

@Composable
fun CircularProgressBar(
    isDisplayed: Boolean
) {
    if (isDisplayed) {
        CircularProgressIndicator()
    }
}

@Composable
fun ClubCard(
    club: Club,
    onClubClicked: (Club) -> Unit,
    distanceToClub: Int
) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .clickable { onClubClicked(club) }
            .fillMaxWidth(),
        elevation = 2.dp,
        shape = RoundedCornerShape(corner = CornerSize(16.dp))
    ) {
        Row {
            if (club.images.isNotEmpty()) {
                ImageLoader(
                    imageUrl = URLDecoder.decode(club.images[0], StandardCharsets.UTF_8.toString()),
                    modifier = Modifier.align(CenterVertically)
                )
            }
            Column(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth()
                    .align(CenterVertically)
            ) {
                Row(Modifier.fillMaxWidth()) {
                    Icon(
                        modifier = Modifier.size(12.dp),
                        painter = painterResource(id = R.drawable.ic_location),
                        contentDescription = null
                    )
                    Spacer(Modifier.width(4.dp))
                    Text(
                        text = "$distanceToClub km away" , // TODO: Change to stringRes
                        style = MaterialTheme.typography.caption.copy(fontWeight = FontWeight.Light),
                        modifier = Modifier.align(CenterVertically)
                    )
                }
                Spacer(modifier = Modifier.size(4.dp))

                Text(
                    text = club.name,
                    style = MaterialTheme.typography.h1
                )
                Spacer(modifier = Modifier.size(4.dp))
                if (club.address.isNotEmpty()) {
                    Text(
                        text = club.address,
                        style = MaterialTheme.typography.h2
                    )
                }
                Spacer(modifier = Modifier.size(4.dp))
                if (club.services.isNotEmpty()) {
                    Text(
                        text = club.services.joinToString(", "),
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
fun ImageLoader(imageUrl: String, modifier: Modifier = Modifier) {
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
        ClubCard(club = club.toModel(""), {}, 100)
    }
}