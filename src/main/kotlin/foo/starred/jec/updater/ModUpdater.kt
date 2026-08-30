package foo.starred.jec.updater

import com.google.gson.JsonElement
import foo.starred.jec.JEC
import foo.starred.jec.annotations.Load
import foo.starred.jec.events.LocationEvent
import foo.starred.jec.events.core.on
import foo.starred.jec.api.scheduling.Scheduler
import foo.starred.jec.utils.mod
import foo.starred.snowbird.utils.open
import moe.nea.libautoupdate.CurrentVersion
import moe.nea.libautoupdate.PotentialUpdate
import moe.nea.libautoupdate.UpdateContext
import moe.nea.libautoupdate.UpdateTarget
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback
import net.fabricmc.fabric.api.client.command.v2.ClientCommands.literal
import net.minecraft.SharedConstants
import java.util.concurrent.CompletableFuture
import kotlin.time.Duration.Companion.seconds

@Load
object ModUpdater {
    private var skippedVersion: String by JEC.main.string("version")

    private val context = UpdateContext(
        ModrinthUpdateSource("H3m1yxQ8", SharedConstants.getCurrentVersion().name()),
        UpdateTarget.deleteAndSaveInTheSameFolder(JEC::class.java),
        current(),
        JEC.modId
    )

    init {
        context.cleanup()

        ClientCommandRegistrationCallback.EVENT.register { d, _ ->
            literal("jec").then(literal("update").executes {
                installUpdate()
                1
            }).apply {
                d.register(this)
            }
        }

        on<LocationEvent.Server.Connect> {
            Scheduler.schedule(3.seconds) {
                checkAndNotify()
            }
        }.once()
    }

    fun checkForUpdate(stream: String = "release"): CompletableFuture<PotentialUpdate> {
        return context.checkUpdate(stream)
    }

    fun checkAndNotify(stream: String = "release", silent: Boolean = true) {
        checkForUpdate(stream).thenAccept { update ->
            if (!silent && !update.isUpdateAvailable) return@thenAccept "No update available!".mod()
            if (!update.isUpdateAvailable) return@thenAccept

            val newVersion = update.update.versionName

            "Update available: $newVersion".mod()
            "Run /${JEC.modId} update to install".mod()

            if (newVersion == skippedVersion) return@thenAccept
            UpdateGUI(JEC.modVersion, newVersion, onUpdate = { installUpdate(stream) }, onSkip = { skippedVersion = newVersion }, onRemind = {}).open()
        }.exceptionally {
            JEC.LOGGER.error("Failed to check for updates: ${it.message}")
            null
        }
    }

    fun installUpdate(stream: String = "release"): CompletableFuture<Boolean> {
        return checkForUpdate(stream).thenCompose { update ->
            if (!update.isUpdateAvailable) {
                "Already on latest version".mod()
                return@thenCompose CompletableFuture.completedFuture(false)
            }

            "Downloading update: ${update.update.versionName}".mod()
            update.launchUpdate().thenApply {
                "Update downloaded! Restart to apply.".mod()
                true
            }
        }.exceptionally {
            "Update failed: ${it.message}".mod()
            JEC.LOGGER.error("Failed to install update: ${it.message}")
            false
        }
    }

    private fun current() = object : CurrentVersion {
        override fun display() = JEC.modVersion

        override fun isOlderThan(element: JsonElement): Boolean {
            if (!element.isJsonPrimitive) return true

            fun String.parse() = removePrefix("v").split('.', '-').map { it.toIntOrNull() ?: 0 }

            val local = JEC.modVersion.parse()
            val remote = element.asString.parse()

            val maxLength = maxOf(local.size, remote.size)
            val l = local + List(maxLength - local.size) { 0 }
            val r = remote + List(maxLength - remote.size) { 0 }

            for (i in 0 until maxLength) {
                if (l[i] < r[i]) return true
                if (l[i] > r[i]) return false
            }

            return false
        }
    }
}
