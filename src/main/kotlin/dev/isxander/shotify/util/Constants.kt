package dev.isxander.shotify.util

import net.minecraft.client.MinecraftClient
import org.slf4j.Logger
import org.slf4j.LoggerFactory

val client: MinecraftClient
    get() = MinecraftClient.getInstance()

val logger: Logger = LoggerFactory.getLogger("Shotify")
