package com.app.posts.domain.usecase

import com.app.posts.domain.model.PostModel
import com.app.posts.domain.repository.PostRepository

class GetLocalPost constructor(private val postRepository: PostRepository){
    suspend operator fun invoke(): List<PostModel>{
        return postRepository.getLocalPosts()
    }
}