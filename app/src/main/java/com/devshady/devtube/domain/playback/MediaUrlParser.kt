package com.devshady.devtube.domain.playback

import com.devshady.devtube.domain.model.MediaSourceType

/**
 * Interface for parsing media URLs to determine their source type.
 */
interface MediaUrlParser {
    fun canHandle(url: String): Boolean
    fun parseSourceType(url: String): MediaSourceType
}
