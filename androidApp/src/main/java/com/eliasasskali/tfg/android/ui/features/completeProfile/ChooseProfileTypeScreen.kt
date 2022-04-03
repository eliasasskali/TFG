package com.eliasasskali.tfg.android.ui.features.completeProfile

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.eliasasskali.tfg.android.ui.theme.AppTheme

@Composable
fun ChooseProfileTypeScreen(
    athleteButtonClick: () -> Unit = {},
    clubButtonClick: () -> Unit = {},
) {
    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(start = 24.dp, top = 24.dp, end = 24.dp, bottom = 24.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Welcome, let's complete your profile")
        Text(text = "Are you an athlete or a club?")
        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = athleteButtonClick
        ) {
            Text(text = "Athlete")
        }
        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = clubButtonClick
        ) {
            Text(text = "Club")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ChooseProfileTypeScreenPreview() {
    AppTheme {
        ChooseProfileTypeScreen()
    }
}