package com.eliasasskali.tfg.android.ui.features.posts

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import com.eliasasskali.tfg.android.ui.features.clubs.Loading
import com.eliasasskali.tfg.android.ui.theme.AppTheme
import com.eliasasskali.tfg.model.Post

@Composable
fun PostsScreen(
    viewModel: PostsViewModel,
    onPostClicked: (Post) -> Unit = {},
    paddingValues: PaddingValues
) {
    when (viewModel.state.value.step) {
        is PostsSteps.Error -> {}
        is PostsSteps.IsLoading -> Loading()
        is PostsSteps.ShowPosts -> PostsView(viewModel, onPostClicked, paddingValues)
    }
}

@Composable
fun PostsView(
    viewModel: PostsViewModel,
    onPostClicked: (Post) -> Unit = {},
    paddingValues: PaddingValues
) {
    val posts = viewModel.posts.collectAsLazyPagingItems()

    posts.apply {
        when {
            loadState.refresh is LoadState.Loading -> {}
            loadState.refresh is LoadState.NotLoading -> {}
            loadState.refresh is LoadState.Error -> {}
            loadState.append is LoadState.Loading -> {}
            loadState.append is LoadState.Error -> {}
        }
    }

    Surface(
        Modifier
            .padding(paddingValues)
            .fillMaxSize()
    ) {
        LazyColumn(
            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp),
            modifier = Modifier
                .background(color = MaterialTheme.colors.background)
                .fillMaxSize()
        ) {
            items(
                items = posts
            ) { post ->
                post?.let { it ->
                    PostCard(it)
                }
            }
        }
    }
}

@Composable
fun PostCard(post: Post, onPostClicked: (Post) -> Unit = {}) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .clickable { onPostClicked(post) }
            .fillMaxWidth(),
        elevation = 2.dp,
        shape = RoundedCornerShape(corner = CornerSize(16.dp))
    ) {
        Column(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
        ) {
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = post.clubName,
                    style = MaterialTheme.typography.h1,
                    modifier = Modifier.align(CenterVertically)
                )

                Text(
                    text = post.dateString, //post.date.toString(),
                    style = MaterialTheme.typography.caption,
                    modifier = Modifier.align(CenterVertically)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = post.title,
                style = MaterialTheme.typography.h1.copy(fontSize = 24.sp)
            )
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = post.content,
                style = MaterialTheme.typography.body1,
                maxLines = 4,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
@Preview(showSystemUi = true, showBackground = true)
fun PostCardPreview() {
    val post = Post(
        title = "Test post",
        content = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Phasellus erat neque, auctor in risus nec, aliquam laoreet nunc. Vivamus ut ante odio. Cras quis interdum sapien. Nullam pulvinar vel metus vel pellentesque. Sed iaculis non diam feugiat malesuada. Donec elementum, turpis at fringilla lobortis, turpis ligula faucibus mauris, id aliquet felis ex eget nisi. Fusce nec maximus dolor. Curabitur volutpat gravida massa, vulputate consectetur ex sodales ac.",
        date = 1651314605617,
        clubName = "Club Name"
    )
    AppTheme {
        PostCard(post = post)
    }
}