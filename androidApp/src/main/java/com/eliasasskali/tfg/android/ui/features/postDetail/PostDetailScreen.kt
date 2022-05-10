package com.eliasasskali.tfg.android.ui.features.postDetail

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.eliasasskali.tfg.android.R
import com.eliasasskali.tfg.android.ui.features.clubs.Loading
import com.eliasasskali.tfg.android.ui.features.editClubProfile.EditClubProfileViewModel
import com.eliasasskali.tfg.android.ui.features.post.CreatePostButton
import com.eliasasskali.tfg.android.ui.features.post.PostContentField
import com.eliasasskali.tfg.android.ui.features.post.PostTitleField
import com.eliasasskali.tfg.android.ui.features.post.PostViewModel

@Composable
fun PostDetailScreen(
    viewModel: PostDetailViewModel,
    paddingValues: PaddingValues,
    onPostDeleted: () -> Unit
) {
    when (viewModel.state.value.step) {
        is PostDetailSteps.Error -> {}
        is PostDetailSteps.IsLoading -> Loading()
        is PostDetailSteps.ShowPostDetail -> PostDetailView(viewModel, paddingValues, onPostDeleted)
        is PostDetailSteps.ShowEditPost -> EditPostView(viewModel, paddingValues)
    }
}

@Composable
fun PostDetailView(
    viewModel: PostDetailViewModel,
    paddingValues: PaddingValues = PaddingValues(0.dp),
    onPostDeleted: () -> Unit
) {
    val post = viewModel.state.value.post
    val openDeleteDialog = remember { mutableStateOf(false) }
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
    ) {
        if (openDeleteDialog.value && viewModel.state.value.isPostOwner) {
            AlertDialog(
                onDismissRequest = { openDeleteDialog.value = false },
                title = {
                    Text(
                        text = stringResource(id = R.string.confirm_delete_post)
                    )
                },
                text = {
                    Text(
                        text = "${post.title} ${stringResource(id = R.string.will_be_deleted)}"
                    )
                },
                confirmButton = {
                    Button(
                        onClick = {
                            viewModel.deletePost {
                                onPostDeleted()
                            }
                            openDeleteDialog.value = false
                        }) {
                        Text(
                            stringResource(id = R.string.delete_post)
                        )
                    }
                },
                dismissButton = {
                    Button(
                        onClick = {
                            openDeleteDialog.value = false
                        }) {
                        Text(
                            stringResource(id = R.string.cancel)
                        )
                    }
                }

            )
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            PostDetailHeader(post.clubName, post.dateString)
            Spacer(modifier = Modifier.height(12.dp))

            PostDetailTitle(post.title)
            Spacer(modifier = Modifier.height(12.dp))

            if (viewModel.state.value.isPostOwner) {
                EditDeleteButtons(
                    onEditButtonClick = {
                        viewModel.setStep(PostDetailSteps.ShowEditPost)
                    },
                    onDeleteButtonClick = {
                        openDeleteDialog.value = true
                    },
                )
                Spacer(modifier = Modifier.height(12.dp))
            }

            Text(
                modifier = Modifier.padding(horizontal = 12.dp),
                text = post.content,
                style = MaterialTheme.typography.body1
            )
        }
    }
}

@Composable
fun PostDetailTitle(
    postTitle: String
) {
    Column(
        modifier = Modifier
            .padding(horizontal = 12.dp)
    ) {
        Text(
            text = postTitle,
            style = MaterialTheme.typography.h5,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun EditPostView(
    viewModel: PostDetailViewModel,
    paddingValues: PaddingValues
) {
    Surface(
        Modifier
            .padding(paddingValues)
            .fillMaxSize()
    ) {
        Column(
            Modifier
                .padding(12.dp)
                .fillMaxSize()
        ) {
            PostTitleField(
                viewModel = viewModel,
                modifier = Modifier
                    .padding(bottom = 4.dp)
            )
            PostContentField(viewModel, Modifier.weight(1f))
            ApplyCancelChangesButtons(
                viewModel = viewModel,
                onCancelButtonClicked = { viewModel.setStep(PostDetailSteps.ShowPostDetail) }
            )
        }
    }
}


@Composable
fun PostDetailHeader(clubName: String, date: String) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(start = 12.dp, end = 12.dp, top = 12.dp)
    ) {
        Text(
            text = clubName,
            style = MaterialTheme.typography.body1,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.weight(1f))

        Text(
            text = date,
            style = MaterialTheme.typography.caption,
        )
    }
}

@Composable
fun EditDeleteButtons(
    onEditButtonClick: () -> Unit,
    onDeleteButtonClick: () -> Unit,
) {
    Row(
        Modifier
            .padding(12.dp)
            .fillMaxWidth()
    ) {
        OutlinedButton(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            onClick = {
                onEditButtonClick()
            }
        ) {
            Text(
                text = stringResource(id = R.string.edit_post)
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Button(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            onClick = {
                onDeleteButtonClick()
            }
        ) {
            Text(
                text = stringResource(id = R.string.delete_post)
            )
        }
    }
}

@Composable
fun PostTitleField(
    viewModel: PostDetailViewModel,
    modifier: Modifier = Modifier
) {
    val newPostTitle = viewModel.state.value.newPostTitle
    TextField(
        modifier = modifier.fillMaxWidth(),
        value = newPostTitle,
        label = { Text(text = stringResource(id = R.string.post_title)) },
        onValueChange = { viewModel.setNewPostTitle(it) }
    )
}

@Composable
fun PostContentField(
    viewModel: PostDetailViewModel,
    modifier: Modifier = Modifier
) {
    val newPostContent = viewModel.state.value.newPostContent
    TextField(
        modifier = modifier.fillMaxWidth(),
        value = newPostContent,
        label = { Text(text = stringResource(id = R.string.post_content)) },
        onValueChange = { viewModel.setNewPostContent(it) }
    )
}

@Composable
fun ApplyCancelChangesButtons(
    viewModel: PostDetailViewModel,
    onCancelButtonClicked: () -> Unit,
) {
    Row(
        Modifier
            .padding(12.dp)
            .fillMaxWidth()
    ) {
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            onClick = { onCancelButtonClicked() }
        ) {
            Text(
                text = stringResource(id = R.string.cancel)
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Button(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            onClick = {
                if (viewModel.state.value.hasTitleChanged || viewModel.state.value.hasContentChanged) {
                    viewModel.editPostContent()
                }
                onCancelButtonClicked()
            }
        ) {
            Text(
                text = stringResource(id = R.string.apply_changes)
            )
        }
    }
}