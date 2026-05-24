package com.devshady.devtube

import android.app.Application
import com.devshady.devtube.data.extractor.OkHttpDownloader
import dagger.hilt.android.HiltAndroidApp
import okhttp3.OkHttpClient
import org.schabi.newpipe.extractor.NewPipe
import org.schabi.newpipe.extractor.localization.Localization

@HiltAndroidApp
class DevTubeApp : Application() {
    override fun onCreate() {
        super.onCreate()
        // Initialize NewPipe Extractor with optimized client
        val client = OkHttpClient.Builder()
            .followRedirects(true)
            .followSslRedirects(true)
            .build()
        NewPipe.init(OkHttpDownloader(client), Localization.DEFAULT)
    }
}
