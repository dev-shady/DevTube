package com.devshady.devtube.presentation.player

import com.devshady.devtube.domain.model.DomainMediaItem
import com.devshady.devtube.domain.model.PlaybackSessionState

data class PlayerUiState(
    val urlInput: String = "",
    val playbackState: PlaybackSessionState = PlaybackSessionState.Idle,
    val currentMediaItem: DomainMediaItem? = null,
    val isVideo: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
