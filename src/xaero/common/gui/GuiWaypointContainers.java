package xaero.common.gui;

import java.util.ArrayList;
import java.util.Collections;
import xaero.common.AXaeroMinimap;
import xaero.common.MinimapLogs;
import xaero.common.minimap.waypoints.WaypointsManager;
import xaero.common.misc.KeySortableByOther;

public class GuiWaypointContainers extends GuiDropdownHelper {
   public GuiWaypointContainers(AXaeroMinimap modMain, WaypointsManager waypointsManager, String currentContainer, String autoContainer) {
      String c = currentContainer;
      String a = autoContainer == null ? null : autoContainer.split("/")[0];
      this.current = -1;
      this.auto = -1;
      ArrayList<KeySortableByOther<String>> keysList = new ArrayList<>();
      ArrayList<String> keysStringList = new ArrayList<>();
      ArrayList<String> optionsList = new ArrayList<>();
      String[] containerKeys = waypointsManager.getWaypointMap().keySet().toArray(new String[0]);

      for (int i = 0; i < containerKeys.length; i++) {
         String containerKey = containerKeys[i];
         String[] details = containerKey.split("_");
         String containerKeyx;
         if (details.length > 1 && details[0].equals("Realms")) {
            containerKeyx = "Realm ID " + details[1].substring(details[1].indexOf(".") + 1);
         } else {
            containerKeyx = details[details.length - 1].replace("%us%", "_").replace("%fs%", "/").replace("%bs%", "\\").replace("§", ":");
         }

         if (modMain.getSettings().hideWorldNames == 1 && details.length > 1 && details[0].equals("Multiplayer")) {
            String[] dotSplit = containerKeyx.split("(\\.|:+)");
            StringBuilder builder = new StringBuilder();

            for (int o = 0; o < dotSplit.length; o++) {
               if (o < dotSplit.length - 2) {
                  builder.append("-.");
               } else if (o < dotSplit.length - 1) {
                  builder.append(dotSplit[o].charAt(0) + "-.");
               } else {
                  builder.append(dotSplit[o]);
               }
            }

            containerKeyx = builder.toString();
         }

         keysList.add(
            new KeySortableByOther<>(
               containerKey,
               containerKey.startsWith("Multiplayer_") ? 1 : (containerKey.startsWith("Realms_") ? 2 : 0),
               containerKeyx.toLowerCase(),
               containerKeyx
            )
         );
      }

      Collections.sort(keysList);

      for (int i = 0; i < keysList.size(); i++) {
         KeySortableByOther<String> k = keysList.get(i);
         String containerKeyxx = k.getKey();
         String option = "Error";

         try {
            if (this.current == -1 && containerKeyxx.equals(c)) {
               this.current = i;
            }

            if (this.auto == -1 && containerKeyxx.equals(a)) {
               this.auto = i;
            }

            option = (String)k.getDataToSortBy()[2];
            if (modMain.getSettings().hideWorldNames == 2) {
               option = "hidden " + optionsList.size();
            }

            if (this.auto == optionsList.size()) {
               option = option + " (auto)";
            }
         } catch (Exception var18) {
            MinimapLogs.LOGGER.error("suppressed exception", var18);
         }

         keysStringList.add(containerKeyxx);
         optionsList.add(option);
      }

      this.keys = keysStringList.toArray(new String[0]);
      this.options = optionsList.toArray(new String[0]);
   }
}
