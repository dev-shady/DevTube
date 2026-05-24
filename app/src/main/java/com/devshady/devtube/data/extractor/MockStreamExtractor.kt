package com.devshady.devtube.data.extractor

import com.devshady.devtube.domain.model.DomainMediaItem
import com.devshady.devtube.domain.playback.MediaStreamExtractor
import javax.inject.Inject

class MockStreamExtractor @Inject constructor() : MediaStreamExtractor {
    override suspend fun extractPlayableUri(mediaId: String, isAudioOnly: Boolean): DomainMediaItem {
        return if (isAudioOnly) {
             DomainMediaItem(
                id = mediaId,
                title = "Mock Audio ($mediaId)",
                artist = "Mock Artist",
                artworkUrl = "https://picsum.photos/seed/${mediaId.hashCode()}/400/400",
                isVideo = false, 
                resolvedStreamingUrl = "https://storage.googleapis.com/exoplayer-test-media-0/play.mp3"
            )
        } else {
            DomainMediaItem(
                id = mediaId,
                title = "Mock Video ($mediaId)",
                artist = "Mock Artist",
                artworkUrl = "https://picsum.photos/seed/${mediaId.hashCode()}/400/400",
                isVideo = true,
                resolvedStreamingUrl = "https://storage.googleapis.com/exoplayer-test-media-0/BigBuckBunny_320x180.mp4"
            )
        }
    }
}
