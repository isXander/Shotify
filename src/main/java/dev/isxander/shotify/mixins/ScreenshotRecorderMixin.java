package dev.isxander.shotify.mixins;

import dev.isxander.shotify.Shotify;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.util.ScreenshotRecorder;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.File;
import java.util.function.Consumer;

@Mixin(ScreenshotRecorder.class)
public class ScreenshotRecorderMixin {
    // show preview and upload to imgur
    @Inject(method = "method_1661", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/texture/NativeImage;writeTo(Ljava/io/File;)V", shift = At.Shift.AFTER))
    private static void onFileWrite(NativeImage nativeImage, File file, Consumer<Text> consumer, CallbackInfo ci) {
        Shotify.INSTANCE.handleScreenshot(nativeImage, file);
    }

    @ModifyArg(method = "method_1661", at = @At(value = "INVOKE", target = "Ljava/util/function/Consumer;accept(Ljava/lang/Object;)V"))
    private static Object acceptText(Object t) {
        // generics are lost so we need to return Text
        return Shotify.INSTANCE.getScreenshotText();
    }
}
