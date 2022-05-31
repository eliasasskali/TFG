package com.eliasasskali.tfg.android.ui.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.eliasasskali.tfg.android.R

@Composable
fun ErrorDialog(
    errorMessage: String,
    onRetryClick: () -> Unit,
    onCancelClick: () -> Unit
) {
    val openDeleteDialog = remember { mutableStateOf(true) }

    Surface(
        Modifier.fillMaxSize()
    ) {
        AlertDialog(
            onDismissRequest = { openDeleteDialog.value = false },
            title = {
                Text(
                    text = stringResource(id = R.string.error)
                )
            },
            text = {
                Text(
                    text = errorMessage
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        onRetryClick()
                        openDeleteDialog.value = false
                    }
                ) {
                    Text(
                        stringResource(id = R.string.retry)
                    )
                }
            },
            dismissButton = {
                Button(
                    onClick = {
                        onCancelClick()
                        openDeleteDialog.value = false
                    }
                ) {
                    Text(
                        stringResource(id = R.string.cancel)
                    )
                }
            }

        )
    }
}