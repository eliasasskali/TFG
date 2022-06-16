package com.eliasasskali.tfg.android.ui.features.reviews

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Star
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Alignment.Companion.End
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import com.eliasasskali.tfg.R
import com.eliasasskali.tfg.android.ui.components.ErrorDialog
import com.eliasasskali.tfg.android.ui.features.clubs.Loading
import com.eliasasskali.tfg.android.ui.features.posts.PostsSteps
import com.eliasasskali.tfg.android.ui.theme.AppTheme
import com.eliasasskali.tfg.model.Review

@Composable
fun ReviewsScreen(
    viewModel: ReviewsViewModel,
    paddingValues: PaddingValues,
    onCancelErrorClicked: () -> Unit = {}
) {
    when (viewModel.state.value.step) {
        is ReviewsSteps.Error -> {
            val errorStep = viewModel.state.value.step as ReviewsSteps.Error
            ErrorDialog(
                errorMessage = errorStep.error,
                onRetryClick = errorStep.onRetry,
                onCancelClick = {
                    viewModel.setStep(ReviewsSteps.ShowReviews)
                    onCancelErrorClicked()
                }
            )
        }
        is ReviewsSteps.IsLoading -> Loading()
        is ReviewsSteps.ShowReviews ->
            ReviewsView(
                viewModel,
                paddingValues,
            )
    }
}

@Composable
fun ReviewsView(
    viewModel: ReviewsViewModel,
    paddingValues: PaddingValues,
) {
    val reviews = viewModel.reviews.collectAsLazyPagingItems()

    reviews.apply {
        when {
            loadState.refresh is LoadState.Loading -> {
                Loading()
            }
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
        Column(
            Modifier
                .fillMaxSize()
        ) {
            Text(
                text = stringResource(R.string.club_reviews),
                style = MaterialTheme.typography.h5.copy(fontWeight = FontWeight.Bold),
                textAlign = TextAlign.Start,
                modifier = Modifier.padding(
                    horizontal = 12.dp,
                    vertical = 8.dp
                )
            )
            Divider()
            Spacer(Modifier.height(12.dp))

            if (!viewModel.isClub() && viewModel.hasNoReviewsInClub(reviews.itemSnapshotList.items)) {
                CreateReviewView(viewModel)
                Spacer(Modifier.height(12.dp))
            }

            if (reviews.itemCount == 0) {
                Column(
                    modifier = Modifier
                        .padding(
                            horizontal = 12.dp,
                            vertical = 16.dp
                        )
                        .fillMaxSize()
                ) {
                    Text(
                        text = stringResource(
                            if (viewModel.isClub()) R.string.your_club_has_no_reviews
                            else R.string.club_has_no_reviews
                        ),
                        style = MaterialTheme.typography.h1,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp),
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    items(
                        items = reviews
                    ) { review ->
                        review?.let { it ->
                            ReviewCard(it)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ReviewCard(review: Review) {
    var maxLines by remember { mutableStateOf(2) }

    Card(
        modifier = Modifier
            .padding(8.dp)
            .clickable {
                maxLines = if (maxLines == 10) 2 else 10
            }
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
                RatingViewInteger(
                    rating = review.rating,
                    modifier = Modifier.align(CenterVertically)
                )

                Spacer(Modifier.width(8.dp))

                Text(
                    text = review.dateString,
                    style = MaterialTheme.typography.caption,
                    modifier = Modifier.align(CenterVertically)
                )
            }

            if (review.comment.isNotBlank()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = review.comment,
                    style = MaterialTheme.typography.body1,
                    maxLines = maxLines,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
fun CreateReviewView(
    viewModel: ReviewsViewModel
) {
    val newComment = viewModel.state.value.newComment
    var ratingSliderPosition by remember { mutableStateOf(0f) }

    Column(
        Modifier
            .padding(12.dp)
            .fillMaxWidth()
    ) {
        Row(
            Modifier
                .fillMaxWidth()
        ) {
            Text(
                text = stringResource(id = R.string.rating) + ": " + ratingSliderPosition.toInt(),
                modifier = Modifier.align(CenterVertically)
            )
            Spacer(Modifier.width(8.dp))
            Slider(
                value = ratingSliderPosition,
                onValueChange = {
                    ratingSliderPosition = it
                    viewModel.setNewRating(it.toInt())
                },
                valueRange = 1.0f..5.0f,
                steps = 5,
                modifier = Modifier.align(CenterVertically)
            )
        }
        Spacer(Modifier.width(8.dp))

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = newComment,
            label = { Text(text = stringResource(id = R.string.comment)) },
            onValueChange = {
                if (it.length <= 500) {
                    viewModel.setNewComment(it)
                }
            },
        )

        Spacer(Modifier.width(8.dp))

        Button(
            modifier = Modifier.align(End),
            onClick = {
                viewModel.postReview()
            }
        ) {
            Text(
                text = stringResource(id = R.string.create_review)
            )
        }
    }
}

@Composable
fun RatingViewInteger(
    rating: Int,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier) {
        for (i in 1..rating) {
            Icon(
                modifier = Modifier
                    .align(CenterVertically)
                    .size(16.dp),
                imageVector = Icons.Outlined.Star,
                contentDescription = stringResource(com.eliasasskali.tfg.android.R.string.rating),
            )
            Spacer(Modifier.width(4.dp))
        }
    }
}

@Composable
@Preview(showSystemUi = true, showBackground = true)
fun ReviewCardPreview() {
    val review = Review(
        dateString = "12-5-2022",
        comment = "reviewComment",
        rating = 3
    )
    AppTheme {
        ReviewCard(review = review)
    }
}