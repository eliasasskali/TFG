package com.eliasasskali.tfg.android.ui.features.completeProfile.completeProfileClub

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.eliasasskali.tfg.android.R
import com.eliasasskali.tfg.android.ui.features.completeProfile.CompleteProfileViewModel
import org.koin.androidx.compose.getViewModel

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun CompleteClubProfileScreenFirst(
    onContinueButtonClick: () -> Unit = {},
    viewModel: CompleteProfileViewModel = getViewModel(),
    onBackClicked: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.complete_profile_club_first_screen_title),
                        style = MaterialTheme.typography.h6
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = { onBackClicked() }
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.ArrowBack,
                            contentDescription = stringResource(id = R.string.back)
                        )
                    }
                },
                backgroundColor = MaterialTheme.colors.primary,
            )
        },
        content = {
            Surface(
                Modifier.fillMaxSize()
            ) {
                Column(
                    modifier = Modifier
                        .verticalScroll(rememberScrollState())
                        .padding(start = 24.dp, top = 24.dp, end = 24.dp, bottom = 24.dp)
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = stringResource(R.string.complete_profile_club_first_screen_message),
                        style = MaterialTheme.typography.h1.copy(fontSize = 16.sp)
                    )
                    ClubNameField(viewModel)
                    ContactEmailField(viewModel)
                    ContactPhoneField(viewModel)
                    ClubDescriptionField(viewModel)
                    Button(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = onContinueButtonClick,
                        enabled = viewModel.isValidName()
                    ) {
                        Text(text = stringResource(R.string.next))
                    }
                }
            }
        }
    )
}

@Composable
fun ClubNameField(viewModel: CompleteProfileViewModel) {
    val clubName = viewModel.state.value.name
    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        value = clubName,
        label = { Text(text = stringResource(id = R.string.club_name)) },
        onValueChange = { viewModel.setName(it) }
    )
}

@Composable
fun ContactPhoneField(viewModel: CompleteProfileViewModel) {
    val contactPhone = viewModel.state.value.contactPhone
    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        value = contactPhone,
        label = { Text(text = stringResource(id = R.string.contact_phone)) },
        onValueChange = { viewModel.setContactPhone(it) }
    )
}

@Composable
fun ContactEmailField(viewModel: CompleteProfileViewModel) {
    val contactEmail = viewModel.state.value.contactEmail
    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        value = contactEmail,
        label = { Text(text = stringResource(id = R.string.contact_email)) },
        onValueChange = { viewModel.setContactEmail(it) }
    )
}

@Composable
fun ClubDescriptionField(viewModel: CompleteProfileViewModel) {
    val description = viewModel.state.value.description
    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        value = description,
        label = { Text(text = stringResource(id = R.string.club_description)) },
        onValueChange = { viewModel.setDescription(it) },
    )
}
