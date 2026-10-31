package com.app.posts.presentation.ui

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.app.posts.presentation.provider.PostState
import com.app.posts.presentation.provider.PostViewModel

@Composable
fun HomeScreen() {
    val postViewModel: PostViewModel = hiltViewModel<PostViewModel>()

    val listState = rememberLazyListState()

    // 2. Derive the state to check if the last item is visible
    val isAtBottom by remember {
        derivedStateOf {
            val layoutInfo = listState.layoutInfo
            val visibleItemsInfo = layoutInfo.visibleItemsInfo

            if (visibleItemsInfo.isEmpty()) {
                false
            } else {
                // Check if the last visible item's index is the last index of the data list
                val lastVisibleItem = visibleItemsInfo.last()
                lastVisibleItem.index == layoutInfo.totalItemsCount - 1
            }
        }
    }

    // 3. React when the state changes to true
    LaunchedEffect(isAtBottom) {
        if (isAtBottom) {
            postViewModel.fetchNext()
        }
    }

    LaunchedEffect(Unit) {
        postViewModel.fetchPosts()
    }

    Scaffold(modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp)) { innerPadding ->
        when (postViewModel.postState.value) {
            PostState.Initial -> {}
            PostState.Loading -> {
                CircularProgressIndicator()
            }
            is PostState.Error -> {}

            is PostState.Loaded -> {
                val posts = (postViewModel.postState.value as PostState.Loaded).posts
                LazyColumn(
                    modifier = Modifier.fillMaxWidth().padding(innerPadding),
                    verticalArrangement = Arrangement.spacedBy(20.dp),
                    state = listState
                ) {
                    items(posts.count()) { index ->
                        CustomTile(post = posts.elementAt(index),
                            modifyPost = {postId-> postViewModel.modifyPostById(postId = postId)})
                    }
                }
            }

            is PostState.OnGoingLoading -> {
                val posts = (postViewModel.postState.value as PostState.OnGoingLoading).posts
                LazyColumn(
                    modifier = Modifier.fillMaxWidth().padding(innerPadding),
                    verticalArrangement = Arrangement.spacedBy(20.dp),
                ) {
                    items(posts.count() + 1) { index ->
                        if(index==posts.count()){
                            CircularProgressIndicator()
                        }
                        else{
                            CustomTile(post = posts.elementAt(index),
                                modifyPost = {postId-> postViewModel.modifyPostById(postId = postId)})
                            }
                    }
                }
            }

            is PostState.OnGoingError -> {
                val posts = (postViewModel.postState.value as PostState.OnGoingError).posts
                LazyColumn(
                    modifier = Modifier.fillMaxWidth().padding(innerPadding),
                    verticalArrangement = Arrangement.spacedBy(20.dp),
                ) {
                    items(posts.count() + 1) { index ->
                        if(index==posts.count()){
                            Text("Something went wrong !", modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.Center)
                        }
                        else{
                            CustomTile(post = posts.elementAt(index),
                                modifyPost = {postId-> postViewModel.modifyPostById(postId = postId)})
                        }
                    }
                }
            }
        }

    }
}