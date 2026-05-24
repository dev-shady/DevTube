package com.devshady.devtube.data.extractor

import com.devshady.devtube.domain.model.MediaSourceType
import com.devshady.devtube.domain.playback.MediaStreamExtractor
import javax.inject.Inject
import javax.inject.Provider

class StreamExtractorFactory @Inject constructor(
    private val mockExtractor: Provider<MockStreamExtractor>,
    private val youtubeExtractor: Provider<YoutubeStreamExtractor>
) {
    fun getExtractor(type: MediaSourceType): MediaStreamExtractor {
        return when (type) {
            MediaSourceType.YOUTUBE, MediaSourceType.YOUTUBE_MUSIC -> youtubeExtractor.get()
            else -> mockExtractor.get()
        }
    }
}
