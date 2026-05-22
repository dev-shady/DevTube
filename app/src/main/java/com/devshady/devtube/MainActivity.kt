package com.devshady.devtube

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation3.ui.NavDisplay
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.NavEntry
import com.devshady.devtube.presentation.navigation.DevTubeNavKey
import com.devshady.devtube.presentation.player.PlayerScreen
import com.devshady.devtube.ui.theme.DevTubeTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DevTubeTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val backStack = rememberNavBackStack(DevTubeNavKey.Player)
                    val entryProvider: (NavKey) -> NavEntry<NavKey> = entryProvider {
                        entry<DevTubeNavKey.Player> {
                            PlayerScreen()
                        }
                    }

                    NavDisplay(
                        backStack = backStack,
                        entryProvider = entryProvider,
                        onBack = { if (backStack.size > 1) backStack.removeAt(backStack.size - 1) }
                    )
                }
            }
        }
    }
}
