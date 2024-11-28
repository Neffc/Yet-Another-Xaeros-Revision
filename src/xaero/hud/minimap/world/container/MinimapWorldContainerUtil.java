package xaero.hud.minimap.world.container;

import xaero.hud.path.XaeroPath;

public class MinimapWorldContainerUtil {
   public static boolean isMultiplayer(XaeroPath containerPath) {
      String rootNode = containerPath.getRoot().getLastNode();
      return rootNode.startsWith("Multiplayer_") || rootNode.startsWith("Realms_");
   }
}
