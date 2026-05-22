package com.devshady.devtube.domain.model

/**
 * Domain representation of a media item in DevTube.
 */
data class DomainMediaItem(
    val id: String,
    val title: String,
    val artist: String,
    val artworkUrl: String?,
    val isVideo: Boolean
)
