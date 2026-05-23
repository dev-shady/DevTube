package com.devshady.devtube.domain.model

/**
 * Represents the various states of the playback session.
 */
sealed class PlaybackSessionState {
    object Idle : PlaybackSessionState()
    
    data class Buffering(
        val currentItem: DomainMediaItem,
        val position: Long,
        val duration: Long
    ) : PlaybackSessionState()
    
    data class Playing(
        val currentItem: DomainMediaItem,
        val position: Long,
        val duration: Long
    ) : PlaybackSessionState()
    
    data class Paused(
        val currentItem: DomainMediaItem,
        val position: Long,
        val duration: Long
    ) : PlaybackSessionState()
    
    data class Error(val message: String) : PlaybackSessionState()
}
