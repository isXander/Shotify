package dev.isxander.shotify.api.updater

import dev.isxander.shotify.Shotify
import dev.isxander.shotify.util.logger
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import net.minecraft.SharedConstants
import org.bundleproject.libversion.Version
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import kotlinx.serialization.Serializable

object ShotifyUpdateChecker {
    private val url = URI("https://api.isxander.dev/updater/latest/shotify?loader=fabric&minecraft=${SharedConstants.getGameVersion().name}")

    fun getLatestVersion(): Version? {
        val httpClient = HttpClient.newHttpClient()
        val request = HttpRequest.newBuilder(url).apply {
            header("User-Agent", "Shotify/${Shotify.VERSION}")
            header("accept", "application/json")
        }.build()

        val response = httpClient.send(request, HttpResponse.BodyHandlers.ofString())
        val json = Json.decodeFromString<VersionResponse>(response.body())

        if (!json.success)
            logger.error("Failed to get latest version from $url: ${json.error}")

        return json.version?.also { logger.info("Latest version is $it") }
    }

    @Serializable
    data class VersionResponse(val success: Boolean, val version: Version? = null, val error: String? = null)
}
