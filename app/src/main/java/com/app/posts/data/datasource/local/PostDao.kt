package com.app.posts.data.datasource.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.app.posts.data.entity.PostCacheDto

@Dao
abstract class PostDao {
    @Upsert
    abstract suspend fun upsert(posts: List<PostCacheDto>)

    @Query("SELECT * FROM post_table WHERE id = :postId")
    abstract suspend fun getById(postId: Int) : PostCacheDto

    @Query("SELECT * FROM post_table")
    abstract suspend fun getAllPosts(): List<PostCacheDto>

    @Query("UPDATE post_table SET 'like' = 1 WHERE id = :postId")
    abstract suspend fun like(postId: Int)

    @Query("UPDATE post_table SET 'like' = 0 WHERE id = :postId")
    abstract suspend fun unlike(postId: Int)
}