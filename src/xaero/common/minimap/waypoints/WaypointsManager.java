package xaero.common.minimap.waypoints;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map.Entry;
import net.minecraft.class_1074;
import net.minecraft.class_124;
import net.minecraft.class_1657;
import net.minecraft.class_1659;
import net.minecraft.class_1937;
import net.minecraft.class_2338;
import net.minecraft.class_2558;
import net.minecraft.class_2561;
import net.minecraft.class_2568;
import net.minecraft.class_2960;
import net.minecraft.class_310;
import net.minecraft.class_437;
import net.minecraft.class_5218;
import net.minecraft.class_5250;
import net.minecraft.class_5321;
import net.minecraft.class_634;
import net.minecraft.class_638;
import net.minecraft.class_7924;
import net.minecraft.class_2558.class_2559;
import net.minecraft.class_2568.class_5247;
import xaero.common.AXaeroMinimap;
import xaero.common.MinimapLogs;
import xaero.common.XaeroMinimapSession;
import xaero.common.minimap.mcworld.MinimapClientWorldData;
import xaero.common.minimap.mcworld.MinimapClientWorldDataHelper;
import xaero.common.misc.OptimizedMath;
import xaero.common.settings.ModSettings;

public class WaypointsManager {
   private AXaeroMinimap modMain;
   private XaeroMinimapSession minimapSession;
   private class_310 mc;
   private HashMap<String, WaypointWorldContainer> waypointMap = new HashMap<>();
   private WaypointSet waypoints = null;
   private List<Waypoint> serverWaypoints = null;
   public static final Hashtable<String, Hashtable<Integer, Waypoint>> customWaypoints = new Hashtable<>();
   private String mainContainerID;
   private String containerID = null;
   private String containerIDIgnoreCaseCache;
   private String customContainerID = null;
   private String worldID = null;
   private String customWorldID = null;
   private class_2338 currentSpawn;
   public long setChanged;
   public static final String TELEPORT_ANYWAY_COMMAND = "xaero_tp_anyway";
   private Waypoint teleportAnywayWP;
   private WaypointWorld teleportAnywayWorld;

   public WaypointsManager(AXaeroMinimap modMain, XaeroMinimapSession minimapSession) {
      this.modMain = modMain;
      this.minimapSession = minimapSession;
      this.mc = class_310.method_1551();
   }

   public void onLoad(class_634 connection) throws IOException {
      this.mainContainerID = this.getMainContainer(false, connection);
      this.fixIPv6Folder(connection);
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

   public double getDimensionDivision(WaypointWorld waypointWorld) {
      if (class_310.method_1551().field_1687 == null) {
         return 1.0;
      } else {
         double selectedDimDiv = this.getDimCoordinateScale(waypointWorld);
         return class_310.method_1551().field_1687.method_8597().comp_646() / selectedDimDiv;
      }
   }

   public double getDimCoordinateScale(WaypointWorld waypointWorld) {
      if (waypointWorld == null) {
         return 1.0;
      } else {
         WaypointWorldRootContainer rootContainer = waypointWorld.getContainer().getRootContainer();
         class_5321<class_1937> dimKey = waypointWorld.getDimId();
         return dimKey == null ? 1.0 : rootContainer.getDimensionScale(dimKey);
      }
   }

   public String getDimensionDirectoryName(class_5321<class_1937> dimKey) {
      if (dimKey == class_1937.field_25179) {
         return "dim%0";
      } else if (dimKey == class_1937.field_25180) {
         return "dim%-1";
      } else if (dimKey == class_1937.field_25181) {
         return "dim%1";
      } else {
         class_2960 identifier = dimKey.method_29177();
         return "dim%" + identifier.method_12836() + "$" + identifier.method_12832().replace('/', '%');
      }
   }

   public class_5321<class_1937> findDimensionKey(String validatedName) {
      for (class_5321<class_1937> dk : this.mc.field_1724.field_3944.method_29356()) {
         if (validatedName.equals(dk.method_29177().method_12832().replaceAll("[^a-zA-Z0-9_]+", ""))) {
            return dk;
         }
      }

      return null;
   }

   public class_5321<class_1937> getDimensionKeyForDirectoryName(String dirName) {
      String dimIdPart = dirName.substring(4);
      if (dimIdPart.equals("0")) {
         return class_1937.field_25179;
      } else if (dimIdPart.equals("1")) {
         return class_1937.field_25181;
      } else if (dimIdPart.equals("-1")) {
         return class_1937.field_25180;
      } else {
         String[] idArgs = dimIdPart.split("\\$");
         if (idArgs.length == 1) {
            return null;
         } else {
            try {
               Integer.parseInt(dimIdPart);
               return null;
            } catch (NumberFormatException var5) {
               return class_5321.method_29179(class_7924.field_41223, new class_2960(idArgs[0], idArgs[1].replace('%', '/')));
            }
         }
      }
   }

   private String getMainContainer(boolean preIP6Fix, class_634 connection) {
      String potentialContainerID;
      if (this.mc.method_1576() != null) {
         potentialContainerID = this.mc
            .method_1576()
            .method_27050(class_5218.field_24188)
            .getParent()
            .getFileName()
            .toString()
            .replace("_", "%us%")
            .replace("/", "%fs%")
            .replace("\\", "%bs%");
      } else if (this.mc.method_1589() && this.modMain.getEvents().latestRealm != null) {
         potentialContainerID = "Realms_" + this.modMain.getEvents().latestRealm.field_22605 + "." + this.modMain.getEvents().latestRealm.field_22599;
      } else if (connection.method_45734() != null) {
         String serverIP = this.modMain.getSettings().differentiateByServerAddress ? connection.method_45734().field_3761 : "Any Address";
         int portDivider;
         if (!preIP6Fix && serverIP.indexOf(":") != serverIP.lastIndexOf(":")) {
            portDivider = serverIP.lastIndexOf("]:") + 1;
         } else {
            portDivider = serverIP.indexOf(":");
         }

         if (portDivider > 0) {
            serverIP = serverIP.substring(0, portDivider);
         }

         while (serverIP.endsWith(".")) {
            serverIP = serverIP.substring(0, serverIP.length() - 1);
         }

         potentialContainerID = "Multiplayer_" + serverIP.replace(":", "§").replace("_", "%us%").replace("/", "%fs%").replace("\\", "%bs%");
      } else {
         potentialContainerID = "Unknown";
      }

      return this.ignoreContainerCase(potentialContainerID, null);
   }

   public String ignoreContainerCase(String potentialContainerID, String current) {
      if (potentialContainerID.equalsIgnoreCase(current)) {
         return current;
      } else {
         for (Entry<String, WaypointWorldContainer> e : this.waypointMap.entrySet()) {
            String containerSearch = e.getValue().getEqualIgnoreCaseSub(potentialContainerID);
            if (containerSearch != null) {
               return containerSearch;
            }
         }

         return potentialContainerID;
      }
   }

   public String getNewAutoWorldID(class_5321<class_1937> dimId, boolean useWorldmap) {
      if (this.mc.method_1576() != null) {
         return "waypoints";
      } else {
         WaypointWorldRootContainer rootContainer = (WaypointWorldRootContainer)this.getWorldContainer(this.mainContainerID);
         Object autoIdBase = this.getAutoIdBase(rootContainer);
         String worldmapMultiworldId = useWorldmap ? this.modMain.getSupportMods().worldmapSupport.tryToGetMultiworldId(dimId) : null;
         if (autoIdBase != null && (!useWorldmap || worldmapMultiworldId != null)) {
            String actualMultiworldId;
            if (autoIdBase instanceof class_2338 pos) {
               actualMultiworldId = "mw" + (pos.method_10263() >> 6) + "," + (pos.method_10264() >> 6) + "," + (pos.method_10260() >> 6);
               if (!rootContainer.isUsingMultiworldDetection()) {
                  String defaultMultiworldId = rootContainer.getDefaultMultiworldId();
                  if (defaultMultiworldId == null) {
                     rootContainer.setDefaultMultiworldId(actualMultiworldId);
                     rootContainer.saveConfig();
                  } else {
                     actualMultiworldId = defaultMultiworldId;
                  }
               }
            } else {
               actualMultiworldId = "mw$" + autoIdBase;
            }

            if (useWorldmap && worldmapMultiworldId != "minimap") {
               actualMultiworldId = worldmapMultiworldId;
            }

            return actualMultiworldId;
         } else {
            return null;
         }
      }
   }

   public String getCurrentContainerAndWorldID() {
      return this.getCurrentContainerAndWorldID(this.containerID, this.worldID);
   }

   public String getCurrentContainerID() {
      return this.getCurrentContainerID(this.containerID);
   }

   public String getCurrentWorldID() {
      return this.getCurrentWorldID(this.worldID);
   }

   public WaypointWorld getCurrentWorld() {
      return this.getCurrentWorld(this.containerID, this.worldID);
   }

   public String getCurrentOriginContainerID() {
      return this.getCurrentOriginContainerID(this.containerID);
   }

   public String getCurrentContainerAndWorldID(String autoContainer, String autoWorldID) {
      return this.getCurrentContainerID(autoContainer) + "_" + this.getCurrentWorldID(autoWorldID);
   }

   public String getCurrentContainerID(String autoContainer) {
      return this.customContainerID == null ? autoContainer : this.customContainerID;
   }

   public String getCurrentWorldID(String autoWorldID) {
      return this.customWorldID == null ? autoWorldID : this.customWorldID;
   }

   public WaypointWorld getCurrentWorld(String autoContainer, String autoWorldID) {
      return this.getWorld(this.getCurrentContainerID(autoContainer), this.getCurrentWorldID(autoWorldID));
   }

   public String getCurrentOriginContainerID(String autoContainer) {
      return this.getCurrentContainerID(autoContainer) == null ? null : this.getCurrentContainerID(autoContainer).split("/")[0];
   }

   public WaypointWorld getAutoWorld() {
      return this.getWorld(this.getAutoContainerID(), this.getAutoWorldID());
   }

   public String getAutoRootContainerID() {
      return this.mainContainerID;
   }

   public String getAutoContainerID() {
      return this.containerID;
   }

   public String getAutoWorldID() {
      return this.worldID;
   }

   public WaypointWorld getWorld(String container, String world) {
      return this.addWorld(container, world);
   }

   public WaypointWorld addWorld(String container, String world) {
      if (container == null) {
         return null;
      } else {
         WaypointWorldContainer wc = this.addWorldContainer(container);
         return wc.addWorld(world);
      }
   }

   public WaypointWorldContainer getWorldContainer(String id) {
      return this.addWorldContainer(id);
   }

   public WaypointWorldContainer addWorldContainer(String id) {
      WaypointWorldContainer container = null;
      String[] subs = id.split("/");

      for (int i = 0; i < subs.length; i++) {
         if (i == 0) {
            container = this.waypointMap.get(subs[i]);
            if (container == null) {
               this.waypointMap.put(subs[i], container = new WaypointWorldRootContainer(this.modMain, this.minimapSession, subs[i]));
               WaypointWorldRootContainer rootContainer = (WaypointWorldRootContainer)container;
               if (!rootContainer.configLoaded) {
                  rootContainer.loadConfig();
               }
            }
         } else {
            container = container.addSubContainer(subs[i]);
         }
      }

      return container;
   }

   public WaypointWorldContainer getWorldContainerNullable(String id) {
      WaypointWorldContainer container = null;
      String[] subs = id.split("/");

      for (int i = 0; i < subs.length; i++) {
         if (i == 0) {
            container = this.waypointMap.get(subs[i]);
         } else {
            container = container.subContainers.get(subs[i]);
         }

         if (container == null) {
            return null;
         }
      }

      return container;
   }

   public void removeContainer(String id) {
      WaypointWorldContainer container = null;
      String[] subs = id.split("/");

      for (int i = 0; i < subs.length; i++) {
         if (i == 0) {
            container = this.waypointMap.get(subs[i]);
            if (container == null) {
               return;
            }

            if (i == subs.length - 1) {
               this.waypointMap.remove(subs[i]);
               return;
            }
         } else {
            if (!container.containsSub(subs[i])) {
               return;
            }

            if (i == subs.length - 1) {
               container.deleteSubContainer(subs[i]);
               return;
            }

            container = container.addSubContainer(subs[i]);
         }
      }
   }

   public boolean containerExists(String id) {
      WaypointWorldContainer container = null;
      String[] subs = id.split("/");

      for (int i = 0; i < subs.length; i++) {
         if (i == 0) {
            container = this.waypointMap.get(subs[i]);
            if (container == null) {
               return false;
            }

            if (i == subs.length - 1) {
               return true;
            }
         } else {
            if (!container.containsSub(subs[i])) {
               return false;
            }

            if (i == subs.length - 1) {
               return true;
            }

            container = container.addSubContainer(subs[i]);
         }
      }

      return false;
   }

   public void updateWorldIds() {
      String oldContainerID = this.containerID;
      String oldWorldID = this.worldID;
      this.containerID = this.getPotentialContainerID();
      this.containerIDIgnoreCaseCache = this.containerID;
      String potentialWorldID = this.getNewAutoWorldID(this.mc.field_1687.method_27983(), this.modMain.getSupportMods().worldmap());
      if (potentialWorldID == null) {
         this.containerID = oldContainerID;
         this.worldID = oldWorldID;
      } else {
         this.worldID = potentialWorldID;
         if (this.containerID != null && !this.containerID.equals(oldContainerID)) {
            if (oldWorldID != null && oldWorldID.startsWith("plugin")) {
               WaypointWorldContainer oldContainer = this.getWorldContainer(oldContainerID);
               ArrayList<WaypointWorld> worlds = new ArrayList<>(oldContainer.worlds.values());

               for (int i = 0; i < worlds.size(); i++) {
                  worlds.get(i).getServerWaypoints().clear();
               }
            }

            WaypointWorldContainer rootContainer = this.getWorldContainer(this.mainContainerID);
            rootContainer.renameOldContainer(this.containerID);
            ((WaypointWorldRootContainer)rootContainer).updateDimensionType(this.mc.field_1687);
         }
      }
   }

   private String getPotentialContainerID() {
      return this.ignoreContainerCase(
         this.mainContainerID + "/" + this.getDimensionDirectoryName(this.mc.field_1687.method_27983()), this.containerIDIgnoreCaseCache
      );
   }

   public void updateWaypoints() {
      if (this.containerID != null && this.worldID != null) {
         this.addWorld(this.containerID, this.worldID);
         WaypointWorld world = this.getCurrentWorld();
         this.waypoints = world.getCurrentSet();
         if (!world.getServerWaypoints().isEmpty()) {
            this.serverWaypoints = new ArrayList<>(world.getServerWaypoints().values());
         } else {
            this.serverWaypoints = null;
         }
      }
   }

   public void createDeathpoint(class_1657 p) {
      this.updateWorldIds();
      if (this.modMain.getSettings().switchToAutoOnDeath) {
         this.setCustomContainerID(null);
         this.setCustomWorldID(null);
      }

      WaypointWorld autoWorld = this.getAutoWorld();
      boolean shouldAddToAuto = autoWorld != null;
      if (this.modMain.getSupportMods().worldmap()) {
         String realCurrentDimContainerKey = this.getPotentialContainerID();
         boolean containerIsSynced = realCurrentDimContainerKey.equals(this.containerID);
         List<String> allPotentialMWIds = this.modMain.getSupportMods().worldmapSupport.getPotentialMultiworldIds(this.mc.field_1687.method_27983());
         if (allPotentialMWIds != null && !allPotentialMWIds.isEmpty()) {
            for (String mwId : allPotentialMWIds) {
               WaypointWorld potentialWorld = this.getWorld(realCurrentDimContainerKey, mwId);
               if (!containerIsSynced || potentialWorld != autoWorld) {
                  this.createDeathpoint(p, potentialWorld, false);
               }
            }

            shouldAddToAuto = shouldAddToAuto && containerIsSynced;
         }
      }

      if (shouldAddToAuto) {
         this.createDeathpoint(p, autoWorld, false);
      }
   }

   private void createDeathpoint(class_1657 p, WaypointWorld wpw, boolean temp) {
      boolean disabled = false;
      WaypointSet waypoints = wpw.getCurrentSet();
      if (waypoints != null) {
         for (WaypointSet set : wpw.getSets().values()) {
            for (int i = 0; i < set.getList().size(); i++) {
               Waypoint w = set.getList().get(i);
               if (w.getWaypointType() == 1) {
                  if (set == waypoints) {
                     disabled = w.isDisabled();
                  }

                  if (!this.modMain.getSettings().getOldDeathpoints()) {
                     set.getList().remove(i);
                     i--;
                  } else {
                     w.setType(2);
                     w.setName("gui.xaero_deathpoint_old");
                  }
                  break;
               }
            }
         }

         List<Waypoint> list = waypoints.getList();
         double dimDiv = this.getDimensionDivision(wpw);
         if (this.modMain.getSettings().getDeathpoints()) {
            Waypoint deathpoint = new Waypoint(
               OptimizedMath.myFloor((double)OptimizedMath.myFloor(p.method_23317()) * dimDiv),
               OptimizedMath.myFloor(p.method_23318()),
               OptimizedMath.myFloor((double)OptimizedMath.myFloor(p.method_23321()) * dimDiv),
               "gui.xaero_deathpoint",
               "D",
               0,
               1
            );
            deathpoint.setDisabled(disabled);
            deathpoint.setTemporary(temp);
            list.add(0, deathpoint);
         }

         try {
            this.modMain.getSettings().saveWaypoints(wpw);
         } catch (IOException var10) {
            MinimapLogs.LOGGER.error("suppressed exception", var10);
         }
      }
   }

   public void createTemporaryWaypoints(WaypointWorld waypointWorld, int x, int y, int z) {
      this.createTemporaryWaypoints(waypointWorld, x, y, z, true);
   }

   public void createTemporaryWaypoints(WaypointWorld waypointWorld, int x, int y, int z, boolean yIncluded, double dimScale) {
      if (this.modMain.getSettings().waypointsGUI(this) && waypointWorld != null) {
         double waypointDestDimScale = this.getDimCoordinateScale(waypointWorld);
         double dimDiv = dimScale / waypointDestDimScale;
         x = OptimizedMath.myFloor((double)x * dimDiv);
         z = OptimizedMath.myFloor((double)z * dimDiv);
         Waypoint instant = new Waypoint(x, y, z, "Waypoint", "X", (int)(Math.random() * (double)ModSettings.ENCHANT_COLORS.length), 0, true, yIncluded);
         if (!this.modMain.getSettings().waypointsBottom) {
            waypointWorld.getCurrentSet().getList().add(0, instant);
         } else {
            waypointWorld.getCurrentSet().getList().add(instant);
         }
      }
   }

   public void createTemporaryWaypoints(WaypointWorld waypointWorld, int x, int y, int z, boolean yIncluded) {
      this.createTemporaryWaypoints(waypointWorld, x, y, z, yIncluded, class_310.method_1551().field_1687.method_8597().comp_646());
   }

   public boolean canTeleport(boolean displayingTeleportableWorld, WaypointWorld displayedWorld) {
      return (this.modMain.getSettings().allowWrongWorldTeleportation || displayingTeleportableWorld)
         && displayedWorld.getContainer().getRootContainer().isTeleportationEnabled();
   }

   public void teleportAnyway() {
      if (this.teleportAnywayWP != null) {
         class_437 dummyScreen = new class_437(class_2561.method_43470("")) {
         };
         class_310 minecraft = class_310.method_1551();
         dummyScreen.method_25423(minecraft, minecraft.method_22683().method_4486(), minecraft.method_22683().method_4502());
         this.teleportToWaypoint(this.teleportAnywayWP, this.teleportAnywayWorld, dummyScreen, false);
      }
   }

   public void teleportToWaypoint(Waypoint selected, WaypointWorld displayedWorld, class_437 screen) {
      this.teleportToWaypoint(selected, displayedWorld, screen, true);
   }

   public void teleportToWaypoint(Waypoint selected, WaypointWorld displayedWorld, class_437 screen, boolean respectHiddenCoords) {
      this.updateWorldIds();
      boolean displayingTeleportableWorld = this.isWorldTeleportable(displayedWorld);
      if (selected != null && this.canTeleport(displayingTeleportableWorld, displayedWorld)) {
         this.mc.method_1507(null);
         if (!selected.isYIncluded() && this.mc.field_1761.method_2908()) {
            class_5250 messageComponent = class_2561.method_43470(class_1074.method_4662("gui.xaero_teleport_y_unknown", new Object[0]));
            messageComponent.method_10862(messageComponent.method_10866().method_10977(class_124.field_1061));
            this.mc.field_1705.method_1743().method_1812(messageComponent);
            return;
         }

         String tpCommand = "";
         boolean reachableDimension = true;
         boolean crossDimension = false;
         WaypointWorldRootContainer rootContainer = displayedWorld.getContainer().getRootContainer();
         WaypointWorld autoWorld = this.getAutoWorld();
         if (displayingTeleportableWorld && displayedWorld != autoWorld) {
            if (!this.isTeleportationSafe(displayedWorld)) {
               class_5250 messageComponent = class_2561.method_43470(class_1074.method_4662("gui.xaero_teleport_not_connected", new Object[0]));
               messageComponent.method_10862(messageComponent.method_10866().method_10977(class_124.field_1061));
               this.mc.field_1705.method_1743().method_1812(messageComponent);
               return;
            }

            if (autoWorld == null || autoWorld.getContainer() != displayedWorld.getContainer()) {
               crossDimension = true;
               String[] containerKeySplit = displayedWorld.getContainer().getKey().split("/");
               if (containerKeySplit.length > 1) {
                  String dimensionKey = containerKeySplit[1];
                  if (!dimensionKey.startsWith("dim%")) {
                     this.mc.field_1705.method_1743().method_1812(class_2561.method_43471("gui.xaero_visit_needed"));
                     return;
                  }

                  class_5321<class_1937> dimensionId = this.getDimensionKeyForDirectoryName(dimensionKey);
                  if (dimensionId != null) {
                     this.setCustomContainerID(null);
                     this.setCustomWorldID(null);
                     this.updateWaypoints();
                     tpCommand = "/execute in " + dimensionId.method_29177().toString() + " run ";
                  } else {
                     reachableDimension = false;
                  }
               } else {
                  reachableDimension = false;
               }
            }
         }

         if (reachableDimension) {
            if (respectHiddenCoords
               && this.modMain.getSettings().hideWaypointCoordinates
               && this.mc.field_1690.method_42539().method_41753() != class_1659.field_7536) {
               class_5250 messageComponent = class_2561.method_43470(class_1074.method_4662("gui.xaero_teleport_coordinates_hidden", new Object[0]));
               messageComponent.method_10862(messageComponent.method_10866().method_10977(class_124.field_1075));
               this.mc.field_1705.method_1743().method_1812(messageComponent);
               class_5250 clickableQuestion = class_2561.method_43470("§e[" + class_1074.method_4662("gui.xaero_teleport_anyway", new Object[0]) + "]");
               clickableQuestion.method_10862(
                  clickableQuestion.method_10866()
                     .method_10958(new class_2558(class_2559.field_11750, "/xaero_tp_anyway"))
                     .method_10949(
                        new class_2568(
                           class_5247.field_24342,
                           class_2561.method_43470(class_1074.method_4662("gui.xaero_teleport_shows_coordinates", new Object[0]))
                              .method_27692(class_124.field_1061)
                        )
                     )
               );
               this.teleportAnywayWP = selected;
               this.teleportAnywayWorld = displayedWorld;
               this.mc.field_1705.method_1743().method_1812(clickableQuestion);
            } else {
               int x = selected.getX();
               int z = selected.getZ();
               double dimDiv = this.getDimensionDivision(displayedWorld);
               if (!crossDimension && dimDiv != 1.0) {
                  x = (int)Math.floor((double)x / dimDiv);
                  z = (int)Math.floor((double)z / dimDiv);
               }

               String serverTpCommand = selected.isRotation()
                  ? rootContainer.getServerTeleportCommandRotationFormat()
                  : rootContainer.getServerTeleportCommandFormat();
               String defaultTpCommand = selected.isRotation()
                  ? this.modMain.getSettings().defaultWaypointTPCommandRotationFormat
                  : this.modMain.getSettings().defaultWaypointTPCommandFormat;
               String tpCommandPrefix = !rootContainer.isUsingDefaultTeleportCommand() && serverTpCommand != null ? serverTpCommand : defaultTpCommand;
               if (tpCommand.length() > 0) {
                  if (tpCommandPrefix.startsWith("/")) {
                     tpCommandPrefix = tpCommandPrefix.substring(1);
                  }

                  if (tpCommandPrefix.startsWith("minecraft:")) {
                     tpCommandPrefix = tpCommandPrefix.substring(10);
                  }
               }

               String yString = !selected.isYIncluded()
                  ? "~"
                  : (this.modMain.getSettings().getPartialYTeleportation() ? (double)selected.getY() + 0.5 + "" : selected.getY() + "");
               tpCommandPrefix = tpCommandPrefix.replace("{x}", x + "")
                  .replace("{y}", yString)
                  .replace("{z}", z + "")
                  .replace("{name}", selected.getLocalizedName());
               if (selected.isRotation()) {
                  tpCommandPrefix = tpCommandPrefix.replace("{yaw}", selected.getYaw() + "");
               }

               tpCommand = tpCommand + tpCommandPrefix;
               if (tpCommand.startsWith("/")) {
                  tpCommand = tpCommand.substring(1);
                  if (!this.mc.field_1724.field_3944.method_45731(tpCommand)) {
                     this.mc.field_1724.field_3944.method_45730(tpCommand);
                  }
               } else {
                  this.mc.field_1724.field_3944.method_45729(tpCommand);
               }
            }
         } else {
            this.mc
               .field_1705
               .method_1743()
               .method_1812(
                  class_2561.method_43470(class_1074.method_4662("gui.xaero_unreachable_dimension", new Object[0])).method_27692(class_124.field_1061)
               );
         }
      }
   }

   public boolean isWorldTeleportable(WaypointWorld displayedWorld) {
      WaypointWorld autoWorld = this.getAutoWorld();
      WaypointWorldRootContainer rootContainer = displayedWorld.getContainer().getRootContainer();
      return rootContainer.getKey().equals(this.getAutoRootContainerID())
         && (
            autoWorld == displayedWorld
               || autoWorld != null
                  && (
                     autoWorld.getContainer() == displayedWorld.getContainer()
                        || this.modMain.getSettings().crossDimensionalTp && autoWorld.getContainer() != displayedWorld.getContainer()
                  )
         );
   }

   public boolean isTeleportationSafe(WaypointWorld displayedWorld) {
      if (!class_310.method_1551().field_1761.method_2908()) {
         return true;
      } else {
         WaypointWorld autoWorld = this.getAutoWorld();
         WaypointWorldRootContainer rootContainer = displayedWorld.getContainer().getRootContainer();
         return rootContainer.getSubWorldConnections().isConnected(autoWorld, displayedWorld);
      }
   }

   public WaypointSet getWaypoints() {
      return this.waypoints;
   }

   public void setWaypoints(WaypointSet waypoints) {
      this.waypoints = waypoints;
   }

   public List<Waypoint> getServerWaypoints() {
      return this.serverWaypoints;
   }

   public HashMap<String, WaypointWorldContainer> getWaypointMap() {
      return this.waypointMap;
   }

   public void setCurrentSpawn(class_2338 currentSpawn, class_638 clientWorld) {
      this.currentSpawn = currentSpawn;
   }

   public String getCustomContainerID() {
      return this.customContainerID;
   }

   public void setCustomContainerID(String customContainerID) {
      this.customContainerID = customContainerID;
   }

   public String getCustomWorldID() {
      return this.customWorldID;
   }

   public void setCustomWorldID(String customWorldID) {
      this.customWorldID = customWorldID;
   }

   public static Hashtable<Integer, Waypoint> getCustomWaypoints(String modName) {
      Hashtable<Integer, Waypoint> wps = customWaypoints.get(modName);
      if (wps == null) {
         customWaypoints.put(modName, wps = new Hashtable<>());
      }

      return wps;
   }

   public boolean isMultiplayer(String containerId) {
      return containerId.startsWith("Multiplayer_") || containerId.startsWith("Realms_");
   }

   private boolean hasServerLevelId(WaypointWorldRootContainer rootContainer) {
      MinimapClientWorldData worldData = MinimapClientWorldDataHelper.getCurrentWorldData();
      return worldData.serverLevelId != null && !rootContainer.isIgnoreServerLevelId();
   }

   private Object getAutoIdBase(WaypointWorldRootContainer rootContainer) {
      MinimapClientWorldData worldData = MinimapClientWorldDataHelper.getCurrentWorldData();
      return this.hasServerLevelId(rootContainer) ? worldData.serverLevelId : this.currentSpawn;
   }

   public void onServerLevelId(int id) {
      MinimapClientWorldData worldData = MinimapClientWorldDataHelper.getCurrentWorldData();
      worldData.serverLevelId = id;
      MinimapLogs.LOGGER.info("Minimap updated server level id: " + id + " for world " + class_310.method_1551().field_1687.method_27983());
   }

   private void fixIPv6Folder(class_634 connection) throws IOException {
      if (this.mainContainerID.startsWith("Multiplayer_")) {
         String preIP6FixMainContainerID = this.getMainContainer(true, connection);
         if (!this.mainContainerID.equals(preIP6FixMainContainerID)) {
            Path preFixFolder = new File(this.modMain.getWaypointsFolder(), preIP6FixMainContainerID).toPath();
            if (Files.exists(preFixFolder)) {
               Path fixedFolder = new File(this.modMain.getWaypointsFolder(), this.mainContainerID).toPath();
               if (!Files.exists(fixedFolder)) {
                  Files.move(preFixFolder, fixedFolder);
               }
            }
         }
      }
   }
}
