package com.eliasasskali.tfg.android.ui.features.completeProfile

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.eliasasskali.tfg.android.ui.components.Mockup
import com.eliasasskali.tfg.android.ui.components.ServicesGrid
import com.eliasasskali.tfg.android.ui.theme.AppTheme
import org.koin.androidx.compose.getViewModel

@Composable
fun CompleteAthleteProfileScreen(
    onContinueButtonClick: () -> Unit = {},
    viewModel: CompleteProfileViewModel = getViewModel()
) {
    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(start = 24.dp, top = 24.dp, end = 24.dp, bottom = 24.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Welcome Athlete!")
        Text(text = "What is your name?")
        NameField(viewModel)
        Text(text = "Which are your interests?")
        ServicesGrid(Mockup().services, viewModel)
        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = onContinueButtonClick,
            enabled = viewModel.isValidName()
        ) {
            Text(text = "Continue")
        }
    }
}

@Composable
fun NameField(viewModel: CompleteProfileViewModel) {
    val userName = viewModel.state.value.name
    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        value = userName,
        label = { Text(text = "Name") },
        onValueChange = { viewModel.setName(it) }
    )
}

@Preview(showBackground = true)
@Composable
fun CompleteAthleteProfileScreenPreview() {
    AppTheme {
        CompleteAthleteProfileScreen()
    }
}