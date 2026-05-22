package com.devshady.devtube.playback.controller

import android.content.ComponentName
import android.content.Context
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.devshady.devtube.domain.model.DomainMediaItem
import com.devshady.devtube.domain.model.PlaybackSessionState
import com.devshady.devtube.domain.playback.IMediaController
import com.devshady.devtube.presentation.player.PlayerHandleProvider
import com.devshady.devtube.playback.service.PlaybackService
import com.google.common.util.concurrent.ListenableFuture
import com.google.common.util.concurrent.MoreExecutors
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MediaControllerImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : IMediaController, PlayerHandleProvider {

    private var controllerFuture: ListenableFuture<MediaController>? = null
    val controller: MediaController?
        get() = if (controllerFuture?.isDone == true) controllerFuture?.get() else null

    override fun getPlayer(): Player? = controller

    private val _sessionState = MutableStateFlow<PlaybackSessionState>(PlaybackSessionState.Idle)
    override val sessionState: StateFlow<PlaybackSessionState> = _sessionState.asStateFlow()

    init {
        val sessionToken = SessionToken(context, ComponentName(context, PlaybackService::class.java))
        controllerFuture = MediaController.Builder(context, sessionToken).buildAsync()
        controllerFuture?.addListener({
            controller?.addListener(object : Player.Listener {
                override fun onPlaybackStateChanged(playbackState: Int) {
                    updateState()
                }

                override fun onIsPlayingChanged(isPlaying: Boolean) {
                    updateState()
                }
            })
            updateState()
        }, MoreExecutors.directExecutor())
    }

    private fun updateState() {
        val currentController = controller ?: return
        val currentMediaItem = currentController.currentMediaItem
        val domainItem = currentMediaItem?.let {
            DomainMediaItem(
                id = it.mediaId,
                title = it.mediaMetadata.title?.toString() ?: "",
                artist = it.mediaMetadata.artist?.toString() ?: "",
                artworkUrl = it.mediaMetadata.artworkUri?.toString(),
                isVideo = false // Simplified for now
            )
        }

        val state = when {
            currentController.playbackState == Player.STATE_BUFFERING -> PlaybackSessionState.Buffering
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
        _sessionState.value = state
    }

    override fun play() {
        controller?.play()
    }

    override fun pause() {
        controller?.pause()
    }

    override fun seekTo(position: Long) {
        controller?.seekTo(position)
    }

    override fun skipToNext() {
        controller?.seekToNext()
    }

    override fun skipToPrevious() {
        controller?.seekToPrevious()
    }

    override fun prepare(mediaItem: DomainMediaItem, playableUri: String) {
        val m3Item = MediaItem.Builder()
            .setMediaId(mediaItem.id)
            .setUri(playableUri)
            .build()
        controller?.setMediaItem(m3Item)
        controller?.prepare()
    }

    override fun release() {
        controllerFuture?.let {
            MediaController.releaseFuture(it)
        }
    }
}
