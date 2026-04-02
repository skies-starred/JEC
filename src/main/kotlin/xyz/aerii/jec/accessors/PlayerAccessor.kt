@file:Suppress("FunctionName")

package xyz.aerii.jec.accessors

import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.DyeColor

interface PlayerAccessor {
    fun `jec$variant`(): ResourceLocation?
    fun `jec$variant2`(): ResourceLocation?
    fun `jec$collar`(): DyeColor?
    fun `jec$baby`(): Boolean
    fun `jec$scale`(): Boolean
}