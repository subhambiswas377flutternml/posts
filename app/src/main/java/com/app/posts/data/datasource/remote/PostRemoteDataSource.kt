package com.app.posts.data.datasource.remote

import com.app.posts.data.entity.PostDto
import retrofit2.http.GET
import retrofit2.http.Query

interface PostRemoteDataSource {
    @GET("/posts")
    suspend fun getPosts(@Query("_page") page: Int, @Query("_limit") limit: Int): List<PostDto>
}