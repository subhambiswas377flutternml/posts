package com.app.posts.domain.repository

import com.app.posts.domain.model.PostModel

interface PostRepository {
    suspend fun fetchAllPosts(): List<PostModel>
    suspend fun getLocalPosts(): List<PostModel>
    suspend fun modifyPost(postId:Int) : List<PostModel>
}