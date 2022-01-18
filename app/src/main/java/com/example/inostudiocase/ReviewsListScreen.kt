package com.example.inostudiocase

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.inostudiocase.common.ListState
import com.example.inostudiocase.common.Screen
import com.example.inostudiocase.data.Reviews
import com.example.inostudiocase.ui.components.ScreenError
import com.example.inostudiocase.ui.reviewlist.ReviewListViewModel

@Composable
fun ReviewsListScreen() {
    val viewModel: ReviewListViewModel = hiltViewModel()
    val state = viewModel.state.value

    Column {
        when (state) {
            is ListState.Loading -> ScreenLoading()
            is ListState.Loaded -> ScreenLoaded(state.data)
            is ListState.Error -> ScreenError(state.message, onRefresh = { viewModel.reviews() })
        }
    }
}

@Composable
fun ScreenLoaded(
    items: List<Reviews>
) {
    LazyColumn {
        items(items) {
            ReviewItem(
                reviews = it
            )
        }
    }
}

@Composable
fun ReviewItem(reviews: Reviews) {
    var isExpanded by remember { mutableStateOf(false) }
    Row(
        modifier =
        Modifier
            .padding(14.dp)
    ) {
        Text(
            text = stringResource(R.string.rew),
            style = MaterialTheme.typography.h1
        )
    }
    Column(
        Modifier
            .padding(14.dp)
            .clickable { isExpanded = !isExpanded }
    ) {
        Text(
            overflow = TextOverflow.Ellipsis,
            text = reviews.author,
            style = MaterialTheme.typography.overline
        )
        Text(
            modifier = Modifier.animateContentSize(),
            maxLines = if (isExpanded) Int.MAX_VALUE else 5,
            text = reviews.content,
            style = MaterialTheme.typography.subtitle1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}