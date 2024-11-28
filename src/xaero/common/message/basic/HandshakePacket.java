package xaero.common.message.basic;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.class_2540;
import net.minecraft.class_3222;
import net.minecraft.class_3244;
import net.minecraft.server.MinecraftServer;
import xaero.common.XaeroMinimapSession;
import xaero.common.message.MinimapMessage;
import xaero.common.message.client.ClientMessageConsumer;
import xaero.common.message.server.ServerMessageConsumer;
import xaero.common.server.player.ServerPlayerData;

public class HandshakePacket extends MinimapMessage<HandshakePacket> {
   private final int networkVersion;

   public HandshakePacket(int networkVersion) {
      this.networkVersion = networkVersion;
   }

   public HandshakePacket() {
      this(2);
   }

   public void write(class_2540 u) {
      u.writeInt(this.networkVersion);
   }

   public static HandshakePacket read(class_2540 buffer) {
      return new HandshakePacket(buffer.readInt());
   }

   public static class ClientHandler implements ClientMessageConsumer<HandshakePacket> {
      public void handle(HandshakePacket message, PacketSender responseSender) {
         XaeroMinimapSession session = XaeroMinimapSession.getCurrentSession();
         if (session != null) {
            session.getMinimapProcessor().setServerModNetworkVersion(message.networkVersion);
            session.getModMain().getMessageHandler().sendToServer(new HandshakePacket());
         }
      }
   }

   public static class ServerHandler implements ServerMessageConsumer<HandshakePacket> {
      public void handle(MinecraftServer server, class_3222 player, class_3244 handler, HandshakePacket message, PacketSender responseSender) {
         ServerPlayerData playerData = ServerPlayerData.get(player);
         playerData.setClientModNetworkVersion(message.networkVersion);
      }
   }
}
