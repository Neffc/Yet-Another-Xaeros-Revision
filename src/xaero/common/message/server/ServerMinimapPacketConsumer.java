package xaero.common.message.server;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking.PlayChannelHandler;
import net.minecraft.class_2540;
import net.minecraft.class_3222;
import net.minecraft.class_3244;
import net.minecraft.server.MinecraftServer;
import xaero.common.message.MinimapMessage;
import xaero.common.message.MinimapMessageHandlerFabric;
import xaero.common.message.MinimapMessageType;

public class ServerMinimapPacketConsumer implements PlayChannelHandler {
   private final MinimapMessageHandlerFabric messageHandler;

   public ServerMinimapPacketConsumer(MinimapMessageHandlerFabric messageHandler) {
      this.messageHandler = messageHandler;
   }

   public void receive(MinecraftServer server, class_3222 player, class_3244 handler, class_2540 buffer, PacketSender responseSender) {
      int index = buffer.readByte();
      MinimapMessageType<?> messageType = this.messageHandler.getByIndex(index);
      this.handleMessageWithType(messageType, server, player, handler, buffer, responseSender);
   }

   private <T extends MinimapMessage<T>> void handleMessageWithType(
      MinimapMessageType<T> type, MinecraftServer server, class_3222 player, class_3244 handler, class_2540 buffer, PacketSender responseSender
   ) {
      if (type != null && type.getServerHandler() != null) {
         T message = (T)type.getDecoder().apply(buffer);
         server.method_20493(() -> type.getServerHandler().handle(server, player, message));
      }
   }
}
