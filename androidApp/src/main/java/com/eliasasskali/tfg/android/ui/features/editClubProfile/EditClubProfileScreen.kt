package com.eliasasskali.tfg.android.ui.features.editClubProfile

import android.annotation.SuppressLint
import android.net.Uri
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.eliasasskali.tfg.android.R
import com.eliasasskali.tfg.android.ui.components.Mockup
import com.eliasasskali.tfg.android.ui.components.StaggeredGrid
import com.eliasasskali.tfg.android.ui.features.clubs.Loading
import com.eliasasskali.tfg.android.ui.features.completeProfile.completeProfileClub.MapPinOverlay
import com.eliasasskali.tfg.android.ui.features.completeProfile.rememberMapViewWithLifecycle
import com.google.android.libraries.maps.CameraUpdateFactory
import com.google.android.libraries.maps.MapView
import com.google.android.libraries.maps.model.LatLng

@Composable
fun EditClubProfileScreen(
    viewModel: EditClubProfileViewModel,
    onBackClicked: () -> Unit,
    onProfileUpdated: () -> Unit
) {
    when (viewModel.state.value.step) {
        is EditClubProfileSteps.Error -> {}
        is EditClubProfileSteps.IsLoading -> Loading()
        is EditClubProfileSteps.ShowEditClub -> EditClubProfileView(viewModel, onBackClicked, onProfileUpdated)
        is EditClubProfileSteps.ShowEditLocation -> EditClubLocationView(
            viewModel = viewModel,
            onBackClicked = { viewModel.setStep(EditClubProfileSteps.ShowEditClub) }
        )
    }
}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun EditClubProfileView(
    viewModel: EditClubProfileViewModel,
    onBackClicked: () -> Unit,
    onProfileUpdated: () -> Unit
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
                        items(8) { imageIndex ->
                            EditClubImagesItem(
                                imageIndex = imageIndex,
                                viewModel = viewModel
                            )
                        }

                        item(
                            span = { GridItemSpan(4) }
                        ) {
                            ClubNameField(viewModel)
                        }

                        item(
                            span = { GridItemSpan(4) }
                        ) {
                            ContactEmailField(viewModel)
                        }

                        item(
                            span = { GridItemSpan(4) }
                        ) {
                            ContactPhoneField(viewModel)
                        }

                        item(
                            span = { GridItemSpan(4) }
                        ) {
                            ClubDescriptionField(viewModel)
                        }

                        item(
                            span = { GridItemSpan(4) }
                        ) {
                            ClubServicesGrid(
                                services = Mockup().services,
                                viewModel = viewModel
                            )
                        }

                        item(
                            span = { GridItemSpan(4) }
                        ) {
                            AddressField(viewModel)
                        }
                    }

                    ApplyCancelChangesButtons(viewModel, onBackClicked, onProfileUpdated)
                }
            }
        }
    )
}

@Composable
fun EditClubImagesItem(
    imageIndex: Int,
    viewModel: EditClubProfileViewModel
) {
    var expanded by remember { mutableStateOf(false) }
    val dropDownMenuItems = listOf(
        stringResource(R.string.delete_image),
        stringResource(R.string.replace_image),
    )
    val clubImages = viewModel.state.value.bitmapImages

    var replaceIndex by remember { mutableStateOf(7) }

    // Image selector
    val context = LocalContext.current

    // Single Image
    val launcher = rememberLauncherForActivityResult(
        contract =
        ActivityResultContracts.GetContent()
    ) { uri: Uri ->
        val image = MediaStore
            .Images
            .Media
            .getBitmap(context.contentResolver, uri)
        viewModel.replaceImageAt(replaceIndex, image)
    }

    // Multiple images
    val launcherMultipleImages = rememberLauncherForActivityResult(
        contract =
        ActivityResultContracts.GetMultipleContents()
    ) { uriList: List<Uri> ->
        if (uriList.size > 8 - clubImages.size) {
            // TODO: Show message
            println("Only processing first ${8 - clubImages.size} selected images, 8 images is the maximum")
        }

        val bitmapList = uriList.subList(0, minOf(8 - clubImages.size, uriList.size)).map { uri ->
            MediaStore
                .Images
                .Media
                .getBitmap(context.contentResolver, uri)
        }

        viewModel.appendImages(bitmapList)
    }

    if (imageIndex < clubImages.size) {
        Column {
            Image(
                bitmap = clubImages[imageIndex].asImageBitmap(),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .clickable(onClick = { expanded = true })
                    .padding(4.dp)
                    .clip(RoundedCornerShape(corner = CornerSize(16.dp)))
                    .fillMaxWidth()
                    .size(80.dp)
            )
            if (expanded) {
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                ) {
                    dropDownMenuItems.forEachIndexed { dropDownIndex, option ->
                        DropdownMenuItem(onClick = {
                            expanded = false
                            if (dropDownIndex == 0) {
                                viewModel.removeImage(imageIndex)
                            } else if (dropDownIndex == 1) {
                                replaceIndex = imageIndex
                                launcher.launch("image/*")
                            }
                        }
                        ) {
                            Text(text = option)
                        }
                    }
                }
            }
        }
    } else {
        Card(
            modifier = Modifier
                .clickable {
                    launcherMultipleImages.launch("image/*")
                }
                .padding(4.dp)
                .fillMaxWidth()
                .size(80.dp),
            elevation = 2.dp,
            shape = RoundedCornerShape(corner = CornerSize(16.dp))
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_add_photo_gallery),
                contentDescription = null,
                modifier = Modifier
                    .padding(12.dp),
            )
        }
    }
}

@Composable
fun ClubNameField(viewModel: EditClubProfileViewModel) {
    val clubName = viewModel.state.value.name
    OutlinedTextField(
        modifier = Modifier
            .padding(vertical = 12.dp)
            .fillMaxWidth(),
        value = clubName,
        label = { Text(text = stringResource(id = R.string.club_name)) },
        onValueChange = { viewModel.setName(it) }
    )
}

@Composable
fun ContactPhoneField(viewModel: EditClubProfileViewModel) {
    val contactPhone = viewModel.state.value.contactPhone
    OutlinedTextField(
        modifier = Modifier
            .padding(vertical = 12.dp)
            .fillMaxWidth(),
        value = contactPhone,
        label = { Text(text = stringResource(id = R.string.contact_phone)) },
        onValueChange = { viewModel.setContactPhone(it) }
    )
}

@Composable
fun ContactEmailField(viewModel: EditClubProfileViewModel) {
    val contactEmail = viewModel.state.value.contactEmail
    OutlinedTextField(
        modifier = Modifier
            .padding(vertical = 12.dp)
            .fillMaxWidth(),
        value = contactEmail,
        label = { Text(text = stringResource(id = R.string.contact_email)) },
        onValueChange = { viewModel.setContactEmail(it) }
    )
}

@Composable
fun ClubDescriptionField(viewModel: EditClubProfileViewModel) {
    val description = viewModel.state.value.description
    OutlinedTextField(
        modifier = Modifier
            .padding(vertical = 12.dp)
            .fillMaxWidth(),
        value = description,
        label = { Text(text = stringResource(id = R.string.club_description)) },
        onValueChange = { viewModel.setDescription(it) },
    )
}

@Composable
fun ClubServicesGrid(
    services: List<String>,
    viewModel: EditClubProfileViewModel
) {
    val selectedServices = rememberSaveable {
        mutableStateOf(viewModel.state.value.services)
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
                            viewModel.setServices(selectedServices.value)
                        } else {
                            selectedServices.value = selectedServices.value + it
                            viewModel.setServices(selectedServices.value)
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun AddressField(viewModel: EditClubProfileViewModel) {
    val address = viewModel.state.value.address
    Row(modifier = Modifier.padding(vertical = 12.dp)) {
        Icon(
            painter = painterResource(id = R.drawable.ic_location),
            contentDescription = null,
            modifier = Modifier
                .align(CenterVertically)
                .size(40.dp)
        )
        Spacer(Modifier.width(8.dp))

        Text(
            text = address,
            modifier = Modifier
                .weight(1f)
                .align(CenterVertically),
            style = MaterialTheme.typography.body1,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1
        )
        Spacer(Modifier.width(8.dp))

        IconButton(
            modifier = Modifier.align(CenterVertically),
            onClick = {
                viewModel.setStep(EditClubProfileSteps.ShowEditLocation)
            }
        ) {
            Icon(
                imageVector = Icons.Outlined.Edit,
                contentDescription = null,
                modifier = Modifier.size(40.dp)
            )
        }
    }
}

@Composable
fun ApplyCancelChangesButtons(
    viewModel: EditClubProfileViewModel,
    onCancelButtonClicked: () -> Unit,
    onProfileUpdated: () -> Unit
) {
    val context = LocalContext.current
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
                viewModel.updateClub(context, onCancelButtonClicked)
            }
        ) {
            Text(
                text = stringResource(id = R.string.apply_changes)
            )
        }
    }
}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun EditClubLocationView(
    viewModel: EditClubProfileViewModel,
    onBackClicked: () -> Unit
) {
    Scaffold(
        content = {
            Surface(
                Modifier.fillMaxSize()
            ) {
                val mapView = rememberMapViewWithLifecycle()
                val currentLocation = viewModel.location.collectAsState()
                var text by remember { viewModel.addressText }
                val context = LocalContext.current

                Column(
                    Modifier.fillMaxWidth()
                ) {
                    Row(
                        Modifier
                            .padding(12.dp)
                            .fillMaxWidth()
                    ) {
                        IconButton(
                            modifier = Modifier
                                .align(CenterVertically)
                                .size(24.dp),
                            onClick = onBackClicked
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.ArrowBack,
                                contentDescription = null
                            )
                        }
                        Spacer(Modifier.width(8.dp))

                        OutlinedTextField(
                            value = text,
                            onValueChange = {
                                text = it
                                if (!viewModel.isMapEditable.value)
                                    viewModel.onTextChanged(context, text)
                            },
                            enabled = !viewModel.isMapEditable.value,
                            modifier = Modifier.weight(1f),
                            trailingIcon = {
                                if (text.isNotEmpty() && !viewModel.isMapEditable.value) {
                                    IconButton(
                                        onClick = {
                                            text =
                                                "" // Remove text from TextField when you press the 'X' icon
                                        }
                                    ) {
                                        Icon(
                                            Icons.Default.Close,
                                            contentDescription = "",
                                            modifier = Modifier
                                                .padding(15.dp)
                                                .size(24.dp)
                                        )
                                    }
                                }
                            }
                        )
                        Spacer(Modifier.width(8.dp))

                        Button(
                            modifier = Modifier.align(CenterVertically),
                            onClick = {
                                viewModel.isMapEditable.value = !viewModel.isMapEditable.value
                            }
                        ) {
                            Text(
                                text = if (viewModel.isMapEditable.value) stringResource(id = R.string.edit)
                                else stringResource(id = R.string.save)
                            )
                        }
                    }

                    Box(Modifier.weight(1f)) {
                        currentLocation.value.let {
                            if (viewModel.isMapEditable.value) {
                                text = viewModel.getAddressFromLocation(context)
                            }
                            MapViewContainer(
                                viewModel.isMapEditable.value,
                                mapView,
                                viewModel
                            )
                        }

                        MapPinOverlay()
                    }

                    Row(
                        Modifier
                            .padding(12.dp)
                            .fillMaxWidth()
                    ) {
                        Button(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f),
                            onClick = {
                                viewModel.setStep(EditClubProfileSteps.ShowEditClub)
                            }
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
                                viewModel.setStep(EditClubProfileSteps.ShowEditClub)
                                viewModel.setAddress(viewModel.addressText.value)
                            }
                        ) {
                            Text(
                                text = stringResource(id = R.string.apply_changes)
                            )
                        }
                    }
                }
            }
        }
    )
}

@Composable
private fun MapViewContainer(
    isEnabled: Boolean,
    mapView: MapView,
    viewModel: EditClubProfileViewModel,
) {
    AndroidView(
        factory = { mapView }
    ) {
        mapView.getMapAsync { map ->

            map.uiSettings.setAllGesturesEnabled(isEnabled)

            val location = viewModel.location.value
            val position = LatLng(location.latitude, location.longitude)
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 15f))

            map.setOnCameraIdleListener {
                val cameraPosition = map.cameraPosition
                viewModel.updateLocation(
                    cameraPosition.target.latitude,
                    cameraPosition.target.longitude
                )
            }
        }
    }
}