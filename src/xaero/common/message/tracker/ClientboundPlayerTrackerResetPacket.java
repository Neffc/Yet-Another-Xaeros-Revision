package xaero.common.message.tracker;

import net.minecraft.class_2540;
import xaero.common.XaeroMinimapSession;
import xaero.common.message.MinimapMessage;
import xaero.common.message.client.ClientMessageConsumer;

public class ClientboundPlayerTrackerResetPacket extends MinimapMessage<ClientboundPlayerTrackerResetPacket> {
   public void write(class_2540 buffer) {
   }

   public static ClientboundPlayerTrackerResetPacket read(class_2540 buffer) {
      return new ClientboundPlayerTrackerResetPacket();
   }

   public static class Handler implements ClientMessageConsumer<ClientboundPlayerTrackerResetPacket> {
      public void handle(ClientboundPlayerTrackerResetPacket t) {
         XaeroMinimapSession minimapSession = XaeroMinimapSession.getCurrentSession();
         if (minimapSession != null) {
            minimapSession.getMinimapProcessor().getClientSyncedTrackedPlayerManager().reset();
         }
      }
   }
}
