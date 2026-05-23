package com.devshady.devtube.data.repository

import com.devshady.devtube.domain.model.DomainMediaItem
import com.devshady.devtube.domain.repository.MediaRepository
import com.devshady.devtube.domain.playback.MediaUrlParser
import com.devshady.devtube.data.extractor.StreamExtractorFactory
import com.devshady.devtube.domain.model.MediaSourceType
import javax.inject.Inject

class MediaRepositoryImpl @Inject constructor(
    private val urlParsers: Set<@JvmSuppressWildcards MediaUrlParser>,
    private val extractorFactory: StreamExtractorFactory
) : MediaRepository {

    override suspend fun getMediaItem(id: String): DomainMediaItem? {
        val parser = urlParsers.find { it.canHandle(id) }
        val sourceType = parser?.parseSourceType(id) ?: MediaSourceType.UNKNOWN
        val isAudioOnly = sourceType == MediaSourceType.YOUTUBE_MUSIC
        
        return DomainMediaItem(
            id = id,
            title = "Mock Title for $id",
            artist = "Mock Artist",
            artworkUrl = "https://picsum.photos/seed/$id/400/400",
            isVideo = !isAudioOnly
        )
    }
}
