package dev.isxander.shotify.integrations

import com.terraformersmc.modmenu.api.ConfigScreenFactory
import com.terraformersmc.modmenu.api.ModMenuApi
import dev.isxander.shotify.config.ShotifyConfig

object ModMenuIntegration : ModMenuApi {
    override fun getModConfigScreenFactory() = ConfigScreenFactory {
        ShotifyConfig.clothGui(it)
    }
}
