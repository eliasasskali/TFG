package com.eliasasskali.tfg.android.ui.features.loginSignup

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.eliasasskali.tfg.android.ui.components.ErrorField
import com.eliasasskali.tfg.android.ui.components.loginRegisterComponents.*
import org.koin.androidx.compose.getViewModel

@Composable
fun SignUpScreen(viewModel: LoginSignupViewModel = getViewModel(), onUserLogged: () -> Unit) {
    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(start = 24.dp, top = 24.dp, end = 24.dp, bottom = 24.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(18.dp),
        horizontalAlignment = CenterHorizontally
    ) {
        val buttonWidth = 300.dp
        if (viewModel.state.value.error.isNotBlank()) {
            ErrorField(viewModel)
        }
        Text(
            text = "Sign Up",
            modifier = Modifier.align(CenterHorizontally)
        )
        EmailField(viewModel)
        PasswordField(viewModel)
        ButtonEmailPasswordCreate(viewModel, onUserLogged)
        Text(
            text = "Or continue with one of the following options...",
            modifier = Modifier.align(CenterHorizontally)
        )
        SignInWithGoogleButton(buttonWidth, viewModel, onUserLogged)
        //ContinueAsGuestButton(buttonWidth) {}
    }
}

