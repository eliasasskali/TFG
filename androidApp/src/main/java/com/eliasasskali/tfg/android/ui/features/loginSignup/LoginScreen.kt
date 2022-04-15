package com.eliasasskali.tfg.android.ui.features.loginSignup

import android.content.Intent
import android.util.Log
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.eliasasskali.tfg.android.ui.components.ErrorField
import com.eliasasskali.tfg.android.ui.components.loginRegisterComponents.SignInWithEmailButton
import com.eliasasskali.tfg.android.ui.components.loginRegisterComponents.SignInWithGoogleButton
import com.eliasasskali.tfg.android.ui.components.loginRegisterComponents.SignUpButton
import com.eliasasskali.tfg.android.ui.theme.AppTheme
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import org.koin.androidx.compose.getViewModel

private const val TAG = "LoginScreen"

@Composable
fun LoginScreen(
    emailLoginClick: () -> Unit,
    signUpLoginClick: () -> Unit = {},
    onUserLogged: () -> Unit = {},
    viewModel: LoginSignupViewModel = getViewModel()
) {
    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(all = 24.dp)
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(18.dp),
        horizontalAlignment = CenterHorizontally,
    ) {
        val buttonWidth = 300.dp
        Spacer(modifier = Modifier.height(18.dp))
        if (viewModel.state.value.error.isNotBlank()) {
            ErrorField(viewModel)
        }
        Text(
            text = "Welcome back!",
            modifier = Modifier.align(CenterHorizontally)
        )
        SignInWithEmailButton(buttonWidth, emailLoginClick)
        Text(
            text = "Don't have an account? Sign up.",
            modifier = Modifier.align(CenterHorizontally)
        )
        SignUpButton(buttonWidth, signUpLoginClick)
        SignInWithGoogleButton(buttonWidth, viewModel, onUserLogged)
    }
}


@Composable
fun registerGoogleActivityResultLauncher(viewModel: LoginSignupViewModel, onUserLogged: () -> Unit): ManagedActivityResultLauncher<Intent, ActivityResult> {
    return rememberLauncherForActivityResult(contract = ActivityResultContracts.StartActivityForResult()) {
        val task = GoogleSignIn.getSignedInAccountFromIntent(it.data)
        try {
            val account = task.getResult(ApiException::class.java)!!
            viewModel.signInWithGoogleToken(account.idToken!!, onUserLogged)
        } catch (e: ApiException) {
            Log.w(TAG, "Google sign in failed", e)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    AppTheme {
        LoginScreen(emailLoginClick = {}, onUserLogged = {})
    }
}