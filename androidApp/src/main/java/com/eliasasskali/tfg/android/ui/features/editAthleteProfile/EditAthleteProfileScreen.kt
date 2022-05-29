package com.eliasasskali.tfg.android.ui.features.editAthleteProfile

import android.annotation.SuppressLint
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.eliasasskali.tfg.android.R
import com.eliasasskali.tfg.android.ui.components.Mockup
import com.eliasasskali.tfg.android.ui.components.StaggeredGrid
import com.eliasasskali.tfg.android.ui.features.clubs.Loading

@Composable
fun EditAthleteProfileScreen(
    viewModel: EditAthleteProfileViewModel,
    onBackClicked: () -> Unit,
) {
    when (viewModel.state.value.step) {
        is EditAthleteProfileSteps.Error -> {}
        is EditAthleteProfileSteps.IsLoading -> Loading()
        is EditAthleteProfileSteps.ShowEditAthleteProfile -> EditAthleteProfileView(
            viewModel,
            onBackClicked,
        )
    }
}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun EditAthleteProfileView(
    viewModel: EditAthleteProfileViewModel,
    onBackClicked: () -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.edit_profile),
                        style = MaterialTheme.typography.h6
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = { onBackClicked() }
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.ArrowBack,
                            contentDescription = "back"
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
                Column(Modifier.fillMaxSize()) {
                    LazyVerticalGrid(
                        modifier = Modifier
                            .padding(12.dp)
                            .weight(1f),
                        columns = GridCells.Fixed(4),
                    ) {
                        item(
                            span = { GridItemSpan(4) }
                        ) {
                            ClubNameField(viewModel)
                        }

                        item(
                            span = { GridItemSpan(4) }
                        ) {
                            ClubServicesGrid(
                                services = Mockup().services,
                                viewModel = viewModel
                            )
                        }
                    }

                    ApplyCancelChangesButtons(viewModel, onBackClicked)
                }
            }
        }
    )
}

@Composable
fun ClubNameField(viewModel: EditAthleteProfileViewModel) {
    val athleteName = viewModel.state.value.name
    OutlinedTextField(
        modifier = Modifier
            .padding(vertical = 12.dp)
            .fillMaxWidth(),
        value = athleteName,
        label = { Text(text = stringResource(id = R.string.name)) },
        onValueChange = { viewModel.setName(it) }
    )
}

@Composable
fun ClubServicesGrid(
    services: List<String>,
    viewModel: EditAthleteProfileViewModel
) {
    val selectedServices = rememberSaveable {
        mutableStateOf(viewModel.state.value.interests)
    }
    Row(modifier = Modifier.horizontalScroll(rememberScrollState())) {
        StaggeredGrid(modifier = Modifier) {
            for (service in services) {
                com.eliasasskali.tfg.android.ui.components.Chip(
                    modifier = Modifier.padding(8.dp),
                    text = service,
                    isSelected = selectedServices.value.contains(service),
                    onSelectionChanged = {
                        if (selectedServices.value.contains(it)) {
                            selectedServices.value = selectedServices.value - it
                            viewModel.setInterests(selectedServices.value)
                        } else {
                            selectedServices.value = selectedServices.value + it
                            viewModel.setInterests(selectedServices.value)
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun ApplyCancelChangesButtons(
    viewModel: EditAthleteProfileViewModel,
    onCancelButtonClicked: () -> Unit,
) {
    Row(
        Modifier
            .padding(12.dp)
            .fillMaxWidth()
    ) {
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            onClick = { onCancelButtonClicked() }
        ) {
            Text(
                text = stringResource(id = R.string.cancel)
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Button(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            onClick = {
                viewModel.updateAthleteProfile(onCancelButtonClicked)
            }
        ) {
            Text(
                text = stringResource(id = R.string.apply_changes)
            )
        }
    }
}