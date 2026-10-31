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
    data class OnGoingLoading(val posts: List<PostModel>): PostState()
    data class OnGoingError(val posts: List<PostModel>,
                            val ex: Exception?=null): PostState()
}

@HiltViewModel
class PostViewModel @Inject constructor(
    private val getLocalPost: GetLocalPost,
    private val fetchPostUseCase: FetchPost,
    private val modifyPost: ModifyPost
) : ViewModel() {
    private val _postState: MutableState<PostState> =
        mutableStateOf<PostState>(value = PostState.Initial)
    val postState: State<PostState> = _postState

    private val _pageNumber: MutableState<Int> = mutableStateOf(value = 1)

    private val _perPageValue:Int = 5

    // Exception Handler
    val exceptionHandler = CoroutineExceptionHandler { context, throwable ->
        _postState.value = PostState.Error()
    }

    fun fetchPosts() {
        _postState.value = PostState.Loading
        viewModelScope.launch(Dispatchers.IO + exceptionHandler) {
            try {
                val localPosts = getLocalPost()
                _postState.value = PostState.Loaded(posts = localPosts)

                val posts = fetchPostUseCase(page = _pageNumber.value, limit = _perPageValue)
                _postState.value = PostState.Loaded(posts = posts)
                if(posts.count()==_perPageValue){
                    _pageNumber.value += 1;
                }
            } catch (ex: Exception) {
                throw ex
            }
        }
    }

    fun modifyPostById(postId: Int) {
        viewModelScope.launch(Dispatchers.IO + exceptionHandler) {
            try {
                val posts = modifyPost(postId = postId)
                _postState.value = PostState.Loaded(posts = posts)
            } catch (ex: Exception) {
                throw ex
            }
        }
    }

    fun fetchNext(){
        val oldPosts = (_postState.value as PostState.Loaded).posts
        _postState.value = PostState.OnGoingLoading(posts = oldPosts)

        viewModelScope.launch(Dispatchers.IO + exceptionHandler) {
            try {
                val newPosts = fetchPostUseCase(page = _pageNumber.value, limit = _perPageValue)
                _postState.value = PostState.Loaded(posts = oldPosts.plus(newPosts))

                if(newPosts.count()==_perPageValue){
                    _pageNumber.value += 1;
                }
            } catch (ex: Exception) {
                _postState.value = PostState.OnGoingError(posts = oldPosts, ex)
            }
        }
    }
}