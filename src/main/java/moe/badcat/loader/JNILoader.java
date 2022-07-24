package moe.badcat.loader;

import moe.badcat.BadCat;
import net.minecraft.client.Minecraft;

public class JNILoader {
    static {
        Minecraft.getMinecraft().addScheduledTask(() -> BadCat.getInstance().init());
    }
}
