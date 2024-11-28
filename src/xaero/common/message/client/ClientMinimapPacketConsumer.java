package xaero.common.message.client;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking.PlayChannelHandler;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.class_2540;
import net.minecraft.class_310;
import net.minecraft.class_634;
import xaero.common.message.MinimapMessage;
import xaero.common.message.MinimapMessageHandlerFabric;
import xaero.common.message.type.MinimapMessageType;

public class ClientMinimapPacketConsumer implements PlayChannelHandler {
   private final MinimapMessageHandlerFabric messageHandler;

   public ClientMinimapPacketConsumer(MinimapMessageHandlerFabric messageHandler) {
      this.messageHandler = messageHandler;
   }

   public void receive(class_310 client, class_634 handler, class_2540 buffer, PacketSender responseSender) {
      int index = buffer.readByte();
      MinimapMessageType<?> messageType = this.messageHandler.getByIndex(index);
      this.handleMessageWithType(messageType, client, handler, buffer, responseSender);
   }

   private <T extends MinimapMessage<T>> void handleMessageWithType(
      MinimapMessageType<T> type, class_310 client, class_634 handler, class_2540 buffer, PacketSender responseSender
   ) {
      if (type != null && type.getClientHandler() != null) {
         T message = (T)type.getDecoder().apply(buffer);
         class_310.method_1551().method_18858(() -> type.getClientHandler().handle(message));
      }
   }
}
