package foo.starred.jec.modules.impl.render

import foo.starred.jec.JEC
import foo.starred.jec.annotations.Load
import foo.starred.jec.config.categories.MiscCategory
import foo.starred.jec.events.GameEvent
import foo.starred.jec.modules.Module
import foo.starred.snowbird.api.name
import net.minecraft.core.ClientAsset
import net.minecraft.resources.Identifier
import java.util.concurrent.ConcurrentHashMap

@Load
object CatCapes : Module(MiscCategory.catCapes) {
    private val set = setOf(Identifier.fromNamespaceAndPath(JEC.modId, "cape"))
    val map = ConcurrentHashMap<String, Cape>()

    init {
        on<GameEvent.Start> {
            for (s in set) map[name] = Cape(ClientAsset.ResourceTexture(s))
        }
    }

    data class Cape(var texture: ClientAsset.ResourceTexture)
}