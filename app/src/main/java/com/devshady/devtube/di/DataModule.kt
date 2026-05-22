package com.devshady.devtube.di

import com.devshady.devtube.data.repository.MediaRepositoryImpl
import com.devshady.devtube.domain.repository.MediaRepository
import com.devshady.devtube.domain.playback.MediaUrlParser
import com.devshady.devtube.data.parser.YouTubeUrlParser
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {

    @Binds
    @Singleton
    abstract fun bindMediaRepository(impl: MediaRepositoryImpl): MediaRepository
}
