package com.devshady.devtube.data.parser

import com.devshady.devtube.domain.model.MediaSourceType
import com.devshady.devtube.domain.playback.MediaUrlParser
import javax.inject.Inject

class YouTubeUrlParser @Inject constructor() : MediaUrlParser {
    override fun canHandle(url: String): Boolean {
        return url.contains("youtube.com") || url.contains("youtu.be")
    }

    override fun parseSourceType(url: String): MediaSourceType {
        return when {
            url.contains("music.youtube.com") -> MediaSourceType.YOUTUBE_MUSIC
            url.contains("youtube.com") || url.contains("youtu.be") -> MediaSourceType.YOUTUBE
            else -> MediaSourceType.UNKNOWN
        }
    }
}
