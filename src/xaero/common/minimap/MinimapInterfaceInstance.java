package xaero.common.minimap;

import net.minecraft.class_310;
import net.minecraft.class_332;
import net.minecraft.class_408;
import net.minecraft.class_418;
import xaero.common.AXaeroMinimap;
import xaero.common.XaeroMinimapSession;
import xaero.common.effect.Effects;
import xaero.common.graphics.CustomVertexConsumers;
import xaero.common.gui.IScreenBase;
import xaero.common.interfaces.InterfaceInstance;
import xaero.common.minimap.render.MinimapRendererHelper;

public class MinimapInterfaceInstance extends InterfaceInstance {
   private XaeroMinimapSession minimapSession;
   private AXaeroMinimap modMain;

   public MinimapInterfaceInstance(MinimapInterface inter, AXaeroMinimap modMain, XaeroMinimapSession minimapSession) {
      super(inter);
      this.minimapSession = minimapSession;
      this.modMain = modMain;
   }

   @Override
   public void prePotentialRender() {
      try {
         super.prePotentialRender();
         this.minimapSession.getMinimapProcessor().checkFBO();
         this.modMain.getTrackedPlayerRenderer().getCollector().update(class_310.method_1551());
      } catch (Throwable var2) {
         ((MinimapInterface)this.inter).setCrashedWith(var2);
         ((MinimapInterface)this.inter).checkCrashes();
      }
   }

   @Override
   public void render(class_332 guiGraphics, int width, int height, double scale, float partial, CustomVertexConsumers cvc) {
      class_310 mc = class_310.method_1551();
      if (!mc.field_1724.method_6059(Effects.NO_MINIMAP)
         && !mc.field_1724.method_6059(Effects.NO_MINIMAP_HARMFUL)
         && !this.minimapSession.getMinimapProcessor().getNoMinimapMessageReceived()) {
         if ((
               !this.modMain.getSettings().hideMinimapUnderScreen
                  || mc.field_1755 == null
                  || mc.field_1755 instanceof IScreenBase
                  || mc.field_1755 instanceof class_408
                  || mc.field_1755 instanceof class_418
            )
            && (!this.modMain.getSettings().hideMinimapUnderF3 || !mc.field_1690.field_1866)) {
            MinimapRendererHelper.restoreDefaultShaderBlendState();
            this.minimapSession
               .getMinimapProcessor()
               .onRender(guiGraphics, this.inter.getX(), this.inter.getY(), width, height, scale, this.getInterfaceWidth(), this.getW(scale), partial, cvc);
            super.render(guiGraphics, width, height, scale, partial, cvc);
            MinimapRendererHelper.restoreDefaultShaderBlendState();
         }
      }
   }

   @Override
   public int getW(double scale) {
      return (int)((double)((float)this.getInterfaceWidth() * this.modMain.getSettings().getMinimapScale()) / scale);
   }

   @Override
   public int getH(double scale) {
      return this.getW(scale);
   }

   @Override
   public int getWC(double scale) {
      return this.getW(scale);
   }

   @Override
   public int getHC(double scale) {
      return this.getH(scale);
   }

   @Override
   public int getW0(double scale) {
      return this.getW(scale);
   }

   @Override
   public int getH0(double scale) {
      return this.getH(scale);
   }

   public int getInterfaceWidth() {
      return this.minimapSession.getMinimapProcessor().getMinimapSize() / 2 + 18;
   }

   @Override
   public int getSize() {
      int w = this.getInterfaceWidth();
      return w * w;
   }
}
