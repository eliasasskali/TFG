package com.eliasasskali.tfg.android.ui.features.clubDetail

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.eliasasskali.tfg.android.ui.features.completeProfile.rememberMapViewWithLifecycle
import com.eliasasskali.tfg.model.Club
import com.google.android.libraries.maps.CameraUpdateFactory
import com.google.android.libraries.maps.MapView
import com.google.android.libraries.maps.model.LatLng
import com.google.android.libraries.maps.model.LatLngBounds
import com.google.android.libraries.maps.model.MarkerOptions
import com.google.maps.android.ktx.awaitMap
import kotlinx.coroutines.launch

@Composable
fun ClubDetailMapView(club: Club) {
    val mapView = rememberMapViewWithLifecycle()
    ClubDetailMapViewContainer(
        mapView = mapView,
        club = club,
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
    )
}

@Composable
fun ClubsMapViewContainer(
    mapView: MapView,
    clubs: List<Club>,
    modifier: Modifier = Modifier,
) {
    val builder = LatLngBounds.builder()

    if (clubs.isNotEmpty()) {
        for (club in clubs) {
            val location = LatLng( // TODO: Check !!
                club.location?.latitude!!,
                club.location?.longitude!!
            )
            val markerOptions = MarkerOptions()
                .title(club.name)
                .snippet(club.address)
                .position(location)

            // Include marker to get bounds
            builder.include(location)

            // Add marker to map
            LaunchedEffect(mapView) {
                val map = mapView.awaitMap()
                map.addMarker(markerOptions)
            }
        }
        val bounds = builder.build()

        val mapLocation = remember { mutableStateOf(bounds.center) }
        val mapZoom = remember { mutableStateOf(7F) }

        // Set zoom to show all markers
        val coroutineScope = rememberCoroutineScope()
        AndroidView(modifier = modifier, factory = { mapView }) {
            coroutineScope.launch {
                val map = it.awaitMap()
                map.uiSettings.isZoomControlsEnabled = true
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(mapLocation.value, mapZoom.value))

                map.setOnCameraIdleListener {
                    mapLocation.value = map.cameraPosition.target
                    mapZoom.value = map.cameraPosition.zoom
                }
            }
        }
    }
}

@Composable
fun ClubDetailMapViewContainer(
    mapView: MapView,
    club: Club,
    modifier: Modifier = Modifier,
) {
    val builder = LatLngBounds.builder()

    val location = club.location?.latitude?.let { lat ->
        club.location?.longitude?.let { lng ->
            LatLng(
                lat,
                lng
            )
        }
    } ?: LatLng(0.0, 0.0) // TODO: Revise LatLng

    val markerOptions = MarkerOptions()
        .title(club.name)
        .snippet(club.address)
        .position(location)

    // Include marker to get bounds
    builder.include(location)

    // Add marker to map
    LaunchedEffect(mapView) {
        val map = mapView.awaitMap()
        map.addMarker(markerOptions)
    }

    val bounds = builder.build()

    val mapLocation = remember { mutableStateOf(bounds.center) }
    val mapZoom = remember { mutableStateOf(10F) }

    // Set zoom to show all markers
    val coroutineScope = rememberCoroutineScope()
    AndroidView(modifier = modifier, factory = { mapView }) {
        coroutineScope.launch {
            val map = it.awaitMap()
            map.uiSettings.isZoomControlsEnabled = true
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(mapLocation.value, mapZoom.value))

            map.setOnCameraIdleListener {
                mapLocation.value = map.cameraPosition.target
                mapZoom.value = map.cameraPosition.zoom
            }
        }
    }

}