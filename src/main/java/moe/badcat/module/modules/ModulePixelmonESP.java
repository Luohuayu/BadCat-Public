package moe.badcat.module.modules;

import java.lang.reflect.Field;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;

public class ModulePixelmonESP extends ModuleEntityESP {
    private Class<?> pixelmonRenderClass;
    private Render<?> pixelmonRender;
    private Field nameRenderDistanceNormalField;
    private Field nameRenderDistanceBossField;

    private int originNameRenderDistanceNormal;
    private int originNameRenderDistanceBoss;

    public ModulePixelmonESP() {
        super("MOD类", "宝可梦透视");
    }

    @Override
    public boolean initModule() {
        try {
            pixelmonRenderClass = Class.forName("com.pixelmonmod.pixelmon.client.render.RenderPixelmon");
            nameRenderDistanceNormalField = pixelmonRenderClass.getDeclaredField("nameRenderDistanceNormal");
            nameRenderDistanceBossField = pixelmonRenderClass.getDeclaredField("nameRenderDistanceBoss");
            nameRenderDistanceNormalField.setAccessible(true);
            nameRenderDistanceBossField.setAccessible(true);
            return true;
        } catch (Exception ignored) {}
        return false;
    }

    @Override
    public void onUse() {
        super.onUse();

        setPixelmonESP(isEnable());

        for (Render<? extends Entity> render : Minecraft.getMinecraft().getRenderManager().entityRenderMap.values()) {
            if (render.getClass() == pixelmonRenderClass) {
                pixelmonRender = render;
                break;
            }
        }

        if (pixelmonRender != null) {
            if (isEnable()) {
                try {
                    originNameRenderDistanceNormal = nameRenderDistanceNormalField.getInt(pixelmonRender);
                    originNameRenderDistanceBoss = nameRenderDistanceBossField.getInt(pixelmonRender);
                    nameRenderDistanceNormalField.setInt(pixelmonRender, 10000);
                    nameRenderDistanceBossField.setInt(pixelmonRender, 10000);
                } catch (Exception e) {
                    if (ModuleDebug.enable) e.printStackTrace();
                }
            } else {
                try {
                    nameRenderDistanceNormalField.setInt(pixelmonRender, originNameRenderDistanceNormal);
                    nameRenderDistanceBossField.setInt(pixelmonRender, originNameRenderDistanceBoss);
                } catch (Exception e) {
                    if (ModuleDebug.enable) e.printStackTrace();
                }
            }
        } else {
            if (ModuleDebug.enable) System.out.println("Can't found RenderPixelmon!");
        }
    }
}
