package com.eliasasskali.tfg.android.ui.features.post

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.eliasasskali.tfg.android.R
import com.eliasasskali.tfg.android.ui.features.clubs.Loading

@Composable
fun PostScreen(
    viewModel: PostViewModel,
    onPostCreated: () -> Unit,
    paddingValues: PaddingValues
) {
    when (viewModel.state.value.step) {
        is PostSteps.Error -> {
            // TODO: Implement error screen
        }
        is PostSteps.IsLoading -> Loading()
        is PostSteps.ShowCreatePost -> PostView(
            viewModel = viewModel,
            onPostCreated = onPostCreated,
            paddingValues = paddingValues
        )
        is PostSteps.PostCreated -> {
            Text(text = "Post created successfully")
        }
    }
}

@Composable
fun PostView(
    viewModel: PostViewModel,
    onPostCreated: () -> Unit,
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
            CreatePostButton(viewModel, onPostCreated)
        }
    }
}

@Composable
fun PostTitleField(
    viewModel: PostViewModel,
    modifier: Modifier = Modifier
) {
    val postTitle = viewModel.state.value.title
    TextField(
        modifier = modifier.fillMaxWidth(),
        value = postTitle,
        label = { Text(text = stringResource(id = R.string.post_title)) },
        onValueChange = { viewModel.setTitle(it) }
    )
}

@Composable
fun PostContentField(
    viewModel: PostViewModel,
    modifier: Modifier = Modifier
) {
    val postContent = viewModel.state.value.content
    TextField(
        modifier = modifier.fillMaxWidth(),
        value = postContent,
        label = { Text(text = stringResource(id = R.string.post_content)) },
        onValueChange = { viewModel.setContent(it) }
    )
}

@Composable
fun CreatePostButton(
    viewModel: PostViewModel,
    onPostCreated: () -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedButton(
        onClick = {
            viewModel.uploadPost()
            onPostCreated()
        },
        modifier = modifier.fillMaxWidth()
    ) {
        Text(text = stringResource(id = R.string.create_post))
    }
}