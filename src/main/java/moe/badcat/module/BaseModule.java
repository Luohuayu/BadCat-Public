package moe.badcat.module;

public class BaseModule {
    protected final String type;
    protected final String displayName;

    public BaseModule(String type, String displayName) {
        this.type = type;
        this.displayName = displayName;
    }

    public boolean initModule() {
        return true;
    }

    public void onUse() { }

    public String getType() {
        return this.type;
    }

    public String getDisplayText() {
        return String.format("%s", this.displayName);
    }
}
