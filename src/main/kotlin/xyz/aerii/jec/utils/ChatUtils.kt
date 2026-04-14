package xyz.aerii.jec.utils

import net.minecraft.network.chat.Component
import xyz.aerii.library.api.lie
import xyz.aerii.library.handlers.parser.parse
import xyz.aerii.library.utils.literal

private val prefix = "<#8A6EEC>[<#6E60F2>J<#5257F8>E<#6E60F2>C<#8A6EEC>]".parse()
private val component = " ".literal()

fun String.message() {
    literal().message()
}

fun Component.message() {
    prefix.copy().append(component.copy()).append(this).lie()
}