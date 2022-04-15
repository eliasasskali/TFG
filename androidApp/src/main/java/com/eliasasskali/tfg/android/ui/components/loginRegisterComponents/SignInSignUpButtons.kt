package com.eliasasskali.tfg.android.ui.components.loginRegisterComponents

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.eliasasskali.tfg.android.R
import com.eliasasskali.tfg.android.ui.features.loginSignup.LoginSignUpEvent
import com.eliasasskali.tfg.android.ui.features.loginSignup.LoginSignupViewModel

@Composable
fun EmailField(viewModel: LoginSignupViewModel) {
    val userEmail = viewModel.state.value.userEmail
    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        value = userEmail,
        label = { Text(text = stringResource(R.string.email)) },
        onValueChange = { viewModel.setUserEmail(it) }
    )
}
@Composable
fun PasswordField(viewModel: LoginSignupViewModel) {
    val password = viewModel.state.value.password
    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        visualTransformation = PasswordVisualTransformation(),
        value = password,
        label = { Text(text = stringResource(R.string.password)) },
        onValueChange = { viewModel.setPassword(it) }
    )
}
@Composable
fun ButtonEmailPasswordLogin(viewModel: LoginSignupViewModel, onUserLogged: () -> Unit) {
    Button(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp),
        enabled = viewModel.state.value.isValidEmailAndPassword,
        content = { Text(text = stringResource(R.string.login)) },
        onClick = { viewModel.onTriggerEvent(LoginSignUpEvent.OnEmailLoginButtonClicked(onUserLogged)) }
    )
}

@Composable
fun ButtonEmailPasswordCreate(viewModel: LoginSignupViewModel, onUserLogged: () -> Unit) {
    Button(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp),
        enabled = viewModel.state.value.isValidEmailAndPassword,
        content = { Text(text = stringResource(R.string.create)) },
        onClick = { viewModel.onTriggerEvent(LoginSignUpEvent.OnSignUpEmailButtonClicked(onUserLogged)) }
    )
}