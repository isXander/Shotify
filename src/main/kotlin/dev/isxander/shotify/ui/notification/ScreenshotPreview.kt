package dev.isxander.shotify.ui.notification

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

    fun init() {
        HudRenderCallback.EVENT.register { matrices, _ -> window.draw(UMatrixStack(matrices)) }
    }
}
