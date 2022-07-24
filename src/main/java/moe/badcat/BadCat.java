package moe.badcat;

import moe.badcat.gui.ClickGUI;
import moe.badcat.hook.QueueHook;
import moe.badcat.module.ModuleManager;
import moe.badcat.module.modules.ModuleAE2Crash;
import moe.badcat.module.modules.ModuleAWCrash;
import moe.badcat.module.modules.ModuleAntiClientCrash;
import moe.badcat.module.modules.ModuleArmorBreaker;
import moe.badcat.module.modules.ModuleAuthMeBypass;
import moe.badcat.module.modules.ModuleCFMCrash;
import moe.badcat.module.modules.ModuleDebug;
import moe.badcat.module.modules.ModuleDragonCore;
import moe.badcat.module.modules.ModuleEntityESP;
import moe.badcat.module.modules.ModuleFakeGamemode;
import moe.badcat.module.modules.ModuleFly;
import moe.badcat.module.modules.ModuleFullbright;
import moe.badcat.module.modules.ModuleKillaura;
import moe.badcat.module.modules.ModuleLSDESP;
import moe.badcat.module.modules.ModuleMenuManager;
import moe.badcat.module.modules.ModuleMouse;
import moe.badcat.module.modules.ModulePacketDelay;
import moe.badcat.module.modules.ModulePixelmonCrash;
import moe.badcat.module.modules.ModulePixelmonESP;
import moe.badcat.module.modules.ModulePixelmonRidingCrash;
import moe.badcat.module.modules.ModulePlayerESP;
import moe.badcat.module.modules.ModuleRecipeCrash;
import moe.badcat.module.modules.ModuleSkinSave;
import moe.badcat.module.modules.ModuleTimer;
import moe.badcat.module.modules.ModuleUnload;
import moe.badcat.module.modules.ModuleXRay;
import moe.badcat.utils.ReflectionUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.util.text.ChatType;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.relauncher.CoreModManager;

public class BadCat {
    private final static BadCat INSTANCE = new BadCat();
    private final static Minecraft minecraft = Minecraft.getMinecraft();
    private final static boolean deobfuscatedEnvironment = (boolean)ReflectionUtils.getPrivateField(CoreModManager.class, null, "deobfuscatedEnvironment");

    private ModuleManager moduleManager;
    private ClickGUI clickGUI;

    public static BadCat getInstance() {
        return INSTANCE;
    }

    public void init() {
        moduleManager = new ModuleManager();
        clickGUI = new ClickGUI();
        QueueHook.hook();
        registerModules();
    }

    public boolean isDeobfEnv() {
        return deobfuscatedEnvironment;
    }

    public void registerModules() {
        // 原版类
        moduleManager.registerModule(new ModuleMenuManager("原版类", true));
        moduleManager.registerModule(new ModuleXRay());
        moduleManager.registerModule(new ModuleFly());
        moduleManager.registerModule(new ModuleFullbright());
        moduleManager.registerModule(new ModulePlayerESP());
        moduleManager.registerModule(new ModuleKillaura());
        moduleManager.registerModule(new ModuleTimer());
        moduleManager.registerModule(new ModuleArmorBreaker());
        moduleManager.registerModule(new ModuleMouse());
        moduleManager.registerModule(new ModuleLSDESP());
        // 漏洞类
        moduleManager.registerModule(new ModuleMenuManager("漏洞类", false));
        moduleManager.registerModule(new ModuleDragonCore());
        moduleManager.registerModule(new ModuleAuthMeBypass());
        // MOD类
        moduleManager.registerModule(new ModuleMenuManager("MOD类", true));
        moduleManager.registerModule(new ModuleSkinSave());
        moduleManager.registerModule(new ModulePixelmonESP());
        // 发包崩服
        moduleManager.registerModule(new ModuleMenuManager("发包崩服", true));
        moduleManager.registerModule(new ModulePacketDelay());
        moduleManager.registerModule(new ModuleRecipeCrash());
        moduleManager.registerModule(new ModuleAE2Crash());
        moduleManager.registerModule(new ModuleCFMCrash());
        moduleManager.registerModule(new ModuleAWCrash());
        moduleManager.registerModule(new ModulePixelmonCrash());
        moduleManager.registerModule(new ModulePixelmonRidingCrash());
        // 其他
        moduleManager.registerModule(new ModuleMenuManager("其他", true));
        moduleManager.registerModule(new ModuleDebug());
        moduleManager.registerModule(new ModuleAntiClientCrash());
        moduleManager.registerModule(new ModuleFakeGamemode());
        moduleManager.registerModule(new ModuleUnload());
    }

    public ModuleManager getModuleManager() {
        return moduleManager;
    }

    public void onTick() {
        this.clickGUI.tick();
        this.moduleManager.tick();
        ModuleEntityESP.disable(false);
    }

    public boolean isClickGuiClose() {
        return !(minecraft.currentScreen instanceof GuiChat);
    }

    public void addChatMessage(String message) {
        minecraft.ingameGUI.addChatMessage(ChatType.CHAT, new TextComponentString(message));
    }

    public ClickGUI getClickGUI() {
        return clickGUI;
    }
}
