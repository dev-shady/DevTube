package com.devshady.devtube.playback.controller

import android.content.ComponentName
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.annotation.OptIn
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.devshady.devtube.domain.model.DomainMediaItem
import com.devshady.devtube.domain.model.PlaybackSessionState
import com.devshady.devtube.domain.playback.IMediaController
import com.devshady.devtube.presentation.player.PlayerHandleProvider
import com.devshady.devtube.playback.service.PlaybackService
import androidx.core.content.ContextCompat
import com.google.common.util.concurrent.ListenableFuture
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import javax.inject.Inject
import javax.inject.Singleton

private const val EXTRA_IS_VIDEO = "is_video"
private const val POSITION_UPDATE_INTERVAL_MS = 500L

@OptIn(UnstableApi::class)
@Singleton
class MediaControllerImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : IMediaController, PlayerHandleProvider {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main)
    private var positionUpdateJob: Job? = null

    private var controllerFuture: ListenableFuture<MediaController>? = null
    val controller: MediaController?
        get() = if (controllerFuture?.isDone == true) {
            try {
                controllerFuture?.get()
            } catch (e: Exception) {
                null
            }
        } else null

    override fun getPlayer(): Player? = controller

    private val _sessionState = MutableStateFlow<PlaybackSessionState>(PlaybackSessionState.Idle)
    override val sessionState: StateFlow<PlaybackSessionState> = _sessionState.asStateFlow()

    private val _isConnected = MutableStateFlow(false)
    override val isConnected: StateFlow<Boolean> = _isConnected.asStateFlow()

    private var cachedMediaItem: DomainMediaItem? = null
    private val mainExecutor = ContextCompat.getMainExecutor(context)

    private fun ensureConnected() {
        if (controllerFuture != null) return
        
        val sessionToken = SessionToken(context, ComponentName(context, PlaybackService::class.java))
        controllerFuture = MediaController.Builder(context, sessionToken).buildAsync()
        controllerFuture?.addListener({
            _isConnected.value = true
            controller?.addListener(object : Player.Listener {
                override fun onPlaybackStateChanged(playbackState: Int) {
                    updateState()
                    handlePositionPolling(controller?.isPlaying == true)
                }

                override fun onIsPlayingChanged(isPlaying: Boolean) {
                    updateState()
                    handlePositionPolling(isPlaying)
                }

                override fun onPlayerError(error: androidx.media3.common.PlaybackException) {
                    updateState()
                    handlePositionPolling(false)
                }

                override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
                    updateState()
                }

                override fun onMediaMetadataChanged(mediaMetadata: MediaMetadata) {
                    updateState()
                }

                override fun onMetadata(metadata: androidx.media3.common.Metadata) {
                    updateState()
                }
            })
            updateState()
            handlePositionPolling(controller?.isPlaying == true)
        }, mainExecutor)
    }

    private fun handlePositionPolling(isPlaying: Boolean) {
        positionUpdateJob?.cancel()
        if (isPlaying) {
            positionUpdateJob = scope.launch {
                while (isActive) {
                    delay(POSITION_UPDATE_INTERVAL_MS)
                    updateState()
                }
            }
        }
    }

    private fun updateState() {
        val currentController = controller ?: return
        val currentM3Item = currentController.currentMediaItem
        
        val domainItem = if (currentM3Item != null) {
            val metadata = currentM3Item.mediaMetadata
            if (cachedMediaItem?.id == currentM3Item.mediaId) {
                val updatedItem = cachedMediaItem?.copy(
                    title = metadata.title?.toString() ?: cachedMediaItem?.title ?: "Unknown Title",
                    artist = metadata.artist?.toString() ?: cachedMediaItem?.artist ?: "Unknown Artist",
                    artworkUrl = metadata.artworkUri?.toString() ?: cachedMediaItem?.artworkUrl,
                    isVideo = metadata.extras?.let { 
                        if (it.containsKey(EXTRA_IS_VIDEO)) it.getBoolean(EXTRA_IS_VIDEO) 
                        else cachedMediaItem?.isVideo 
                    } ?: cachedMediaItem?.isVideo ?: false
                )
                if (updatedItem != cachedMediaItem) {
                    cachedMediaItem = updatedItem
                }
                cachedMediaItem
            } else {
                DomainMediaItem(
                    id = currentM3Item.mediaId,
                    title = metadata.title?.toString() ?: "Unknown Title",
                    artist = metadata.artist?.toString() ?: "Unknown Artist",
                    artworkUrl = metadata.artworkUri?.toString(),
                    isVideo = metadata.extras?.getBoolean(EXTRA_IS_VIDEO) ?: false,
                    resolvedStreamingUrl = currentM3Item.localConfiguration?.uri?.toString() ?: ""
                ).also { cachedMediaItem = it }
            }
        } else {
            // If player is idle and NOT preparing, clear cache. 
            if (currentController.playbackState == Player.STATE_IDLE && currentController.currentMediaItem == null) {
                cachedMediaItem = null
            }
            cachedMediaItem
        }

        val newState = when {
            currentController.playerError != null -> {
                PlaybackSessionState.Error(currentController.playerError?.message ?: "Playback error")
            }
            currentController.playbackState == Player.STATE_BUFFERING -> {
                domainItem?.let {
                    PlaybackSessionState.Buffering(it, currentController.currentPosition, currentController.duration)
                } ?: PlaybackSessionState.Idle
            }
            currentController.isPlaying -> {
                domainItem?.let {
                    PlaybackSessionState.Playing(it, currentController.currentPosition, currentController.duration)
                } ?: PlaybackSessionState.Idle
            }
            currentController.playbackState == Player.STATE_READY -> {
                domainItem?.let {
                    PlaybackSessionState.Paused(it, currentController.currentPosition, currentController.duration)
                } ?: PlaybackSessionState.Idle
            }
            else -> PlaybackSessionState.Idle
        }
        
        if (_sessionState.value != newState) {
            Log.d("MediaControllerImpl", "Emitting new state: $newState")
            _sessionState.value = newState
        }
    }

    override fun play() {
        ensureConnected()
        controller?.play()
    }

    override fun pause() {
        ensureConnected()
        controller?.pause()
    }

    override fun seekTo(position: Long) {
        ensureConnected()
        controller?.seekTo(position)
    }

    override fun skipToNext() {
        ensureConnected()
        controller?.seekToNext()
    }

    override fun skipToPrevious() {
        ensureConnected()
        controller?.seekToPrevious()
    }

    override fun prepare(mediaItem: DomainMediaItem, playableUri: String) {
        cachedMediaItem = mediaItem
        Log.d("MediaControllerImpl", "Preparing with mediaItem: ${mediaItem.id}")
        ensureConnected()
        
        val m3Item = MediaItem.Builder()
            .setMediaId(mediaItem.id)
            .setUri(playableUri)
            .setMediaMetadata(
                MediaMetadata.Builder()
                    .setTitle(mediaItem.title)
                    .setArtist(mediaItem.artist)
                    .setArtworkUri(mediaItem.artworkUrl?.let { Uri.parse(it) })
                    .setExtras(Bundle().apply { putBoolean(EXTRA_IS_VIDEO, mediaItem.isVideo) })
                    .build()
            )
            .build()
        
        val currentController = controller
        if (currentController != null) {
            currentController.setMediaItem(m3Item)
            currentController.playWhenReady = true
            currentController.prepare()
            updateState()
        } else {
            controllerFuture?.addListener({
                controller?.let { player ->
                    player.setMediaItem(m3Item)
                    player.playWhenReady = true
                    player.prepare()
                    updateState()
                }
            }, mainExecutor)
        }
    }

    override fun release() {
        controllerFuture?.let {
            MediaController.releaseFuture(it)
        }
    }
}
