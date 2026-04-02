package xyz.aerii.jec.utils

import net.minecraft.client.Minecraft
import net.minecraft.network.chat.Component

val client: Minecraft = Minecraft.getInstance()

inline fun mainThread(
    crossinline block: Minecraft.() -> Unit
) {
    client.execute { client.block() }
}

fun nextTick(
    action: Minecraft.() -> Unit
) {
    client.schedule {
        client.action()
    }
}

fun String.message() {
    client.connection?.sendChat(this)
}

fun String.lie() {
    //~ if >= 26.1 'addMessage' -> 'addClientSystemMessage'
    mainThread { gui?.chat?.addMessage(Component.literal("§d[JEC] §f" + this@lie)) }
}

fun Component.lie() {
    //~ if >= 26.1 'addMessage' -> 'addClientSystemMessage'
    mainThread { gui?.chat?.addMessage(this@lie) }
}