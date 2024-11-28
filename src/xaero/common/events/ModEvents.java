package xaero.common.events;

import net.minecraft.class_1059;
import xaero.common.AXaeroMinimap;
import xaero.common.XaeroMinimapSession;
import xaero.common.interfaces.InterfaceManager;

public class ModEvents {
   private AXaeroMinimap modMain;

   public ModEvents(AXaeroMinimap modMain) {
      this.modMain = modMain;
   }

   public void handleTextureStitchEventPost(class_1059 texture) {
      if (texture.method_24106().equals(class_1059.field_5275)) {
         XaeroMinimapSession minimapSession = XaeroMinimapSession.getCurrentSession();
         if (minimapSession != null) {
            minimapSession.getMinimapProcessor().getMinimapWriter().setClearBlockColours(true);
            minimapSession.getMinimapProcessor().getMinimapWriter().resetShortBlocks();
         }

         InterfaceManager interfaceManager = this.modMain.getInterfaces();
         if (interfaceManager != null) {
            interfaceManager.getMinimapInterface().getMinimapFBORenderer().resetEntityIcons();
            interfaceManager.getMinimapInterface().getMinimapFBORenderer().resetEntityIconsResources();
         }
      }
   }
}
