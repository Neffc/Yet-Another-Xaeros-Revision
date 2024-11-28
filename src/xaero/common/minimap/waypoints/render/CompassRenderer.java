package xaero.common.minimap.waypoints.render;

import com.mojang.blaze3d.platform.GlStateManager.class_4534;
import com.mojang.blaze3d.platform.GlStateManager.class_4535;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.class_1074;
import net.minecraft.class_310;
import net.minecraft.class_4587;
import net.minecraft.class_4588;
import net.minecraft.class_4597.class_4598;
import xaero.common.IXaeroMinimap;
import xaero.common.minimap.element.render.over.MinimapElementOverMapRendererHandler;
import xaero.common.minimap.render.MinimapRendererHelper;
import xaero.common.misc.Misc;
import xaero.common.settings.ModSettings;

public class CompassRenderer {
   private final IXaeroMinimap modMain;
   private final class_310 mc;
   private double[] partialDest;

   public CompassRenderer(IXaeroMinimap modMain, class_310 mc) {
      this.modMain = modMain;
      this.mc = mc;
      this.partialDest = new double[2];
   }

   public void drawCompass(
      class_4587 matrixStack,
      MinimapRendererHelper rendererHelper,
      int specW,
      int specH,
      double ps,
      double pc,
      double zoom,
      boolean circle,
      float minimapScale,
      boolean background,
      class_4598 textRenderTypeBuffer,
      class_4588 nameBgBuilder
   ) {
      if (this.modMain.getSettings().compassLocation != 0) {
         String[] nesw = new String[]{
            class_1074.method_4662("gui.xaero_compass_north", new Object[0]),
            class_1074.method_4662("gui.xaero_compass_east", new Object[0]),
            class_1074.method_4662("gui.xaero_compass_south", new Object[0]),
            class_1074.method_4662("gui.xaero_compass_west", new Object[0])
         };
         int defaultColor = ModSettings.COLORS[this.modMain.getSettings().compassColor];
         RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

         for (int i = 0; i < 4; i++) {
            double offx = i != 0 && i != 2 ? (double)(i == 1 ? 10000 : -10000) : 0.0;
            double offy = i != 1 && i != 3 ? (double)(i == 2 ? 10000 : -10000) : 0.0;
            matrixStack.method_46416(0.0F, 0.0F, 2.0F);
            matrixStack.method_22903();
            MinimapElementOverMapRendererHandler.translatePosition(matrixStack, specW, specH, specW, specH, ps, pc, offx, offy, zoom, circle, this.partialDest);
            matrixStack.method_46416(-1.0F, -1.0F, 0.0F);
            matrixStack.method_22905(minimapScale, minimapScale, 1.0F);
            int halfW = this.mc.field_1772.method_1727(nesw[i]) / 2 - 1;
            int effectiveColor = i == 0 ? ModSettings.COLORS[this.modMain.getSettings().getNorthCompassColor()] : defaultColor;
            if (background) {
               RenderSystem.enableBlend();
               RenderSystem.blendFuncSeparate(class_4535.SRC_ALPHA, class_4534.ONE_MINUS_SRC_ALPHA, class_4535.ZERO, class_4534.ONE);
               int addedFrame = halfW > 3 ? halfW - 3 : 0;
               rendererHelper.addColoredRectToExistingBuffer(
                  matrixStack.method_23760().method_23761(),
                  nameBgBuilder,
                  (float)(-4 - addedFrame),
                  (float)(-4 - addedFrame),
                  9 + 2 * addedFrame,
                  9 + 2 * addedFrame,
                  -1879048192 | effectiveColor & 16777215
               );
               RenderSystem.defaultBlendFunc();
            }

            Misc.drawNormalText(matrixStack, nesw[i], (float)(-halfW + 1), -2.0F, effectiveColor, false, textRenderTypeBuffer);
            matrixStack.method_46416(0.0F, 0.0F, 1.0F);
            Misc.drawNormalText(matrixStack, nesw[i], (float)(-halfW), -3.0F, -1, false, textRenderTypeBuffer);
            matrixStack.method_22909();
         }

         matrixStack.method_46416(0.0F, 0.0F, 2.0F);
      }
   }
}
