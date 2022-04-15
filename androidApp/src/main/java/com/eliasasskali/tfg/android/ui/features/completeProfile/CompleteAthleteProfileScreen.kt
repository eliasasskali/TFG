package com.eliasasskali.tfg.android.ui.features.completeProfile

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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.eliasasskali.tfg.android.R
import com.eliasasskali.tfg.android.ui.components.Mockup
import com.eliasasskali.tfg.android.ui.components.ServicesGrid
import com.eliasasskali.tfg.android.ui.theme.AppTheme
import org.koin.androidx.compose.getViewModel

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun CompleteAthleteProfileScreen(
    onContinueButtonClick: () -> Unit = {},
    viewModel: CompleteProfileViewModel = getViewModel(),
    onBackClicked: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.complete_profile_athlete_screen_title),
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
                        text = stringResource(id = R.string.complete_profile_athlete_screen_message),
                        style = MaterialTheme.typography.h1.copy(fontSize = 20.sp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = stringResource(id = R.string.complete_profile_athlete_add_name),
                        style = MaterialTheme.typography.h1.copy(fontSize = 16.sp)
                    )
                    NameField(viewModel)
                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = stringResource(id = R.string.complete_profile_athlete_add_interests),
                        style = MaterialTheme.typography.h1.copy(fontSize = 16.sp)
                    )
                    ServicesGrid(Mockup().services, viewModel)

                    Button(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = onContinueButtonClick,
                        enabled = viewModel.isValidName()
                    ) {
                        Text(
                            text = stringResource(id = R.string.complete_profile)
                        )
                    }
                }
            }
        }
    )
}

@Composable
fun NameField(viewModel: CompleteProfileViewModel) {
    val userName = viewModel.state.value.name
    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        value = userName,
        label = { Text(text = stringResource(id = R.string.name)) },
        onValueChange = { viewModel.setName(it) }
    )
}

@Preview(showBackground = true)
@Composable
fun CompleteAthleteProfileScreenPreview() {
    AppTheme {
        CompleteAthleteProfileScreen(onBackClicked = {})
    }
}