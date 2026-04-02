@file:Suppress("FunctionName")

package xyz.aerii.jec.accessors

import net.minecraft.world.entity.player.Player

interface CatRenderStateAccessor {
    fun `jec$getPlayer`(): Player?
    fun `jec$setPlayer`(player: Player)
}