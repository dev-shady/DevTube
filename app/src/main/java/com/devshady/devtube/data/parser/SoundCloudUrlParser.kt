package com.devshady.devtube.data.parser

import com.devshady.devtube.domain.model.MediaSourceType
import com.devshady.devtube.domain.playback.MediaUrlParser
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SoundCloudUrlParser @Inject constructor() : MediaUrlParser {

    companion object {
        const val URL = "soundcloud.com"
    }
    override fun canHandle(url: String): Boolean {
        return url.contains(URL)
    }

    override fun parseSourceType(url: String): MediaSourceType {
        return when {
            url.contains(URL) -> MediaSourceType.SOUNDCLOUD
            else -> MediaSourceType.UNKNOWN
        }
    }
}
