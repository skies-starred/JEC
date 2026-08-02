package foo.starred.jec.config.other

import net.minecraft.resources.Identifier

enum class CatVariant(path: String?) {
    RANDOM(null),
    CUSTOM(null),

    //~ if >= 26.1 'cat/' -> 'cat/cat_' {
    ALL_BLACK("textures/entity/cat/cat_all_black"),
    BLACK("textures/entity/cat/cat_black"),
    BRITISH("textures/entity/cat/cat_british_shorthair"),
    CALICO("textures/entity/cat/cat_calico"),
    JELLIE("textures/entity/cat/cat_jellie"),
    PERSIAN("textures/entity/cat/cat_persian"),
    RAGDOLL("textures/entity/cat/cat_ragdoll"),
    RED("textures/entity/cat/cat_red"),
    SIAMESE("textures/entity/cat/cat_siamese"),
    TABBY("textures/entity/cat/cat_tabby"),
    WHITE("textures/entity/cat/cat_white");
    //~ }

    val identifier: Identifier? =
        if (path != null) Identifier.withDefaultNamespace("$path.png") else null

    //? >= 26.1 {
    val identifierBaby: Identifier? =
        if (path != null) Identifier.withDefaultNamespace("${path}_baby.png") else null
    //? }

    override fun toString(): String {
        return name.lowercase().replace("_", " ").replaceFirstChar { it.uppercase() }
    }

    companion object {
        val all: List<CatVariant> = entries.filter { it != RANDOM && it != CUSTOM }
    }
}