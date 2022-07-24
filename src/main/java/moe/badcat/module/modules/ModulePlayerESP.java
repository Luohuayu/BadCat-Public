package moe.badcat.module.modules;

public class ModulePlayerESP extends ModuleEntityESP {
    public ModulePlayerESP() {
        super("原版类", "玩家透视");
    }

    @Override
    public void onUse() {
        super.onUse();

        setPlayerESP(isEnable());
    }
}
