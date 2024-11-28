package xaero.common.server.core;

import net.minecraft.class_1657;
import net.minecraft.class_3222;
import xaero.common.AXaeroMinimap;

public class XaeroMinimapServerCore {
   public static AXaeroMinimap modMain;

   public static void onServerWorldInfo(class_1657 player) {
      if (modMain != null) {
         modMain.getCommonEvents().onPlayerWorldJoin((class_3222)player);
      }
   }
}
