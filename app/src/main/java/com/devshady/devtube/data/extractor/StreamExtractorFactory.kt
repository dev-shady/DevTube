package com.devshady.devtube.data.extractor

import android.util.Log
import com.devshady.devtube.domain.model.MediaSourceType
import com.devshady.devtube.domain.playback.MediaStreamExtractor
import javax.inject.Inject
import javax.inject.Provider

class StreamExtractorFactory @Inject constructor(
    private val mockExtractor: Provider<MockStreamExtractor>,
    private val youtubeExtractor: Provider<YoutubeStreamExtractor>,
    private val soundcloudExtractor: Provider<SoundCloudStreamExtractor>
) {
    fun getExtractor(type: MediaSourceType): MediaStreamExtractor {
        Log.d("StreamExtractorFactory", "${type}")
        return when (type) {
            MediaSourceType.YOUTUBE, MediaSourceType.YOUTUBE_MUSIC -> youtubeExtractor.get()
            MediaSourceType.SOUNDCLOUD -> soundcloudExtractor.get()
            else -> mockExtractor.get()
        }
    }
}
