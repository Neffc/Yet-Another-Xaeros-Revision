package xaero.common.events;

import net.minecraft.class_1657;
import net.minecraft.class_3222;
import xaero.common.AXaeroMinimap;

public class FMLCommonEventHandler {
   private AXaeroMinimap modMain;

   public FMLCommonEventHandler(AXaeroMinimap modMain) {
      this.modMain = modMain;
   }

   public void handlePlayerTickStart(class_1657 player) {
      if (player instanceof class_3222) {
         this.modMain.getServerPlayerTickHandler().tick((class_3222)player);
      } else {
         if (AXaeroMinimap.INSTANCE.getFMLEvents() != null) {
            AXaeroMinimap.INSTANCE.getFMLEvents().handlePlayerTickStart(player);
         }
      }
   }
}
