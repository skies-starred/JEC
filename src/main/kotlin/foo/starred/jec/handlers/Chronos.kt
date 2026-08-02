@file:Suppress("UNUSED")

package foo.starred.jec.handlers

import foo.starred.jec.annotations.Load
import foo.starred.snowbird.handlers.time.AbstractChronos

@Load
object Chronos : AbstractChronos()