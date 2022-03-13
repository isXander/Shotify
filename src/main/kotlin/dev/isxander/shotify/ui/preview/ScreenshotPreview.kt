package dev.isxander.shotify.ui.preview

import dev.isxander.shotify.config.ShotifyConfig
import dev.isxander.shotify.util.Screenshot
import gg.essential.elementa.ElementaVersion
import gg.essential.elementa.components.Window
import gg.essential.elementa.dsl.childOf
import gg.essential.elementa.dsl.provideDelegate
import gg.essential.universal.UMatrixStack
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback

object ScreenshotPreview {
    private val window = Window(ElementaVersion.V1)

    fun addScreenshot(screenshot: Screenshot) {
        val screenshotComponent by ScreenshotComponent(screenshot)
        screenshotComponent childOf window
        screenshotComponent.startAnimation()
    }

    fun clear() {
        window.children.clear()
    }

    fun init() {
        HudRenderCallback.EVENT.register { matrices, _ ->
            if (ShotifyConfig.renderPreview)
                window.draw(UMatrixStack(matrices))
        }
    }
}
