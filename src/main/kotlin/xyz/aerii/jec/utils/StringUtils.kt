package xyz.aerii.jec.utils

import net.minecraft.network.chat.Component

//? if >= 1.21.11 {
/*import net.minecraft.util.Util
*///? } else {
import net.minecraft.Util
//? }

private val STRIP_COLOR_REGEX = Regex("(?i)§.")

fun String.url() {
    Util.getPlatform().openUri(this)
}

fun String.stripped(): String {
    return STRIP_COLOR_REGEX.replace(this, "")
}

fun Component.stripped(): String {
    return STRIP_COLOR_REGEX.replace(this.string, "")
}
