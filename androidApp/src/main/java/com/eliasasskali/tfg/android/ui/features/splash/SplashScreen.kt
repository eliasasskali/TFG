package com.eliasasskali.tfg.android.ui.features.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
            painter = painterResource(id = R.drawable.splash_image),
            contentDescription = "",
            modifier = Modifier
                .matchParentSize()
                .align(Alignment.BottomCenter),
            contentScale = ContentScale.Crop
        )

        Text(
            text = stringResource(R.string.app_name),
            style = MaterialTheme.typography.h5.copy(fontWeight = FontWeight.Bold, fontSize = 40.sp),
            color = Color.White,
            modifier = Modifier
                .padding(24.dp)
                .align(Alignment.BottomCenter)
        )
    }
}