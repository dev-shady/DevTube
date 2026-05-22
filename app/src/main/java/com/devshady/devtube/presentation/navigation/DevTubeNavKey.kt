package com.devshady.devtube.presentation.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

sealed interface DevTubeNavKey : NavKey {
    @Serializable
    data object Player : DevTubeNavKey
}
