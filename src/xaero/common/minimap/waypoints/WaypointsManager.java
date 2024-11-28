package xaero.common.minimap.waypoints;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import net.minecraft.class_1657;
import net.minecraft.class_1937;
import net.minecraft.class_2338;
import net.minecraft.class_437;
import net.minecraft.class_5321;
import net.minecraft.class_634;
import net.minecraft.class_638;
import xaero.common.HudMod;
import xaero.hud.minimap.module.MinimapSession;
import xaero.hud.minimap.world.container.MinimapWorldContainerUtil;
import xaero.hud.module.HudModule;
import xaero.hud.path.XaeroPath;
import xaero.hud.path.XaeroPathReader;

@Deprecated
public class WaypointsManager extends MinimapSession {
   private final XaeroPathReader pathReader = new XaeroPathReader();
   public static final Hashtable<String, Hashtable<Integer, Waypoint>> customWaypoints = new Hashtable<>();
   private final List<Waypoint> serverWaypoints = new ArrayList<>();

   public WaypointsManager(HudMod modMain, HudModule<MinimapSession> module, class_634 connection) {
      super(modMain, module, connection);
   }

   private String pathToString(XaeroPath path) {
      return path == null ? null : path.toString();
   }

   private String worldPathToOldString(XaeroPath path) {
      return path == null ? null : path.getParent().toString() + "_" + path.getLastNode();
   }

   private String worldPathToLastNode(XaeroPath path) {
      return path == null ? null : path.getLastNode();
   }

   private String worldPathToContainerID(XaeroPath path) {
      return path == null ? null : path.getParent().toString();
   }

   @Deprecated
   public double getDimensionDivision(String worldContainerID) {
      if (worldContainerID == null) {
         return 1.0;
      } else {
         WaypointWorldContainer container = this.getWorldContainerNullable(worldContainerID);
         return container == null ? 1.0 : this.getDimensionDivision(container.getFirstWorld());
      }
   }

   @Deprecated
   public double getDimensionDivision(WaypointWorld waypointWorld) {
      return this.getDimensionHelper().getDimensionDivision(waypointWorld);
   }

   @Deprecated
   public double getDimCoordinateScale(WaypointWorld waypointWorld) {
      return this.getDimensionHelper().getDimCoordinateScale(waypointWorld);
   }

   @Deprecated
   public String getDimensionDirectoryName(class_5321<class_1937> dimKey) {
      return this.getDimensionHelper().getDimensionDirectoryName(dimKey);
   }

   @Deprecated
   public class_5321<class_1937> findDimensionKey(String validatedName) {
      return this.getDimensionHelper().findDimensionKeyForOldName(this.getMc().field_1724, validatedName);
   }

   @Deprecated
   public class_5321<class_1937> getDimensionKeyForDirectoryName(String dirName) {
      return this.getDimensionHelper().getDimensionKeyForDirectoryName(dirName);
   }

   @Deprecated
   private String getMainContainer(boolean preIP6Fix, class_634 connection) {
      return this.getMainContainer(preIP6Fix ? 0 : 1, connection);
   }

   @Deprecated
   private String getMainContainer(int version, class_634 connection) {
      return this.pathToString(this.getWorldStateUpdater().getAutoRootContainerPath(version));
   }

   @Deprecated
   public String ignoreContainerCase(String potentialContainerID, String current) {
      return this.pathToString(this.getWorldStateUpdater().ignoreContainerCase(this.pathReader.read(potentialContainerID), this.pathReader.read(current)));
   }

   @Deprecated
   public String getNewAutoWorldID(class_5321<class_1937> dimId, boolean useWorldmap) {
      return this.getWorldStateUpdater().getPotentialWorldNode(dimId, useWorldmap);
   }

   @Deprecated
   public String getCurrentContainerAndWorldID() {
      return this.worldPathToOldString(this.getWorldState().getCurrentWorldPath());
   }

   @Deprecated
   public String getCurrentContainerID() {
      return this.pathToString(this.getWorldState().getCurrentContainerPath());
   }

   @Deprecated
   public String getCurrentWorldID() {
      return this.worldPathToLastNode(this.getWorldState().getCurrentWorldPath());
   }

   @Deprecated
   public WaypointWorld getCurrentWorld() {
      return (WaypointWorld)this.worldManager.getCurrentWorld();
   }

   @Deprecated
   public String getCurrentOriginContainerID() {
      return this.pathToString(this.getWorldState().getCurrentRootContainerPath());
   }

   @Deprecated
   public String getCurrentContainerAndWorldID(String autoContainer, String autoWorldID) {
      XaeroPath autoWorldPath = autoContainer == null ? null : this.pathReader.read(autoContainer).resolve(autoWorldID);
      return this.worldPathToOldString(this.getWorldState().getCurrentWorldPath(autoWorldPath));
   }

   @Deprecated
   public String getCurrentContainerID(String autoContainer) {
      XaeroPath customContainerPath = this.getWorldState().getCustomContainerPath();
      return customContainerPath == null ? autoContainer : this.pathToString(customContainerPath);
   }

   @Deprecated
   public String getCurrentWorldID(String autoWorldID) {
      XaeroPath customWorldPath = this.getWorldState().getCustomWorldPath();
      return customWorldPath == null ? autoWorldID : this.worldPathToLastNode(customWorldPath);
   }

   @Deprecated
   public WaypointWorld getCurrentWorld(String autoContainer, String autoWorldID) {
      XaeroPath autoWorldPath = autoContainer == null ? null : this.pathReader.read(autoContainer).resolve(autoWorldID);
      return (WaypointWorld)this.worldManager.getWorld(this.getWorldState().getCurrentWorldPath(autoWorldPath));
   }

   @Deprecated
   public String getCurrentOriginContainerID(String autoContainer) {
      XaeroPath customContainer = this.getWorldState().getCustomContainerPath();
      if (customContainer != null) {
         return customContainer.getRoot().getLastNode();
      } else {
         return autoContainer == null ? null : autoContainer.split("/")[0];
      }
   }

   @Deprecated
   public WaypointWorld getAutoWorld() {
      return (WaypointWorld)this.worldManager.getAutoWorld();
   }

   @Deprecated
   public String getAutoRootContainerID() {
      return this.getWorldState().getAutoRootContainerPath().getLastNode();
   }

   @Deprecated
   public String getAutoContainerID() {
      return this.worldPathToContainerID(this.getWorldState().getAutoWorldPath());
   }

   @Deprecated
   public String getAutoWorldID() {
      return this.worldPathToLastNode(this.getWorldState().getAutoWorldPath());
   }

   @Deprecated
   public WaypointWorld getWorld(String container, String world) {
      return this.addWorld(container, world);
   }

   @Deprecated
   public WaypointWorld addWorld(String container, String world) {
      XaeroPath worldPath = this.pathReader.read(container).resolve(world);
      return (WaypointWorld)this.worldManager.addWorld(worldPath);
   }

   @Deprecated
   public WaypointWorldContainer getWorldContainer(String id) {
      return this.addWorldContainer(id);
   }

   @Deprecated
   public WaypointWorldContainer addWorldContainer(String id) {
      return (WaypointWorldContainer)this.worldManager.addWorldContainer(this.pathReader.read(id));
   }

   @Deprecated
   public WaypointWorldContainer getWorldContainerNullable(String id) {
      return (WaypointWorldContainer)this.worldManager.getWorldContainerNullable(this.pathReader.read(id));
   }

   @Deprecated
   public void removeContainer(String id) {
      this.worldManager.removeContainer(this.pathReader.read(id));
   }

   @Deprecated
   public boolean containerExists(String id) {
      return this.worldManager.containerExists(this.pathReader.read(id));
   }

   @Deprecated
   public void updateWorldIds() {
      this.getWorldStateUpdater().update();
   }

   @Deprecated
   private String getPotentialContainerID() {
      return this.pathToString(this.getWorldStateUpdater().getPotentialContainerPath());
   }

   @Deprecated
   public void updateWaypoints() {
   }

   @Deprecated
   public void createDeathpoint(class_1657 p) {
      this.getWaypointSession().getDeathpointHandler().createDeathpoint(p);
   }

   @Deprecated
   private void createDeathpoint(class_1657 p, WaypointWorld wpw, boolean temp) {
      this.getWaypointSession().getDeathpointHandler().createDeathpoint(p, wpw, temp);
   }

   @Deprecated
   public void createTemporaryWaypoints(WaypointWorld waypointWorld, int x, int y, int z) {
      this.getWaypointSession().getTemporaryHandler().createTemporaryWaypoint(waypointWorld, x, y, z);
   }

   @Deprecated
   public void createTemporaryWaypoints(WaypointWorld waypointWorld, int x, int y, int z, boolean yIncluded, double dimScale) {
      this.getWaypointSession().getTemporaryHandler().createTemporaryWaypoint(waypointWorld, x, y, z, yIncluded, dimScale);
   }

   @Deprecated
   public void createTemporaryWaypoints(WaypointWorld waypointWorld, int x, int y, int z, boolean yIncluded) {
      this.getWaypointSession().getTemporaryHandler().createTemporaryWaypoint(waypointWorld, x, y, z, yIncluded);
   }

   @Deprecated
   public boolean canTeleport(boolean displayingTeleportableWorld, WaypointWorld displayedWorld) {
      return this.getWaypointSession().getTeleport().canTeleport(displayingTeleportableWorld, displayedWorld);
   }

   @Deprecated
   public void teleportAnyway() {
      this.getWaypointSession().getTeleport().teleportAnyway();
   }

   @Deprecated
   public void teleportToWaypoint(Waypoint selected, WaypointWorld displayedWorld, class_437 screen) {
      this.getWaypointSession().getTeleport().teleportToWaypoint(selected, displayedWorld, screen);
   }

   @Deprecated
   public void teleportToWaypoint(Waypoint selected, WaypointWorld displayedWorld, class_437 screen, boolean respectHiddenCoords) {
      this.getWaypointSession().getTeleport().teleportToWaypoint(selected, displayedWorld, screen, respectHiddenCoords);
   }

   @Deprecated
   public boolean isWorldTeleportable(WaypointWorld displayedWorld) {
      return this.getWaypointSession().getTeleport().isWorldTeleportable(displayedWorld);
   }

   @Deprecated
   public boolean isTeleportationSafe(WaypointWorld displayedWorld) {
      return this.getWaypointSession().getTeleport().isTeleportationSafe(displayedWorld);
   }

   @Deprecated
   public WaypointSet getWaypoints() {
      WaypointWorld world = (WaypointWorld)this.worldManager.getCurrentWorld();
      return world == null ? null : (WaypointSet)world.getCurrentWaypointSet();
   }

   @Deprecated
   public void setWaypoints(WaypointSet waypoints) {
   }

   @Deprecated
   public List<Waypoint> getServerWaypoints() {
      return this.serverWaypoints;
   }

   @Deprecated
   public HashMap<String, WaypointWorldContainer> getWaypointMap() {
      return this.worldManager.getRootContainersDirect();
   }

   @Deprecated
   public void setCurrentSpawn(class_2338 currentSpawn, class_638 clientWorld) {
      this.getWorldStateUpdater().setCurrentWorldSpawn(currentSpawn);
   }

   @Deprecated
   public String getCustomContainerID() {
      return this.pathToString(this.getWorldState().getCustomContainerPath());
   }

   @Deprecated
   public void setCustomContainerID(String customContainerID) {
      if (customContainerID == null) {
         this.getWorldState().setCustomWorldPath(null);
      } else {
         XaeroPath newCustomContainerPath = this.pathReader.read(customContainerID);
         XaeroPath customWorldPath = this.getWorldState().getCustomWorldPath();
         this.getWorldState().setCustomWorldPath(newCustomContainerPath.resolve(customWorldPath == null ? "" : customWorldPath.getLastNode()));
      }
   }

   @Deprecated
   public String getCustomWorldID() {
      return this.worldPathToLastNode(this.getWorldState().getCustomWorldPath());
   }

   @Deprecated
   public void setCustomWorldID(String customWorldID) {
      if (customWorldID == null) {
         this.getWorldState().setCustomWorldPath(null);
      } else {
         XaeroPath customWorldPath = this.getWorldState().getCustomWorldPath();
         XaeroPath newCustomContainerPath = customWorldPath == null ? XaeroPath.root("") : customWorldPath.getParent();
         this.getWorldState().setCustomWorldPath(newCustomContainerPath.resolve(customWorldID));
      }
   }

   @Deprecated
   public static Hashtable<Integer, Waypoint> getCustomWaypoints(String modName) {
      Hashtable<Integer, Waypoint> wps = customWaypoints.get(modName);
      if (wps == null) {
         customWaypoints.put(modName, wps = new Hashtable<>());
      }

      return wps;
   }

   @Deprecated
   public boolean isMultiplayer(String containerId) {
      return MinimapWorldContainerUtil.isMultiplayer(this.pathReader.read(containerId));
   }

   @Deprecated
   private boolean hasServerLevelId(WaypointWorldRootContainer rootContainer) {
      return this.getWorldStateUpdater().hasServerLevelId(rootContainer);
   }

   @Deprecated
   private Object getAutoIdBase(WaypointWorldRootContainer rootContainer) {
      return this.getWorldStateUpdater().getAutoWorldNodeBase(rootContainer);
   }

   @Deprecated
   public void onServerLevelId(int id) {
      this.getWorldStateUpdater().onServerLevelId(id);
   }
}
