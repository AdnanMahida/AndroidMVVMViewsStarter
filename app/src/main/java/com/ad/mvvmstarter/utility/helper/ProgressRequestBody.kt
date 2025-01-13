package com.ad.mvvmstarter.utility.helper

import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okio.BufferedSink
import java.io.File

/**
 * Used for calculate progress
 * while using okhttp3.MediaType
 * */
class ProgressRequestBody(
    private val file: File,
    private val contentType: String,
    private val progressCallback: (uploadedBytes: Long, totalBytes: Long) -> Unit
) : RequestBody() {

    override fun contentType(): MediaType? {
        return contentType.toMediaTypeOrNull()
    }

    override fun contentLength(): Long {
        return file.length()
    }

    override fun writeTo(sink: BufferedSink) {
        val buffer = ByteArray(DEFAULT_BUFFER_SIZE)
        val fileLength = contentLength()
        var uploaded = 0L

        file.inputStream().use { inputStream ->
            var read: Int
            while (inputStream.read(buffer).also { read = it } != -1) {
                sink.write(buffer, 0, read)
                uploaded += read
                sink.flush() // Ensure data is written to the network
                progressCallback(uploaded, fileLength)
            }
        }
    }

    companion object {
        private const val DEFAULT_BUFFER_SIZE = 8192 // 8 KB
    }
}
