package foo.starred.jec.modules.impl.render

import com.mojang.blaze3d.platform.NativeImage
import com.mojang.serialization.Codec
import foo.starred.jec.JEC
import foo.starred.jec.annotations.Load
import foo.starred.jec.config.categories.RenderCategory
import foo.starred.jec.config.other.CatCollar
import foo.starred.jec.config.other.CatVariant
import foo.starred.jec.events.GameEvent
import foo.starred.jec.handlers.Scribble
import foo.starred.jec.modules.Module
import foo.starred.jec.utils.command
import foo.starred.jec.utils.message
import foo.starred.snowbird.api.client
import foo.starred.snowbird.api.lie
import foo.starred.snowbird.handlers.parser.parse
import foo.starred.snowbird.utils.safely
import net.fabricmc.loader.api.FabricLoader
import net.minecraft.client.model.EntityModel
import net.minecraft.client.model.geom.ModelPart
import net.minecraft.client.player.LocalPlayer
import net.minecraft.client.renderer.entity.EntityRendererProvider
import net.minecraft.client.renderer.entity.LivingEntityRenderer
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState
import net.minecraft.client.renderer.texture.DynamicTexture
import net.minecraft.resources.Identifier
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.DyeColor
import java.io.File

@Load
object CatModels : Module(RenderCategory.catModel) {
    private val set = CatVariant.all.map { it.name.lowercase() }

    private val scribble = Scribble("texture")
    private var custom = scribble.mutableMap("custom", Codec.STRING, Codec.STRING)

    private val model by lazy {
        object : EntityModel<LivingEntityRenderState>(ModelPart(emptyList(), emptyMap())) {}
    }

    @JvmStatic
    lateinit var b: LivingEntityRenderer<LivingEntity, LivingEntityRenderState, *>
        private set

    init {
        File(FabricLoader.getInstance().configDir.toFile(), "jec/textures/").mkdirs()

        on<GameEvent.Start> {
            for ((_, fn) in custom.value) {
                safely {
                    val i = Identifier.parse(fn)
                    if (i.namespace != JEC.modId) return@safely null

                    val f = File(FabricLoader.getInstance().configDir.toFile(), "jec/textures/${i.path}")
                    if (!f.exists()) return@safely null

                    client.textureManager.register(i, DynamicTexture({ i.path }, NativeImage.read(f.inputStream())))
                }
            }
        }

        command {
            "modal" {
                "load" / string("user") / string("file") {
                    fno(string("user"), string("file"))
                }.suggests {
                    File(FabricLoader.getInstance().configDir.toFile(), "jec/textures/").listFiles()?.filter { it.isFile }?.map { it.name} ?: emptyList()
                }

                "help" {
                    "<click:url:https://minecraft.novaskin.me/resourcepacks#default/><hover:Click to open page!><gray>- <white>You can use https://minecraft.novaskin.me/resourcepacks#default/ to design your cat texture.".parse().lie()
                    "<gray>- <white>Put the custom texture file at the File Path.".parse().lie()
                    "<gray>- <white>You can also use one of the default textures.".parse().lie()
                    "<gray>- <white>Put the name of the player that you want to change.".parse().lie()
                    "<gray>----------------------------------------------".parse().lie()
                    "<gray>- <white>File path: §c./minecraft/config/jec/textures/".parse().lie()
                    "<gray>- <white>Command: /jec model load <username> <fileName>".parse().lie()
                }
            }
        }
    }

    @JvmStatic
    fun fn0(a: Player): Identifier? {
        return (if (RenderCategory.catVariant == CatVariant.RANDOM) CatVariant.all.random().identifier else if (RenderCategory.catVariant == CatVariant.CUSTOM) a.fna() else RenderCategory.catVariant.identifier)
    }

    @JvmStatic
    fun fn1(a: Player): Boolean {
        if (a is LocalPlayer) return RenderCategory.catBabySelf
        if (a.uuid.version() == 4) return RenderCategory.catBabyOthers

        return RenderCategory.catBabyNpc
    }

    @JvmStatic
    fun fn2(): DyeColor? {
        return if (RenderCategory.catCollar == CatCollar.RANDOM) CatCollar.all.random().dyeColor else RenderCategory.catCollar.dyeColor
    }

    @JvmStatic
    fun fn3(a: Player): Boolean {
        if (a is LocalPlayer) return RenderCategory.self
        if (a.uuid.version() == 4) return RenderCategory.others

        return RenderCategory.npc
    }

    @JvmStatic
    fun fn4(a: EntityRendererProvider.Context) {
        b = object : LivingEntityRenderer<LivingEntity, LivingEntityRenderState, EntityModel<LivingEntityRenderState>>(a, model, 20f) {
            override fun getTextureLocation(renderState: LivingEntityRenderState): Identifier = Identifier.fromNamespaceAndPath(JEC.modId, "abc")
            override fun createRenderState(): LivingEntityRenderState = LivingEntityRenderState()
        }
    }

    @JvmStatic
    fun fn5(a: Player): Boolean {
        if (a is LocalPlayer) return RenderCategory.catScaleSelf
        if (a.uuid.version() == 4) return  RenderCategory.catScaleOthers

        return RenderCategory.catScaleNpc
    }

    @JvmStatic
    private fun Player.fna(): Identifier? {
        return Identifier.parse(custom.value[name.string.lowercase()] ?: return CatVariant.all.random().identifier)
    }

    private fun fno(u: String, fn: String) {
        try {
            if (fn in set) {
                val c = CatVariant.all.find { it.name.lowercase() == fn }?.identifier ?: return "Variant not found!".message()
                custom.update { put(u.lowercase(), "$c") }

                return "Loaded texture for $u successfully! Change worlds for it to take effect.".message()
            }

            val f = File(FabricLoader.getInstance().configDir.toFile(), "jec/textures/$fn")
            if (!f.exists()) return "No texture found at §c${f.path}§r!".message()

            val r = Identifier.fromNamespaceAndPath(JEC.modId, fn)
            client.textureManager.register(r, DynamicTexture({ fn }, NativeImage.read(f.inputStream())))

            custom.update { put(u.lowercase(), "$r") }
            "Loaded texture for $u successfully! Change worlds for it to take effect.".message()
        } catch (e: Exception) {
            JEC.LOGGER.error("Error loading texture: ${e.message}")
        }
    }
}