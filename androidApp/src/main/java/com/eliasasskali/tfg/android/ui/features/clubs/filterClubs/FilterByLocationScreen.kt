package com.eliasasskali.tfg.android.ui.features.clubs.filterClubs

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
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
import com.eliasasskali.tfg.android.ui.features.clubs.ClubsViewModel
import com.eliasasskali.tfg.android.ui.features.completeProfile.rememberMapViewWithLifecycle
import com.google.android.libraries.maps.CameraUpdateFactory
import com.google.android.libraries.maps.MapView
import com.google.android.libraries.maps.model.LatLng
import kotlin.math.absoluteValue

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun FilterByLocationScreen(
    viewModel: ClubsViewModel,
    onSearchButtonClick: () -> Unit = {},
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
                var sliderPosition by remember { mutableStateOf(viewModel.state.value.filterLocationRadius.toFloat()) }

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
                                viewModel,
                                sliderPosition.absoluteValue.toDouble()
                            )
                        }

                        MapPinOverlay()
                    }

                    Column(Modifier.padding(12.dp)) {
                        Slider(
                            value = sliderPosition,
                            onValueChange = {
                                sliderPosition = it
                            },
                            valueRange = 1.0f..101.0f,
                            colors = SliderDefaults.colors(
                                activeTickColor = Color.Transparent,
                                inactiveTickColor = Color.Transparent
                            )
                        )
                        Spacer(Modifier.height(8.dp))
                        Text(
                            text =
                                if (sliderPosition.absoluteValue.toInt() == 101) "Without limits."
                                else "Up to ${sliderPosition.absoluteValue.toInt()} km from me"
                        )
                        Spacer(Modifier.height(12.dp))
                        Button(
                            modifier = Modifier
                                .fillMaxWidth(),
                            onClick = {
                                viewModel.setFilterLocationRadius(sliderPosition.absoluteValue.toInt())
                                viewModel.setFilterLocation(currentLocation.value)
                                viewModel.setFilterLocationCity(text.split(",")[0])
                                onSearchButtonClick()
                            },
                        ) {
                            // TODO: Change for string resource
                            Text(
                                text = "Search up to ${sliderPosition.absoluteValue.toInt()} km from me",
                                style = MaterialTheme.typography.caption
                            )
                        }
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
private fun MapViewContainer(
    isEnabled: Boolean,
    mapView: MapView,
    viewModel: ClubsViewModel,
    radius: Double = 0.0
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