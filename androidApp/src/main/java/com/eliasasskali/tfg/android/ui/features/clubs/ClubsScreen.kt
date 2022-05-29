package com.eliasasskali.tfg.android.ui.features.clubs

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState.*
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import com.eliasasskali.tfg.android.ui.components.ClubCard
import com.eliasasskali.tfg.android.ui.components.SearchView
import com.eliasasskali.tfg.android.ui.features.clubs.filterClubs.ClubsFilterView
import com.eliasasskali.tfg.android.ui.features.clubs.filterClubs.FilterByLocationScreen
import com.eliasasskali.tfg.android.ui.features.clubs.filterClubs.FilterBySportsView
import com.eliasasskali.tfg.model.Club
import com.eliasasskali.tfg.model.DomainError

@Composable
fun Loading() {
    Surface(
        Modifier.fillMaxSize()
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier.align(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator()
            }
        }
    }
}

@Composable
fun ClubsScreen(
    viewModel: ClubsViewModel,
    onClubClicked: (Club) -> Unit,
    paddingValues: PaddingValues
) {
    when (viewModel.state.value.step) {
        is ClubListSteps.Error -> {}
        is ClubListSteps.IsLoading -> Loading()
        is ClubListSteps.ShowClubs -> ClubsView(viewModel, onClubClicked, paddingValues)
        is ClubListSteps.ShowFilterByLocation -> FilterByLocationScreen(
            viewModel = viewModel,
            onSearchButtonClick = { viewModel.setStep(ClubListSteps.ShowClubs) },
            onBackClicked = { viewModel.setStep(ClubListSteps.ShowClubs) },
            paddingValues = paddingValues
        )
        is ClubListSteps.ShowFilterBySports -> FilterBySportsView(
            viewModel = viewModel,
            paddingValues = paddingValues
        )
    }
}

@Composable
fun ClubsView(
    viewModel: ClubsViewModel,
    onClubClicked: (Club) -> Unit,
    paddingValues: PaddingValues
) {
    val activity = LocalContext.current as Activity
    LaunchedEffect(Unit) {
        viewModel.setUserLocation(activity)
    }

    val clubs = viewModel.clubs.collectAsLazyPagingItems()
    var error: DomainError? = null
    val textState = remember { mutableStateOf(TextFieldValue(viewModel.state.value.searchString)) }

    clubs.apply {
        when {
            loadState.refresh is Loading -> viewModel.setIsLoading(true)
            loadState.refresh is NotLoading -> viewModel.setIsLoading(false)
            loadState.refresh is Error -> viewModel.setError(
                viewModel.errorHandler.convert(
                    DomainError.LoadClubsError
                )
            )
            loadState.append is Loading -> {}
            loadState.append is Error -> viewModel.setError(
                viewModel.errorHandler.convert(
                    DomainError.LoadClubsError
                )
            )
        }
    }
    Surface(
        Modifier
            .padding(paddingValues)
            .fillMaxSize()
    ) {
        Column() {
            SearchView(state = textState, viewModel)
            ClubsFilterView(viewModel)
            if (viewModel.state.value.isLoading) {
                Loading()
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp),
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    items(
                        items = clubs
                    ) { club ->
                        club?.let { it ->
                            val distanceToClub =
                                if (viewModel.state.value.filterLocation.latitude != 0.0) {
                                    viewModel.distanceToClub(
                                        club.location,
                                        viewModel.state.value.filterLocation
                                    )
                                } else {
                                    viewModel.distanceToClub(
                                        club.location,
                                        viewModel.state.value.userLocation
                                    )
                                }

                            if (viewModel.state.value.filterLocationRadius == 0 || viewModel.state.value.filterLocationRadius >= distanceToClub) {
                                ClubCard(
                                    club = it,
                                    onClubClicked = onClubClicked,
                                    distanceToClub = distanceToClub
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    error?.let {
        Text(
            text = it.toString(), // TODO
            modifier = Modifier.padding(16.dp)
        )
    }
}