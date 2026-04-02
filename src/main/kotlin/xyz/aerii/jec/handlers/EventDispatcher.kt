package xyz.aerii.jec.handlers

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientEntityEvents
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents
import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents
import net.fabricmc.fabric.api.client.message.v1.ClientSendMessageEvents
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents
import net.minecraft.network.protocol.game.ClientboundSystemChatPacket
import xyz.aerii.jec.annotations.Load
import xyz.aerii.jec.events.EntityEvent
import xyz.aerii.jec.events.GameEvent
import xyz.aerii.jec.events.LocationEvent
import xyz.aerii.jec.events.MessageEvent
import xyz.aerii.jec.events.PacketEvent
import xyz.aerii.jec.events.core.on
import xyz.aerii.jec.utils.mainThread

@Load
object EventDispatcher {
    init {
        on<PacketEvent.Receive, ClientboundSystemChatPacket> {
            if (!overlay) mainThread { MessageEvent.Chat.Receive(content).post() }
        }

        ClientReceiveMessageEvents.ALLOW_GAME.register { component, _ ->
            !MessageEvent.Chat.Intercept(component).post()
        }

        ClientEntityEvents.ENTITY_LOAD.register { entity, _ ->
            EntityEvent.Load(entity).post()
        }

        ClientEntityEvents.ENTITY_UNLOAD.register { entity, _ ->
            EntityEvent.Unload(entity).post()
        }

        ClientLifecycleEvents.CLIENT_STARTED.register { _ ->
            GameEvent.Start.post()
        }

        ClientLifecycleEvents.CLIENT_STOPPING.register { _ ->
            GameEvent.Stop.post()
        }

        ClientPlayConnectionEvents.JOIN.register { _, _, _ ->
            LocationEvent.Server.Connect.post()
        }

        ClientPlayConnectionEvents.DISCONNECT.register { _, _ ->
            LocationEvent.Server.Disconnect.post()
        }

        ClientSendMessageEvents.MODIFY_CHAT.register { s ->
            val event = MessageEvent.Chat.Send(s)
            event.post()
            event.message
        }

        ClientSendMessageEvents.MODIFY_COMMAND.register { s ->
            val event = MessageEvent.Chat.Command(s)
            event.post()
            event.message
        }
    }
}