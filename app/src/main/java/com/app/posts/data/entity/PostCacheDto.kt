package com.app.posts.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.app.posts.domain.model.PostModel

@Entity(tableName = "post_table")
data class PostCacheDto(
    @ColumnInfo(name = "user_id")
    val userId: Int,
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    @ColumnInfo
    val title: String,
    @ColumnInfo
    val body: String,
    @ColumnInfo
    val like: Boolean = false,
)

fun PostCacheDto.toModel() : PostModel{
    return PostModel(
        userId = this.userId,
        id = this.id,
        title = this.title,
        body = this.body,
        like = this.like
    )
}