package xaero.common.minimap.waypoints;

import java.util.ArrayList;

public class WaypointSet {
   private String name;
   private ArrayList<Waypoint> list;

   public WaypointSet(String name) {
      this.name = name;
      this.list = new ArrayList<>();
   }

   public String getName() {
      return this.name;
   }

   public ArrayList<Waypoint> getList() {
      return this.list;
   }
}
