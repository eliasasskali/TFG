package com.eliasasskali.tfg.android.ui.components

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ScrollableChipsRow(elements: List<String>) {
    Row(modifier = Modifier.horizontalScroll(rememberScrollState()).padding(horizontal = 8.dp).fillMaxWidth()) {
        elements.forEach {
            Chip(it)
        }
    }
}

@Composable
fun Chip(label: String) {
    Box(modifier = Modifier.padding(4.dp)) {
        Surface(
            elevation = 1.dp,
            shape = MaterialTheme.shapes.small,
            color = MaterialTheme.colors.primary
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    label,
                    modifier = Modifier.padding(8.dp),
                    color = MaterialTheme.colors.onPrimary,
                    style = MaterialTheme.typography.button
                )
            }
        }
    }
}