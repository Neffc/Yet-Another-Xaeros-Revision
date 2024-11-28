package xaero.common.minimap.waypoints.render;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import xaero.common.IXaeroMinimap;
import xaero.common.MinimapLogs;
import xaero.common.minimap.waypoints.Waypoint;
import xaero.common.minimap.waypoints.WaypointSet;
import xaero.common.minimap.waypoints.WaypointWorld;

public class WaypointDeleter {
   private final IXaeroMinimap modMain;
   private final List<Waypoint> toDeleteList;
   private boolean started;

   public WaypointDeleter(IXaeroMinimap modMain) {
      this.modMain = modMain;
      this.toDeleteList = new ArrayList<>();
   }

   public void begin() {
      this.started = true;
   }

   public void add(Waypoint w) {
      if (!this.started) {
         throw new IllegalStateException();
      } else {
         this.toDeleteList.add(w);
      }
   }

   public void deleteCollected(WaypointWorld world, boolean allSets) {
      if (!this.started) {
         throw new IllegalStateException();
      } else {
         this.started = false;
         if (!this.toDeleteList.isEmpty()) {
            if (world != null) {
               if (allSets) {
                  HashMap<String, WaypointSet> sets = world.getSets();

                  for (Entry<String, WaypointSet> setEntry : sets.entrySet()) {
                     setEntry.getValue().getList().removeAll(this.toDeleteList);
                  }
               } else {
                  world.getCurrentSet().getList().removeAll(this.toDeleteList);
               }

               try {
                  this.modMain.getSettings().saveWaypoints(world);
               } catch (IOException var6) {
                  MinimapLogs.LOGGER.error("suppressed exception", var6);
               }
            }

            this.toDeleteList.clear();
         }
      }
   }
}
