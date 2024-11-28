package xaero.common.events;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.class_1059;
import net.minecraft.class_310;
import net.minecraft.class_332;
import xaero.common.IXaeroMinimap;
import xaero.common.XaeroMinimapSession;
import xaero.common.anim.MultiplyAnimationHelper;
import xaero.common.interfaces.InterfaceManager;
import xaero.hud.minimap.BuiltInHudModules;
import xaero.hud.minimap.module.MinimapSession;

public abstract class ModClientEvents {
   protected IXaeroMinimap modMain;

   public ModClientEvents(IXaeroMinimap modMain) {
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
            this.handleTextureStitchEventPost_onReset();
         }
      }
   }

   public void handleRenderModOverlay(class_332 guiGraphics, float partialTicks) {
      MultiplyAnimationHelper.tick();
      RenderSystem.clear(256, class_310.field_1703);
      RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
      MinimapSession minimapSession = BuiltInHudModules.MINIMAP.getCurrentSession();
      if (minimapSession != null) {
         guiGraphics.method_51448().method_22903();
         guiGraphics.method_51448().method_46416(0.0F, 0.0F, -1.0F);
         this.modMain.getHudRenderer().render(this.modMain.getHud(), guiGraphics, partialTicks);
         this.modMain.getMinimap().getWaypointGuiRenderer().drawSetChange(minimapSession, guiGraphics, class_310.method_1551().method_22683());
         guiGraphics.method_51448().method_22909();
      }
   }

   protected void handleTextureStitchEventPost_onReset() {
   }
}
