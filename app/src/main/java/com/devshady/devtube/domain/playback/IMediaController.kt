package com.devshady.devtube.domain.playback

import com.devshady.devtube.domain.model.DomainMediaItem
import com.devshady.devtube.domain.model.PlaybackSessionState
import kotlinx.coroutines.flow.StateFlow

/**
 * Interface for controlling media playback.
 */
interface IMediaController {
    val sessionState: StateFlow<PlaybackSessionState>
    val isConnected: StateFlow<Boolean>
    
    fun play()
    fun pause()
    fun seekTo(position: Long)
    fun skipToNext()
    fun skipToPrevious()
    fun prepare(mediaItem: DomainMediaItem, playableUri: String)
    fun release()
}
