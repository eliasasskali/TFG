package com.eliasasskali.tfg.android.ui.features.clubs.filterClubs

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.eliasasskali.tfg.android.R
import com.eliasasskali.tfg.android.ui.components.Mockup
import com.eliasasskali.tfg.android.ui.features.clubs.ClubListSteps
import com.eliasasskali.tfg.android.ui.features.clubs.ClubsViewModel
import java.util.*

@Composable
fun FilterBySportsView(
    viewModel: ClubsViewModel,
    paddingValues: PaddingValues
) {
    val textState = remember { mutableStateOf(TextFieldValue("")) }
    val selectedSports = rememberSaveable {
        mutableStateOf(viewModel.state.value.sportFilters.toSet())
    }
    var filteredSports: List<String>
    val sports = ArrayList(Mockup().services)

    Column(
        Modifier
            .padding(paddingValues)
            .fillMaxSize()
    ) {
        SearchView(state = textState)
        LazyVerticalGrid(
            modifier = Modifier
                .weight(1f),
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(12.dp),
        ) {
            val searchText = textState.value.text
            filteredSports = if (searchText.isEmpty()) {
                sports
            } else {
                val resultList = ArrayList<String>()
                for (sport in sports) {
                    if (sport.lowercase(Locale.getDefault())
                            .contains(searchText.lowercase(Locale.getDefault()))
                    ) {
                        resultList.add(sport)
                    }
                }
                resultList
            }
            items(filteredSports.size) { index ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    elevation = 10.dp,
                    shape = RoundedCornerShape(10.dp),
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        val checked = selectedSports.value.contains(filteredSports[index])
                        Checkbox(
                            modifier = Modifier.align(CenterVertically),
                            checked = checked,
                            onCheckedChange = {
                                selectedSports.value = if (checked) {
                                    selectedSports.value - filteredSports[index]
                                } else {
                                    selectedSports.value + filteredSports[index]
                                }
                            },
                            colors = CheckboxDefaults.colors(MaterialTheme.colors.primary)
                        )
                        Text(
                            modifier = Modifier.align(CenterVertically),
                            text = filteredSports[index],
                            style = MaterialTheme.typography.body1
                        )
                    }
                }
            }
        }

        ApplyCancelFilters(viewModel = viewModel, onApplyClicked =  {
            viewModel.setSportFilters(selectedSports.value.toList())
        })
    }
}

@Composable
fun SearchView(state: MutableState<TextFieldValue>) {
    TextField(
        value = state.value,
        onValueChange = { value ->
            state.value = value
        },
        modifier = Modifier
            .fillMaxWidth(),
        textStyle = TextStyle(color = Color.White, fontSize = 18.sp),
        leadingIcon = {
            Icon(
                Icons.Default.Search,
                contentDescription = "",
                modifier = Modifier
                    .padding(15.dp)
                    .size(24.dp)
            )
        },
        trailingIcon = {
            if (state.value != TextFieldValue("")) {
                IconButton(
                    onClick = {
                        state.value =
                            TextFieldValue("") // Remove text from TextField when you press the 'X' icon
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
        },
        singleLine = true,
        shape = RectangleShape, // The TextFiled has rounded corners top left and right by default
        colors = TextFieldDefaults.textFieldColors(
            textColor = MaterialTheme.colors.onPrimary,
            cursorColor = MaterialTheme.colors.onPrimary,
            leadingIconColor = MaterialTheme.colors.onPrimary,
            trailingIconColor = MaterialTheme.colors.onPrimary,
            backgroundColor = MaterialTheme.colors.primary,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent
        )
    )
}

@Composable
fun ApplyCancelFilters(
    viewModel: ClubsViewModel,
    onApplyClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
        .fillMaxWidth()
    ) {
        Button(
            modifier = Modifier
                .padding(horizontal = 12.dp, vertical = 8.dp)
                .weight(1f),
            onClick = {
                viewModel.setStep(ClubListSteps.ShowClubs)
            }
        ) {
            Text(
                text = stringResource(R.string.cancel),
                style = MaterialTheme.typography.caption
            )
        }

        OutlinedButton(
            modifier = Modifier
                .padding(end = 12.dp, top = 8.dp, bottom = 8.dp)
                .weight(1f),
            onClick = {
                onApplyClicked()
                viewModel.setStep(ClubListSteps.ShowClubs)
            }
        ) {
            Text(
                text = stringResource(R.string.apply_filters),
                style = MaterialTheme.typography.caption
            )
        }
    }
}