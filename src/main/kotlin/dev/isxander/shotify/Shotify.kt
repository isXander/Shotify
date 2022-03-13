package dev.isxander.shotify

import dev.isxander.shotify.api.metrics.UniqueUsersMetric
import dev.isxander.shotify.api.updater.ShotifyUpdateChecker
import dev.isxander.shotify.config.ShotifyConfig
import dev.isxander.shotify.share.TwitterShareTask
import dev.isxander.shotify.ui.preview.ScreenshotPreview
import dev.isxander.shotify.upload.ImgurUploadTask
import dev.isxander.shotify.util.Screenshot
import dev.isxander.shotify.util.logger
import gg.essential.universal.UDesktop
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.networking.v1.ClientLoginConnectionEvents
import net.fabricmc.loader.api.FabricLoader
import net.minecraft.client.texture.NativeImage
import net.minecraft.network.MessageType
import net.minecraft.text.ClickEvent
import net.minecraft.text.LiteralText
import net.minecraft.text.Text
import net.minecraft.text.TranslatableText
import net.minecraft.util.Formatting
import org.bundleproject.libversion.Version
import java.io.File
import java.util.*

object Shotify : ClientModInitializer {
    val VERSION = Version.of(FabricLoader.getInstance().getModContainer("shotify").get().metadata.version.friendlyString)

    var currentScreenshot: Screenshot? = null
    var latestVersion: Version? = null

    override fun onInitializeClient() {
        ShotifyConfig.load()
        ScreenshotPreview.init()

        ClientLoginConnectionEvents.INIT.register { handler, client ->
            if (latestVersion != null) {
                val versionString = latestVersion.toString()
                val updateUrl = "https://modrinth.com/mod/shotify/version/$versionString"
                val text = TranslatableText("shotify.update.available", versionString).formatted(Formatting.RED).append(
                    LiteralText(updateUrl).formatted(Formatting.UNDERLINE, Formatting.BLUE).styled { it.withClickEvent(ClickEvent(ClickEvent.Action.OPEN_URL, updateUrl)) })

                client.inGameHud.addChatMessage(MessageType.CHAT, text, UUID.randomUUID())
                latestVersion = null
            }
        }

        Thread {
            UniqueUsersMetric.putApi()
            latestVersion = ShotifyUpdateChecker.getLatestVersion()?.takeIf { it > VERSION }
        }.apply {
            name = "shotify-concurrent"
            start()
        }

        logger.info("Successfully loaded Shotify $VERSION")
    }

    fun handleScreenshot(nativeImage: NativeImage, file: File) {
        var screenshot = Screenshot(nativeImage, file)

        if (ShotifyConfig.uploadToImgur)
            screenshot = ImgurUploadTask.upload(screenshot)
        if (ShotifyConfig.copyUploadedUrlToClipboard)
            screenshot.url?.let { UDesktop.setClipboardString(it.toString()) }

        currentScreenshot = screenshot
        ScreenshotPreview.addScreenshot(screenshot)
    }

    fun getScreenshotText(): Text? {
        if (currentScreenshot == null)
            return null

        val openFolder = TranslatableText("shotify.text.open_folder").apply {
            formatted(Formatting.BOLD, Formatting.RED)
            styled {
                it.withClickEvent(ClickEvent(ClickEvent.Action.OPEN_FILE, currentScreenshot!!.file.parentFile.path))
            }
        }
        val openImage = TranslatableText("shotify.text.open_image").apply {
            formatted(Formatting.BOLD, Formatting.BLUE)
            styled {
                it.withClickEvent(ClickEvent(ClickEvent.Action.OPEN_FILE, currentScreenshot!!.file.path))
            }
        }
        val copyUrlToClipboard = TranslatableText("shotify.text.copy_url").apply {
            formatted(Formatting.BOLD, Formatting.GREEN)
            styled {
                it.withClickEvent(ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, currentScreenshot!!.url?.toString()))
            }
        }
        val shareTwitter = TranslatableText("shotify.text.share_twitter").apply {
            formatted(Formatting.BOLD, Formatting.AQUA)
            styled {
                it.withClickEvent(ClickEvent(ClickEvent.Action.OPEN_URL, TwitterShareTask.share(currentScreenshot!!)))
            }
        }

        return TranslatableText("shotify.text.content").apply {
            formatted(Formatting.YELLOW).append(" ")
            append(openFolder).append(" ")
            append(openImage).append(" ")
            if (!ShotifyConfig.copyUploadedUrlToClipboard)
                append(copyUrlToClipboard).append(" ")
            append(shareTwitter)
        }
    }
}
