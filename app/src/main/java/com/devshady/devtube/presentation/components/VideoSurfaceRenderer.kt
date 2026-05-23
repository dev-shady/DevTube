package com.devshady.devtube.presentation.components

import androidx.annotation.OptIn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.util.UnstableApi
import androidx.media3.ui.PlayerView
import com.devshady.devtube.presentation.player.LocalPlayerHandleProvider

@OptIn(UnstableApi::class)
@Composable
fun VideoSurfaceRenderer(
    modifier: Modifier = Modifier
) {
    val provider = LocalPlayerHandleProvider.current
    
    AndroidView(
        factory = { context ->
            PlayerView(context).apply {
                useController = false
                player = provider.getPlayer()
            }
        },
        update = { playerView ->
            val currentPlayer = provider.getPlayer()
            if (playerView.player != currentPlayer) {
                playerView.player = currentPlayer
            }
        },
        modifier = modifier,
        onRelease = { playerView ->
            playerView.player = null
        }
    )
}
