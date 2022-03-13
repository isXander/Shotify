package dev.isxander.shotify.util

import gg.essential.elementa.components.UIImage
import gg.essential.elementa.components.image.ImageProvider
import gg.essential.universal.UMatrixStack
import net.minecraft.client.texture.NativeImage
import java.awt.Color
import java.io.ByteArrayInputStream
import java.util.concurrent.CompletableFuture
import javax.imageio.ImageIO

fun UIImage.Companion.ofNativeImage(nativeImage: NativeImage): UIImage {
    val bufferedImage = ImageIO.read(ByteArrayInputStream(nativeImage.bytes))
    return UIImage(CompletableFuture.completedFuture(bufferedImage), loadingImage = EmptyImageProvider)
}

object EmptyImageProvider : ImageProvider {
    override fun drawImage(
        matrixStack: UMatrixStack,
        x: Double,
        y: Double,
        width: Double,
        height: Double,
        color: Color
    ) {
        // just dont render anything
    }
}
