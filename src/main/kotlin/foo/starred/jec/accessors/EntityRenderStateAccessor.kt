@file:Suppress("FunctionName")

package foo.starred.jec.accessors

import net.minecraft.world.entity.Entity

interface EntityRenderStateAccessor {
    fun `jec$getEntity`(): Entity?
    fun `jec$setEntity`(entity: Entity?)
}