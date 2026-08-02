package foo.starred.jec.utils

import foo.starred.jec.JEC
import foo.starred.snowbird.kommand.ICommand
import foo.starred.snowbird.kommand.dsl.BuilderScope

fun command(block: BuilderScope.() -> Unit) {
    Command.command(JEC.modId, block)
}

private object Command : ICommand