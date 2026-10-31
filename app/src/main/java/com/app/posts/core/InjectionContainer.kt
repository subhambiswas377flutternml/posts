package com.app.posts.core

import android.content.Context
import androidx.room.Room
import com.app.posts.data.datasource.local.PostDao
import com.app.posts.data.datasource.remote.PostRemoteDataSource
import com.app.posts.data.repository.PostRepositoryImpl
import com.app.posts.domain.repository.PostRepository
import com.app.posts.domain.usecase.FetchPost
import com.app.posts.domain.usecase.GetLocalPost
import com.app.posts.domain.usecase.ModifyPost
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object InjectionContainer {

    @Provides
    @Singleton
    fun apiClientProvider(): Retrofit{
        val retrofit: Retrofit = Retrofit.Builder().baseUrl("https://jsonplaceholder.typicode.com").
        addConverterFactory(GsonConverterFactory.create()).build()

        return retrofit
    }

    @Provides
    @Singleton
    fun cacheClientProvider(@ApplicationContext context: Context) : CacheClient {
        return Room.databaseBuilder(context, CacheClient::class.java, "post_db").build()
    }

    @Provides
    @Singleton
    fun provideRemoteDatasource(retrofit: Retrofit) : PostRemoteDataSource {
        return retrofit.create(PostRemoteDataSource::class.java)
    }

    @Provides
    @Singleton
    fun postDaoProvider(cacheClient: CacheClient): PostDao {
        return cacheClient.providePostDao()
    }

    @Provides
    @Singleton
    fun providePostRepository(postRemoteDataSource: PostRemoteDataSource, postDao: PostDao): PostRepository{
        return PostRepositoryImpl(postRemoteDataSource = postRemoteDataSource, postDao = postDao)
    }

    @Provides
    @Singleton
    fun provideFetchPostUseCase(postRepository: PostRepository) : FetchPost {
        return FetchPost(postRepository = postRepository)
    }

    @Provides
    @Singleton
    fun provideGetLocalPostUseCase(postRepository: PostRepository) : GetLocalPost {
        return GetLocalPost(postRepository = postRepository)
    }

    @Provides
    @Singleton
    fun provideModifyPostUseCase(postRepository: PostRepository) : ModifyPost {
        return ModifyPost(postRepository = postRepository)
    }
}






















