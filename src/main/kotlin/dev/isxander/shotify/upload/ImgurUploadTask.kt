package dev.isxander.shotify.upload

import dev.isxander.shotify.util.Screenshot
import java.net.URI
import java.net.URL
import java.net.URLEncoder
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpRequest.BodyPublishers
import java.net.http.HttpResponse.BodyHandlers
import java.util.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

object ImgurUploadTask : UploadTask {
    private const val imgurClientId = "fb714c908164e90"

    private val json = Json { ignoreUnknownKeys = true }

    override fun upload(screenshot: Screenshot): Screenshot {
        val base64 = Base64.getEncoder().encodeToString(screenshot.image.bytes)
        val form = "image=${URLEncoder.encode(base64, "UTF-8")}"

        val client = HttpClient.newHttpClient()
        val request = HttpRequest.newBuilder(URI("https://api.imgur.com/3/image")).apply {
            POST(BodyPublishers.ofString(form))
            header("Content-Type", "application/x-www-form-urlencoded")
            header("Authorization", "Client-ID $imgurClientId")
        }.build()

        val response = client.send(request, BodyHandlers.ofString())
        val json = json.decodeFromString<ImgurResponse>(response.body())

        if (json.success) {
            return Screenshot(screenshot.image, screenshot.file, URL(json.data.link))
        } else {
            throw RuntimeException("Failed to upload image to Imgur: ${json.status}")
        }
    }

    @Serializable
    data class ImgurResponse(val data: ImgurData, val success: Boolean, val status: Int)

    @Serializable
    data class ImgurData(val link: String)
}
