package com.devshady.devtube.data.extractor

import android.util.Log
import com.devshady.devtube.domain.model.DomainMediaItem
import com.devshady.devtube.domain.playback.MediaStreamExtractor
import org.schabi.newpipe.extractor.ServiceList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class YoutubeStreamExtractor @Inject constructor() : MediaStreamExtractor {
    override suspend fun extractPlayableUri(mediaId: String, isAudioOnly: Boolean): DomainMediaItem {
        return withContext(Dispatchers.IO) {
            Log.d("YoutubeStreamExtractor", "Extracting for $mediaId, audioOnly: $isAudioOnly")
            try {
                val service = ServiceList.YouTube
                val extractor = service.getStreamExtractor(mediaId)
                extractor.fetchPage()
                
                val stream = if (isAudioOnly) {
                    extractor.audioStreams.maxByOrNull { it.bitrate }
                } else {
                    // Get the best quality video stream with audio if possible
                    extractor.videoStreams.maxByOrNull { it.bitrate }
                }
                
                val url = stream?.url ?: throw Exception("No suitable stream found")
                Log.d("YoutubeStreamExtractor", "Successfully extracted URL: $url")
                return@withContext DomainMediaItem(
                    id = mediaId,
                    title = extractor.name ?: "Unknown Title",
                    artist = extractor.uploaderName ?: "Unknown Artist",
                    artworkUrl = extractor.thumbnails.firstOrNull()?.url ?: "https://picsum.photos",
                    isVideo = !isAudioOnly,
                    resolvedStreamingUrl = url // Embedded directly into your domain model configuration
                )
            } catch (e: Exception) {
                Log.e("YoutubeStreamExtractor", "Failed to extract URL for $mediaId", e)
                throw Exception("Stream extraction crashed completely: ${e.message}", e)

            }
        }
    }
}
