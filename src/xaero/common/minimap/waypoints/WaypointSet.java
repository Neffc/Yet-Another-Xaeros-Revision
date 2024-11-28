package xaero.common.minimap.waypoints;

import java.util.ArrayList;

@Deprecated
public class WaypointSet extends xaero.hud.minimap.waypoint.set.WaypointSet {
   @Deprecated
   public WaypointSet(String name) {
      super(name);
   }

   @Deprecated
   @Override
   public String getName() {
      return super.getName();
   }

   @Deprecated
   public ArrayList<Waypoint> getList() {
      return (ArrayList<Waypoint>)this.list;
   }
}
