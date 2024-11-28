package xaero.common.events;

import xaero.common.IXaeroMinimap;

public class ModClientEventsFabric extends ModClientEvents {
   public ModClientEventsFabric(IXaeroMinimap modMain) {
      super(modMain);
   }

   @Override
   protected void handleTextureStitchEventPost_onReset() {
      super.handleTextureStitchEventPost_onReset();
      this.modMain.getInterfaces().getMinimapInterface().getMinimapFBORenderer().resetEntityIconsResources();
   }
}
