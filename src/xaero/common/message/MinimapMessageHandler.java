package xaero.common.message;

import io.netty.buffer.Unpooled;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.class_2540;
import net.minecraft.class_3222;
import xaero.common.message.client.ClientMessageConsumer;
import xaero.common.message.server.ServerMessageConsumer;

public class MinimapMessageHandler {
   public static final int NETWORK_COMPATIBILITY = 2;
   private Map<Integer, MinimapMessageType<?>> typeByIndex = new HashMap<>();
   private Map<Class<?>, MinimapMessageType<?>> typeByClass = new HashMap<>();

   public <T extends MinimapMessage<T>> void register(
      int index,
      Class<T> type,
      ServerMessageConsumer<T> serverHandler,
      ClientMessageConsumer<T> clientHandler,
      Function<class_2540, T> decoder,
      BiConsumer<T, class_2540> encoder
   ) {
      MinimapMessageType<T> messageType = new MinimapMessageType<>(index, type, serverHandler, clientHandler, decoder, encoder);
      this.typeByIndex.put(index, messageType);
      this.typeByClass.put(type, messageType);
   }

   public MinimapMessageType<?> getByIndex(int index) {
      return this.typeByIndex.get(index);
   }

   public MinimapMessageType<?> getByClass(Class<?> clazz) {
      return this.typeByClass.get(clazz);
   }

   private class_2540 toBuffer(MinimapMessage<?> message) {
      class_2540 buf = new class_2540(Unpooled.buffer());
      MinimapMessageType<?> type = this.typeByClass.get(message.getClass());
      buf.writeByte(type.getIndex());
      this.toBufferHelper(type, message, buf);
      return buf;
   }

   private <T extends MinimapMessage<T>> void toBufferHelper(MinimapMessageType<T> type, MinimapMessage<?> message, class_2540 buf) {
      type.getEncoder().accept((T)message, buf);
   }

   public void sendToPlayer(class_3222 player, MinimapMessage<?> message) {
      ServerPlayNetworking.send(player, MinimapMessage.MAIN_CHANNEL, this.toBuffer(message));
   }

   public void sendToServer(MinimapMessage<?> message) {
      ClientPlayNetworking.send(MinimapMessage.MAIN_CHANNEL, this.toBuffer(message));
   }
}
