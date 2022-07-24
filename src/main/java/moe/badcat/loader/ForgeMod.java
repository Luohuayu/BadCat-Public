package moe.badcat.loader;

import moe.badcat.BadCat;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLLoadCompleteEvent;

@Mod(modid = "badcat")
public class ForgeMod {
    @Mod.EventHandler
    public void init(FMLLoadCompleteEvent event) {
        BadCat.getInstance().init();
        new Thread(() -> Minecraft.getMinecraft().addScheduledTask(() -> Loader.instance().getActiveModList().removeIf(modContainer -> modContainer.getMod() == this))).start();
    }
}
