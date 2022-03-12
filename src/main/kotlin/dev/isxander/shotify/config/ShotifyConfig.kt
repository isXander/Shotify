package dev.isxander.shotify.config

import dev.isxander.shotify.util.client
import dev.isxander.settxi.clothconfig.SettxiGuiWrapper
import dev.isxander.settxi.impl.boolean
import net.minecraft.text.TranslatableText
import java.io.File

object ShotifyConfig : SettxiGuiWrapper(TranslatableText("shotify.config.title"), File(client.runDirectory, "config/shotify.json")) {
    private const val UPLOAD_CATEGORY = "shotify.config.category.upload"

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
}
