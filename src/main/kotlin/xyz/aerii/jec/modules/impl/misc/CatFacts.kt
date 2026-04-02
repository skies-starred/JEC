package xyz.aerii.jec.modules.impl.misc

import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.HoverEvent
import net.minecraft.network.chat.Style
import xyz.aerii.jec.annotations.Load
import xyz.aerii.jec.config.categories.MiscCategory
import xyz.aerii.jec.handlers.Beacon
import xyz.aerii.jec.handlers.Chronos
import xyz.aerii.jec.modules.Module
import xyz.aerii.jec.utils.lie
import kotlin.time.Duration.Companion.minutes

@Load
object CatFacts : Module(MiscCategory.randomCatFact) {
    private var task: Chronos.Task? = null

    init {
        task = Chronos.Time every MiscCategory.randomCatFactDelay.value.minutes repeat ::fn

        MiscCategory.randomCatFactDelay.onChange {
            task?.cancel()
            task = Chronos.Time every it.minutes repeat ::fn
        }

        ClientCommandRegistrationCallback.EVENT.register { d, _ ->
            d.register(literal("jec").then(literal("fact").executes {
                fn()
                1
            }))
        }
    }

    private fun fn() {
        Beacon.get("https://catfact.ninja/fact") {
            onJsonSuccess {
                Component.literal("§d[JEC] §f${it["fact"].asString}").withStyle(Style.EMPTY.withHoverEvent(HoverEvent.ShowText(Component.literal("§dRandom cat fact! Delivered to you every §c${MiscCategory.randomCatFactDelay.value}§d minutes :3")))).lie()
            }
        }
    }
}