package com.eliasasskali.tfg.android.ui.features.loginSignup

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.eliasasskali.tfg.R
import com.eliasasskali.tfg.android.ui.components.ErrorField
import com.eliasasskali.tfg.android.ui.components.loginRegisterComponents.*
import org.koin.androidx.compose.getViewModel

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun SignUpScreen(viewModel: LoginSignupViewModel = getViewModel(), onUserLogged: () -> Unit, onBackClicked: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Sign up",
                        style = MaterialTheme.typography.h6
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = { onBackClicked() }
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.ArrowBack,
                            contentDescription = "back"
                        )
                    }
                },
                backgroundColor = MaterialTheme.colors.primary,
            )
        },
        content = {
            Surface(
                Modifier.fillMaxSize()
            ) {
                Column(
                    modifier = Modifier
                        .verticalScroll(rememberScrollState())
                        .padding(start = 24.dp, top = 24.dp, end = 24.dp, bottom = 24.dp)
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(18.dp),
                    horizontalAlignment = CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.height(18.dp))
                    Text(
                        text = stringResource(R.string.signup),
                        modifier = Modifier.align(CenterHorizontally),
                        style = MaterialTheme.typography.h1.copy(fontSize = 24.sp)
                    )
                    if (viewModel.state.value.error.isNotBlank()) {
                        ErrorField(viewModel)
                    }
                    EmailField(viewModel)
                    PasswordField(viewModel)
                    ConfirmPasswordField(viewModel)
                    ButtonEmailPasswordCreate(viewModel, onUserLogged)
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
    )
}

