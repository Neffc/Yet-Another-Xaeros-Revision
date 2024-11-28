package xaero.common.server.radar.tracker;

import java.util.HashMap;
import java.util.Map;
import xaero.common.MinimapLogs;

public class SyncedPlayerTrackerSystemManager {
   private final Map<String, ISyncedPlayerTrackerSystem> systems = new HashMap<>();

   public void register(String name, ISyncedPlayerTrackerSystem system) {
      if (this.systems.containsKey(name)) {
         MinimapLogs.LOGGER.error("Synced player tracker system with the name " + name + " has already been registered!");
      } else {
         this.systems.put(name, system);
         MinimapLogs.LOGGER.info("Registered synced player tracker system: " + name);
      }
   }

   public Iterable<ISyncedPlayerTrackerSystem> getSystems() {
      return this.systems.values();
   }
}
