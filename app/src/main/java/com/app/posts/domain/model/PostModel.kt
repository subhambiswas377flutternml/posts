package com.app.posts.domain.model

data class PostModel(
    val userId: Int,
    val id: Int,
    val title: String,
    val body: String,
    val like: Boolean
)