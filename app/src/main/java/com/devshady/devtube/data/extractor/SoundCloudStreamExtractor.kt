package com.devshady.devtube.data.extractor

import android.util.Log
import com.devshady.devtube.domain.playback.MediaStreamExtractor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.schabi.newpipe.extractor.ServiceList
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SoundCloudStreamExtractor @Inject constructor() : MediaStreamExtractor {

    override suspend fun extractPlayableUri(mediaId: String, isAudioOnly: Boolean): String =
        withContext(Dispatchers.IO) {
            try {
                // 1. Target NewPipe's native SoundCloud extraction pipeline
                val service = ServiceList.SoundCloud
                val extractor = service.getStreamExtractor(mediaId)

                // 2. Execute the direct manifest node scrape
                extractor.fetchPage()

                // 3. Extract the premium quality audio asset streaming path
                val optimalStream = extractor.audioStreams.maxByOrNull { it.bitrate }

                return@withContext optimalStream?.url
                    ?: throw IllegalStateException("No suitable SoundCloud audio track found")

            } catch (e: Exception) {
                Log.e("DevTubeExtractor", "SoundCloud extraction critical failure", e)
                throw Exception("SoundCloud extraction failed: ${e.message}", e)
            }
        }
}
