package xaero.common.message.client;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import xaero.common.message.MinimapMessage;

public interface ClientMessageConsumer<T extends MinimapMessage<T>> {
   void handle(T var1, PacketSender var2);
}
