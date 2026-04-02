@file:Suppress("ConstPropertyName", "Unused")

package xyz.aerii.jec

import com.teamresourceful.resourcefulconfig.api.client.ResourcefulConfigScreen
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback
import net.minecraft.network.chat.ClickEvent
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.HoverEvent
import net.minecraft.network.chat.Style
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import xyz.aerii.jec.annotations.AnnotationLoader
import xyz.aerii.jec.events.LocationEvent
import xyz.aerii.jec.events.core.on
import xyz.aerii.jec.handlers.Scribble
import xyz.aerii.jec.utils.client
import xyz.aerii.jec.utils.lie
import xyz.aerii.jec.utils.nextTick
import java.net.URI

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
        "§7----------------------------------------------".lie()
        "§7- §rThanks for downloading §d[JEC] §8[v$modVersion]§r!".lie()
        "§7----------------------------------------------".lie()
        "§7- §rQuick start:".lie()
        "§7 > §r/jec §7- §rOpens the Config GUI".lie()
        "§7 > §r/jec fact §7- §rSends a random cat fact".lie()
        "§7 > §r/jec model help §7- §rShows info about custom models".lie()
        "§7----------------------------------------------".lie()
        Component.literal("§d[JEC] §fNeed help or want to suggest features? Click to join the Discord!")
            .withStyle(
                Style.EMPTY
                    .withClickEvent(ClickEvent.OpenUrl(URI(discordUrl)))
                    .withHoverEvent(HoverEvent.ShowText(Component.literal("Click to join!").withStyle(Style.EMPTY.withColor(0xFFC4B5FD.toInt()))))
                    .withUnderlined(true)
            )
            .lie()
        "§7----------------------------------------------".lie()
        Component.literal("§d[JEC] §fLove the mod? You can support future updates and similar projects with a donation!")
            .withStyle(
                Style.EMPTY
                    .withClickEvent(ClickEvent.OpenUrl(URI("https://aerii.xyz/donate/")))
                    .withHoverEvent(HoverEvent.ShowText(Component.literal("Click to open page!").withStyle(Style.EMPTY.withColor(0xFFC4B5FD.toInt()))))
                    .withUnderlined(true)
            )
            .lie()
        "§7----------------------------------------------".lie()
    }
}