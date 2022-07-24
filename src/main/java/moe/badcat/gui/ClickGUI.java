package moe.badcat.gui;

import moe.badcat.BadCat;
import moe.badcat.module.BaseModule;
import moe.badcat.module.modules.ModuleDebug;
import moe.badcat.module.modules.ModuleMenuManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiScreen;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

public class ClickGUI {
    private final Field buttonListFiled;

    public ClickGUI() {
        try {
            buttonListFiled = GuiScreen.class.getDeclaredField(BadCat.getInstance().isDeobfEnv() ? "buttonList" : "field_146292_n");
            buttonListFiled.setAccessible(true);
        } catch (Exception e) {
            throw new RuntimeException("Menu init fail");
        }
    }

    public void initGui(List<GuiButton> buttonList) {
        int x = 0, y = 0;
        for (Map.Entry<String, List<BaseModule>> entry : BadCat.getInstance().getModuleManager().getModules().entrySet()) {
            if (ModuleMenuManager.shouldShow(entry.getKey()) && entry.getValue().size() > 0) {
                buttonList.add(new ClickButton(x, y++, String.format("[%s]", entry.getKey()), null));
                for (BaseModule module : entry.getValue()) {
                    buttonList.add(new ClickButton(x, y++,"", module));
                }
                x++;
            }
            y = 0;
        }
    }

    public void tick() {
        GuiScreen currentScreen = Minecraft.getMinecraft().currentScreen;
        if (currentScreen instanceof GuiChat) {
            try {
                List<GuiButton> buttonList = (List<GuiButton>) buttonListFiled.get(currentScreen);
                if (buttonList.stream().noneMatch(button -> button.getClass() == ClickButton.class)) {
                    initGui(buttonList);
                }
            } catch (Exception e) {
                if (ModuleDebug.enable) e.printStackTrace();
            }
        }
    }

    public void refresh() {
        GuiScreen currentScreen = Minecraft.getMinecraft().currentScreen;
        if (currentScreen instanceof GuiChat) {
            try {
                ((List<GuiButton>) buttonListFiled.get(currentScreen)).clear();
            } catch (Exception e) {
                if (ModuleDebug.enable) e.printStackTrace();
            }
        }
    }
}
