package xyz.aerii.jec.modules.impl.render

import com.mojang.blaze3d.platform.NativeImage
import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.serialization.Codec
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback
import net.fabricmc.loader.api.FabricLoader
import net.minecraft.client.model.EntityModel
import net.minecraft.client.model.geom.ModelPart
import net.minecraft.client.player.LocalPlayer
import net.minecraft.client.renderer.entity.EntityRendererProvider
import net.minecraft.client.renderer.entity.LivingEntityRenderer
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState
import net.minecraft.client.renderer.texture.DynamicTexture
import net.minecraft.network.chat.ClickEvent
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.HoverEvent
import net.minecraft.network.chat.Style
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.DyeColor
import xyz.aerii.jec.JEC
import xyz.aerii.jec.annotations.Load
import xyz.aerii.jec.config.categories.RenderCategory
import xyz.aerii.jec.config.other.CatCollar
import xyz.aerii.jec.config.other.CatVariant
import xyz.aerii.jec.events.GameEvent
import xyz.aerii.jec.handlers.Scribble
import xyz.aerii.jec.modules.Module
import xyz.aerii.jec.utils.client
import xyz.aerii.jec.utils.lie
import xyz.aerii.jec.utils.safely
import java.io.File
import java.net.URI

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
                    val i = ResourceLocation.parse(fn)
                    if (i.namespace != JEC.modId) return@safely

                    val f = File(FabricLoader.getInstance().configDir.toFile(), "jec/textures/${i.path}")
                    if (!f.exists()) return@safely

                    client.textureManager.register(i, DynamicTexture({ i.path }, NativeImage.read(f.inputStream())))
                }
            }
        }

        ClientCommandRegistrationCallback.EVENT.register { dispatcher, _ ->
            dispatcher.register(literal("jec").then(literal("model")
                .then(literal("load")
                    .then(ClientCommandManager.argument("user", StringArgumentType.word())
                        .then(ClientCommandManager.argument("file", StringArgumentType.word())
                            .suggests { _, builder ->
                                for (s in set) builder.suggest(s)

                                val dir = File(FabricLoader.getInstance().configDir.toFile(), "jec/textures/").listFiles() ?: emptyArray()
                                for (d in dir) if (d.isFile) builder.suggest(d.name)

                                builder.buildFuture()
                            }
                            .executes { ctx ->
                                val user = StringArgumentType.getString(ctx, "user")
                                val file = StringArgumentType.getString(ctx, "file")

                                fno(user, file)
                                1
                            }
                        )
                    )
                )
                .then(literal("help").executes {
                    Component.literal("§d[JEC] §7- §fYou can use https://minecraft.novaskin.me/resourcepacks#default/ to design your cat texture.").withStyle(
                        Style.EMPTY
                            .withClickEvent(
                                ClickEvent.OpenUrl(URI("https://minecraft.novaskin.me/resourcepacks#default/"))
                            )
                            .withHoverEvent(
                                HoverEvent.ShowText(Component.literal("Click to open page!").withStyle(Style.EMPTY.withColor(0xFFC4B5FD.toInt())))
                            )
                    ).lie()
                    "§7- §fPut the custom texture file at the File Path.".lie()
                    "§7- §fYou can also use one of the default textures.".lie()
                    "§7- §fPut the name of the player that you want to change.".lie()
                    "§7----------------------------------------------".lie()
                    "§7- §fFile path: §c./minecraft/config/jec/textures/".lie()
                    "§7- §fCommand: /jec model load <username> <fileName>".lie()
                    1
                })
            ))
        }
    }

    @JvmStatic
    fun fn0(a: Player): ResourceLocation? {
        return (if (RenderCategory.catVariant == CatVariant.RANDOM) CatVariant.all.random().identifier else if (RenderCategory.catVariant == CatVariant.CUSTOM) a.fna() else RenderCategory.catVariant.identifier)
    }

    @JvmStatic
    fun fn1(a: Player): Boolean {
        if (a is LocalPlayer && RenderCategory.catBabySelf) return true

        val v = a.uuid.version() == 4
        if (v && RenderCategory.catBabyOthers) return true
        if (!v && RenderCategory.catBabyNpc) return true

        return false
    }

    @JvmStatic
    fun fn2(): DyeColor? {
        return if (RenderCategory.catCollar == CatCollar.RANDOM) CatCollar.all.random().dyeColor else RenderCategory.catCollar.dyeColor
    }

    @JvmStatic
    fun fn3(a: Player): Boolean {
        if (a is LocalPlayer && RenderCategory.self) return true

        val v = a.uuid.version() == 4
        if (v && RenderCategory.others) return true
        if (!v && RenderCategory.npc) return true

        return false
    }

    @JvmStatic
    fun fn4(a: EntityRendererProvider.Context) {
        b = object : LivingEntityRenderer<LivingEntity, LivingEntityRenderState, EntityModel<LivingEntityRenderState>>(a, model, 20f) {
            override fun getTextureLocation(renderState: LivingEntityRenderState): ResourceLocation = ResourceLocation.fromNamespaceAndPath(JEC.modId, "abc")
            override fun createRenderState(): LivingEntityRenderState = LivingEntityRenderState()
        }
    }

    @JvmStatic
    fun fn5(a: Player): Boolean {
        if (a is LocalPlayer && RenderCategory.catScaleSelf) return true

        val v = a.uuid.version() == 4
        if (v && RenderCategory.catScaleOthers) return true
        if (!v && RenderCategory.catScaleNpc) return true

        return false
    }

    @JvmStatic
    private fun Player.fna(): ResourceLocation? {
        return ResourceLocation.parse(custom.value[name.string.lowercase()] ?: return CatVariant.all.random().identifier)
    }

    private fun fno(u: String, fn: String) {
        try {
            if (fn in set) {
                val c = CatVariant.all.find { it.name.lowercase() == fn }?.identifier ?: return "Variant not found!".lie()
                custom.update { put(u.lowercase(), "$c") }

                return "Loaded texture for $u successfully! Change worlds for it to take effect.".lie()
            }

            val f = File(FabricLoader.getInstance().configDir.toFile(), "jec/textures/$fn")
            if (!f.exists()) return "No texture found at §c${f.path}§r!".lie()

            val r = ResourceLocation.fromNamespaceAndPath(JEC.modId, fn)
            client.textureManager.register(r, DynamicTexture({ fn }, NativeImage.read(f.inputStream())))

            custom.update { put(u.lowercase(), "$r") }
            "Loaded texture for $u successfully! Change worlds for it to take effect.".lie()
        } catch (e: Exception) {
            JEC.LOGGER.error("Error loading texture: ${e.message}")
        }
    }
}