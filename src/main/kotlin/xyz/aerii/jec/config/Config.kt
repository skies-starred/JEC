package xyz.aerii.jec.config

import com.teamresourceful.resourcefulconfig.api.loader.Configurator
import com.teamresourceful.resourcefulconfigkt.api.ConfigKt
import xyz.aerii.jec.JEC
import xyz.aerii.jec.annotations.Load
import xyz.aerii.jec.config.categories.ChatCategory
import xyz.aerii.jec.config.categories.MiscCategory
import xyz.aerii.jec.config.categories.RenderCategory
import xyz.aerii.jec.config.categories.SoundsCategory
import xyz.aerii.library.utils.open

@Load
object Config : ConfigKt("jec/config") {
    override val name = Literal(JEC.modName)
    override val description = Literal("A small mod that adds cat-istic features! Made by @skies.starred")

    init {
        separator {
            title = "Links"
            description = "Links to stuff :eyes:"
        }

        button {
            title = "Discord"
            description = "Join if you need help, or want to check out the other mods made by Starred!"
            text = "Join"

            onClick {
                JEC.discordUrl.open()
            }
        }

        button {
            title = "GitHub"
            description = "The source code for the mod! Star the repo?"
            text = "Open page"

            onClick {
                "https://github.com/skies-starred/jec".open()
            }
        }

        button {
            title = "Issues"
            description = "Opens the page to create bug reports"
            text = "Open page"

            onClick {
                "https://github.com/skies-starred/jec/issues".open()
            }
        }

        separator {
            title = "Other mods"
        }

        button {
            title = "Athen"
            description = "A very cool Quality-of-Life mod for Hypixel Skyblock."
            text = "Open page"

            onClick {
                "https://modrinth.com/mod/athen".open()
            }
        }

        button {
            title = "REC"
            description = "Roughly Enough Calculations, a mod for calculations."
            text = "Open page"

            onClick {
                "https://modrinth.com/mod/roughly-enough-calcs".open()
            }
        }

        category(SoundsCategory)
        category(RenderCategory)
        category(ChatCategory)
        category(MiscCategory)

        register(Configurator(JEC.modId))
    }
}