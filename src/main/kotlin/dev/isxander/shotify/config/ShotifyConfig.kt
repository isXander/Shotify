package dev.isxander.shotify.config

import dev.isxander.shotify.util.client
import dev.isxander.settxi.clothconfig.SettxiGuiWrapper
import dev.isxander.settxi.impl.OptionContainer
import dev.isxander.settxi.impl.boolean
import dev.isxander.settxi.impl.int
import dev.isxander.settxi.impl.option
import dev.isxander.shotify.Shotify
import dev.isxander.shotify.ui.preview.ScreenshotPreview
import net.minecraft.text.TranslatableText
import java.io.File

object ShotifyConfig : SettxiGuiWrapper(TranslatableText("shotify.config.title"), File(client.runDirectory, "config/shotify.json")) {
    private const val RENDERING_CATEGORY = "shotify.config.category.rendering"
    private const val UPLOAD_CATEGORY = "shotify.config.category.upload"

    var renderPreview by boolean(true) {
        name = "shotify.config.render_preview.name"
        category = RENDERING_CATEGORY
        description = "shotify.config.render_preview.description"

        set {
            if (!it) {
                ScreenshotPreview.clear()
                Shotify.currentScreenshot = null
            }
            it
        }
    }

    var previewDirection by option(PreviewDirection.BottomRight) {
        name = "shotify.config.preview_direction.name"
        category = RENDERING_CATEGORY
        description = "shotify.config.preview_direction.description"
    }

    var previewTime by int(5) {
        name = "shotify.config.preview_time.name"
        category = RENDERING_CATEGORY
        description = "shotify.config.preview_time.description"
        range = 1..10
    }

    var uploadToImgur by boolean(true) {
        name = "shotify.config.upload_imgur.name"
        category = UPLOAD_CATEGORY
        description = "shotify.config.upload_imgur.description"
    }

    var copyUploadedUrlToClipboard by boolean(true) {
        name = "shotify.config.copy_upload_clipboard.name"
        category = UPLOAD_CATEGORY
        description = "shotify.config.copy_upload_clipboard.description"
    }

    object PreviewDirection : OptionContainer() {
        val TopLeft = option("shotify.config.preview_direction.top_left")
        val TopRight = option("shotify.config.preview_direction.top_right")
        val BottomLeft = option("shotify.config.preview_direction.bottom_left")
        val BottomRight = option("shotify.config.preview_direction.bottom_right")
    }
}
