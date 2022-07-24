package moe.badcat.module.modules;

import moe.badcat.BadCat;
import moe.badcat.module.BaseEnableModule;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

public class ModuleMenuManager extends BaseEnableModule {
    private static final Map<String, Boolean> showMenus = new TreeMap<>();

    public ModuleMenuManager(String displayName, boolean defaultShow) {
        super("菜单管理", displayName);

        showMenus.put(displayName, defaultShow);
        if (defaultShow) {
            super.onUse();
        }
    }

    @Override
    public void onUse() {
        super.onUse();

        showMenus.put(displayName, isEnable());
        BadCat.getInstance().getClickGUI().refresh();
    }

    public static boolean shouldShow(String type) {
        return Objects.equals(type, "菜单管理") || showMenus.getOrDefault(type, false);
    }

    @Override
    public String getDisplayText() {
        return String.format("%s [%s]", this.displayName, isEnable() ? "显示" : "隐藏");
    }
}
