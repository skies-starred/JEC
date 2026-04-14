@file:Suppress("ConstPropertyName", "Unused")

package xyz.aerii.jec

import com.teamresourceful.resourcefulconfig.api.client.ResourcefulConfigScreen
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import xyz.aerii.jec.annotations.AnnotationLoader
import xyz.aerii.jec.events.LocationEvent
import xyz.aerii.jec.events.core.on
import xyz.aerii.jec.handlers.Scribble
import xyz.aerii.jec.utils.message
import xyz.aerii.library.api.client
import xyz.aerii.library.api.nextTick
import xyz.aerii.library.handlers.parser.parse

object JEC : ClientModInitializer {
    const val modVersion: String = /*$ mod_version*/ "0.0.1"
    const val modId: String = /*$ mod_id*/ "jec"
    const val modName: String = /*$ mod_name*/ "JEC"
    const val discordUrl: String = "https://discord.gg/DB5S3DjQVa"

    @JvmField
    val LOGGER: Logger = LogManager.getLogger(JEC::class.java)

    @JvmStatic
    val main: Scribble = Scribble("main")

    @JvmStatic
    var last: String by main.string("last")

    override fun onInitializeClient() {
        AnnotationLoader.load()

        on<LocationEvent.Server.Connect> {
            if (last == modVersion) return@on
            last = modVersion
            li()
        }

        ClientCommandRegistrationCallback.EVENT.register { d, _ ->
            literal("jec").executes {
                nextTick { client.setScreen(ResourcefulConfigScreen.getFactory(modId).apply(null)) }
                1
            }.apply {
                d.register(this)
            }
        }
    }

    private fun li() {
        "§7----------------------------------------------".message()
        "§7- §rThanks for downloading §d[JEC] §8[v$modVersion]§r!".message()
        "§7----------------------------------------------".message()
        "§7- §rQuick start:".message()
        "§7 > §r/jec §7- §rOpens the Config GUI".message()
        "§7 > §r/jec fact §7- §rSends a random cat fact".message()
        "§7 > §r/jec model help §7- §rShows info about custom models".message()
        "§7----------------------------------------------".message()
        "<click:url:$discordUrl><hover:Click to join!>Need help or want to suggest features? Click to join the <red>Discord<r>!".parse().message()
        "§7----------------------------------------------".message()
        "<click:url:https://aerii.xyz/donate/><hover:Click to open page!>Love the mod? You can support future updates and similar projects with a donation!".parse().message()
        "§7----------------------------------------------".message()
    }
}