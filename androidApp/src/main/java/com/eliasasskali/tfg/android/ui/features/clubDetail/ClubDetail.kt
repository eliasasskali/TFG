package com.eliasasskali.tfg.android.ui.features.clubDetail

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.eliasasskali.tfg.android.R
import com.eliasasskali.tfg.android.ui.components.*
import com.eliasasskali.tfg.android.ui.theme.AppTheme
import com.eliasasskali.tfg.model.Club
import com.eliasasskali.tfg.model.ClubDto
import com.eliasasskali.tfg.model.ClubLocation

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ClubDetailScreen(
    club: Club,
    distanceToClub: String = "",
    isClubOwner: Boolean,
    onBackClicked: () -> Unit = {},
    onEditButtonClick: () -> Unit = {},
    paddingValues: PaddingValues = PaddingValues(0.dp)
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    if (!isClubOwner) {
                        Text(
                            text = stringResource(id = R.string.club_detail_screen_title),
                            style = MaterialTheme.typography.h6
                        )
                    } else {
                        Text(
                            text = stringResource(id = R.string.my_club),
                            style = MaterialTheme.typography.h6
                        )
                    }
                },
                navigationIcon = {
                    if (!isClubOwner) {
                        IconButton(
                            onClick = { onBackClicked() }
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.ArrowBack,
                                contentDescription = stringResource(id = R.string.back)
                            )
                        }
                    }
                },
                actions = {
                    if (isClubOwner) {
                        IconButton(onClick = { /*TODO*/ }) {
                            Icon(
                                imageVector = Icons.Outlined.Settings,
                                contentDescription = stringResource(id = R.string.settings)
                            )
                        }
                    }
                },
                backgroundColor = MaterialTheme.colors.primary,
            )
        },
        content = {
            Surface(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                ) {
                    if (club.images.isNotEmpty()) {
                        ImagePager(imageList = club.images)
                    }
                    Spacer(modifier = Modifier.height(12.dp))

                    if (isClubOwner) {
                        ClubDetailEditButton(
                            modifier = Modifier.align(Alignment.End),
                            onEditButtonClick = onEditButtonClick
                        )
                    } else {
                        ClubDetailHeader(club, distanceToClub)
                    }
                    Spacer(modifier = Modifier.height(12.dp))

                    ClubDetailTitle(club)
                    Spacer(modifier = Modifier.height(12.dp))

                    if (club.services.isNotEmpty()) {
                        ScrollableChipsRow(elements = club.services)
                    }
                    Spacer(modifier = Modifier.height(12.dp))

                    club.description?.let {
                        Text(
                            modifier = Modifier.padding(horizontal = 12.dp),
                            text = it,
                            style = MaterialTheme.typography.body1
                        )
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    ClubDetailContact(club)
                    Spacer(modifier = Modifier.height(12.dp))
                    if (club.location.latitude != 0.0 && club.location.longitude != 0.0) {
                        ClubDetailMap(club)
                    }
                }
            }
        }
    )
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
    val clubDetailState = ClubDetailState(club = club.toModel(""))
    AppTheme {
        //ClubDetailScreen(clubDetailState, , {})
    }
}