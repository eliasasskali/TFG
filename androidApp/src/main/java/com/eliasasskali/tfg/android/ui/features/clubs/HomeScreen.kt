package com.eliasasskali.tfg.android.ui.features.clubs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.paging.LoadState.Error
import androidx.paging.LoadState.Loading
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import com.eliasasskali.tfg.android.navigation.HomeRoutesClub
import com.eliasasskali.tfg.android.ui.components.CircularProgressBar
import com.eliasasskali.tfg.android.ui.components.ClubCard
import com.eliasasskali.tfg.android.ui.components.SearchView
import com.eliasasskali.tfg.model.DomainError
import com.google.gson.Gson

@Composable
fun Loading() {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Loading")
            CircularProgressIndicator()
        }
    }
}

@Composable
fun HomeScreen(
    viewModel: ClubsViewModel,
    onClubClicked: (Club) -> Unit
) {
    val clubs = viewModel.clubs.collectAsLazyPagingItems()
    var error: DomainError? = null
    val textState = remember { mutableStateOf(TextFieldValue("")) }

    clubs.apply {
        when {
            loadState.refresh is Loading -> Loading()
            loadState.refresh is Error -> viewModel.setError(viewModel.errorHandler.convert(DomainError.LoadClubsError))
            loadState.append is Loading -> Loading()
            loadState.append is Error -> viewModel.setError(viewModel.errorHandler.convert(DomainError.LoadClubsError))
        }
    }

    Column {
        SearchView(state = textState, viewModel)
        LazyColumn(
            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp),
            modifier = Modifier
                .background(color = MaterialTheme.colors.background)
                .fillMaxSize()
        ) {
            items(
                items = clubs
            ) { club ->
                club?.let { it ->
                    ClubCard(
                        club = it,
                        onClubClicked = {
                            val jsonClub = Gson().toJson(club)
                            navController.navigate(HomeRoutesClub.ClubDetail.routeName.plus("/$jsonClub"))
                        }
                    )
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

    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressBar(
            isDisplayed = viewModel.state.value.isLoading
        )
    }
}