package xaero.common.message.server;

import net.minecraft.class_3222;
import net.minecraft.server.MinecraftServer;
import xaero.common.message.MinimapMessage;

public interface ServerMessageConsumer<T extends MinimapMessage<T>> {
   void handle(MinecraftServer var1, class_3222 var2, T var3);
}
