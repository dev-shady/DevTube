package com.devshady.devtube.data.extractor

import com.devshady.devtube.domain.model.MediaSourceType
import com.devshady.devtube.domain.playback.MediaStreamExtractor
import javax.inject.Inject
import javax.inject.Provider

class StreamExtractorFactory @Inject constructor(
    private val mockExtractor: Provider<MockStreamExtractor>
) {
    fun getExtractor(type: MediaSourceType): MediaStreamExtractor {
        return when (type) {
            MediaSourceType.YOUTUBE, MediaSourceType.YOUTUBE_MUSIC -> mockExtractor.get()
            else -> mockExtractor.get() // Fallback to mock
        }
    }
}
