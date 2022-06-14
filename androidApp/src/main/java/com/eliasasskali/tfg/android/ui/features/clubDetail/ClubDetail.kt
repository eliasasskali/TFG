package com.eliasasskali.tfg.android.ui.features.clubDetail

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.eliasasskali.tfg.android.R
import com.eliasasskali.tfg.android.ui.components.*
import com.eliasasskali.tfg.android.ui.theme.AppTheme
import com.eliasasskali.tfg.model.Club
import org.koin.androidx.compose.getViewModel
import java.util.*

@Composable
fun ClubDetailScreen(
    detailState: ClubDetailState = ClubDetailState(),
    club: Club,
    distanceToClub: String = "",
    isClubOwner: Boolean,
    onBackClicked: () -> Unit = {},
    onEditButtonClick: () -> Unit = {},
    onFollowButtonClick: () -> Unit = {},
    onUnfollowButtonClick: () -> Unit = {},
    onChatButtonClick: () -> Unit = {},
    onLogOutButtonClick: () -> Unit = {},
    paddingValues: PaddingValues = PaddingValues(0.dp),
    clubDetailViewModel: ClubDetailViewModel?
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
                    if (isClubOwner) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_group),
                            contentDescription = null
                        )
                    } else {
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
        content = { paddingValuesScaffold ->
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValuesScaffold)
                    .padding(paddingValues)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    Column(
                        modifier = Modifier
                            .weight(1f)
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
                                onEditButtonClick = onEditButtonClick,
                                rating = club.ratings.average()
                            )
                        } else {
                            FollowChatButtons(
                                isFollowing = detailState.athleteFollowsClub,
                                onFollowButtonClick = onFollowButtonClick,
                                onUnfollowButtonClick = onUnfollowButtonClick,
                                onChatButtonClick = onChatButtonClick
                            )
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

                        if (club.schedule.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(12.dp))
                            if (clubDetailViewModel != null) {
                                ClubScheduleCard(clubDetailViewModel, club)
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))
                        ClubDetailContact(club)
                        Spacer(modifier = Modifier.height(12.dp))
                        if (club.location.latitude != 0.0 && club.location.longitude != 0.0) {
                            ClubDetailMap(club)
                        }
                    }

                    if (isClubOwner) {
                        Row(
                            Modifier
                                .padding(12.dp)
                                .fillMaxWidth()
                        ) {
                            Button(
                                onClick = {
                                    onLogOutButtonClick()
                                },
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(
                                    text = stringResource(id = com.eliasasskali.tfg.R.string.logout)
                                )
                            }

                            Spacer(Modifier.width(12.dp))

                            Button(
                                onClick = onEditButtonClick,
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(
                                    text = stringResource(id = com.eliasasskali.tfg.R.string.edit_profile)
                                )
                            }
                        }
                    }
                }
            }
        }
    )
}

@Composable
fun FollowChatButtons(
    isFollowing: Boolean,
    onFollowButtonClick: () -> Unit,
    onUnfollowButtonClick: () -> Unit,
    onChatButtonClick: () -> Unit
) {
    Row(
        Modifier
            .padding(vertical = 12.dp)
            .fillMaxWidth()
    ) {
        if (isFollowing) {
            OutlinedButton(
                modifier = Modifier
                    .padding(horizontal = 12.dp)
                    .weight(1f),
                onClick = { onUnfollowButtonClick() }
            ) {
                Text(
                    text = stringResource(id = R.string.unfollow),
                )
            }
        } else {
            OutlinedButton(
                modifier = Modifier
                    .padding(horizontal = 12.dp)
                    .weight(1f),
                onClick = { onFollowButtonClick() }
            ) {
                Text(
                    text = stringResource(id = R.string.follow),
                )
            }
        }

        OutlinedButton(
            modifier = Modifier
                .padding(end = 12.dp)
                .weight(1f),
            onClick = { onChatButtonClick() }
        ) {
            Text(
                text = stringResource(id = R.string.chat),
            )
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ClubScheduleCard(
    viewModel: ClubDetailViewModel,
    club: Club
) {
    var isExpanded by remember { mutableStateOf(false) }
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
        onClick = {
            isExpanded = !isExpanded
        },
        elevation = 2.dp,
        shape = RoundedCornerShape(corner = CornerSize(16.dp))
    ) {
        Column(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
        ) {
            if (isExpanded) {
                for ((weekDayInt, schedule) in club.schedule) {
                    val dayOfWeek = viewModel.convertIntToWeekdayString(
                        weekDayInt = weekDayInt.toInt(),
                        context = LocalContext.current
                    )
                    Row(Modifier.fillMaxWidth()) {
                        DayScheduleCard(dayOfWeek, schedule, true)
                    }
                }
            } else {
                val today = Calendar.getInstance().get(Calendar.DAY_OF_WEEK)
                val schedule = club.schedule.getOrDefault(today.toString(), stringResource(id = R.string.unknown))
                val dayOfWeek = viewModel.convertIntToWeekdayString(
                    weekDayInt = today,
                    context = LocalContext.current
                )
                DayScheduleCard(
                    "${stringResource(id = R.string.today)} ($dayOfWeek)",
                    schedule
                )
            }
        }
    }
}

@Composable
fun DayScheduleCard(
    dayOfWeek: String,
    schedule: String,
    addIcon: Boolean = false
) {
    Row(
        Modifier
            .padding(8.dp)
            .fillMaxWidth()
    ) {
        if (addIcon) {
            Icon(
                painter = painterResource(id = R.drawable.ic_time),
                contentDescription = null
            )
            Spacer(modifier = Modifier.width(8.dp))
        }
        
        Text(
            text = "$dayOfWeek:",
            style = MaterialTheme.typography.body1.copy(fontWeight = FontWeight.Medium),
        )
        Spacer(Modifier.weight(1f))

        Text(
            text = schedule,
            style = MaterialTheme.typography.body1,
        )
    }
}


@Preview(showSystemUi = true, showBackground = true)
@Composable
fun ClubScheduleCardPreview() {
    val schedule = mapOf(
        "0" to "8:00-22:00",
        "1" to "8:00-22:00",
        "2" to "8:00-22:00",
        "3" to "8:00-22:00",
        "4" to "8:00-22:00",
        "5" to "9:00-20:00",
        "6" to "9:00-20:00",
    )
    val club = Club(schedule = schedule)
    val viewModel: ClubDetailViewModel = getViewModel()
    AppTheme() {
        ClubScheduleCard(club = club, viewModel = viewModel)
    }
}

/*@Preview(showSystemUi = true, showBackground = true)
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
}*/