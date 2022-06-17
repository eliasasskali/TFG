package com.eliasasskali.tfg.android.ui.features.completeProfile.completeProfileClub

import android.annotation.SuppressLint
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.VisualTransformation
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
                    SchedulePicker(viewModel)
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

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SchedulePicker(viewModel: CompleteProfileViewModel) {
    val schedule = viewModel.state.value.schedule

    Card(
        modifier = Modifier
            .padding(vertical = 8.dp)
            .fillMaxWidth(),
        elevation = 2.dp,
        shape = RoundedCornerShape(corner = CornerSize(16.dp))
    ) {
        Column(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
        ) {

            for ((weekDayInt, scheduleString) in schedule) {
                val scheduleVal = remember { mutableStateOf(scheduleString) }
                val dayOfWeek = viewModel.convertIntToWeekdayString(
                    weekDayInt = weekDayInt.toInt(),
                    context = LocalContext.current
                )
                Row(Modifier.fillMaxWidth()) {
                    Row(
                        Modifier
                            .padding(8.dp)
                            .fillMaxWidth()
                    ) {
                        Text(
                            text = "$dayOfWeek:",
                            style = MaterialTheme.typography.body1.copy(fontWeight = FontWeight.Medium),
                            modifier = Modifier
                                .align(CenterVertically)
                        )

                        Spacer(Modifier.weight(1f))

                        OutlinedTextField(
                            value = scheduleVal.value,
                            onValueChange = { value ->
                                scheduleVal.value = value
                                viewModel.setSchedule(weekDayInt, value)
                            },
                            maxLines = 1,
                            modifier = Modifier
                                .width(120.dp)
                                .align(CenterVertically),
                        )
                    }
                }
            }
        }
    }
}

/*@Preview(showSystemUi = true, showBackground = true)
@Composable
fun SchedulePickerPreview() {
    val viewModel: CompleteProfileViewModel = getViewModel()
    AppTheme() {
        SchedulePicker(viewModel)
    }
}*/