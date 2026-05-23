package com.devshady.devtube.presentation.player

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devshady.devtube.domain.coordinator.PlaybackCoordinator
import com.devshady.devtube.domain.model.PlaybackSessionState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import androidx.media3.common.Player
import javax.inject.Inject

@HiltViewModel
class PlayerViewModel @Inject constructor(
    private val playbackCoordinator: PlaybackCoordinator
) : ViewModel() {

    private val _urlInput = MutableStateFlow("")
    private val _isLoading = MutableStateFlow(false)
    private val _errorMessage = MutableStateFlow<String?>(null)

    val uiState: StateFlow<PlayerUiState> = combine(
        _urlInput,
        playbackCoordinator.sessionState,
        _isLoading,
        _errorMessage
    ) { url, sessionState, loading, error ->
        val currentItem = when (sessionState) {
            is PlaybackSessionState.Playing -> sessionState.currentItem
            is PlaybackSessionState.Paused -> sessionState.currentItem
            else -> null
        }
        PlayerUiState(
            urlInput = url,
            playbackState = sessionState,
            currentMediaItem = currentItem,
            isVideo = currentItem?.isVideo ?: false,
            isLoading = loading,
            errorMessage = error
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = PlayerUiState()
    )

    fun onIntent(intent: PlayerUserIntent) {
        when (intent) {
            is PlayerUserIntent.UpdateUrlInput -> _urlInput.value = intent.url
            is PlayerUserIntent.PlayFromUrl -> playFromUrl()
            is PlayerUserIntent.TogglePlayPause -> togglePlayPause()
            is PlayerUserIntent.SeekTo -> playbackCoordinator.seekTo(intent.position)
            is PlayerUserIntent.SkipNext -> playbackCoordinator.skipToNext()
            is PlayerUserIntent.SkipPrevious -> playbackCoordinator.skipToPrevious()
        }
    }

    private fun playFromUrl() {
        val url = _urlInput.value
        if (url.isBlank()) return

        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            try {
                playbackCoordinator.playMedia(url)
            } catch (e: Exception) {
                _errorMessage.value = e.message ?: "Unknown error"
            } finally {
                _isLoading.value = false
            }
        }
    }

    private fun togglePlayPause() {
        when (val state = playbackCoordinator.sessionState.value) {
            is PlaybackSessionState.Playing -> playbackCoordinator.pause()
            is PlaybackSessionState.Paused -> playbackCoordinator.play()
            is PlaybackSessionState.Idle -> { /* Do nothing or play default */ }
            else -> playbackCoordinator.play()
        }
    }

    override fun onCleared() {
        super.onCleared()
        playbackCoordinator.release()
    }
}
