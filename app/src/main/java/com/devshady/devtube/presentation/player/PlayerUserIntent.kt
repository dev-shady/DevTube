package com.devshady.devtube.presentation.player

sealed class PlayerUserIntent {
    data class UpdateUrlInput(val url: String) : PlayerUserIntent()
    object PlayFromUrl : PlayerUserIntent()
    object TogglePlayPause : PlayerUserIntent()
    data class SeekTo(val position: Long) : PlayerUserIntent()
    object SkipNext : PlayerUserIntent()
    object SkipPrevious : PlayerUserIntent()
}
