package xaero.common.minimap.waypoints;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import net.minecraft.class_1937;
import net.minecraft.class_5321;

public class WaypointWorld {
   private String id;
   private HashMap<String, WaypointSet> sets;
   private HashMap<Integer, Waypoint> serverWaypoints;
   private HashMap<String, Boolean> serverWaypointsDisabled;
   private String current = "gui.xaero_default";
   private WaypointWorldContainer container;
   private List<String> toRemoveOnSave;
   private final class_5321<class_1937> dimId;

   public WaypointWorld(WaypointWorldContainer container, String id, class_5321<class_1937> dimId) {
      this.container = container;
      this.id = id;
      this.sets = new HashMap<>();
      this.serverWaypoints = new HashMap<>();
      this.serverWaypointsDisabled = new HashMap<>();
      this.addSet("gui.xaero_default");
      this.toRemoveOnSave = new ArrayList<>();
      this.dimId = dimId;
   }

   public WaypointSet getCurrentSet() {
      return this.sets.get(this.current);
   }

   public void addSet(String s) {
      this.sets.put(s, new WaypointSet(s));
   }

   public void onSaveCleanup(File worldFile) throws IOException {
      Path folder = worldFile.toPath().getParent();

      for (int i = 0; i < this.toRemoveOnSave.size(); i++) {
         String s = this.toRemoveOnSave.get(i);
         Path path = folder.resolve(this.id + "_" + s + ".txt");
         Files.deleteIfExists(path);
      }
   }

   public String getInternalWorldKey() {
      String containerKey = this.container.getKey();
      StringBuilder keyBuilder = new StringBuilder();
      if (containerKey.contains("/")) {
         keyBuilder.append(containerKey.substring(containerKey.indexOf(47) + 1));
         keyBuilder.append("/");
      }

      keyBuilder.append(this.id);
      return keyBuilder.toString();
   }

   public HashMap<String, Boolean> getServerWaypointsDisabled() {
      return this.serverWaypointsDisabled;
   }

   public HashMap<Integer, Waypoint> getServerWaypoints() {
      return this.serverWaypoints;
   }

   public HashMap<String, WaypointSet> getSets() {
      return this.sets;
   }

   public String getCurrent() {
      return this.current;
   }

   public void setCurrent(String current) {
      this.current = current;
   }

   public String getId() {
      return this.id;
   }

   public String getFullId() {
      return this.container.getKey() + "_" + this.id;
   }

   public void setId(String id) {
      this.id = id;
   }

   public WaypointWorldContainer getContainer() {
      return this.container;
   }

   public void setContainer(WaypointWorldContainer container) {
      this.container = container;
   }

   public void requestRemovalOnSave(String name) {
      this.toRemoveOnSave.add(name);
   }

   public boolean hasSomethingToRemoveOnSave() {
      return !this.toRemoveOnSave.isEmpty();
   }

   public class_5321<class_1937> getDimId() {
      return this.dimId;
   }
}
