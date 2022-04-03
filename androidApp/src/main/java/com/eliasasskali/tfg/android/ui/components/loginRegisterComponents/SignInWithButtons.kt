package com.eliasasskali.tfg.android.ui.components.loginRegisterComponents

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.eliasasskali.tfg.android.R
import com.eliasasskali.tfg.android.ui.features.loginSignup.LoginSignupViewModel
import com.eliasasskali.tfg.android.ui.features.loginSignup.registerGoogleActivityResultLauncher
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions

@Composable
fun SignInWithEmailButton(buttonWidth: Dp, emailLoginClick: () -> Unit) {
    OutlinedButton(
        onClick = { emailLoginClick() },
        modifier = Modifier.width(buttonWidth),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = colorResource(R.color.fui_bgEmail),
            contentColor = colorResource(R.color.white)
        )
    ) {
        SignInButtonRow(iconId =  R.drawable.fui_ic_mail_white_24dp, buttonTextId = R.string.sign_in_with_email)
    }
}

@Composable
fun SignUpButton(buttonWidth: Dp, signUpLoginClick: () -> Unit) {
    OutlinedButton(
        onClick = { signUpLoginClick() },
        modifier = Modifier.width(buttonWidth),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = colorResource(R.color.fui_bgEmail),
            contentColor = colorResource(R.color.white)
        )
    ) {
        SignInButtonRow(iconId =  R.drawable.fui_ic_mail_white_24dp, buttonTextId = R.string.signup)
    }
}

@Composable
fun SignInWithGoogleButton(buttonWidth: Dp, viewModel: LoginSignupViewModel, onUserLogged: () -> Unit) {
    val context = LocalContext.current
    val token = stringResource(R.string.default_web_client_id)
    val launcher = registerGoogleActivityResultLauncher(viewModel, onUserLogged)
    OutlinedButton(
        modifier = Modifier.width(buttonWidth),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = colorResource(R.color.fui_bgGoogle),
            contentColor = MaterialTheme.colors.onSurface
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
        SignInButtonRow(iconId = R.drawable.fui_ic_googleg_color_24dp, buttonTextId = R.string.sign_in_with_google)
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
            .padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 8.dp)
            .fillMaxWidth()
    )
}