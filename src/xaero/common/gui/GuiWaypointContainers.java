package xaero.common.gui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import xaero.common.HudMod;
import xaero.common.IXaeroMinimap;
import xaero.common.minimap.waypoints.WaypointsManager;
import xaero.common.misc.KeySortableByOther;
import xaero.hud.minimap.world.MinimapWorldManager;
import xaero.hud.minimap.world.container.MinimapWorldRootContainer;
import xaero.hud.path.XaeroPath;
import xaero.hud.path.XaeroPathReader;

public class GuiWaypointContainers extends GuiDropdownHelper<String> {
   @Deprecated
   private static final XaeroPathReader pathReader = new XaeroPathReader();

   @Deprecated
   public GuiWaypointContainers(IXaeroMinimap modMain, WaypointsManager waypointsManager, String currentContainer, String autoContainer) {
      this(
         (HudMod)modMain,
         waypointsManager.getWorldManager(),
         currentContainer == null ? null : pathReader.read(currentContainer),
         autoContainer == null ? null : pathReader.read(autoContainer + "/old")
      );
   }

   public GuiWaypointContainers(HudMod modMain, MinimapWorldManager manager, XaeroPath currentContainer, XaeroPath autoWorldPath) {
      List<KeySortableByOther<String>> sortableKeyList = new ArrayList<>();

      for (MinimapWorldRootContainer rootContainer : manager.getRootContainers()) {
         String rootContainerNode = rootContainer.getPath().getLastNode();
         String[] details = rootContainerNode.split("_");
         String sortName;
         if (details.length > 1 && details[0].equals("Realms")) {
            sortName = "Realm ID " + details[1].substring(details[1].indexOf(".") + 1);
         } else {
            sortName = details[details.length - 1]
               .replace("%us%", "_")
               .replace("%fs%", "/")
               .replace("%bs%", "\\")
               .replace("§", ":")
               .replace("%lb%", "[")
               .replace("%rb%", "]");
         }

         if (modMain.getSettings().hideWorldNames == 1 && details.length > 1 && details[0].equals("Multiplayer")) {
            String[] dotSplit = sortName.split("(\\.|:+)");
            StringBuilder builder = new StringBuilder();

            for (int o = 0; o < dotSplit.length; o++) {
               if (o < dotSplit.length - 2) {
                  builder.append("-.");
               } else if (o < dotSplit.length - 1) {
                  builder.append(dotSplit[o].isEmpty() ? "" : dotSplit[o].charAt(0)).append("-.");
               } else {
                  builder.append(dotSplit[o]);
               }
            }

            sortName = builder.toString();
         }

         sortableKeyList.add(
            new KeySortableByOther<>(
               rootContainerNode,
               rootContainerNode.startsWith("Multiplayer_") ? 1 : (rootContainerNode.startsWith("Realms_") ? 2 : 0),
               sortName.toLowerCase(),
               sortName
            )
         );
      }

      Collections.sort(sortableKeyList);
      this.current = -1;
      this.auto = -1;
      List<String> keyList = new ArrayList<>();
      List<String> optionList = new ArrayList<>();
      String currentRoot = currentContainer == null ? null : currentContainer.getLastNode();
      String autoRoot = autoWorldPath == null ? null : autoWorldPath.getRoot().getLastNode();

      for (int i = 0; i < sortableKeyList.size(); i++) {
         KeySortableByOther<String> k = sortableKeyList.get(i);
         String containerKey = k.getKey();
         if (this.current == -1 && containerKey.equals(currentRoot)) {
            this.current = i;
         }

         String option = (String)k.getDataToSortBy()[2];
         if (modMain.getSettings().hideWorldNames == 2) {
            option = "hidden " + optionList.size();
         }

         if (this.auto == -1 && containerKey.equals(autoRoot)) {
            this.auto = i;
            option = option + " (auto)";
         }

         keyList.add(containerKey);
         optionList.add(option);
      }

      this.keys = keyList.toArray(new String[0]);
      this.options = optionList.toArray(new String[0]);
   }
}
