package com.eliasasskali.tfg.android.ui.features.clubs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.eliasasskali.tfg.android.ui.components.CircularProgressBar
import com.eliasasskali.tfg.android.ui.components.ClubCard
import com.eliasasskali.tfg.model.Club
import com.eliasasskali.tfg.model.DomainError
import com.eliasasskali.tfg.model.Either

@Composable
fun HomeScreen(
    dataOrException: Either<DomainError, List<Club>>,
    viewModel: ClubsViewModel,
    onClubClicked: (clubId: String) -> Unit,
) {
    var clubs: List<Club>? = null
    var error: DomainError? = null
    dataOrException.fold(
        success = {
            clubs = it
        },
        error = {
            error = DomainError.ErrorNotHandled(it.toString())
        }
    )

    clubs?.let {
        LazyColumn(
            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp),
            modifier = Modifier
                .background(color = MaterialTheme.colors.background)
                .fillMaxSize()
        ) {
            items(
                items = it
            ) { club ->
                ClubCard(club, onClubClicked)
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