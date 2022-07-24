package moe.badcat.module.modules;

import moe.badcat.module.BaseEnableModule;

public class ModuleDebug extends BaseEnableModule {
    public static boolean enable = false;

    public ModuleDebug() {
        super("其他", "调试模式");
    }

    @Override
    public void onUse() {
        super.onUse();

        enable = isEnable();
    }
}
