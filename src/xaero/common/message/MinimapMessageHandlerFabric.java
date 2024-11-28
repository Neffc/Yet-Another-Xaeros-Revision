package xaero.common.message;

import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.class_2540;
import net.minecraft.class_3222;
import xaero.common.message.type.MinimapMessageType;

public class MinimapMessageHandlerFabric extends MinimapMessageHandlerFull {
   private class_2540 toBuffer(MinimapMessage<?> message) {
      class_2540 buf = new class_2540(Unpooled.buffer());
      MinimapMessageType<?> type = this.messageTypes.getByClass(message.getClass());
      this.encodeMessage(type, message, buf);
      return buf;
   }

   @Override
   public <T extends MinimapMessage<T>> void sendToPlayer(class_3222 player, T message) {
      ServerPlayNetworking.send(player, MinimapMessage.MAIN_CHANNEL, this.toBuffer(message));
   }

   @Override
   public <T extends MinimapMessage<T>> void sendToServer(T message) {
      ClientPlayNetworking.send(MinimapMessage.MAIN_CHANNEL, this.toBuffer(message));
   }
}
