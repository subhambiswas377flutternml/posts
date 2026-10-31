package com.app.posts.data.entity

data class PostDto(
    val userId: Int,
    val id: Int,
    val title: String,
    val body: String
)

fun PostDto.toCacheDto() : PostCacheDto{
    return PostCacheDto(
        userId = this.userId,
        id = this.id,
        title = this.title,
        body = this.body
    )
}