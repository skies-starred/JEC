package foo.starred.jec.updater

import foo.starred.jec.JEC
import foo.starred.jec.utils.Catppuccin.Mocha
import foo.starred.jec.utils.RenderUtils.drawOutline
import foo.starred.jec.utils.RenderUtils.drawRectangle
import foo.starred.jec.utils.RenderUtils.text
import foo.starred.jec.utils.message
import foo.starred.snowbird.api.client
import foo.starred.snowbird.handlers.minecraft.AbstractScreen
import net.minecraft.client.gui.GuiGraphicsExtractor

class UpdateGUI(
    private val currentVersion: String,
    private val newVersion: String,
    private val onUpdate: () -> Unit,
    private val onSkip: () -> Unit,
    private val onRemind: () -> Unit
) : AbstractScreen("Update GUI [JEC]") {
    private var booling = false

    override fun isPauseScreen(): Boolean {
        return false
    }

    override fun onScramRender(graphics: GuiGraphicsExtractor, mouseX: Int, mouseY: Int, delta: Float) {
        graphics.drawRectangle(0, 0, width, height, Mocha.Crust.withAlpha(0.6f))
        graphics.drawPanel(mouseX, mouseY, (width - 360) / 2, (height - 175) / 2)
    }

    private fun GuiGraphicsExtractor.drawPanel(mouseX: Int, mouseY: Int, px: Int, py: Int) {
        drawRectangle(px, py, 360, 28, Mocha.Base.argb)
        drawRectangle(px, py + 28, 360, 175 - 28, Mocha.Mantle.argb)
        drawOutline(px, py, 360, 175, 1, Mocha.Surface0.argb)
        drawRectangle(px, py + 28, 360, 1, Mocha.Surface0.argb)

        text("Update available for ${JEC.modName}", px + 16, py + 10, false, Mocha.Mauve.argb)

        val a = client.font.lineHeight + 6
        val b = py + 40

        text("Current version:", px + 16, b, false, Mocha.Subtext0.argb)
        text(currentVersion, px + 344 - client.font.width(currentVersion), b, false, Mocha.Text.argb)

        text("New version:", px + 16, b + a, false, Mocha.Subtext0.argb)
        text(newVersion, px + 344 - client.font.width(newVersion), b + a, false, Mocha.Green.argb)

        drawRectangle(px + 16, b + a + 30, 330, 1, Mocha.Surface0.argb)

        drawButton(mouseX, mouseY, px + 16, py + 175 - 34, "Update Now",   Mocha.Green.argb)
        drawButton(mouseX, mouseY, px + 128, py + 175 - 34, "Remind Later", Mocha.Peach.argb)
        drawButton(mouseX, mouseY, px + 240, py + 175 - 34, if (booling) "Confirm?" else "Skip Version", Mocha.Red.argb)
    }

    private fun GuiGraphicsExtractor.drawButton( mouseX: Int, mouseY: Int, x: Int, y: Int, label: String, color: Int) {
        val b = mouseX in x until x + 104 && mouseY in y until y + 22
        drawRectangle(x, y, 104, 22, if (b) color else Mocha.Surface1.argb)
        drawOutline(x, y, 104, 22, 1, color)
        text(label, x + (104 - client.font.width(label)) / 2, y + (22 - client.font.lineHeight) / 2 + 1, false, if (b) Mocha.Base.argb else color)
    }

    override fun onScramMouseClick(mouseX: Int, mouseY: Int, button: Int): Boolean {
        if (button != 0) return super.onScramMouseClick(mouseX, mouseY, button)

        val x = (width - 360) / 2 + 16
        val y = (height - 175) / 2 + 141

        fun fn(i: Int): Boolean {
            val xo = x + i * (104 + 8)
            return mouseX in xo until xo + 104 && mouseY in y until y + 22
        }

        when {
            fn(0) -> {
                onUpdate()
                onClose()
            }

            fn(1) -> {
                if (booling) {
                    booling = false
                    return true
                }

                onRemind()
                "Will remind to update for version $newVersion on next launch".message()
                onClose()
            }

            fn(2) -> {
                if (booling) {
                    onSkip()
                    "Skipped update for version $newVersion".message()
                    onClose()
                    return true
                }

                booling = true
            }

            else -> return super.onScramMouseClick(mouseX, mouseY, button)
        }

        return true
    }
}