package xyz.aerii.jec.utils

import xyz.aerii.jec.JEC
import xyz.aerii.library.kommand.ICommand
import xyz.aerii.library.kommand.dsl.BuilderScope

fun command(block: BuilderScope.() -> Unit) {
    Command.command(JEC.modId, block)
}

private object Command : ICommand