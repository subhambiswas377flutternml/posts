package com.app.posts.presentation.provider

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.posts.domain.model.PostModel
import com.app.posts.domain.usecase.FetchPost
import com.app.posts.domain.usecase.GetLocalPost
import com.app.posts.domain.usecase.ModifyPost
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class PostState{
    data object Initial: PostState()
    data object Loading : PostState()
    data class Loaded(val posts: List<PostModel>) : PostState()
    data class Error(val ex: Exception?=null) : PostState()
}

@HiltViewModel
class PostViewModel @Inject constructor(
    private val getLocalPost: GetLocalPost,
    private val fetchPost: FetchPost,
    private val modifyPost: ModifyPost
) : ViewModel() {
    private val _postState : MutableState<PostState> = mutableStateOf<PostState>(value = PostState.Initial)
    val postState: State<PostState> = _postState

    // Exception Handler
    val exceptionHandler = CoroutineExceptionHandler{context, throwable->
        _postState.value = PostState.Error()
    }

    fun fetchPosts(){
        _postState.value = PostState.Loading
        viewModelScope.launch(Dispatchers.IO + exceptionHandler) {
            try{
                val localPosts = getLocalPost()
                _postState.value = PostState.Loaded(posts = localPosts)

                val posts = fetchPost()
                _postState.value = PostState.Loaded(posts = posts)
            }catch(ex: Exception){
                throw ex
            }
        }
    }
}