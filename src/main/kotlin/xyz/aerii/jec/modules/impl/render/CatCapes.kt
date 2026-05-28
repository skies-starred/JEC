package xyz.aerii.jec.modules.impl.render

import net.minecraft.core.ClientAsset
import net.minecraft.resources.ResourceLocation
import xyz.aerii.jec.JEC
import xyz.aerii.jec.annotations.Load
import xyz.aerii.jec.config.categories.MiscCategory
import xyz.aerii.jec.events.GameEvent
import xyz.aerii.jec.modules.Module
import xyz.aerii.library.api.name
import java.util.concurrent.ConcurrentHashMap

@Load
object CatCapes : Module(MiscCategory.catCapes) {
    private val set = setOf(ResourceLocation.fromNamespaceAndPath(JEC.modId, "cape"))
    val map = ConcurrentHashMap<String, Cape>()

    init {
        on<GameEvent.Start> {
            for (s in set) map[name] = Cape(ClientAsset.ResourceTexture(s))
        }
    }

    data class Cape(var texture: ClientAsset.ResourceTexture)
}