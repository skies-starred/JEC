package foo.starred.jec.utils

import foo.starred.snowbird.api.lie
import foo.starred.snowbird.api.text.parser.impl.parse
import foo.starred.snowbird.utils.literal
import net.minecraft.network.chat.Component

private val prefix = "<#8A6EEC>[<#6E60F2>J<#5257F8>E<#6E60F2>C<#8A6EEC>]".parse()
private val component = " ".literal()

fun String.mod() {
    literal().mod()
}

fun Component.mod() {
    prefix.copy().append(component.copy()).append(this).lie()
}
