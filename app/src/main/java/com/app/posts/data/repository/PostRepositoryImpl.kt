package com.app.posts.data.repository

import com.app.posts.data.datasource.local.PostDao
import com.app.posts.data.datasource.remote.PostRemoteDataSource
import com.app.posts.data.entity.PostCacheDto
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
    override suspend fun fetchAllPosts(page:Int, limit:Int): List<PostModel> {
        val posts: List<PostDto> = postRemoteDataSource.getPosts(page = page, limit = limit)
        val postLocal:MutableList<PostCacheDto> = mutableListOf()
        posts.forEach {
            val isLiked: Boolean = isLiked(it.id)
            postLocal.add(it.toCacheDto(isLike = isLiked))
        }

        postDao.upsert(postLocal)
        return postLocal.map { p-> p.toModel() }.toList()
    }

    private suspend fun isLiked(postId:Int) :Boolean{
        val localPosts = postDao.getAllPosts()
        if(localPosts.isEmpty()){
            return false
        }else{
            val post = localPosts.find { p-> p.id == postId }
            return post?.like == true
        }
    }

    override suspend fun getLocalPosts(): List<PostModel> {
        val localPosts = postDao.getAllPosts()
        return localPosts.map { p-> p.toModel() }.toList()
    }

    override suspend fun modifyPost(postId: Int) : List<PostModel> {
        val post: PostCacheDto = postDao.getById(postId = postId)
        if(post.like){
            postDao.unlike(postId = postId)
        }else{
            postDao.like(postId = postId)
        }

        val localPosts = getLocalPosts()
        return localPosts
    }
}