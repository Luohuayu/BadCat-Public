package moe.badcat.module;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ModuleManager {
    private Map<String, List<BaseModule>> modules = new LinkedHashMap<String, List<BaseModule>>();

    public void registerModule(BaseModule module) {
        if (module.initModule()) {
            modules.computeIfAbsent(module.getType(), key -> new ArrayList<BaseModule>()).add(module);
        }
    }

    public Map<String, List<BaseModule>> getModules() {
        return this.modules;
    }

    public void tick() {
        for (List<BaseModule> modules : this.modules.values()) {
            for (BaseModule module : modules) {
                if (module instanceof ITickable) {
                    ((ITickable) module).tick();
                }
            }
        }
    }
}
