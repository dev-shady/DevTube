package com.devshady.devtube.di

import com.devshady.devtube.domain.playback.IMediaController
import com.devshady.devtube.playback.controller.MediaControllerImpl
import com.devshady.devtube.presentation.player.PlayerHandleProvider
import com.devshady.devtube.domain.coordinator.PlaybackCoordinator
import com.devshady.devtube.domain.repository.MediaRepository
import com.devshady.devtube.domain.playback.MediaUrlParser
import com.devshady.devtube.domain.playback.MediaStreamExtractor
import com.devshady.devtube.data.parser.YouTubeUrlParser
import com.devshady.devtube.data.extractor.MockStreamExtractor
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class PlaybackModule {

    @Binds
    @Singleton
    abstract fun bindMediaController(impl: MediaControllerImpl): IMediaController

    @Binds
    @Singleton
    abstract fun bindPlayerHandleProvider(impl: MediaControllerImpl): PlayerHandleProvider

    @Binds
    @Singleton
    abstract fun bindMediaUrlParser(impl: YouTubeUrlParser): MediaUrlParser

    @Binds
    @Singleton
    abstract fun bindMediaStreamExtractor(impl: MockStreamExtractor): MediaStreamExtractor

    companion object {
        @Provides
        @Singleton
        fun providePlaybackCoordinator(
            mediaController: IMediaController,
            mediaRepository: MediaRepository,
            urlParser: MediaUrlParser,
            streamExtractor: MediaStreamExtractor
        ): PlaybackCoordinator {
            return PlaybackCoordinator(mediaController, mediaRepository, urlParser, streamExtractor)
        }
    }
}
