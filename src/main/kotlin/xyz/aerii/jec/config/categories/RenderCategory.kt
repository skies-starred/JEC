package xyz.aerii.jec.config.categories

import com.teamresourceful.resourcefulconfig.api.types.options.TranslatableValue
import com.teamresourceful.resourcefulconfigkt.api.CategoryKt
import xyz.aerii.jec.config.other.CatCollar
import xyz.aerii.jec.config.other.CatVariant
import xyz.aerii.jec.config.react

object RenderCategory : CategoryKt("Render") {
    override val description: TranslatableValue
        get() = TranslatableValue.literal("Changes how things render in-game.")

    init {
        separator {
            title = "Cat model rendering"
        }
    }

    var catModel by boolean(false) {
        name = Literal("Cat models")
        description = Literal("Turns player models into cats!")
    }.react()

    var catVariant by enum(CatVariant.RANDOM) {
        name = Literal("Cat variant")
        description = Literal("The variant of cat to turn the models into. Use the command \"/jec model help\" if you want to set a custom texture. If a texture is not set with custom, it will default to random.")
    }

    var catCollar by enum(CatCollar.RANDOM) {
        name = Literal("Cat collar")
        description = Literal("The color of the cat's collar color.")
    }

    var self by boolean(true) {
        name = Literal("Transform self")
        description = Literal("Whether to turn the user into a cat.")
    }

    var catBabySelf by boolean(false) {
        name = Literal("User baby")
        description = Literal("Whether to turn the user into a baby cat.")
    }

    var others by boolean(false) {
        name = Literal("Transform others")
        description = Literal("Whether to turn other players into cats.")
    }

    var catBabyOthers by boolean(false) {
        name = Literal("Others baby")
        description = Literal("Whether to turn other players into baby cats.")
    }

    var npc by boolean(false) {
        name = Literal("Transform NPCs")
        description = Literal("Whether to turn NPCs into cats.")
    }

    var catBabyNpc by boolean(false) {
        name = Literal("NPCs baby")
        description = Literal("Whether to turn NPCs into baby cats.")
    }

    init {
        separator {
            title = "Cat model scaling"
        }
    }

    var catScale by boolean(false) {
        name = Literal("Scale cat model")
        description = Literal("Enable to scale cat models!")
    }

    var catScaleX by float(1f) {
        name = Literal("Cat scale X")
        description = Literal("The X scale for the cat model.")

        range = 1f..10f
        slider = true
    }

    var catScaleY by float(1f) {
        name = Literal("Cat scale Y")
        description = Literal("The Y scale for the cat model.")

        range = 1f..10f
        slider = true
    }

    var catScaleZ by float(1f) {
        name = Literal("Cat scale Z")
        description = Literal("The Z scale for the cat model.")

        range = 1f..10f
        slider = true
    }

    var catScaleSelf by boolean(true) {
        name = Literal("Scale self")
        description = Literal("Whether to scale the user's cat model.")
    }

    var catScaleOthers by boolean(true) {
        name = Literal("Scale others")
        description = Literal("Whether to scale other player's cat models.")
    }

    var catScaleNpc by boolean(true) {
        name = Literal("Scale NPCs")
        description = Literal("Whether to scale NPCs' cat models.")
    }
}