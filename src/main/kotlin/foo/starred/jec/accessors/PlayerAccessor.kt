@file:Suppress("FunctionName")

package foo.starred.jec.accessors

import net.minecraft.resources.Identifier
import net.minecraft.world.item.DyeColor

interface PlayerAccessor {
    fun `jec$variant`(): Identifier?
    fun `jec$variant2`(): Identifier?
    fun `jec$collar`(): DyeColor?
    fun `jec$baby`(): Boolean
    fun `jec$scale`(): Boolean
}