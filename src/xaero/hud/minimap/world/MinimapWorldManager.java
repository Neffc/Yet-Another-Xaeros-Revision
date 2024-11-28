package xaero.hud.minimap.world;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMaps;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import net.minecraft.class_2960;
import xaero.common.HudMod;
import xaero.common.minimap.waypoints.Waypoint;
import xaero.hud.minimap.module.MinimapSession;
import xaero.hud.minimap.world.container.MinimapWorldContainer;
import xaero.hud.minimap.world.container.MinimapWorldRootContainer;
import xaero.hud.path.XaeroPath;

public class MinimapWorldManager {
   private final HudMod modMain;
   private final MinimapSession session;
   private final Map<String, MinimapWorldRootContainer> rootContainers;
   private final Map<class_2960, Int2ObjectMap<Waypoint>> customWaypoints;

   public MinimapWorldManager(HudMod modMain, MinimapSession session) {
      this.modMain = modMain;
      this.session = session;
      this.rootContainers = new HashMap<>();
      this.customWaypoints = new Hashtable<>();
   }

   public MinimapWorldContainer getWorldContainer(XaeroPath path) {
      return this.addWorldContainer(path);
   }

   public MinimapWorldContainer getWorldContainerNullable(XaeroPath path) {
      return this.containerExists(path) ? this.addWorldContainer(path) : null;
   }

   public MinimapWorldRootContainer getRootWorldContainer(String rootContainerId) {
      return this.getRootWorldContainer(XaeroPath.root(rootContainerId));
   }

   public MinimapWorldRootContainer getRootWorldContainer(XaeroPath rootContainerPath) {
      return this.getWorldContainer(rootContainerPath).getRoot();
   }

   public MinimapWorldContainer addWorldContainer(XaeroPath path) {
      XaeroPath rootPath = path.getRoot();
      MinimapWorldRootContainer rootContainer = this.rootContainers.get(rootPath.getLastNode());
      if (rootContainer == null) {
         this.rootContainers
            .put(
               rootPath.getLastNode(),
               rootContainer = MinimapWorldRootContainer.Builder.begin().setModMain(this.modMain).setSession(this.session).setPath(rootPath).build()
            );
         this.session.getWorldManagerIO().onRootContainerAdded(rootContainer);
      }

      return (MinimapWorldContainer)(path.getNodeCount() > 1 ? rootContainer.addSubContainer(path) : rootContainer);
   }

   public void addRootWorldContainer(MinimapWorldRootContainer container) {
      if (this.rootContainers.containsKey(container.getPath().getLastNode())) {
         throw new IllegalArgumentException();
      } else {
         this.rootContainers.put(container.getPath().getLastNode(), container);
      }
   }

   public boolean removeContainer(XaeroPath path) {
      if (path.getNodeCount() == 1) {
         return this.rootContainers.remove(path.getLastNode()) != null;
      } else {
         MinimapWorldRootContainer rootContainer = this.rootContainers.get(path.getRoot().getLastNode());
         return rootContainer == null ? false : rootContainer.deleteSubContainer(path);
      }
   }

   public boolean containerExists(XaeroPath path) {
      MinimapWorldRootContainer rootContainer = this.rootContainers.get(path.getRoot().getLastNode());
      if (rootContainer == null) {
         return false;
      } else {
         return path.getNodeCount() == 1 ? true : rootContainer.containsSubContainer(path);
      }
   }

   public MinimapWorld getWorld(XaeroPath worldPath) {
      return this.addWorld(worldPath);
   }

   public MinimapWorld addWorld(XaeroPath worldPath) {
      if (worldPath == null) {
         return null;
      } else {
         MinimapWorldContainer wc = this.addWorldContainer(worldPath.getParent());
         return wc.addWorld(worldPath.getLastNode());
      }
   }

   public MinimapWorld getCurrentWorld() {
      return this.getCurrentWorld(this.session.getWorldState().getAutoWorldPath());
   }

   public MinimapWorld getCurrentWorld(XaeroPath autoWorldPath) {
      return this.getWorld(this.session.getWorldState().getCurrentWorldPath(autoWorldPath));
   }

   public MinimapWorld getAutoWorld() {
      return this.getWorld(this.session.getWorldState().getAutoWorldPath());
   }

   public Iterable<MinimapWorldRootContainer> getRootContainers() {
      return this.rootContainers.values();
   }

   public MinimapWorldRootContainer getAutoRootContainer() {
      return this.getRootWorldContainer(this.session.getWorldState().getAutoRootContainerPath());
   }

   public MinimapWorldRootContainer getCurrentRootContainer() {
      MinimapWorld currentWorld = this.getCurrentWorld();
      return currentWorld == null ? null : currentWorld.getContainer().getRoot();
   }

   @Deprecated
   public HashMap<String, MinimapWorldRootContainer> getRootContainersDirect() {
      return (HashMap<String, MinimapWorldRootContainer>)this.rootContainers;
   }

   public Int2ObjectMap<Waypoint> getCustomWaypoints(class_2960 modId) {
      return this.customWaypoints.computeIfAbsent(modId, s -> Int2ObjectMaps.synchronize(new Int2ObjectOpenHashMap()));
   }

   public boolean hasCustomWaypoints() {
      return !this.customWaypoints.isEmpty();
   }

   public Iterable<Waypoint> getCustomWaypoints() {
      return this.customWaypoints.values().stream().flatMap(modMap -> modMap.values().stream())::iterator;
   }
}
