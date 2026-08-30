package foo.starred.jec.events.dispatcher

import foo.starred.jec.annotations.Load
import foo.starred.jec.events.EntityEvent
import foo.starred.jec.events.GameEvent
import foo.starred.jec.events.LocationEvent
import foo.starred.jec.events.MessageEvent
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientEntityEvents
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents
import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents
import net.fabricmc.fabric.api.client.message.v1.ClientSendMessageEvents
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents

@Load
object FabricEventDispatcher {
    init {
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
