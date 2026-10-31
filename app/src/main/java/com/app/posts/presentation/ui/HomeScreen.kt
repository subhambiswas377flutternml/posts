package com.app.posts.presentation.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.app.posts.presentation.provider.PostState
import com.app.posts.presentation.provider.PostViewModel

@Composable
fun HomeScreen() {
    val postViewModel: PostViewModel = hiltViewModel<PostViewModel>()

    LaunchedEffect(Unit) {
        postViewModel.fetchPosts()
    }

    Scaffold(modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp)) { innerPadding ->
        when (postViewModel.postState.value) {
            PostState.Initial -> {}
            PostState.Loading -> {
                CircularProgressIndicator()
            }

            is PostState.Loaded -> {
                val posts = (postViewModel.postState.value as PostState.Loaded).posts
                LazyColumn(
                    modifier = Modifier.fillMaxWidth().padding(innerPadding),
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    items(posts.count()) { index ->
                        CustomTile(post = posts.elementAt(index),
                            modifyPost = {postId-> postViewModel.modifyPostById(postId = postId)})
                    }
                }
            }

            is PostState.Error -> {}
        }
    }
}