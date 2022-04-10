package com.eliasasskali.tfg.android.ui.features.completeProfile.completeProfileClub

import android.annotation.SuppressLint
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
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.eliasasskali.tfg.android.R
import com.eliasasskali.tfg.android.ui.components.ImagePager
import com.eliasasskali.tfg.android.ui.components.ImagePagerBitmap
import com.eliasasskali.tfg.android.ui.components.Mockup
import com.eliasasskali.tfg.android.ui.components.ServicesGrid
import com.eliasasskali.tfg.android.ui.features.completeProfile.CompleteProfileViewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import org.koin.androidx.compose.getViewModel

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@OptIn(ExperimentalPagerApi::class)
@Composable
fun CompleteClubProfileScreenSecond(
    onContinueButtonClick: () -> Unit = {},
    viewModel: CompleteProfileViewModel = getViewModel(),
    onBackClicked: () -> Unit
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

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.complete_profile_club_second_screen_title),
                        style = MaterialTheme.typography.h6
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = { onBackClicked() }
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.ArrowBack,
                            contentDescription = stringResource(id = R.string.back)
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
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = stringResource(id = R.string.complete_profile_club_second_screen_add_pictures),
                        style = MaterialTheme.typography.h1.copy(fontSize = 16.sp)
                    )
                    Button(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = { launcher.launch("image/*") }
                    ) {
                        Text(
                            text = stringResource(id = R.string.complete_profile_club_second_screen_select_pictures)
                        )
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

                    Text(
                        text = stringResource(id = R.string.complete_profile_club_second_screen_add_services),
                        style = MaterialTheme.typography.h1.copy(fontSize = 16.sp)
                    )
                    ServicesGrid(Mockup().services, viewModel)

                    Button(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = onContinueButtonClick
                    ) {
                        Text(
                            text = stringResource(id = R.string.next)
                        )
                    }
                }
            }
        }
    )
}