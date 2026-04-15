@file:Suppress("Unused")

package xyz.aerii.jec.config.categories

import com.teamresourceful.resourcefulconfig.api.types.options.TranslatableValue
import com.teamresourceful.resourcefulconfigkt.api.CategoryKt
import xyz.aerii.jec.config.other.CatSounds
import xyz.aerii.jec.config.observe
import xyz.aerii.jec.modules.impl.sounds.SoundReplacer

object SoundsCategory : CategoryKt("Sounds") {
    override val description: TranslatableValue
        get() = TranslatableValue.literal("Plays and changes sounds for various things in-game.")

    init {
        separator {
            title = "Sound replacer"
        }
    }

    var replaceSounds by boolean(false) {
        name = Literal("Replace all sounds")
        description = Literal("Replaces all sounds with the selected cat sound!")
    }.observe()

    var replacedSound by observable(enum(CatSounds.PURREOW) {
        name = Literal("Sound to replace with")
        description = Literal("The sound to replace other sounds with.")
    }) { old, new ->
        if ((old == CatSounds.RANDOM && new != CatSounds.RANDOM) || (old != CatSounds.RANDOM && new == CatSounds.RANDOM)) SoundReplacer.map.clear()
    }

    var randomiseEvery by boolean(false) {
        name = Literal("Randomise everytime")
        description = Literal("Randomises the sound everytime when it plays. Only affects anything if \"Random\" is selected in the Replaced Sound.")
    }

    var randomiseWorld by boolean(true) {
        name = Literal("Re-randomise sounds")
        description = Literal("Re-randomises the sounds on world change. Only affects anything if \"Random\" is selected in the Replaced Sound.")
    }

    var replacedSoundVolume by int(10) {
        name = Literal("Replaced sound volume")
        description = Literal("The volume for the replaced sound to play at. Ignored if random is selected.")

        range = 1..10
        slider = true
    }

    var replacedSoundPitch by int(10) {
        name = Literal("Replaced sound pitch")
        description = Literal("The pitch for the replaced sound to play at. Ignored if random is selected.")

        range = 1..10
        slider = true
    }

    init {
        separator {
            title = "Kill sounds"
        }
    }

    var killSounds by boolean(false) {
        name = Literal("Kill sounds")
        description = Literal("Plays cat sounds when mobs die!")
    }.observe()

    var killSoundsCancel by boolean(false) {
        name = Literal("Cancel original kill sounds")
        description = Literal("Cancels the original kill sound that's played when a mob dies if enabled.")
    }.observe()

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
            onClick { killSound.pk(0.0, 0.0, 0.0) }
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
    }.observe()

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