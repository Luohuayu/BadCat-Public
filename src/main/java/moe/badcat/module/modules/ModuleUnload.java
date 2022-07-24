package moe.badcat.module.modules;

import moe.badcat.hook.QueueHook;
import moe.badcat.module.BaseModule;
import java.io.IOException;
import java.net.URLClassLoader;
import net.minecraft.client.Minecraft;

public class ModuleUnload extends BaseModule {
    public ModuleUnload() {
        super("其他", "卸载类加载器(危)");
    }

    @Override
    public boolean initModule() {
        ClassLoader selfClassLoader = getClass().getClassLoader();
        return (selfClassLoader != null && selfClassLoader.getClass() == URLClassLoader.class);
    }

    @Override
    public void onUse() {
        ClassLoader selfClassLoader = getClass().getClassLoader();
        if (selfClassLoader != null && selfClassLoader.getClass() == URLClassLoader.class) {
            try {
                ModuleEntityESP.disable(true);
                Minecraft.getMinecraft().displayGuiScreen(null);
                QueueHook.unhook();
                ((URLClassLoader)selfClassLoader).close();
            } catch (IOException ignored) {}
        }
    }
}
