package dev.isxander.shotify.share

import dev.isxander.shotify.util.Screenshot
import net.minecraft.text.TranslatableText
import java.net.URLEncoder
import java.nio.charset.Charset

object TwitterShareTask : ShareTask {
    override fun share(screenshot: Screenshot): String {
        if (screenshot.url == null)
            throw IllegalArgumentException("Screenshot does not have a URL")

        return "https://twitter.com/intent/tweet?text=${
            URLEncoder.encode(
                TranslatableText("shotify.share.twitter.content").string,
                Charset.defaultCharset()
            )
        }&url=${screenshot.url}&hashtags=screenshotify,minecraft"
    }
}
