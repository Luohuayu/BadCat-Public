package moe.badcat.module.modules;

import moe.badcat.module.BaseModule;

public class ModulePacketDelay extends BaseModule {
    public static int delay = 1;

    public ModulePacketDelay() {
        super("发包崩服", "发包延迟");
    }

    @Override
    public void onUse() {
        if (delay == 0) delay = 1;
        else if (delay == 1) delay = 5;
        else if (delay == 5) delay = 10;
        else if (delay == 10) delay = 20;
        else if (delay == 20) delay = 30;
        else if (delay == 30) delay = 50;
        else if (delay == 50) delay = 100;
        else delay = 0;
    }

    @Override
    public String getDisplayText() {
        return String.format("%s [%d]", this.displayName, delay);
    }
}
