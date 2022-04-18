package com.eliasasskali.tfg.android.ui.features.clubs.filterClubs

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.outlined.ArrowDropDown
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.layout
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role.Companion.Image
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import com.eliasasskali.tfg.android.R
import com.eliasasskali.tfg.android.ui.components.Mockup
import com.eliasasskali.tfg.android.ui.features.clubs.ClubListSteps
import com.eliasasskali.tfg.android.ui.features.clubs.ClubsViewModel

@Composable
fun ClubsFilterView(viewModel: ClubsViewModel) {
    Surface(
        Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 12.dp, vertical = 8.dp)
                .fillMaxWidth()
        ) {
            Text(
                modifier = Modifier.align(CenterVertically),
                text = stringResource(R.string.filter_by),
                style = MaterialTheme.typography.caption.copy(fontWeight = FontWeight.Bold)
            )
            Spacer(modifier = Modifier.width(8.dp))

            SportsFilterChip(viewModel)
            Spacer(modifier = Modifier.width(8.dp))

            LocationFilterChip(viewModel)
            Spacer(modifier = Modifier.width(8.dp))

            ClubsFilterChip(
                viewModel = viewModel,
                label = stringResource(R.string.sort),
                onFilterClick = {},
                drawableId = R.drawable.ic_sort
            )
        }
    }
}

@Composable
fun SportsFilterChip(
    viewModel: ClubsViewModel,
) {
    OutlinedButton(
        modifier = Modifier.height(36.dp),
        onClick = {
            viewModel.setStep(ClubListSteps.ShowFilterBySports)
        },
        border = BorderStroke(1.dp, color = MaterialTheme.colors.primary),
        shape = RoundedCornerShape(50)
    ) {
        Row {
            if (viewModel.state.value.sportFilters.isEmpty()) {
                Icon(
                    painter = painterResource(R.drawable.ic_run),
                    contentDescription = null
                )
            } else {
                CircleText(
                    text = viewModel.state.value.sportFilters.size.toString(),
                    modifier = Modifier.align(CenterVertically)
                )
            }
            Spacer(Modifier.width(8.dp))

            Text(
                modifier = Modifier
                    .align(CenterVertically),
                text = stringResource(R.string.sports),
                style = MaterialTheme.typography.caption,
            )
            Spacer(Modifier.width(8.dp))

            if (viewModel.state.value.sportFilters.isEmpty()) {
                Icon(
                    imageVector = Icons.Outlined.ArrowDropDown,
                    contentDescription = null
                )
            } else {
                IconButton(
                    modifier = Modifier
                        .align(CenterVertically)
                        .size(18.dp),
                    onClick = {
                        viewModel.setSportFilters(listOf())
                    }
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Close,
                        contentDescription = null,
                    )
                }
            }
        }
    }
}

@Composable
fun LocationFilterChip(
    viewModel: ClubsViewModel,
) {
    OutlinedButton(
        modifier = Modifier.height(36.dp),
        onClick = {
            viewModel.setStep(ClubListSteps.ShowFilterByLocation)
        },
        border = BorderStroke(1.dp, color = MaterialTheme.colors.primary),
        shape = RoundedCornerShape(50)
    ) {
        Row {
            Icon(
                painter = painterResource(R.drawable.ic_location),
                contentDescription = null
            )

            Spacer(Modifier.width(8.dp))

            Text(
                modifier = Modifier
                    .align(CenterVertically),
                text = stringResource(R.string.location),
                style = MaterialTheme.typography.caption,
            )
            Spacer(Modifier.width(8.dp))

            //if (viewModel.state.value.sportFilters.isEmpty()) {
                Icon(
                    imageVector = Icons.Outlined.ArrowDropDown,
                    contentDescription = null
                )
            /*} else {
                IconButton(
                    modifier = Modifier
                        .align(CenterVertically)
                        .size(18.dp),
                    onClick = {
                        viewModel.setSportFilters(listOf())
                    }
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Close,
                        contentDescription = null,
                    )
                }
            }*/
        }
    }
}

@Composable
fun ClubsFilterChip(
    viewModel: ClubsViewModel,
    label: String,
    onFilterClick: () -> Unit,
    drawableId: Int?
) {
    val enabled = remember { mutableStateOf(false) }

    OutlinedButton(
        modifier = Modifier.height(36.dp),
        onClick = onFilterClick,
        border = BorderStroke(1.dp, color = MaterialTheme.colors.primary),
        shape = RoundedCornerShape(50),
    ) {
        Row {
            drawableId?.let {
                Icon(
                    painter = painterResource(id = it),
                    contentDescription = null
                )
            }
            Text(
                modifier = Modifier
                    .padding(horizontal = 4.dp)
                    .align(CenterVertically),
                text = label,
                style = MaterialTheme.typography.caption,
            )
            if (!enabled.value) {
                Icon(imageVector = Icons.Outlined.ArrowDropDown, contentDescription = null)
            } else {
                Icon(imageVector = Icons.Outlined.Close, contentDescription = null)
            }
        }
    }
}

@Composable
fun CircleText(
    text: String,
    modifier: Modifier = Modifier
) {
    Box(
        contentAlignment = Center,
        modifier = modifier
            .background(MaterialTheme.colors.primary, shape = CircleShape)
            .layout { measurable, constraints ->
                // Measure the composable
                val placeable = measurable.measure(constraints)

                // Get the current max dimension to assign width=height
                val currentHeight = placeable.height
                var heightCircle = currentHeight
                if (placeable.width > heightCircle)
                    heightCircle = placeable.width

                // Assign the dimension and the center position
                layout(heightCircle, heightCircle) {
                    // Where the composable gets placed
                    placeable.placeRelative(0, (heightCircle - currentHeight) / 2)
                }
            }) {
        Text(
            text = text,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colors.onPrimary,
            modifier = Modifier
                .padding(2.dp)
                .defaultMinSize(18.dp)
        )
    }
}