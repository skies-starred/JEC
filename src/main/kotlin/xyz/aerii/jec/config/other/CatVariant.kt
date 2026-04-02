package xyz.aerii.jec.config.other

import net.minecraft.resources.ResourceLocation

enum class CatVariant(path: String?) {
    RANDOM(null),
    CUSTOM(null),

    //~ if >= 26.1 'cat/' -> 'cat/cat_' {
    ALL_BLACK("textures/entity/cat/all_black"),
    BLACK("textures/entity/cat/black"),
    BRITISH("textures/entity/cat/british_shorthair"),
    CALICO("textures/entity/cat/calico"),
    JELLIE("textures/entity/cat/jellie"),
    PERSIAN("textures/entity/cat/persian"),
    RAGDOLL("textures/entity/cat/ragdoll"),
    RED("textures/entity/cat/red"),
    SIAMESE("textures/entity/cat/siamese"),
    TABBY("textures/entity/cat/tabby"),
    WHITE("textures/entity/cat/white");
    //~ }

    val identifier: ResourceLocation? =
        if (path != null) ResourceLocation.withDefaultNamespace("$path.png") else null

    //? >= 26.1 {
    /*val identifierBaby: ResourceLocation? =
        if (path != null) ResourceLocation.withDefaultNamespace("${path}_baby.png") else null
    *///? }

    override fun toString(): String {
        return name.lowercase().replace("_", " ").replaceFirstChar { it.uppercase() }
    }

    companion object {
        val all: List<CatVariant> = entries.filter { it != RANDOM && it != CUSTOM }
    }
}