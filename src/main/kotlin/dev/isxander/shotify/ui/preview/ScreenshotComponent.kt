package dev.isxander.shotify.ui.preview

import dev.isxander.shotify.Shotify
import dev.isxander.shotify.config.ShotifyConfig
import dev.isxander.shotify.util.Screenshot
import dev.isxander.shotify.util.ofNativeImage
import gg.essential.elementa.components.UIBlock
import gg.essential.elementa.components.UIContainer
import gg.essential.elementa.components.UIImage
import gg.essential.elementa.constraints.ChildBasedMaxSizeConstraint
import gg.essential.elementa.constraints.ImageAspectConstraint
import gg.essential.elementa.constraints.animation.Animations
import gg.essential.elementa.dsl.*
import gg.essential.elementa.effects.OutlineEffect
import java.awt.Color

class ScreenshotComponent(val screenshot: Screenshot) : UIContainer() {
    init {
        constrain {
            x = 0.percent()
            y = 0.percent()
            width = 100.percent()
            height = 100.percent()
        } effect OutlineEffect(Color.white, 1f)
    }

    val image by UIImage.ofNativeImage(screenshot.image).constrain {
        width = 100.percent()
        height = ImageAspectConstraint()
    } childOf this

    val flash by UIBlock(Color(255, 255, 255, 0)).constrain {
        width = 100.percent()
        height = 100.percent() boundTo image
    } childOf this

    fun startAnimation() {
        flash.animate {
            setColorAnimation(Animations.IN_SIN, 0.1f, Color(255, 255, 255, 200).toConstraint()).onComplete {
                flash.animate {
                    setColorAnimation(Animations.OUT_SIN, 0.1f, Color(255, 255, 255, 0).toConstraint()).onComplete {
                        collapseAnimation()
                    }
                }
            }
        }
    }

    private fun collapseAnimation() {
        animate {
            val animation = Animations.IN_OUT_SIN
            val time = 0.5f

            when (ShotifyConfig.previewDirection) {
                ShotifyConfig.PreviewDirection.TopLeft -> {
                    setXAnimation(animation, time, 2.percent())
                    setYAnimation(animation, time, basicYConstraint { 2.percent().getXPositionImpl(it) })
                }
                ShotifyConfig.PreviewDirection.TopRight -> {
                    setXAnimation(animation, time, 68.percent())
                    setYAnimation(animation, time, basicYConstraint { 2.percent().getXPositionImpl(it) })
                }
                ShotifyConfig.PreviewDirection.BottomLeft -> {
                    setXAnimation(animation, time, 2.percent())
                    setYAnimation(animation, time, 0.pixels(alignOpposite = true) - basicYConstraint { 2.percent().getXPositionImpl(it) })
                }
                ShotifyConfig.PreviewDirection.BottomRight -> {
                    setXAnimation(animation, time, 68.percent())
                    setYAnimation(animation, time, 0.pixels(alignOpposite = true) - basicYConstraint { 2.percent().getXPositionImpl(it) })
                }
            }

            setWidthAnimation(animation, time, 30.percent())
            setHeightAnimation(animation, time, ChildBasedMaxSizeConstraint())

            onComplete {
                animate {
                    val x = if (ShotifyConfig.previewDirection == ShotifyConfig.PreviewDirection.TopLeft || ShotifyConfig.previewDirection == ShotifyConfig.PreviewDirection.BottomLeft) {
                        (-30).percent()
                    } else {
                        100.percent()
                    }
                    setXAnimation(Animations.OUT_SIN, 0.5f, x, ShotifyConfig.previewTime.toFloat()).onComplete {
                        if (Shotify.currentScreenshot == screenshot) {
                            Shotify.currentScreenshot = null
                        }
                        hide()
                    }
                }
            }
        }
    }
}
