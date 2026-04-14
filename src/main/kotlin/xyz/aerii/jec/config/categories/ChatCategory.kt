package xyz.aerii.jec.config.categories

import com.teamresourceful.resourcefulconfig.api.types.options.TranslatableValue
import com.teamresourceful.resourcefulconfigkt.api.CategoryKt
import xyz.aerii.jec.config.observe
import kotlin.text.split
import kotlin.text.trim

object ChatCategory : CategoryKt("Chat") {
    override val description: TranslatableValue
        get() = TranslatableValue.literal("Changes things related to the chat.")

    init {
        separator {
            title = "Auto meow"
        }
    }

    var autoMeow by boolean(true) {
        name = Literal("Auto-meow")
        description = Literal("Automatically meows back when someone sends a message containing the keywords.")
    }.observe()

    var autoMeowLooseCheck by boolean(true) {
        name = Literal("Loosely check")
        description = Literal("Whether to check loosely. It only checks if the message contains the keyword if enabled.")
    }

    private var _autoMeowKeywords by observable(string("meow,nya,purr") {
        name = Literal("Keywords")
        description = Literal("Keywords to check for in messages received. Separated by comma")
    }) { _, new ->
        autoMeowKeywords = new.fn(true)
    }

    var autoMeowKeywords: List<String> =
        _autoMeowKeywords.fn(true)

    private var _autoMeowResponses by observable(string("meow,nya,purr") {
        name = Literal("Responses")
        description = Literal("Responses to send in auto-meow. Separated by comma")
    }) { _, new ->
        autoMeowResponses = new.fn()
    }

    var autoMeowResponses: List<String> =
        _autoMeowResponses.fn()

    init {
        separator {
            title = "Cat-ify messages"
        }
    }

    var catifyMessages by boolean(false) {
        name = Literal("Cat-ify messages")
        description = Literal("Adds various keywords to your messages.")
    }.observe()

    private var _catifyWords by observable(string("meow,nya,purr") {
        name = Literal("Catify words")
        description = Literal("Words to add to cat-ified messages.")
    }) { _, new ->
        catifyWords = new.fn()
    }

    var catifyWords: List<String> =
        _catifyWords.fn()

    var catifyChance by int(50) {
        name = Literal("Catify chance")
        description = Literal("The chance for it to catify words.")

        range = 1..100
        slider = true
    }

    var affectCommands by boolean(true) {
        name = Literal("Affect commands")
        description = Literal("Whether the modifications should be applied on chat-related commands. This does not affect non-chat-related commands.")
    }

    private fun String.fn(bool: Boolean = false): List<String> = split(",")
        .mapNotNull {
            val s = it.trim()
            if (s.isEmpty()) null
            else if (bool) s.lowercase()
            else s
        }
}