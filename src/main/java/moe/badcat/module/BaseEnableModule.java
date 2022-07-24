package moe.badcat.module;

public class BaseEnableModule extends BaseModule {
    protected boolean enable = false;

    public BaseEnableModule(String displayType ,String displayName) {
        super(displayType, displayName);
    }

    public void onUse() {
        enable = !enable;
    }

    public boolean isEnable() {
        return this.enable;
    }

    @Override
    public String getDisplayText() {
        return String.format("%s [%s]", this.displayName, isEnable() ? "开" : "关");
    }
}
