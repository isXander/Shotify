package dev.isxander.shotify.share

import dev.isxander.shotify.util.Screenshot

sealed interface ShareTask {
    fun share(screenshot: Screenshot): String
}
