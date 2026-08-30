package foo.starred.jec.api.storage

import foo.starred.jec.JEC
import foo.starred.snowbird.api.storage.AbstractJsonStore

class JsonStore(path: String) : AbstractJsonStore(JEC.modId, path)
