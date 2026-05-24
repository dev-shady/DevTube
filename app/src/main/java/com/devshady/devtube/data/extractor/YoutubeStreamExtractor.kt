package com.devshady.devtube.data.extractor

import android.util.Log
import com.devshady.devtube.domain.playback.MediaStreamExtractor
import org.schabi.newpipe.extractor.ServiceList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class YoutubeStreamExtractor @Inject constructor() : MediaStreamExtractor {
    override suspend fun extractPlayableUri(mediaId: String, isAudioOnly: Boolean): String {
        return withContext(Dispatchers.IO) {
            Log.d("aamku YoutubeStreamExtractor", "Extracting for $mediaId, audioOnly: $isAudioOnly")
            try {
                val service = ServiceList.YouTube
                val extractor = service.getStreamExtractor(mediaId)
                extractor.fetchPage()
                
                val stream = if (isAudioOnly) {
                    extractor.audioStreams.maxByOrNull { it.bitrate }
                } else {
                    // Get best quality video stream with audio if possible
                    extractor.videoStreams.maxByOrNull { it.bitrate }
                }
                
                val url = stream?.url ?: throw Exception("No suitable stream found")
                Log.d("aamku YoutubeStreamExtractor", "Successfully extracted URL: $url")
                url
            } catch (e: Exception) {
                Log.e("aamku YoutubeStreamExtractor", "Failed to extract URL for $mediaId", e)
                throw Exception("Stream extraction crashed completely: ${e.message}", e)

            }
        }
    }
}
