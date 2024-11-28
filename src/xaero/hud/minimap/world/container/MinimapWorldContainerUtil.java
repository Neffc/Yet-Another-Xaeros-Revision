package xaero.hud.minimap.world.container;

import xaero.hud.path.XaeroPath;

public class MinimapWorldContainerUtil {
   public static boolean isMultiplayer(XaeroPath containerPath) {
      String rootNode = containerPath.getRoot().getLastNode();
      return rootNode.startsWith("Multiplayer_") || rootNode.startsWith("Realms_");
   }

   public static String convertWorldFolderToContainerNode(String worldFolder) {
      return convertWorldFolderToContainerNode(worldFolder, 2);
   }

   public static String convertWorldFolderToContainerNode(String worldFolder, int version) {
      String result = worldFolder.replace("_", "%us%").replace("/", "%fs%").replace("\\", "%bs%");
      if (version >= 2) {
         result = result.replace("[", "%lb%").replace("]", "%rb%");
      }

      return result;
   }
}
