package com.app.posts.domain.usecase

import com.app.posts.domain.model.PostModel
import com.app.posts.domain.repository.PostRepository

class FetchPost(private val postRepository: PostRepository){
    suspend operator fun invoke(page: Int, limit: Int = 5): List<PostModel>{
        return postRepository.fetchAllPosts(page=page, limit=limit)
    }
}