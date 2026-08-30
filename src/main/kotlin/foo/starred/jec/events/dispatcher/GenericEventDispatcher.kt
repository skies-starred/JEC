package foo.starred.jec.events.dispatcher

import foo.starred.jec.annotations.Load
import foo.starred.jec.events.MessageEvent
import foo.starred.jec.events.PacketEvent
import foo.starred.jec.events.core.on
import foo.starred.snowbird.api.mainThread
import net.minecraft.network.protocol.game.ClientboundSystemChatPacket

@Load
object GenericEventDispatcher {
    init {
        on<PacketEvent.Receive, ClientboundSystemChatPacket> {
            if (!overlay) mainThread { MessageEvent.Chat.Receive(content).post() }
        }
    }
}
