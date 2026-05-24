package com.devshady.devtube.data.extractor

import android.util.Log
import okhttp3.OkHttpClient
import okhttp3.RequestBody.Companion.toRequestBody
import org.schabi.newpipe.extractor.downloader.Downloader
import org.schabi.newpipe.extractor.downloader.Request
import org.schabi.newpipe.extractor.downloader.Response

class OkHttpDownloader(private val client: OkHttpClient) : Downloader() {

    override fun execute(request: Request): Response {
        val url = request.url()
        val method = request.httpMethod()
        val headers = request.headers()
        
        val data = try {
            val methodData = request.javaClass.getDeclaredMethod("data")
            methodData.isAccessible = true
            methodData.invoke(request) as? ByteArray
        } catch (e: Exception) {
            null
        }

        val body = when {
            data != null && data.isNotEmpty() -> data.toRequestBody()
            method == "POST" -> "".toRequestBody()
            else -> null
        }

        val okHttpRequestBuilder = okhttp3.Request.Builder()
            .url(url)
            .method(method, body)

        var hasUserAgent = false
        headers.forEach { (key, values) ->
            if (key.equals("user-agent", ignoreCase = true)) hasUserAgent = true
            okHttpRequestBuilder.addHeader(key, values.joinToString(", "))
        }
        
        if (!hasUserAgent) {
            okHttpRequestBuilder.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36")
        }
        
        okHttpRequestBuilder.addHeader("Accept-Language", "en-US,en;q=0.9")

        val okHttpResponse = client.newCall(okHttpRequestBuilder.build()).execute()
        val responseCode = okHttpResponse.code
        val responseMessage = okHttpResponse.message
        val responseBody = okHttpResponse.body?.string()
        val responseHeaders = okHttpResponse.headers.toMultimap()
        Log.d("aamku "," ${responseCode} message: ${responseMessage}")
        Log.d("aamku "," ${responseBody} headers: ${responseHeaders}")

        return Response(responseCode, responseMessage, responseHeaders, responseBody, url)
    }
}
