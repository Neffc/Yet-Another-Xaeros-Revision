package xaero.common.minimap.waypoints;

import java.util.HashMap;
import net.minecraft.class_1937;
import net.minecraft.class_5321;
import xaero.hud.minimap.world.MinimapWorld;
import xaero.hud.path.XaeroPath;

@Deprecated
public class WaypointWorld extends MinimapWorld {
   private final HashMap<String, Boolean> serverWaypointsDisabled = new HashMap<>();
   private final HashMap<Integer, Waypoint> serverWaypoints = new HashMap<>();

   @Deprecated
   public WaypointWorld(WaypointWorldContainer container, String id, class_5321<class_1937> dimId) {
      super(container, id, dimId);
   }

   private String worldPathToOldString(XaeroPath path) {
      return path == null ? null : path.getParent().toString() + "_" + path.getLastNode();
   }

   @Deprecated
   public WaypointSet getCurrentSet() {
      return (WaypointSet)super.getCurrentWaypointSet();
   }

   @Deprecated
   public void addSet(String s) {
      super.addWaypointSet(s);
   }

   @Deprecated
   public String getInternalWorldKey() {
      return super.getLocalWorldKey().toString();
   }

   @Deprecated
   public HashMap<String, Boolean> getServerWaypointsDisabled() {
      return this.serverWaypointsDisabled;
   }

   @Deprecated
   public HashMap<Integer, Waypoint> getServerWaypoints() {
      return this.serverWaypoints;
   }

   @Deprecated
   public HashMap<String, WaypointSet> getSets() {
      return (HashMap<String, WaypointSet>)this.waypointSets;
   }

   @Deprecated
   public String getCurrent() {
      return super.getCurrentWaypointSetId();
   }

   @Deprecated
   public void setCurrent(String current) {
      super.setCurrentWaypointSetId(current);
   }

   @Deprecated
   public String getId() {
      return this.getNode();
   }

   @Deprecated
   public String getFullId() {
      return this.worldPathToOldString(this.getFullPath());
   }

   @Deprecated
   public void setId(String id) {
      super.setNode(id);
   }

   @Deprecated
   public WaypointWorldContainer getContainer() {
      return (WaypointWorldContainer)super.getContainer();
   }

   @Deprecated
   public void setContainer(WaypointWorldContainer container) {
      super.setContainer(container);
   }

   @Deprecated
   @Override
   public class_5321<class_1937> getDimId() {
      return super.getDimId();
   }
}
