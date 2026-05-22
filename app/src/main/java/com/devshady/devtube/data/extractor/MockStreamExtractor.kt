package com.devshady.devtube.data.extractor

import com.devshady.devtube.domain.playback.MediaStreamExtractor
import javax.inject.Inject

class MockStreamExtractor @Inject constructor() : MediaStreamExtractor {
    override suspend fun extractPlayableUri(mediaId: String, isAudioOnly: Boolean): String {
        return if (isAudioOnly) {
            "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-1.mp3"
        } else {
            "https://bitdash-a.akamaihd.net/content/sintel/hls/playlist.m3u8"
        }
    }
}
