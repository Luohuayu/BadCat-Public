package moe.badcat.module.modules;

import moe.badcat.module.BaseEnableModule;
import net.minecraft.client.Minecraft;

public class ModuleFullbright extends BaseEnableModule {
    private float oldGamma = 0;

    public ModuleFullbright() {
        super("原版类", "夜视");
    }

    @Override
    public void onUse() {
        super.onUse();

        if (isEnable()) {
            oldGamma = Minecraft.getMinecraft().gameSettings.gammaSetting;
            Minecraft.getMinecraft().gameSettings.gammaSetting = 100.0f;
        } else {
            Minecraft.getMinecraft().gameSettings.gammaSetting = Math.max(oldGamma, 1.0f);
        }
    }
}
