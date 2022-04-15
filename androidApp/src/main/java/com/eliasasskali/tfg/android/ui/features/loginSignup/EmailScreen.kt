package com.eliasasskali.tfg.android.ui.features.loginSignup

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.eliasasskali.tfg.android.ui.components.ErrorField
import com.eliasasskali.tfg.android.ui.components.loginRegisterComponents.ButtonEmailPasswordLogin
import com.eliasasskali.tfg.android.ui.components.loginRegisterComponents.EmailField
import com.eliasasskali.tfg.android.ui.components.loginRegisterComponents.PasswordField

@Composable
fun EmailLoginScreen(
    viewModel: LoginSignupViewModel,
    onUserLogged: () -> Unit
) {
    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(start = 24.dp, top = 24.dp, end = 24.dp, bottom = 24.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(18.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (viewModel.state.value.error.isNotBlank()) {
            ErrorField(viewModel)
        }
        EmailField(viewModel)
        PasswordField(viewModel)
        ButtonEmailPasswordLogin(viewModel, onUserLogged)
    }
}