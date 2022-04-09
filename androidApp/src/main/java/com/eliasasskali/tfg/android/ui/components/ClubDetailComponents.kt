package com.eliasasskali.tfg.android.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.eliasasskali.tfg.android.ui.theme.AppTheme
import com.eliasasskali.tfg.model.Club
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*

@Composable
fun ClubDetailMap(club: Club) {
    val location = LatLng(club.location.latitude, club.location.longitude)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(location, 15f)
    }
    val uiSettings by remember {
        mutableStateOf(
            MapUiSettings(
                zoomControlsEnabled = true,
                scrollGesturesEnabled = true
            )
        )
    }

    GoogleMap(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp),
        cameraPositionState = cameraPositionState,
        uiSettings = uiSettings
    ) {
        Marker(
            state = MarkerState(position = location),
            title = club.name,
            snippet = club.address
        )
    }
}

@Composable
fun ClubDetailTitle(
    club: Club
) {
    Column(modifier = Modifier.padding(horizontal = 12.dp)) {
        Text(
            text = club.name,
            style = MaterialTheme.typography.h5,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun ClubDetailHeader(club: Club) {
    Row(
        modifier = Modifier
            .padding(horizontal = 12.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "950m from your location",
            style = MaterialTheme.typography.caption,
            modifier = Modifier.align(CenterVertically)
            // TODO: Compute distance
        )
        // TODO: Rating View
        Text(
            text = "⭐⭐⭐⭐⭐"
        )
    }
}

@Composable
fun ClubDetailContact(club: Club) {
    Column(
        Modifier
            .padding(horizontal = 12.dp)
            .fillMaxWidth()
    ) {
        club.contactEmail?.let {
            Divider()
            ClubDetailContactItem(Icons.Outlined.Email, it)
        }
        club.contactPhone?.let {
            Divider()
            ClubDetailContactItem(Icons.Outlined.Phone, it)
        }
        Divider()
        ClubDetailContactItem(Icons.Outlined.LocationOn, club.address)
    }
}

@Composable
fun ClubDetailContactItem(icon: ImageVector, value: String) {
    Row(modifier = Modifier.padding(vertical = 12.dp)) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(40.dp)
        )
        Spacer(Modifier.width(12.dp))
        Text(
            text = value,
            modifier = Modifier.align(CenterVertically),
            style = MaterialTheme.typography.body1,
            overflow = TextOverflow.Visible
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ClubDetailContactItemPreview() {
    AppTheme {
        ClubDetailContactItem(
            icon = Icons.Outlined.Email,
            value = "contactMail@mail.com"
        )
    }
}