package xaero.hud.minimap.world.container;

import com.google.common.collect.Iterables;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import net.minecraft.class_1937;
import net.minecraft.class_5321;
import xaero.common.HudMod;
import xaero.common.minimap.waypoints.WaypointWorldContainer;
import xaero.hud.minimap.MinimapLogs;
import xaero.hud.minimap.module.MinimapSession;
import xaero.hud.minimap.waypoint.server.ServerWaypointManager;
import xaero.hud.minimap.world.MinimapWorld;
import xaero.hud.minimap.world.container.config.RootConfig;
import xaero.hud.path.XaeroPath;

public abstract class MinimapWorldContainer {
   private final HudMod modMain;
   protected final MinimapSession session;
   protected final Map<String, MinimapWorldContainer> subContainers;
   protected final Map<String, MinimapWorld> worlds;
   private final Map<String, String> worldNames;
   private final MinimapWorldRootContainer rootContainer;
   private final ServerWaypointManager serverWaypointManager;
   protected XaeroPath path;

   protected MinimapWorldContainer(HudMod modMain, MinimapSession session, XaeroPath path, MinimapWorldRootContainer rootContainer) {
      if (path.getLastNode().contains(":")) {
         throw new IllegalArgumentException();
      } else {
         this.modMain = modMain;
         this.session = session;
         this.path = path;
         this.rootContainer = rootContainer;
         this.worlds = new HashMap<>();
         this.subContainers = new HashMap<>();
         this.worldNames = new HashMap<>();
         this.serverWaypointManager = new ServerWaypointManager();
      }
   }

   public void setPath(XaeroPath path) {
      if (path.getLastNode().contains(":")) {
         throw new IllegalArgumentException();
      } else {
         this.path = path;

         for (MinimapWorldContainer s : this.subContainers.values()) {
            s.setPath(path.resolve(s.getLastNode()));
         }
      }
   }

   public MinimapWorldContainer addSubContainer(XaeroPath containerPath) {
      if (containerPath.getNodeCount() <= this.path.getNodeCount()) {
         throw new IllegalArgumentException();
      } else {
         String nextNode = containerPath.getAtIndex(this.path.getNodeCount()).getLastNode();
         MinimapWorldContainer sub = this.subContainers.get(nextNode);
         if (sub == null) {
            this.subContainers
               .put(
                  nextNode,
                  sub = MinimapWorldContainer.Builder.begin()
                     .setModMain(this.modMain)
                     .setSession(this.session)
                     .setPath(this.path.resolve(nextNode))
                     .setRootContainer(this.getRoot())
                     .build()
               );
         }

         return containerPath.getNodeCount() > this.path.getNodeCount() + 1 ? sub.addSubContainer(containerPath) : sub;
      }
   }

   public boolean containsSubContainer(XaeroPath containerPath) {
      if (containerPath.getNodeCount() <= this.path.getNodeCount()) {
         throw new IllegalArgumentException();
      } else {
         String nextNode = containerPath.getAtIndex(this.path.getNodeCount()).getLastNode();
         MinimapWorldContainer sub = this.subContainers.get(nextNode);
         if (sub == null) {
            return false;
         } else {
            return containerPath.getNodeCount() == this.path.getNodeCount() + 1 ? true : sub.containsSubContainer(containerPath);
         }
      }
   }

   public boolean deleteSubContainer(XaeroPath containerPath) {
      if (containerPath.getNodeCount() <= this.path.getNodeCount()) {
         throw new IllegalArgumentException();
      } else if (containerPath.getNodeCount() == this.path.getNodeCount() + 1) {
         return this.subContainers.remove(containerPath.getLastNode()) != null;
      } else {
         MinimapWorldContainer sub = this.subContainers.get(containerPath.getAtIndex(this.path.getNodeCount()).getLastNode());
         return sub == null ? false : sub.deleteSubContainer(containerPath);
      }
   }

   public boolean isEmpty() {
      return this.subContainers.isEmpty() && this.worlds.isEmpty();
   }

   public MinimapWorld addWorld(String worldNode) {
      MinimapWorld world = this.worlds.get(worldNode);
      if (world != null) {
         return world;
      } else {
         MinimapWorld defaultWorld = this.worlds.get("waypoints");
         if (defaultWorld == null) {
            class_5321<class_1937> dimId = this.path.getNodeCount() < 2
               ? null
               : this.session.getDimensionHelper().getDimensionKeyForDirectoryName(this.path.getAtIndex(1).getLastNode());
            world = MinimapWorld.Builder.begin().setContainer(this).setNode(worldNode).setDimId(dimId).build();
            this.worlds.put(worldNode, world);
            return world;
         } else {
            this.worlds.put(worldNode, defaultWorld);

            try {
               Path defaultFile = this.session.getWorldManagerIO().getWorldFile(defaultWorld);
               defaultWorld.setNode(worldNode);
               Path fixedFile = this.session.getWorldManagerIO().getWorldFile(defaultWorld);
               if (Files.exists(defaultFile)) {
                  Files.move(defaultFile, fixedFile);
               }
            } catch (IOException var6) {
               MinimapLogs.LOGGER.error("suppressed exception", var6);
            }

            this.worlds.remove("waypoints");
            return defaultWorld;
         }
      }
   }

   public void addWorld(MinimapWorld world) {
      if (this.worlds.containsKey(world.getNode())) {
         throw new IllegalArgumentException();
      } else {
         this.worlds.put(world.getNode(), world);
      }
   }

   public void removeWorld(String worldNode) {
      this.worlds.remove(worldNode);
   }

   public void setName(String worldNode, String name) {
      String current = this.worldNames.get(worldNode);
      if (current != null && !current.equals(name)) {
         this.worlds.get(worldNode).requestRemovalOnSave(current);
      }

      this.worldNames.put(worldNode, name);
   }

   public String getName(String worldNode) {
      if (worldNode.equals("waypoints")) {
         return null;
      } else {
         String name = this.worldNames.get(worldNode);
         if (name != null) {
            return name;
         } else {
            int numericName = this.worldNames.size() + 1;

            do {
               name = numericName++ + "";
            } while (this.worldNames.containsValue(name));

            this.setName(worldNode, name);
            return name;
         }
      }
   }

   public void removeName(String worldNode) {
      this.worldNames.remove(worldNode);
   }

   public String getLastNode() {
      return this.path.getLastNode();
   }

   public String getSubName() {
      String subName = this.getLastNode();
      if (!subName.startsWith("dim%")) {
         return subName;
      } else {
         class_5321<class_1937> dimensionKey = this.session.getDimensionHelper().getDimensionKeyForDirectoryName(subName);
         if (dimensionKey == null) {
            return "Dim. " + subName.substring(4);
         } else {
            return dimensionKey.method_29177().method_12836().equals("minecraft")
               ? dimensionKey.method_29177().method_12832()
               : dimensionKey.method_29177().toString();
         }
      }
   }

   public String getFullWorldName(String worldNode, String containerName) {
      if (this.worlds.size() < 2 && !containerName.isEmpty()) {
         return containerName;
      } else {
         String worldName = this.getName(worldNode);
         String dimNode = this.path.getNodeCount() < 2 ? "" : this.path.getAtIndex(1).getLastNode();
         if (dimNode.startsWith("dim%")) {
            class_5321<class_1937> dimId = this.session.getDimensionHelper().getDimensionKeyForDirectoryName(dimNode);
            if (dimId != null
               && this.modMain.getSupportMods().worldmap()
               && this.getRoot().getPath().equals(this.session.getWorldState().getAutoRootContainerPath())) {
               String worldMapMWName = this.modMain.getSupportMods().worldmapSupport.tryToGetMultiworldName(dimId, worldNode);
               if (worldMapMWName != null && !worldMapMWName.equals(worldNode)) {
                  worldName = worldMapMWName;
               }
            }
         }

         if (worldName == null) {
            return containerName;
         } else {
            return !containerName.isEmpty() ? worldName + " - " + containerName : worldName;
         }
      }
   }

   public XaeroPath getPath() {
      return this.path;
   }

   public MinimapWorld getFirstWorld() {
      if (!this.worlds.isEmpty()) {
         return this.worlds.values().stream().findFirst().orElse(null);
      } else {
         for (MinimapWorldContainer sub : this.subContainers.values()) {
            MinimapWorld subFirst = sub.getFirstWorld();
            if (subFirst != null) {
               return subFirst;
            }
         }

         return null;
      }
   }

   public MinimapWorld getFirstWorldConnectedTo(MinimapWorld refWorld) {
      if (!this.worlds.isEmpty()) {
         MinimapWorldRootContainer rootContainer = this.getRoot();

         for (MinimapWorld world : this.worlds.values()) {
            if (rootContainer.getSubWorldConnections().isConnected(refWorld, world)) {
               return world;
            }
         }
      }

      for (MinimapWorldContainer sub : this.subContainers.values()) {
         MinimapWorld subFirst = sub.getFirstWorldConnectedTo(refWorld);
         if (subFirst != null) {
            return subFirst;
         }
      }

      return null;
   }

   @Override
   public String toString() {
      return this.path + " sc:" + this.subContainers.size() + " w:" + this.worlds.size();
   }

   public Iterable<MinimapWorld> getWorlds() {
      return this.worlds.values();
   }

   public List<MinimapWorld> getWorldsCopy() {
      return new ArrayList<>(this.worlds.values());
   }

   public Iterable<MinimapWorldContainer> getSubContainers() {
      return this.subContainers.values();
   }

   public Iterable<MinimapWorld> getAllWorldsIterable() {
      Iterable<MinimapWorld> allWorlds = this.worlds.values();

      for (MinimapWorldContainer sub : this.subContainers.values()) {
         allWorlds = Iterables.concat(allWorlds, sub.getAllWorldsIterable());
      }

      return allWorlds;
   }

   public XaeroPath fixPathCharacterCases(XaeroPath containerPath) {
      if (containerPath.equals(this.path)) {
         return this.path;
      } else if (!containerPath.isSubOf(this.path)) {
         return null;
      } else {
         for (Entry<String, MinimapWorldContainer> entry : this.subContainers.entrySet()) {
            XaeroPath subSearch = entry.getValue().fixPathCharacterCases(containerPath);
            if (subSearch != null) {
               return subSearch;
            }
         }

         XaeroPath fixedContainerPath = this.path;

         for (int i = this.path.getNodeCount(); i < containerPath.getNodeCount(); i++) {
            fixedContainerPath = fixedContainerPath.resolve(containerPath.getAtIndex(i).getLastNode());
         }

         return fixedContainerPath;
      }
   }

   public MinimapWorldRootContainer getRoot() {
      return this.rootContainer;
   }

   public RootConfig getRootConfig() {
      return this.getRoot().getConfig();
   }

   public Path getDirectoryPath() {
      Path worldFolder = this.modMain.getMinimapFolder();
      return this.path.applyToFilePath(worldFolder);
   }

   public MinimapSession getSession() {
      return this.session;
   }

   public ServerWaypointManager getServerWaypointManager() {
      return this.serverWaypointManager;
   }

   public static final class Builder {
      private HudMod modMain;
      private MinimapSession session;
      private XaeroPath path;
      private MinimapWorldRootContainer rootContainer;

      private Builder() {
      }

      public MinimapWorldContainer.Builder setDefault() {
         this.setModMain(null);
         this.setSession(null);
         this.setPath(null);
         this.setRootContainer(null);
         return this;
      }

      public MinimapWorldContainer.Builder setModMain(HudMod modMain) {
         this.modMain = modMain;
         return this;
      }

      public MinimapWorldContainer.Builder setSession(MinimapSession session) {
         this.session = session;
         return this;
      }

      public MinimapWorldContainer.Builder setPath(XaeroPath path) {
         this.path = path;
         return this;
      }

      public MinimapWorldContainer.Builder setRootContainer(MinimapWorldRootContainer rootContainer) {
         this.rootContainer = rootContainer;
         return this;
      }

      public MinimapWorldContainer build() {
         if (this.modMain != null && this.session != null && this.path != null && this.rootContainer != null) {
            return new WaypointWorldContainer(this.modMain, this.session, this.path, this.rootContainer);
         } else {
            throw new IllegalStateException();
         }
      }

      public static MinimapWorldContainer.Builder begin() {
         return new MinimapWorldContainer.Builder().setDefault();
      }
   }
}
