package xaero.common.message.server;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.class_3222;
import net.minecraft.class_3244;
import net.minecraft.server.MinecraftServer;
import xaero.common.message.MinimapMessage;

public interface ServerMessageConsumer<T extends MinimapMessage<T>> {
   void handle(MinecraftServer var1, class_3222 var2, class_3244 var3, T var4, PacketSender var5);
}
