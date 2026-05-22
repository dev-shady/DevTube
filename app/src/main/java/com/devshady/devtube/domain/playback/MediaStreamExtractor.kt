package com.devshady.devtube.domain.playback

/**
 * Interface for extracting playable stream URIs from media IDs.
 */
interface MediaStreamExtractor {
    /**
     * Extracts a playable URI for the given media ID.
     * @param mediaId The ID of the media.
     * @param isAudioOnly Whether to extract only the audio stream.
     * @return The playable URI.
     */
    suspend fun extractPlayableUri(mediaId: String, isAudioOnly: Boolean): String
}
