package com.devshady.devtube

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation3.ui.NavDisplay
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.NavEntry
import com.devshady.devtube.presentation.navigation.DevTubeNavKey
import com.devshady.devtube.presentation.player.LocalPlayerHandleProvider
import com.devshady.devtube.presentation.player.PlayerHandleProvider
import com.devshady.devtube.presentation.player.PlayerScreen
import com.devshady.devtube.ui.theme.DevTubeTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var playerHandleProvider: PlayerHandleProvider

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        android.util.Log.d("MainActivity", "playerHandleProvider injected: ${::playerHandleProvider.isInitialized}")
        enableEdgeToEdge()
        setContent {
            DevTubeTheme {
                CompositionLocalProvider(LocalPlayerHandleProvider provides playerHandleProvider) {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        PlayerScreen()
                    }
                }
            }
        }
    }
}
