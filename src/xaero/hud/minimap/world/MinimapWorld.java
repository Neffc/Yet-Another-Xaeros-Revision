package xaero.hud.minimap.world;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.class_1937;
import net.minecraft.class_5321;
import xaero.common.minimap.waypoints.WaypointWorld;
import xaero.common.minimap.waypoints.WaypointWorldContainer;
import xaero.hud.minimap.waypoint.set.WaypointSet;
import xaero.hud.minimap.world.container.MinimapWorldContainer;
import xaero.hud.minimap.world.container.config.RootConfig;
import xaero.hud.path.XaeroPath;

public abstract class MinimapWorld {
   private String node;
   private class_5321<class_1937> dimId;
   private MinimapWorldContainer container;
   protected final Map<String, WaypointSet> waypointSets;
   private String currentWaypointSetId;
   private final List<String> toRemoveOnSave;

   protected MinimapWorld(MinimapWorldContainer container, String node, class_5321<class_1937> dimId) {
      this.container = container;
      this.node = node;
      this.waypointSets = new LinkedHashMap<>();
      this.toRemoveOnSave = new ArrayList<>();
      this.dimId = dimId;
      this.currentWaypointSetId = "gui.xaero_default";
      this.addWaypointSet("gui.xaero_default");
   }

   public WaypointSet getCurrentWaypointSet() {
      return this.waypointSets.get(this.currentWaypointSetId);
   }

   public void addWaypointSet(String s) {
      this.waypointSets.put(s, WaypointSet.Builder.begin().setName(s).build());
   }

   public void cleanupOnSave(Path worldFile) throws IOException {
      Path folder = worldFile.getParent();

      for (String s : this.toRemoveOnSave) {
         Path path = folder.resolve(this.node + "_" + s + ".txt");
         Files.deleteIfExists(path);
      }
   }

   public XaeroPath getLocalWorldKey() {
      XaeroPath containerKey = this.container.getPath();
      return containerKey.getNodeCount() < 2 ? XaeroPath.root(this.node) : containerKey.getSubPath(1).resolve(this.node);
   }

   public WaypointSet addWaypointSet(WaypointSet set) {
      return this.waypointSets.put(set.getName(), set);
   }

   public WaypointSet getWaypointSet(String key) {
      return this.waypointSets.get(key);
   }

   public WaypointSet removeWaypointSet(String key) {
      return this.waypointSets.remove(key);
   }

   public Iterable<WaypointSet> getIterableWaypointSets() {
      return this.waypointSets.values();
   }

   public String getCurrentWaypointSetId() {
      return this.currentWaypointSetId;
   }

   public void setCurrentWaypointSetId(String currentWaypointSetId) {
      this.currentWaypointSetId = currentWaypointSetId;
   }

   public String getNode() {
      return this.node;
   }

   public XaeroPath getFullPath() {
      return this.container.getPath().resolve(this.node);
   }

   public void setNode(String node) {
      this.node = node;
   }

   public MinimapWorldContainer getContainer() {
      return this.container;
   }

   public void setContainer(MinimapWorldContainer container) {
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

   public void setDimId(class_5321<class_1937> dimId) {
      this.dimId = dimId;
   }

   public int getSetCount() {
      return this.waypointSets.size();
   }

   public RootConfig getRootConfig() {
      return this.getContainer().getRootConfig();
   }

   public static final class Builder {
      private MinimapWorldContainer container;
      private String node;
      private class_5321<class_1937> dimId;

      private Builder() {
      }

      private MinimapWorld.Builder setDefault() {
         this.setContainer(null);
         this.setNode(null);
         this.setDimId(null);
         return this;
      }

      public MinimapWorld.Builder setContainer(MinimapWorldContainer container) {
         this.container = container;
         return this;
      }

      public MinimapWorld.Builder setNode(String node) {
         this.node = node;
         return this;
      }

      public MinimapWorld.Builder setDimId(class_5321<class_1937> dimId) {
         this.dimId = dimId;
         return this;
      }

      public MinimapWorld build() {
         if (this.container == null || this.node == null) {
            throw new IllegalStateException();
         } else if (!(this.container instanceof WaypointWorldContainer)) {
            throw new IllegalStateException("invalid world container class");
         } else {
            return new WaypointWorld((WaypointWorldContainer)this.container, this.node, this.dimId);
         }
      }

      public static MinimapWorld.Builder begin() {
         return new MinimapWorld.Builder().setDefault();
      }
   }
}
