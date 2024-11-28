package xaero.common.interfaces.render;

import xaero.common.IXaeroMinimap;
import xaero.common.graphics.CustomVertexConsumers;

@Deprecated
public class InterfaceRenderer {
   private final IXaeroMinimap modMain;

   public InterfaceRenderer(IXaeroMinimap modMain) {
      this.modMain = modMain;
   }

   public CustomVertexConsumers getCustomVertexConsumers() {
      return this.modMain.getHudRenderer().getCustomVertexConsumers();
   }
}
