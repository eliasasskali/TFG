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
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.eliasasskali.tfg.R
import com.eliasasskali.tfg.android.ui.components.ErrorField
import com.eliasasskali.tfg.android.ui.components.loginRegisterComponents.*
import com.eliasasskali.tfg.android.ui.theme.AppTheme
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import org.koin.androidx.compose.getViewModel

private const val TAG = "LoginScreen"

@Composable
fun LoginScreen(
    signUpLoginClick: () -> Unit = {},
    onUserLogged: () -> Unit = {},
    viewModel: LoginSignupViewModel = getViewModel()
) {
    Surface(
        Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(all = 24.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(18.dp),
            horizontalAlignment = CenterHorizontally,
        ) {
            Spacer(modifier = Modifier.height(18.dp))
            Text(
                text = stringResource(R.string.login_welcome_message),
                modifier = Modifier.align(CenterHorizontally),
                style = MaterialTheme.typography.h1.copy(fontSize = 24.sp)
            )
            if (viewModel.state.value.error.isNotBlank()) {
                ErrorField(viewModel)
            }
            EmailField(viewModel)
            PasswordField(viewModel)
            ButtonEmailPasswordLogin(viewModel, onUserLogged)
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = stringResource(R.string.login_signup_message),
                modifier = Modifier.align(CenterHorizontally),
                style = MaterialTheme.typography.caption.copy(fontSize = 16.sp)
            )
            SignUpButton(signUpLoginClick)
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Divider(Modifier.weight(1f))
                Text(
                    text = stringResource(R.string.or),
                    modifier = Modifier.padding(horizontal = 8.dp),
                    style = MaterialTheme.typography.caption.copy(fontSize = 16.sp)
                )
                Divider(Modifier.weight(1f))
            }
            SignInWithGoogleButton(viewModel, onUserLogged)
        }
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
        LoginScreen(onUserLogged = {})
    }
}