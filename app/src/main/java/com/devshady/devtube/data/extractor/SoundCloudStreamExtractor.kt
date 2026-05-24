package com.devshady.devtube.data.extractor

import android.util.Log
import com.devshady.devtube.domain.model.DomainMediaItem
import com.devshady.devtube.domain.playback.MediaStreamExtractor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.schabi.newpipe.extractor.ServiceList
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SoundCloudStreamExtractor @Inject constructor() : MediaStreamExtractor {

    override suspend fun extractPlayableUri(mediaId: String, isAudioOnly: Boolean): DomainMediaItem =
        withContext(Dispatchers.IO) {
            try {
                // 1. Target NewPipe's native SoundCloud extraction pipeline
                val service = ServiceList.SoundCloud
                val extractor = service.getStreamExtractor(mediaId)

                // 2. Execute the direct manifest node scrape
                extractor.fetchPage()

                // 3. Extract the premium quality audio asset streaming path
                val optimalStream = extractor.audioStreams.maxByOrNull { it.bitrate }
                val url = optimalStream?.url ?: throw Exception("No suitable stream found")

                val thumbnails = extractor.thumbnails
                val artwork = thumbnails?.maxByOrNull { it.width * it.height }?.url
                    ?: thumbnails?.lastOrNull()?.url
                    ?: "https://picsum.photos/seed/${mediaId.hashCode()}/400/400"

                return@withContext DomainMediaItem(
                    id = mediaId,
                    title = extractor.name ?: "Unknown Title",
                    artist = extractor.uploaderName ?: "Unknown Artist",
                    artworkUrl = artwork,
                    isVideo = false, // SoundCloud is audio-only in our UI logic
                    resolvedStreamingUrl = url
                )
            } catch (e: Exception) {
                Log.e("DevTubeExtractor", "SoundCloud extraction critical failure", e)
                throw Exception("SoundCloud extraction failed: ${e.message}", e)
            }
        }
}
