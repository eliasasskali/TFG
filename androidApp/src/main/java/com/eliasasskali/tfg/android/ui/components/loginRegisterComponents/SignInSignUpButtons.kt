package com.eliasasskali.tfg.android.ui.components.loginRegisterComponents

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Create
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.eliasasskali.tfg.android.R
import com.eliasasskali.tfg.android.ui.features.loginSignup.LoginSignUpEvent
import com.eliasasskali.tfg.android.ui.features.loginSignup.LoginSignupViewModel
import com.eliasasskali.tfg.android.ui.features.loginSignup.registerGoogleActivityResultLauncher
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions

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
fun ConfirmPasswordField(viewModel: LoginSignupViewModel) {
    val confirmPassword = viewModel.state.value.confirmPassword
    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        visualTransformation = PasswordVisualTransformation(),
        value = confirmPassword,
        label = { Text(text = stringResource(R.string.confirm_password)) },
        onValueChange = { viewModel.setConfirmPassword(it) }
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
        enabled = viewModel.state.value.isValidEmailAndPasswordSignUp,
        content = { Text(text = stringResource(R.string.signup)) },
        onClick = { viewModel.onTriggerEvent(LoginSignUpEvent.OnSignUpEmailButtonClicked(onUserLogged)) }
    )
}

@Composable
fun SignUpButton(signUpLoginClick: () -> Unit) {
    OutlinedButton(
        onClick = { signUpLoginClick() },
        modifier = Modifier.fillMaxWidth().height(50.dp),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = MaterialTheme.colors.primary,
            contentColor = MaterialTheme.colors.onPrimary
        )
    ) {
        Row(
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 40.dp)
        ) {
            Icon(
                tint = MaterialTheme.colors.onPrimary,
                imageVector = Icons.Outlined.Create,
                contentDescription = null
            )
            LoginButtonText(R.string.signup)
        }
    }
}

@Composable
fun SignInWithGoogleButton(viewModel: LoginSignupViewModel, onUserLogged: () -> Unit) {
    val context = LocalContext.current
    val token = stringResource(R.string.default_web_client_id)
    val launcher = registerGoogleActivityResultLauncher(viewModel, onUserLogged)
    OutlinedButton(
        modifier = Modifier.fillMaxWidth().height(50.dp),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = Color.White,
            contentColor = Color.Black
        ),
        onClick = {
            val signInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(token)
                .requestEmail()
                .build()

            val googleSignInClient = GoogleSignIn.getClient(context, signInOptions)
            launcher.launch(googleSignInClient.signInIntent)
        }
    ) {
        SignInButtonRow(iconId = R.drawable.fui_ic_googleg_color_24dp, buttonTextId = R.string.continue_with_google)
    }
}

@Composable
fun SignInButtonRow(@DrawableRes iconId: Int, @StringRes buttonTextId: Int) {
    Row(
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 40.dp)
    ) {
        LoginButtonIcon(iconId)
        LoginButtonText(buttonTextId)
    }
}
@Composable
fun LoginButtonIcon(@DrawableRes painterResourceId: Int) {
    Icon(
        tint = Color.Unspecified,
        painter = painterResource(painterResourceId),
        contentDescription = null
    )
}
@Composable
fun LoginButtonText(@StringRes stringResourceId: Int) {
    Text(
        text = stringResource(stringResourceId),
        textAlign = TextAlign.Start,
        style = MaterialTheme.typography.button,
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth()
    )
}