package xaero.common.events;

import net.minecraft.class_332;
import xaero.common.IXaeroMinimap;

public class ClientEventsFabric extends ClientEvents {
   public ClientEventsFabric(IXaeroMinimap modMain) {
      super(modMain);
   }

   @Override
   public void handleRenderGameOverlayEventPre(class_332 guiGraphics, float partialTicks) {
      super.handleRenderGameOverlayEventPre(guiGraphics, partialTicks);
   }

   @Override
   public void handleRenderGameOverlayEventPost() {
      super.handleRenderGameOverlayEventPost();
   }
}
