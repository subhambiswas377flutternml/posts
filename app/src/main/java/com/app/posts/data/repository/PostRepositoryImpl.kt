package com.app.posts.data.repository

import com.app.posts.data.datasource.local.PostDao
import com.app.posts.data.datasource.remote.PostRemoteDataSource
import com.app.posts.data.entity.PostDto
import com.app.posts.data.entity.toCacheDto
import com.app.posts.data.entity.toModel
import com.app.posts.domain.model.PostModel
import com.app.posts.domain.repository.PostRepository
import javax.inject.Inject

class PostRepositoryImpl(
    private val postRemoteDataSource: PostRemoteDataSource,
    private val postDao: PostDao
) : PostRepository {
    override suspend fun fetchAllPosts(): List<PostModel> {
        val posts: List<PostDto> = postRemoteDataSource.getPosts(page = 1, limit = 20)
        val postLocal = posts.map { p-> p.toCacheDto() }.toList()
        postDao.upsert(postLocal)
        return postLocal.map { p-> p.toModel() }.toList()
    }

    override suspend fun getLocalPosts(): List<PostModel> {
        val localPosts = postDao.getAllPosts()
        return localPosts.map { p-> p.toModel() }.toList()
    }

    override suspend fun modifyPost(postId: Int) : List<PostModel> {
        postDao.modifyLike(postId = postId)
        return getLocalPosts()
    }
}