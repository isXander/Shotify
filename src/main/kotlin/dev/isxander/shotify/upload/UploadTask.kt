package dev.isxander.shotify.upload

import dev.isxander.shotify.util.Screenshot

sealed interface UploadTask {
    fun upload(screenshot: Screenshot): Screenshot
}
