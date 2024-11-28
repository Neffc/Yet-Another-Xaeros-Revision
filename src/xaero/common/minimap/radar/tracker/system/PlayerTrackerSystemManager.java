package xaero.common.minimap.radar.tracker.system;

import java.util.HashMap;
import java.util.Map;
import xaero.common.MinimapLogs;

public class PlayerTrackerSystemManager {
   private final Map<String, IPlayerTrackerSystem<?>> systems = new HashMap<>();

   public void register(String name, IPlayerTrackerSystem<?> system) {
      if (this.systems.containsKey(name)) {
         MinimapLogs.LOGGER.error("Player tracker system with the name " + name + " has already been registered!");
      } else {
         this.systems.put(name, system);
         MinimapLogs.LOGGER.info("Registered player tracker system: " + name);
      }
   }

   public Iterable<IPlayerTrackerSystem<?>> getSystems() {
      return this.systems.values();
   }
}
