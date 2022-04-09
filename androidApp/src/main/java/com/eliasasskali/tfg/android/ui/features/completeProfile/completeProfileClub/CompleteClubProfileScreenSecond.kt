package com.eliasasskali.tfg.android.ui.features.completeProfile.completeProfileClub

import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.eliasasskali.tfg.android.ui.components.ImagePagerBitmap
import com.eliasasskali.tfg.android.ui.components.Mockup
import com.eliasasskali.tfg.android.ui.components.ServicesGrid
import com.eliasasskali.tfg.android.ui.features.completeProfile.CompleteProfileViewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import org.koin.androidx.compose.getViewModel

@OptIn(ExperimentalPagerApi::class)
@Composable
fun CompleteClubProfileScreenSecond(
    onContinueButtonClick: () -> Unit = {},
    viewModel: CompleteProfileViewModel = getViewModel()
) {
    var imageUriList by remember {
        mutableStateOf<List<Uri>>(emptyList())
    }
    val context = LocalContext.current
    val bitmapList = remember {
        mutableStateOf<List<Bitmap?>>(emptyList())
    }

    val launcher = rememberLauncherForActivityResult(
        contract =
        ActivityResultContracts.GetMultipleContents()
    ) { uriList: List<Uri> ->
        imageUriList = uriList
        viewModel.setClubImages(uriList)
    }

    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(start = 24.dp, top = 24.dp, end = 24.dp, bottom = 24.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Add Pictures")
        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = { launcher.launch("image/*") }
        ) {
            Text("Select Pictures.")
        }

        imageUriList.let { uriList ->
            if (Build.VERSION.SDK_INT < 28) {
                bitmapList.value = uriList.map { uri ->
                    MediaStore.Images
                        .Media.getBitmap(context.contentResolver, uri)
                }
            } else {
                bitmapList.value = uriList.map { uri ->
                    val source = uri.let {
                        ImageDecoder
                            .createSource(context.contentResolver, it)
                    }
                    ImageDecoder.decodeBitmap(source)
                }
            }

            if (bitmapList.value.isNotEmpty()) {
                ImagePagerBitmap(bitmapList.value)
            }
        }

        Text(text = "Which services does the club offer?")
        ServicesGrid(Mockup().services, viewModel)

        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = onContinueButtonClick
        ) {
            Text(text = "Continue")
        }
    }
}