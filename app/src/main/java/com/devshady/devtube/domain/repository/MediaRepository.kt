package com.devshady.devtube.domain.repository

import com.devshady.devtube.domain.model.DomainMediaItem

/**
 * Repository for fetching media metadata.
 */
interface MediaRepository {
    suspend fun getMediaItem(id: String): DomainMediaItem?
}
