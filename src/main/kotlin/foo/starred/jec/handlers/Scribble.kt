@file:Suppress("UNUSED")

package foo.starred.jec.handlers

import foo.starred.jec.JEC
import foo.starred.snowbird.handlers.data.AbstractScribble

class Scribble(path: String) : AbstractScribble(JEC.modId, path)