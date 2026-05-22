package com.devshady.devtube.data.repository

import com.devshady.devtube.domain.model.DomainMediaItem
import com.devshady.devtube.domain.repository.MediaRepository
import com.devshady.devtube.data.parser.YouTubeUrlParser
import com.devshady.devtube.data.extractor.StreamExtractorFactory
import com.devshady.devtube.domain.model.MediaSourceType
import javax.inject.Inject

class MediaRepositoryImpl @Inject constructor(
    private val urlParser: YouTubeUrlParser,
    private val extractorFactory: StreamExtractorFactory
) : MediaRepository {

    override suspend fun getMediaItem(id: String): DomainMediaItem? {
        // In a real app, this would fetch metadata from an API.
        // For now, we return a mock item based on the ID/URL.
        val sourceType = urlParser.parseSourceType(id)
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
