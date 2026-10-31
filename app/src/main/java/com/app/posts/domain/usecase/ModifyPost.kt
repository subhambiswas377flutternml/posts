package com.app.posts.domain.usecase

import com.app.posts.domain.model.PostModel
import com.app.posts.domain.repository.PostRepository

class ModifyPost constructor(private val postRepository: PostRepository) {
    suspend operator fun invoke(postId: Int): List<PostModel> {
        return postRepository.modifyPost(postId = postId)
    }
}