package com.devshady.devtube.presentation.player

import androidx.media3.common.Player

/**
 * Interface to provide a handle to the underlying media player for UI rendering.
 * This is implemented by the playback controller and used by the UI layer (ViewModel/Screen).
 */
interface PlayerHandleProvider {
    fun getPlayer(): Player?
}
