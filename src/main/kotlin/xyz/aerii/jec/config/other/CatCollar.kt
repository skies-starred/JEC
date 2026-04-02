package xyz.aerii.jec.config.other

import net.minecraft.world.item.DyeColor

enum class CatCollar(val dyeColor: DyeColor?) {
    RANDOM(null),
    NONE(null),

    BLACK(DyeColor.BLACK),
    BLUE(DyeColor.BLUE),
    BROWN(DyeColor.BROWN),
    CYAN(DyeColor.CYAN),
    GREEN(DyeColor.GREEN),
    LIGHT_BLUE(DyeColor.LIGHT_BLUE),
    LIGHT_GRAY(DyeColor.LIGHT_GRAY),
    LIME(DyeColor.LIME),
    MAGENTA(DyeColor.MAGENTA),
    ORANGE(DyeColor.ORANGE),
    PINK(DyeColor.PINK),
    PURPLE(DyeColor.PURPLE),
    RED(DyeColor.RED),
    WHITE(DyeColor.WHITE),
    YELLOW(DyeColor.YELLOW),
    GRAY(DyeColor.GRAY);

    override fun toString(): String {
        return name.lowercase().replace("_", " ").replaceFirstChar { it.uppercase() }
    }

    companion object {
        val all: List<CatCollar> = entries.filter { it != RANDOM && it != NONE }
    }
}