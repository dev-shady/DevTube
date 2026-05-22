package com.devshady.devtube.domain.coordinator

import com.devshady.devtube.domain.model.DomainMediaItem
import com.devshady.devtube.domain.model.MediaSourceType
import com.devshady.devtube.domain.model.PlaybackSessionState
import com.devshady.devtube.domain.playback.IMediaController
import com.devshady.devtube.domain.playback.MediaStreamExtractor
import com.devshady.devtube.domain.playback.MediaUrlParser
import com.devshady.devtube.domain.repository.MediaRepository
import kotlinx.coroutines.flow.StateFlow

/**
 * Orchestrates playback by bridging the [IMediaController] and [MediaRepository].
 */
class PlaybackCoordinator(
    private val mediaController: IMediaController,
    private val mediaRepository: MediaRepository,
    private val urlParser: MediaUrlParser,
    private val streamExtractor: MediaStreamExtractor
) {
    /**
     * Exposes the current playback session state.
     */
    val sessionState: StateFlow<PlaybackSessionState> = mediaController.sessionState

    /**
     * Prepares and starts playback for a media item by ID/URL.
     */
    suspend fun playMedia(id: String) {
        val mediaItem = mediaRepository.getMediaItem(id)
        if (mediaItem != null) {
            val sourceType = urlParser.parseSourceType(id)
            val isAudioOnly = sourceType == MediaSourceType.YOUTUBE_MUSIC
            val playableUri = streamExtractor.extractPlayableUri(id, isAudioOnly)
            
            mediaController.prepare(mediaItem, playableUri)
            mediaController.play()
        }
    }

    fun play() = mediaController.play()
    
    fun pause() = mediaController.pause()
    
    fun seekTo(position: Long) = mediaController.seekTo(position)
    
    fun skipToNext() = mediaController.skipToNext()
    
    fun skipToPrevious() = mediaController.skipToPrevious()
    
    fun release() = mediaController.release()
}
