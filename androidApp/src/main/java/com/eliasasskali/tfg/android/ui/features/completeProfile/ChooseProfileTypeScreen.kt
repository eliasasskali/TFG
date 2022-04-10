package com.eliasasskali.tfg.android.ui.features.completeProfile

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.eliasasskali.tfg.android.ui.theme.AppTheme
import com.eliasasskali.tfg.R

@Composable
fun ChooseProfileTypeScreen(
    athleteButtonClick: () -> Unit = {},
    clubButtonClick: () -> Unit = {},
) {
    Surface(
        Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(all = 24.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(id = R.string.complete_profile_welcome),
                style = MaterialTheme.typography.h1.copy(fontSize = 20.sp)
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = stringResource(id = R.string.complete_profile_athlete_or_club),
                style = MaterialTheme.typography.h1.copy(fontSize = 16.sp)
            )
            Button(onClick = athleteButtonClick) {
                ButtonRow(
                    imageVector = Icons.Outlined.Email ,// TODO: DirectionsRun,
                    stringResourceId = R.string.athlete
                )
            }
            Button(onClick = clubButtonClick) {
                ButtonRow(
                    imageVector = Icons.Outlined.Email ,// TODO: FitnessCenter,
                    stringResourceId = R.string.club
                )
            }
        }
    }
}

@Composable
fun ButtonRow(imageVector: ImageVector, stringResourceId: Int) {
    Row(
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 40.dp)
    ) {
        Icon(
            tint = MaterialTheme.colors.onPrimary,
            imageVector = imageVector,
            contentDescription = null
        )
        Text(
            text = stringResource(stringResourceId),
            textAlign = TextAlign.Start,
            style = MaterialTheme.typography.button,
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ChooseProfileTypeScreenPreview() {
    AppTheme {
        ChooseProfileTypeScreen()
    }
}