package com.eliasasskali.tfg.android.ui.features.completeProfile.completeProfileClub

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.eliasasskali.tfg.android.ui.features.completeProfile.CompleteProfileViewModel
import org.koin.androidx.compose.getViewModel

@Composable
fun CompleteClubProfileScreenFirst(
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
        Text(text = "Let's get to know the club!")
        ClubNameField(viewModel)
        ContactEmailField(viewModel)
        ContactPhoneField(viewModel)
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
fun ClubNameField(viewModel: CompleteProfileViewModel) {
    val clubName = viewModel.state.value.name
    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        value = clubName,
        label = { Text(text = "Club Name") },
        onValueChange = { viewModel.setName(it) }
    )
}

@Composable
fun ContactPhoneField(viewModel: CompleteProfileViewModel) {
    val contactPhone = viewModel.state.value.contactPhone
    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        value = contactPhone,
        label = { Text(text = "Contact Phone") },
        onValueChange = { viewModel.setContactPhone(it) }
    )
}

@Composable
fun ContactEmailField(viewModel: CompleteProfileViewModel) {
    val contactEmail = viewModel.state.value.contactEmail
    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        value = contactEmail,
        label = { Text(text = "Contact Email") },
        onValueChange = { viewModel.setContactEmail(it) }
    )
}

