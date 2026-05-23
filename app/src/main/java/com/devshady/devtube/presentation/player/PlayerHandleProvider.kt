package com.devshady.devtube.presentation.player

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.media3.common.Player

/**
 * Interface to provide a handle to the underlying media player for UI rendering.
 */
interface PlayerHandleProvider {
    fun getPlayer(): Player?
}

/**
 * CompositionLocal to provide the player handle throughout the UI tree.
 */
val LocalPlayerHandleProvider = staticCompositionLocalOf<PlayerHandleProvider> {
    error("No PlayerHandleProvider provided")
}
