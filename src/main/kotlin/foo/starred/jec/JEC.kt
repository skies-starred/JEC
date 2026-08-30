@file:Suppress("ConstPropertyName", "Unused")

package foo.starred.jec

import com.teamresourceful.resourcefulconfig.api.client.ResourcefulConfigScreen
import foo.starred.jec.annotations.AnnotationLoader
import foo.starred.jec.events.LocationEvent
import foo.starred.jec.events.core.on
import foo.starred.jec.api.storage.JsonStore
import foo.starred.jec.utils.command
import foo.starred.jec.utils.mod
import foo.starred.snowbird.api.client
import foo.starred.snowbird.api.nextTick
import foo.starred.snowbird.api.text.parser.impl.parse
import net.fabricmc.api.ClientModInitializer
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

object JEC : ClientModInitializer {
    const val modVersion: String = /*$ mod_version*/ "0.0.3"
    const val modId: String = /*$ mod_id*/ "jec"
    const val modName: String = /*$ mod_name*/ "JEC"
    const val discordUrl: String = "https://discord.gg/DB5S3DjQVa"

    @JvmField
    val LOGGER: Logger = LogManager.getLogger(JEC::class.java)

    @JvmStatic
    val main: JsonStore = JsonStore("main")

    @JvmStatic
    var last: String by main.string("last")

    override fun onInitializeClient() {
        AnnotationLoader.load()

        on<LocationEvent.Server.Connect> {
            if (last == modVersion) return@on
            last = modVersion
            li()
        }

        command {
            executes {
                //~ if >= 26.2 'client.setScreen' -> 'client.gui.setScreen'
                nextTick { client.gui.setScreen(ResourcefulConfigScreen.getFactory(modId).apply(null)) }
            }
        }
    }

    private fun li() {
        "§7----------------------------------------------".mod()
        "§7- §rThanks for downloading §d[JEC] §8[v$modVersion]§r!".mod()
        "§7----------------------------------------------".mod()
        "§7- §rQuick start:".mod()
        "§7 > §r/jec §7- §rOpens the Config GUI".mod()
        "§7 > §r/jec fact §7- §rSends a random cat fact".mod()
        "§7 > §r/jec model help §7- §rShows info about custom models".mod()
        "§7----------------------------------------------".mod()
        "<click:url:$discordUrl><hover:Click to join!>Need help or want to suggest features? Click to join the <red>Discord<r>!".parse().mod()
        "§7----------------------------------------------".mod()
        "<click:url:https://aerii.xyz/donate/><hover:Click to open page!>Love the mod? You can support future updates and similar projects with a donation!".parse().mod()
        "§7----------------------------------------------".mod()
    }
}
