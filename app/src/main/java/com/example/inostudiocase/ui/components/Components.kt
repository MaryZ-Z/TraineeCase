package com.example.inostudiocase.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.inostudiocase.R
import com.example.inostudiocase.ui.movielist.MovieListViewModel

@Composable
fun ScreenError(message: String, onRefresh: () -> Unit) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                painter = painterResource(R.drawable.ic_baseline_error_outline_24),
                contentDescription = null,
                tint = Color.Red
            )
            Text(text = message, style = MaterialTheme.typography.body2)
            Button(onClick = { onRefresh.invoke() }) {
                Icon(
                    Icons.Filled.Refresh,
                    contentDescription = stringResource(R.string.refresh),
                    modifier = Modifier.size(ButtonDefaults.IconSize)
                )
                Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                Text(text = stringResource(R.string.refresh),
                style = MaterialTheme.typography.h5)
            }
        }
    }
}