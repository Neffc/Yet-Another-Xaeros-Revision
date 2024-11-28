package xaero.common.minimap.radar.tracker.system.impl;

import java.util.Iterator;
import net.minecraft.class_310;
import xaero.common.XaeroMinimapSession;
import xaero.common.minimap.mcworld.MinimapClientWorldData;
import xaero.common.minimap.mcworld.MinimapClientWorldDataHelper;
import xaero.common.minimap.radar.tracker.synced.ClientSyncedTrackedPlayerManager;
import xaero.common.minimap.radar.tracker.system.IPlayerTrackerSystem;
import xaero.common.minimap.radar.tracker.system.ITrackedPlayerReader;
import xaero.common.server.radar.tracker.SyncedTrackedPlayer;

public class SyncedPlayerTrackerSystem implements IPlayerTrackerSystem<SyncedTrackedPlayer> {
   private final SyncedTrackedPlayerReader reader = new SyncedTrackedPlayerReader();

   @Override
   public ITrackedPlayerReader<SyncedTrackedPlayer> getReader() {
      return this.reader;
   }

   @Override
   public Iterator<SyncedTrackedPlayer> getTrackedPlayerIterator() {
      XaeroMinimapSession minimapSession = XaeroMinimapSession.getCurrentSession();
      if (minimapSession == null) {
         return null;
      } else {
         if (class_310.method_1551().method_1576() == null) {
            MinimapClientWorldData worldData = MinimapClientWorldDataHelper.getCurrentWorldData();
            if (worldData.serverLevelId == null) {
               return null;
            }
         }

         ClientSyncedTrackedPlayerManager manager = minimapSession.getMinimapProcessor().getClientSyncedTrackedPlayerManager();
         return manager.getPlayers().iterator();
      }
   }

   public boolean shouldUseWorldMapTrackedPlayers(XaeroMinimapSession minimapSession) {
      return !minimapSession.getMinimapProcessor().serverHasMod();
   }
}
