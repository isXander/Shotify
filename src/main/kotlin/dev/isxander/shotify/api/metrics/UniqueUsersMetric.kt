package dev.isxander.shotify.api.metrics

import com.mojang.authlib.minecraft.UserApiService
import dev.isxander.shotify.Shotify
import dev.isxander.shotify.mixins.AccessorMinecraftClient
import dev.isxander.shotify.util.client
import dev.isxander.shotify.util.logger
import net.fabricmc.loader.api.FabricLoader
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse.BodyHandlers

object UniqueUsersMetric {
    fun url(uuid: String) =
        URI("https://api.isxander.dev/metric/put/shotify?type=unique_users&uuid=$uuid")

    fun putApi() {
        if ((client as AccessorMinecraftClient).userApiService == UserApiService.OFFLINE) {
            if (!FabricLoader.getInstance().isDevelopmentEnvironment)
                logger.warn("Looks like you have cracked minecraft or have no internet connection!")
            return
        }

        try {
            val httpClient = HttpClient.newHttpClient()
            val request = HttpRequest.newBuilder(url(client.session.profile.id.toString())).apply {
                header("User-Agent", "Shotify/${Shotify.VERSION}")
                header("accept", "application/json")
            }.build()

            val response = httpClient.send(request, BodyHandlers.ofString())
            if (response.statusCode() != 200) {
                logger.error("Failed to put unique users metric: ${response.body()}")
            } else {
                logger.info("Successfully put unique users metric")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
