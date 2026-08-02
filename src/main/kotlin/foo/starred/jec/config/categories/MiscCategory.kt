package foo.starred.jec.config.categories

import com.teamresourceful.resourcefulconfig.api.types.options.TranslatableValue
import com.teamresourceful.resourcefulconfigkt.api.CategoryKt
import foo.starred.jec.config.observe

object MiscCategory : CategoryKt("Misc") {
    override val description: TranslatableValue
        get() = TranslatableValue.literal("Miscellaneous changes and additions to the game.")

    init {
        separator {
            title = "Cat capes"
        }
    }

    var catCapes by boolean(false) {
        name = Literal("Cat capes")
        description = Literal("Adds cat capes for your player model!")
    }.observe()

    init {
        separator {
            title = "Cat facts"
        }
    }

    var randomCatFact by boolean(true) {
        name = Literal("Random cat facts")
        description = Literal("Randomly sends a cat fact in your chat on a set delay. You can also use the command \"/jec fact\"!")
    }.observe()

    var randomCatFactDelay by int(10) {
        name = Literal("Random cat fact delay")
        description = Literal("The delay to wait before sending another cat fact. The delay is set in minutes.")

        range = 5..60
        slider = true
    }.observe()
}