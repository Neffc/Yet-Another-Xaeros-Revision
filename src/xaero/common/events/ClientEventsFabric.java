package xaero.common.events;

import net.minecraft.class_332;
import xaero.common.IXaeroMinimap;

public class ClientEventsFabric extends ClientEvents {
   public static boolean renderCrosshairs = true;
   private boolean crosshairDisabledByThisMod = false;

   public ClientEventsFabric(IXaeroMinimap modMain) {
      super(modMain);
   }

   @Override
   public void handleRenderGameOverlayEventPre(class_332 guiGraphics, float partialTicks) {
      super.handleRenderGameOverlayEventPre(guiGraphics, partialTicks);
      if (renderCrosshairs && this.handleRenderCrosshairOverlay(guiGraphics)) {
         renderCrosshairs = false;
         this.crosshairDisabledByThisMod = true;
      }
   }

   @Override
   public void handleRenderGameOverlayEventPost() {
      if (this.crosshairDisabledByThisMod) {
         renderCrosshairs = true;
         this.crosshairDisabledByThisMod = false;
      }

      super.handleRenderGameOverlayEventPost();
   }
}
