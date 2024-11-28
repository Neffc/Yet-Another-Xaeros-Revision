package xaero.common.gui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import xaero.common.minimap.waypoints.WaypointWorld;
import xaero.common.minimap.waypoints.WaypointWorldContainer;
import xaero.common.minimap.waypoints.WaypointWorldRootContainer;
import xaero.common.minimap.waypoints.WaypointsManager;
import xaero.common.misc.KeySortableByOther;
import xaero.hud.minimap.MinimapLogs;

public class GuiWaypointWorlds extends GuiDropdownHelper {
   public GuiWaypointWorlds(WaypointWorldContainer wc, WaypointsManager waypointsManager, String currentWorld, String autoContainer, String autoWorld) {
      String a = autoContainer + "_" + autoWorld;
      this.current = -1;
      this.auto = -1;
      ArrayList<KeySortableByOther<String>> keysList = new ArrayList<>();
      HashMap<String, String> nameMap = new HashMap<>();
      boolean connections = autoContainer != null
         && waypointsManager.isMultiplayer(autoContainer)
         && wc.getKey().equals(waypointsManager.getAutoRootContainerID());
      WaypointWorld autoWorldObj = autoWorld == null ? null : waypointsManager.getWorld(autoContainer, autoWorld);
      this.addWorlds((WaypointWorldRootContainer)wc, autoWorldObj, connections, wc, a, keysList, nameMap);
      Collections.sort(keysList);
      ArrayList<String> keysStringList = new ArrayList<>();
      ArrayList<String> optionsList = new ArrayList<>();

      for (int j = 0; j < keysList.size(); j++) {
         KeySortableByOther<String> keySortable = keysList.get(j);
         String key = keySortable.getKey();
         if (this.current == -1 && key.equals(currentWorld)) {
            this.current = j;
         }

         String option = "Error";

         try {
            if (this.auto == -1 && key.equals(a)) {
               this.auto = j;
            }

            option = nameMap.get(key);
            if (this.auto == j) {
               option = option + " (auto)";
            }
         } catch (Exception var18) {
            MinimapLogs.LOGGER.error("suppressed exception", var18);
         }

         keysStringList.add(key);
         optionsList.add(option);
      }

      if (this.current == -1) {
         this.current = 0;
      }

      this.keys = keysStringList.toArray(new String[0]);
      this.options = optionsList.toArray(new String[0]);
   }

   private void addWorlds(
      WaypointWorldRootContainer rootWC,
      WaypointWorld autoWorld,
      boolean connections,
      WaypointWorldContainer wc,
      String a,
      ArrayList<KeySortableByOther<String>> keysList,
      HashMap<String, String> nameMap
   ) {
      String[] worldKeys = wc.worlds.keySet().toArray(new String[0]);

      for (int j = 0; j < worldKeys.length; j++) {
         String worldKey = worldKeys[j];
         String containerName = wc.getSubName();
         String worldName = wc.getFullName(worldKey, containerName);
         String fullKey = wc.getKey() + "_" + worldKey;
         int firstNameSpace = worldName.indexOf(32);
         String firstNameWord = firstNameSpace != -1 ? worldName.substring(0, firstNameSpace) : "";
         int firstNameWordAsInt = 0;

         try {
            firstNameWordAsInt = Integer.parseInt(firstNameWord);
         } catch (NumberFormatException var19) {
         }

         boolean connected = false;
         if (connections) {
            WaypointWorld waypointWorld = wc.worlds.get(worldKey);
            if (rootWC.getSubWorldConnections().isConnected(autoWorld, waypointWorld)) {
               connected = true;
               if (!fullKey.equals(a)) {
                  worldName = worldName + " *";
               }
            }
         }

         keysList.add(new KeySortableByOther<>(fullKey, !connected, containerName.toLowerCase(), firstNameWordAsInt, worldName.toLowerCase()));
         nameMap.put(fullKey, worldName);
      }

      WaypointWorldContainer[] subContainers = wc.subContainers.values().toArray(new WaypointWorldContainer[0]);

      for (int i = 0; i < subContainers.length; i++) {
         this.addWorlds(rootWC, autoWorld, connections, subContainers[i], a, keysList, nameMap);
      }
   }

   public String[] getCurrentKeys() {
      String fullKey = this.getCurrentKey();
      return new String[]{fullKey.substring(0, fullKey.lastIndexOf("_")), fullKey.substring(fullKey.lastIndexOf("_") + 1)};
   }
}
