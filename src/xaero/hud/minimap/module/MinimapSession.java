package xaero.hud.minimap.module;

import net.minecraft.class_310;
import xaero.common.HudMod;
import xaero.common.XaeroMinimapSession;
import xaero.common.minimap.MinimapProcessor;
import xaero.hud.minimap.Minimap;
import xaero.hud.module.HudModule;
import xaero.hud.module.ModuleSession;

public class MinimapSession extends ModuleSession<MinimapSession> {
   private Minimap minimap;

   public MinimapSession(HudMod modMain, HudModule<MinimapSession> module) {
      super(modMain, module);
      this.minimap = modMain.getMinimap();
   }

   @Override
   public boolean isActive() {
      return this.modMain.getSettings().getMinimap();
   }

   @Override
   public int getWidth(double screenScale) {
      return (int)((double)((float)this.getConfiguredWidth() * this.modMain.getSettings().getMinimapScale()) / screenScale);
   }

   @Override
   public int getHeight(double screenScale) {
      return this.getWidth(screenScale);
   }

   public int getConfiguredWidth() {
      return this.getProcessor().getMinimapSize() / 2 + 18;
   }

   @Override
   public void prePotentialRender() {
      try {
         super.prePotentialRender();
         this.getProcessor().checkFBO();
         this.modMain.getTrackedPlayerRenderer().getCollector().update(class_310.method_1551());
      } catch (Throwable var2) {
         this.minimap.setCrashedWith(var2);
         this.minimap.checkCrashes();
      }
   }

   public MinimapProcessor getProcessor() {
      return XaeroMinimapSession.getCurrentSession().getMinimapProcessor();
   }

   @Override
   public void close() {
   }

   public boolean getHideMinimapUnderScreen() {
      return this.modMain.getSettings().hideMinimapUnderScreen;
   }

   public boolean getHideMinimapUnderF3() {
      return this.modMain.getSettings().hideMinimapUnderF3;
   }
}
