package com.app.posts.core

import androidx.room.Database
import androidx.room.RoomDatabase
import com.app.posts.data.datasource.local.PostDao
import com.app.posts.data.entity.PostCacheDto

@Database(
    version = 1,
    exportSchema = false,
    entities = [PostCacheDto::class]
)
abstract class CacheClient : RoomDatabase() {
    abstract fun providePostDao() : PostDao
}