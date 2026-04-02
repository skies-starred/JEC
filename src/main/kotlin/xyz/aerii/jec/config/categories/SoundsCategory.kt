@file:Suppress("Unused")

package xyz.aerii.jec.config.categories

import com.teamresourceful.resourcefulconfig.api.types.options.TranslatableValue
import com.teamresourceful.resourcefulconfigkt.api.CategoryKt
import xyz.aerii.jec.config.other.CatSounds
import xyz.aerii.jec.config.react

object SoundsCategory : CategoryKt("Sounds") {
    override val description: TranslatableValue
        get() = TranslatableValue.literal("Plays and changes sounds for various things in-game.")

    var killSounds by boolean(false) {
        name = Literal("Kill sounds")
        description = Literal("Plays cat sounds when mobs die!")
    }.react()

    var killSoundsCancel by boolean(false) {
        name = Literal("Cancel original kill sounds")
        description = Literal("Cancels the original kill sound that's played when a mob dies if enabled.")
    }.react()

    var killSound by enum(CatSounds.HURT) {
        name = Literal("Kill sound")
        description = Literal("The sound to play on kill.")
    }

    var killSoundVolume by int(10) {
        name = Literal("Kill sound volume")
        description = Literal("The volume for the kill sound to play at.")

        range = 1..10
        slider = true
    }

    var killSoundPitch by int(10) {
        name = Literal("Kill sound pitch")
        description = Literal("The pitch for the kill sound to play at.")

        range = 1..10
        slider = true
    }

    init {
        button {
            title = "Test sound"
            description = "Click to test the sounds!"
            text = "Play sound"
            onClick { killSound.pk() }
        }
    }

    init {
        separator {
            title = "Chat sounds"
        }
    }

    var chatSounds by boolean(false) {
        name = Literal("Chat sounds")
        description = Literal("Plays cat sounds when someone sends a message containing specific keywords.")
    }.react()

    var chatSound by enum(CatSounds.PURREOW) {
        name = Literal("Chat sound")
        description = Literal("The sound to play on message.")
    }

    var chatSoundVolume by int(10) {
        name = Literal("Chat sound volume")
        description = Literal("The volume for the chat sound to play at.")

        range = 1..10
        slider = true
    }

    var chatSoundPitch by int(10) {
        name = Literal("Chat sound pitch")
        description = Literal("The pitch for the chat sound to play at.")

        range = 1..10
        slider = true
    }

    init {
        button {
            title = "Test sound"
            description = "Click to test the sounds!"
            text = "Play sound"
            onClick { chatSound.pc() }
        }
    }

    private var _chatKeywords by observable(string("meow") {
        name = Literal("Keywords to detect")
        description = Literal("The keywords for which the mod looks for in chat messages. Separated with commas.")
    }) { _, new ->
        chatKeywords = new.fn()
    }

    var chatKeywords: List<String> =
        _chatKeywords.fn()

    private fun String.fn(bool: Boolean = false): List<String> = split(",")
        .mapNotNull {
            val s = it.trim()
            if (s.isEmpty()) null
            else if (bool) s.lowercase()
            else s
        }
}