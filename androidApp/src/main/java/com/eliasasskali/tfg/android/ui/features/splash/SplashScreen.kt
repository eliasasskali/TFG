package com.eliasasskali.tfg.android.ui.features.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.eliasasskali.tfg.android.R

@Composable
fun SplashScreen(
    state: SplashState,
    viewModel: SplashViewModel,
    onSplashComplete: (SplashState) -> Unit,
) {
    SplashComposable()
    LaunchedEffect(Unit) {
        viewModel.checkUserLoginState(onSplashComplete)
    }
}

@Composable
fun SplashComposable() {
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = R.drawable.splash_image), // TODO: Change drawable
            contentDescription = "",
            modifier = Modifier
                .matchParentSize()
                .align(Alignment.BottomCenter),
            contentScale = ContentScale.Crop
        )
    }
}