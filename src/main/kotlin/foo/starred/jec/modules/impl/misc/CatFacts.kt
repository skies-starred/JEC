package foo.starred.jec.modules.impl.misc

import com.google.gson.JsonObject
import foo.starred.jec.annotations.Load
import foo.starred.jec.config.categories.MiscCategory
import foo.starred.jec.handlers.Beacon.request
import foo.starred.jec.handlers.Chronos
import foo.starred.jec.modules.Module
import foo.starred.jec.utils.command
import foo.starred.jec.utils.message
import foo.starred.snowbird.handlers.parser.parse
import foo.starred.snowbird.handlers.time.Task
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

        command {
            "fact" {
                fn()
            }
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