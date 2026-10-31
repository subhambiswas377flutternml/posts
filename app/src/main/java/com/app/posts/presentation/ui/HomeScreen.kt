package com.app.posts.presentation.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.app.posts.presentation.provider.PostViewModel

@Composable
fun HomeScreen(){
    val postViewModel : PostViewModel = hiltViewModel<PostViewModel>()

    LaunchedEffect(Unit) {
        postViewModel.fetchPosts()
    }

    Scaffold(modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp)) {innerPadding ->
        LazyColumn(modifier = Modifier.fillMaxWidth().padding(innerPadding),
            verticalArrangement = Arrangement.spacedBy(20.dp)) { items(1){
            CustomTile()
        } }
    }
}