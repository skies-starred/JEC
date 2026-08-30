package foo.starred.jec.modules.impl.misc

import com.google.gson.JsonObject
import foo.starred.jec.annotations.Load
import foo.starred.jec.config.categories.MiscCategory
import foo.starred.jec.api.network.WebAPI.request
import foo.starred.jec.api.scheduling.Scheduler
import foo.starred.jec.modules.Module
import foo.starred.jec.utils.command
import foo.starred.jec.utils.mod
import foo.starred.snowbird.api.scheduling.scheduler.data.tasks.base.SchedulerTask
import foo.starred.snowbird.api.text.parser.impl.parse
import kotlin.time.Duration.Companion.minutes

@Load
object CatFacts : Module(MiscCategory.randomCatFact) {
    private var task: SchedulerTask? = null

    init {
        if (enabled) task = Scheduler.repeat(MiscCategory.randomCatFactDelay.value.minutes) { fn() }

        MiscCategory.randomCatFact.onChange {
            if (!it) task?.cancel()
            else task = Scheduler.repeat(MiscCategory.randomCatFactDelay.value.minutes) { fn() }
        }

        MiscCategory.randomCatFactDelay.onChange {
            task?.cancel()
            task = Scheduler.repeat(it.minutes) { fn() }
        }

        command {
            "fact" {
                fn()
            }
        }
    }

    private fun fn() {
        "https://catfact.ninja/fact".request {
            success<JsonObject> {
                "<hover:<pink>Random cat fact! Delivered to you every <red>${MiscCategory.randomCatFactDelay.value}<pink> minutes :3>${it["fact"].asString}".parse().mod()
            }
        }
    }
}
