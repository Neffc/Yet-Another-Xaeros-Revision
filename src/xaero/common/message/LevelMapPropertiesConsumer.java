package xaero.common.message;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import xaero.common.XaeroMinimapSession;
import xaero.common.message.client.ClientMessageConsumer;
import xaero.common.server.level.LevelMapProperties;

public class LevelMapPropertiesConsumer implements ClientMessageConsumer<LevelMapProperties> {
   public void handle(LevelMapProperties t, PacketSender responseSender) {
      XaeroMinimapSession minimapSession = XaeroMinimapSession.getCurrentSession();
      minimapSession.getWaypointsManager().onServerLevelId(t.getId());
   }
}
