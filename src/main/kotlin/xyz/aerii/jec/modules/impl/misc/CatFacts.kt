package xyz.aerii.jec.modules.impl.misc

import com.google.gson.JsonObject
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback
import xyz.aerii.jec.annotations.Load
import xyz.aerii.jec.config.categories.MiscCategory
import xyz.aerii.jec.handlers.Beacon.request
import xyz.aerii.jec.handlers.Chronos
import xyz.aerii.jec.modules.Module
import xyz.aerii.jec.utils.message
import xyz.aerii.library.handlers.parser.parse
import xyz.aerii.library.handlers.time.Task
import kotlin.time.Duration.Companion.minutes

@Load
object CatFacts : Module(MiscCategory.randomCatFact) {
    private var task: Task? = null

    init {
        if (enabled) task = Chronos.repeat(MiscCategory.randomCatFactDelay.value.minutes) { fn() }

        MiscCategory.randomCatFact.onChange {
            if (!it) task?.cancel()
            else task = Chronos.repeat(MiscCategory.randomCatFactDelay.value.minutes) { fn() }
        }

        MiscCategory.randomCatFactDelay.onChange {
            task?.cancel()
            task = Chronos.repeat(it.minutes) { fn() }
        }

        ClientCommandRegistrationCallback.EVENT.register { d, _ ->
            d.register(literal("jec").then(literal("fact").executes {
                fn()
                1
            }))
        }
    }

    private fun fn() {
        "https://catfact.ninja/fact".request {
            onSuccess<JsonObject> {
                "<hover:<pink>Random cat fact! Delivered to you every <red>${MiscCategory.randomCatFactDelay.value}<pink> minutes :3>${it["fact"].asString}".parse().message()
            }
        }
    }
}