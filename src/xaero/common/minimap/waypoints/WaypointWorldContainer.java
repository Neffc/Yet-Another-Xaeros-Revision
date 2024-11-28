package xaero.common.minimap.waypoints;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import net.minecraft.class_1937;
import net.minecraft.class_2960;
import net.minecraft.class_5321;
import xaero.common.AXaeroMinimap;
import xaero.common.MinimapLogs;
import xaero.common.XaeroMinimapSession;
import xaero.common.file.SimpleBackup;

public class WaypointWorldContainer {
   private AXaeroMinimap modMain;
   private XaeroMinimapSession minimapSession;
   protected String key;
   public HashMap<String, WaypointWorldContainer> subContainers;
   public HashMap<String, WaypointWorld> worlds;
   private HashMap<String, String> multiworldNames;
   private final WaypointWorldRootContainer rootContainer;

   public WaypointWorldContainer(AXaeroMinimap modMain, XaeroMinimapSession minimapSession, String key, WaypointWorldRootContainer rootContainer) {
      this.modMain = modMain;
      this.minimapSession = minimapSession;
      this.key = key;
      this.worlds = new HashMap<>();
      this.subContainers = new HashMap<>();
      this.multiworldNames = new HashMap<>();
      this.rootContainer = rootContainer;
   }

   public void setKey(String key) {
      this.key = key;

      for (WaypointWorldContainer s : this.subContainers.values()) {
         String[] subKeySplit = s.getKey().split("/");
         s.setKey(key + "/" + subKeySplit[subKeySplit.length - 1]);
      }
   }

   public WaypointWorldContainer addSubContainer(String subID) {
      WaypointWorldContainer c = this.subContainers.get(subID);
      if (c == null) {
         this.subContainers.put(subID, c = new WaypointWorldContainer(this.modMain, this.minimapSession, this.key + "/" + subID, this.getRootContainer()));
      }

      return c;
   }

   public boolean containsSub(String subId) {
      return this.subContainers.containsKey(subId);
   }

   public void deleteSubContainer(String subId) {
      this.subContainers.remove(subId);
   }

   public boolean isEmpty() {
      return this.subContainers.isEmpty() && this.worlds.isEmpty();
   }

   public WaypointWorld addWorld(String multiworldId) {
      WaypointWorld world = this.worlds.get(multiworldId);
      if (world == null) {
         WaypointWorld defaultWorld = this.worlds.get("waypoints");
         if (defaultWorld == null) {
            class_5321<class_1937> dimId = this.getRootContainer() == this
               ? null
               : this.minimapSession.getWaypointsManager().getDimensionKeyForDirectoryName(this.getSubId());
            world = new WaypointWorld(this, multiworldId, dimId);
            this.worlds.put(multiworldId, world);
         } else {
            this.worlds.put(multiworldId, defaultWorld);

            try {
               File defaultFile = this.modMain.getSettings().getWaypointsFile(defaultWorld);
               defaultWorld.setId(multiworldId);
               File fixedFile = this.modMain.getSettings().getWaypointsFile(defaultWorld);
               if (Files.exists(defaultFile.toPath())) {
                  Files.move(defaultFile.toPath(), fixedFile.toPath());
               }
            } catch (IOException var6) {
               MinimapLogs.LOGGER.error("suppressed exception", var6);
            }

            this.worlds.remove("waypoints");
            world = defaultWorld;
         }
      }

      return world;
   }

   public void addName(String id, String name) {
      String current = this.multiworldNames.get(id);
      if (current != null && !current.equals(name)) {
         this.worlds.get(id).requestRemovalOnSave(current);
      }

      this.multiworldNames.put(id, name);
   }

   public String getName(String id) {
      if (id.equals("waypoints")) {
         return null;
      } else {
         String name = this.multiworldNames.get(id);
         if (name == null) {
            int numericName = this.multiworldNames.size() + 1;

            do {
               name = numericName++ + "";
            } while (this.multiworldNames.containsValue(name));

            this.addName(id, name);
         }

         return name;
      }
   }

   public void removeName(String id) {
      this.multiworldNames.remove(id);
   }

   public String getSubId() {
      return this.key.contains("/") ? this.key.substring(this.key.lastIndexOf("/") + 1) : "";
   }

   public String getSubName() {
      String subName = this.getSubId();
      if (subName.startsWith("dim%")) {
         class_5321<class_1937> dimensionKey = this.minimapSession.getWaypointsManager().getDimensionKeyForDirectoryName(subName);
         if (dimensionKey != null) {
            subName = dimensionKey.method_29177().toString();
            if (subName.startsWith("minecraft:")) {
               subName = subName.substring(10);
            }
         } else {
            try {
               int dimId = Integer.parseInt(subName.substring(4));
               subName = "Dim. " + dimId;
            } catch (NumberFormatException var4) {
            }
         }
      }

      return subName;
   }

   public String getFullName(String id, String containerName) {
      String name = this.getName(id);
      String subID = this.getSubId();
      if (subID.startsWith("dim%")) {
         class_5321<class_1937> dimId = this.minimapSession.getWaypointsManager().getDimensionKeyForDirectoryName(subID);
         if (dimId != null
            && this.modMain.getSupportMods().worldmap()
            && this.getRootContainer().getKey().equals(this.minimapSession.getWaypointsManager().getAutoRootContainerID())) {
            String worldMapMWName = this.modMain.getSupportMods().worldmapSupport.tryToGetMultiworldName(dimId, id);
            if (worldMapMWName != null && !worldMapMWName.equals(id)) {
               name = worldMapMWName;
            }
         }
      }

      if (name != null && (this.worlds.size() >= 2 || containerName.isEmpty())) {
         return containerName.length() > 0 ? name + " - " + containerName : name;
      } else {
         return containerName;
      }
   }

   public String getKey() {
      return this.key;
   }

   public WaypointWorld getFirstWorld() {
      if (!this.worlds.isEmpty()) {
         return ((WaypointWorld[])this.worlds.values().toArray(new WaypointWorld[0]))[0];
      } else {
         WaypointWorldContainer[] subs = this.subContainers.values().toArray(new WaypointWorldContainer[0]);

         for (int i = 0; i < subs.length; i++) {
            WaypointWorld subFirst = subs[i].getFirstWorld();
            if (subFirst != null) {
               return subFirst;
            }
         }

         return null;
      }
   }

   public WaypointWorld getFirstWorldConnectedTo(WaypointWorld refWorld) {
      if (!this.worlds.isEmpty()) {
         WaypointWorldRootContainer rootContainer = this.getRootContainer();

         for (WaypointWorld world : this.worlds.values()) {
            if (rootContainer.getSubWorldConnections().isConnected(refWorld, world)) {
               return world;
            }
         }
      }

      WaypointWorldContainer[] subs = this.subContainers.values().toArray(new WaypointWorldContainer[0]);

      for (int i = 0; i < subs.length; i++) {
         WaypointWorld subFirst = subs[i].getFirstWorldConnectedTo(refWorld);
         if (subFirst != null) {
            return subFirst;
         }
      }

      return null;
   }

   @Override
   public String toString() {
      return this.key + " sc:" + this.subContainers.size() + " w:" + this.worlds.size();
   }

   public ArrayList<WaypointWorld> getAllWorlds() {
      ArrayList<WaypointWorld> allWorlds = new ArrayList<>(this.worlds.values());
      WaypointWorldContainer[] subs = this.subContainers.values().toArray(new WaypointWorldContainer[0]);

      for (int i = 0; i < subs.length; i++) {
         allWorlds.addAll(subs[i].getAllWorlds());
      }

      return allWorlds;
   }

   public String getEqualIgnoreCaseSub(String cId) {
      if (cId.equalsIgnoreCase(this.key)) {
         return this.key;
      } else if (this.subContainers.isEmpty()) {
         return null;
      } else if (!cId.toLowerCase().startsWith(this.key.toLowerCase() + "/")) {
         return null;
      } else {
         for (Entry<String, WaypointWorldContainer> entry : this.subContainers.entrySet()) {
            String subSearch = entry.getValue().getEqualIgnoreCaseSub(cId);
            if (subSearch != null) {
               return subSearch;
            }
         }

         return this.key + cId.substring(this.key.length());
      }
   }

   public WaypointWorldRootContainer getRootContainer() {
      return this.rootContainer;
   }

   public void renameOldContainer(String containerID) {
      if (!this.subContainers.isEmpty()) {
         String dimensionPart = containerID.split("/")[1];
         if (!this.subContainers.containsKey(dimensionPart)) {
            class_5321<class_1937> dimId = this.minimapSession.getWaypointsManager().getDimensionKeyForDirectoryName(dimensionPart);
            if (dimId != null) {
               class_2960 dimKey = dimId.method_29177();
               String dimKeyOldValidation = dimKey.method_12832().replaceAll("[^a-zA-Z0-9_]+", "");
               String currentCustomID = this.minimapSession.getWaypointsManager().getCustomContainerID();
               WaypointWorldContainer currentCustomContainer = currentCustomID == null
                  ? null
                  : this.minimapSession.getWaypointsManager().getWorldContainer(currentCustomID);

               for (Entry<String, WaypointWorldContainer> subContainerEntry : this.subContainers.entrySet()) {
                  String subKey = subContainerEntry.getKey();
                  if (subKey.equals(dimKeyOldValidation)) {
                     WaypointWorldContainer subContainer = subContainerEntry.getValue();
                     boolean currentlySelected = currentCustomContainer == subContainer;
                     this.subContainers.put(dimensionPart, subContainer);
                     this.subContainers.remove(subKey);
                     SimpleBackup.moveToBackup(subContainer.getDirectory().toPath());
                     subContainer.setKey(this.key + "/" + dimensionPart);
                     if (currentlySelected) {
                        this.minimapSession.getWaypointsManager().setCustomContainerID(subContainer.getKey());
                        this.minimapSession.getWaypointsManager().updateWaypoints();
                     }

                     try {
                        this.modMain.getSettings().saveWorlds(this.getAllWorlds());
                     } catch (IOException var15) {
                        throw new RuntimeException("Failed to rename a dimension! Can't continue.", var15);
                     }

                     WaypointWorldRootContainer rootContainer = (WaypointWorldRootContainer)this;
                     WaypointWorldConnectionManager connections = rootContainer.getSubWorldConnections();
                     connections.renameDimension(subKey, dimensionPart);
                     rootContainer.saveConfig();
                     return;
                  }
               }
            }
         }
      }
   }

   public File getDirectory() {
      return new File(this.modMain.getWaypointsFolder(), this.key);
   }
}
