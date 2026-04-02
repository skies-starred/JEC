@file:Suppress("FunctionName")

package xyz.aerii.jec.accessors

import net.minecraft.world.entity.Entity

interface EntityRenderStateAccessor {
    fun `jec$getEntity`(): Entity?
    fun `jec$setEntity`(entity: Entity?)
}