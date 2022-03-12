package dev.isxander.shotify.util

import gg.essential.elementa.components.UIImage
import net.minecraft.client.texture.NativeImage
import java.io.ByteArrayInputStream
import java.util.concurrent.CompletableFuture
import javax.imageio.ImageIO

fun UIImage.Companion.ofNativeImage(nativeImage: NativeImage): UIImage {
    val bufferedImage = ImageIO.read(ByteArrayInputStream(nativeImage.bytes))
    return UIImage(CompletableFuture.completedFuture(bufferedImage))
}
