package com.eliasasskali.tfg.android.ui.features.athleteProfile

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.eliasasskali.tfg.R
import com.eliasasskali.tfg.android.ui.components.ScrollableChipsRow
import com.eliasasskali.tfg.android.ui.features.chats.ChatCard
import com.eliasasskali.tfg.android.ui.features.clubs.Loading
import com.eliasasskali.tfg.android.ui.theme.AppTheme

@Composable
fun AthleteProfileScreen(
    viewModel: AthleteProfileViewModel,
    paddingValues: PaddingValues,
    onLoggedOut: () -> Unit,
    onFollowingClubClicked: (String) -> Unit,
    ) {
    when (viewModel.state.value.step) {
        is AthleteProfileSteps.Error -> {
            // TODO: Handle Error
        }
        is AthleteProfileSteps.IsLoading -> Loading()
        is AthleteProfileSteps.ShowAthleteProfile -> {
            AthleteProfileView(
                viewModel,
                paddingValues,
                onLoggedOut,
                onFollowingClubClicked,
            )
        }
    }
}

@Composable
fun AthleteProfileView(
    viewModel: AthleteProfileViewModel,
    paddingValues: PaddingValues,
    onLoggedOut: () -> Unit,
    onFollowingClubClicked: (String) -> Unit,
) {
    val athlete = viewModel.state.value.athlete
    val followingClubs = viewModel.state.value.followingClubs

    Surface(
        Modifier
            .padding(paddingValues)
            .fillMaxSize()
    ) {
        Column(
            Modifier
                .padding(12.dp)
                .fillMaxSize()
        ) {
            Column(
                Modifier
                    .weight(1f)
            ) {
                Text(
                    text = athlete.name,
                    style = MaterialTheme.typography.h5.copy(fontWeight = FontWeight.Medium),
                    modifier = Modifier
                        .padding(vertical = 8.dp)
                        .fillMaxWidth(),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(Modifier
                    .fillMaxWidth()
                ) {
                    Text(
                        text = stringResource(id = R.string.interests) + ":",
                        fontWeight = FontWeight.Medium,
                        style = MaterialTheme.typography.body1,
                        modifier = Modifier
                            .align(CenterVertically)
                    )

                    Spacer(Modifier.height(12.dp))

                    athlete.interests?.let { interests ->
                        if (interests.isNotEmpty()) {
                            ScrollableChipsRow(elements = interests)
                        }
                    }
                }

                Text(
                    text = stringResource(id = R.string.following_clubs) + ":",
                    fontWeight = FontWeight.Medium,
                    style = MaterialTheme.typography.body1.copy(
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    ),
                )

                Spacer(modifier = Modifier.height(12.dp))

                if (followingClubs.isNotEmpty()) {
                    LazyColumn(
                        Modifier
                            .fillMaxSize()
                    ) {
                        items(followingClubs.size) { index ->
                            FollowingClubCard(
                                clubName = followingClubs[index].second,
                                clubId = followingClubs[index].first,
                                onFollowingClubClicked = onFollowingClubClicked,
                                onUnfollowClubClicked = { clubId ->
                                    viewModel.unFollowClub(clubId)
                                },
                            )
                        }
                    }
                }
            }

            Row(
                Modifier
                    .fillMaxWidth()
            ) {
                Button(
                    onClick = {
                        viewModel.logOut(onLoggedOut)
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = stringResource(id = R.string.logout)
                    )
                }

                Spacer(Modifier.width(12.dp))

                Button(
                    onClick = {
                        // TODO: Edit profile button clicked
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = stringResource(id = R.string.edit_profile)
                    )
                }
            }
        }
    }
}

@Composable
fun FollowingClubCard(
    clubName: String,
    clubId: String,
    onFollowingClubClicked: (String) -> Unit = {},
    onUnfollowClubClicked: (String) -> Unit = {}
) {
    Card(
        modifier = Modifier
            .padding(vertical = 8.dp)
            .clickable { onFollowingClubClicked(clubId) }
            .fillMaxWidth(),
        elevation = 2.dp,
        shape = RoundedCornerShape(corner = CornerSize(16.dp))
    ) {
        Row(
            Modifier
                .padding(horizontal = 8.dp, vertical = 4.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = clubName,
                style = MaterialTheme.typography.h1,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .align(CenterVertically)
                    .weight(1f)
            )

            Spacer(Modifier.width(12.dp))

            Button(
                onClick = {
                    onUnfollowClubClicked(clubId)
                },
                modifier = Modifier
                    .align(CenterVertically),
                shape = RoundedCornerShape(CornerSize(16.dp)),
            ) {
                Text(
                    text = stringResource(id = R.string.unfollow),
                )
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun FollowingClubCardPreview() {
    AppTheme {
        FollowingClubCard(
            clubName = "Test Club",
            clubId = ""
        )
    }
}
