package com.devshady.devtube.data.extractor

import com.devshady.devtube.domain.playback.MediaStreamExtractor
import javax.inject.Inject

class MockStreamExtractor @Inject constructor() : MediaStreamExtractor {
    override suspend fun extractPlayableUri(mediaId: String, isAudioOnly: Boolean): String {
        return if (isAudioOnly) {
            "https://storage.googleapis.com/exoplayer-test-media-0/play.mp3"
        } else {
            // Stable MP4 test stream from Google
            "https://storage.googleapis.com/exoplayer-test-media-0/BigBuckBunny_320x180.mp4"
        }
    }
}
