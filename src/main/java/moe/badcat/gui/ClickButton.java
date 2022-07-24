package moe.badcat.gui;

import moe.badcat.module.BaseModule;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;

public class ClickButton extends GuiButton {
    private final BaseModule module;

    public ClickButton(int indexX, int indexY, String buttonText, BaseModule module) {
        super(65535, 10 + 80 * indexX, 10 * indexY, 50, 10, buttonText);
        this.module = module;
    }

    public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
        if (mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height) {
            if (module != null) module.onUse();
        }
        return false;
    }

    public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
        this.hovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;

        int textColor = this.module != null && this.hovered ? 0xFFFFA0 : 0xE0E0E0;
        String displayString = this.module != null ? this.module.getDisplayText() : this.displayString;
        this.drawCenteredString(mc.fontRenderer, displayString , this.x + this.width / 2, this.y + (this.height - 8) / 2, textColor);
    }
}
