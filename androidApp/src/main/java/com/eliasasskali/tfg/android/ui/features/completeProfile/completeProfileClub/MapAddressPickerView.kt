package com.eliasasskali.tfg.android.ui.features.completeProfile.completeProfileClub

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.eliasasskali.tfg.android.R
import com.eliasasskali.tfg.android.ui.features.clubs.Loading
import com.eliasasskali.tfg.android.ui.features.completeProfile.CompleteProfileSteps
import com.eliasasskali.tfg.android.ui.features.completeProfile.CompleteProfileViewModel
import com.eliasasskali.tfg.android.ui.features.completeProfile.rememberMapViewWithLifecycle
import com.google.android.libraries.maps.CameraUpdateFactory
import com.google.android.libraries.maps.MapView
import com.google.android.libraries.maps.model.LatLng

@Composable
fun CompleteProfileMapScreen(
    onContinueButtonClick: () -> Unit = {},
    viewModel: CompleteProfileViewModel,
    onBackClicked: () -> Unit
) {
    when (viewModel.state.value.step) {
        is CompleteProfileSteps.Error -> TODO()
        is CompleteProfileSteps.IsLoading -> Loading()
        is CompleteProfileSteps.ShowCompleteProfile -> MapAddressPickerView(
            onContinueButtonClick = onContinueButtonClick,
            viewModel = viewModel,
            onBackClicked = onBackClicked
        )
    }
}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun MapAddressPickerView(
    onContinueButtonClick: () -> Unit = {},
    viewModel: CompleteProfileViewModel,
    onBackClicked: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.complete_profile_club_map_screen_title),
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
                val mapView = rememberMapViewWithLifecycle()
                val currentLocation = viewModel.location.collectAsState()
                var text by remember { viewModel.addressText }
                val context = LocalContext.current

                Column(Modifier.fillMaxWidth()) {
                    Box {
                        TextField(
                            value = text,
                            onValueChange = {
                                text = it
                                if (!viewModel.isMapEditable.value)
                                    viewModel.onTextChanged(context, text)
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(end = 80.dp),
                            enabled = !viewModel.isMapEditable.value,
                            colors = TextFieldDefaults.textFieldColors(backgroundColor = Color.Transparent)
                        )

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(10.dp)
                                .padding(bottom = 20.dp),
                            horizontalAlignment = Alignment.End
                        ) {
                            Button(
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
                    }

                    Box(modifier = Modifier.height(500.dp)) {

                        currentLocation.value.let {
                            if (viewModel.isMapEditable.value) {
                                text = viewModel.getAddressFromLocation(context)
                            }
                            MapViewContainer(viewModel.isMapEditable.value, mapView, viewModel)
                        }

                        MapPinOverlay()
                    }

                    Button(
                        modifier = Modifier
                            .padding(horizontal = 8.dp)
                            .fillMaxWidth(),
                        onClick = onContinueButtonClick,
                    ) {
                        Text(text = stringResource(id = R.string.complete_profile_club_map_screen_complete_profile))
                    }
                }
            }
        }
    )
}

@Composable
fun MapPinOverlay() {
    Column(modifier = Modifier.fillMaxWidth()) {
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentAlignment = Alignment.BottomCenter
        ) {
            Image(
                modifier = Modifier.size(50.dp),
                bitmap = ImageBitmap.imageResource(id = R.drawable.pin).asAndroidBitmap()
                    .asImageBitmap(),
                contentDescription = "Pin Image"
            )
        }
        Box(
            Modifier.weight(1f)
        ) {}
    }
}

@Composable
fun MapViewContainer(
    isEnabled: Boolean,
    mapView: MapView,
    viewModel: CompleteProfileViewModel
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