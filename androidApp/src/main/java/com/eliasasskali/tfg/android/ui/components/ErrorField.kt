package com.eliasasskali.tfg.android.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.eliasasskali.tfg.android.ui.features.loginSignup.LoginSignupViewModel

@Composable
fun ErrorField(viewModel: LoginSignupViewModel) {
    Text(
        text = viewModel.state.value.error,
        modifier = Modifier.fillMaxWidth(),
        color = MaterialTheme.colors.error,
        fontSize = 16.sp,
        fontWeight = FontWeight.Bold
    )
}