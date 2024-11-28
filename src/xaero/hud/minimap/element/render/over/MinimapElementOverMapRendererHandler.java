package xaero.hud.minimap.element.render.over;

import java.util.List;
import net.minecraft.class_243;
import net.minecraft.class_332;
import net.minecraft.class_4587;
import net.minecraft.class_4597.class_4598;
import xaero.common.HudMod;
import xaero.common.minimap.element.render.MinimapElementRendererHandler;
import xaero.hud.minimap.element.render.MinimapElementReader;
import xaero.hud.minimap.element.render.MinimapElementRenderInfo;
import xaero.hud.minimap.element.render.MinimapElementRenderLocation;
import xaero.hud.minimap.element.render.MinimapElementRenderer;

public class MinimapElementOverMapRendererHandler extends MinimapElementRendererHandler {
   private int halfViewW;
   private int halfViewH;
   private int specW;
   private int specH;
   private boolean circle;
   private float optionalScale;
   private final double[] partialTranslate;

   protected MinimapElementOverMapRendererHandler(HudMod modMain, List<MinimapElementRenderer<?, ?>> renderers, double[] partialTranslate) {
      super(modMain, renderers, MinimapElementRenderLocation.OVER_MINIMAP, 9800);
      this.partialTranslate = partialTranslate;
   }

   public void prepareRender(int specW, int specH, int halfViewW, int halfViewH, boolean circle, float minimapScale) {
      this.specW = specW;
      this.specH = specH;
      this.halfViewW = halfViewW;
      this.halfViewH = halfViewH;
      this.circle = circle;
      this.optionalScale = minimapScale;
   }

   @Override
   protected <E, RRC, RR extends MinimapElementRenderer<E, RRC>> boolean transformAndRenderForRenderer(
      E element,
      RR renderer,
      RRC context,
      double playerDimDiv,
      double ps,
      double pc,
      double zoom,
      int elementIndex,
      double optionalDepth,
      MinimapElementRenderInfo renderInfo,
      class_332 guiGraphics,
      class_4598 renderTypeBuffers
   ) {
      class_4587 matrixStack = guiGraphics.method_51448();
      float partialTicks = renderInfo.partialTicks;
      class_243 renderPos = renderInfo.renderPos;
      MinimapElementReader<E, RRC> elementReader = renderer.getElementReader();
      double offx = elementReader.getRenderX(element, context, partialTicks) / playerDimDiv - renderPos.field_1352;
      double offy = elementReader.getRenderZ(element, context, partialTicks) / playerDimDiv - renderPos.field_1350;
      matrixStack.method_22903();
      boolean outOfBounds = translatePosition(
         matrixStack, this.specW, this.specH, this.halfViewW, this.halfViewH, ps, pc, offx, offy, zoom, this.circle, this.partialTranslate
      );
      boolean result = renderer.renderElement(
         element,
         false,
         outOfBounds,
         optionalDepth,
         this.optionalScale,
         this.partialTranslate[0],
         this.partialTranslate[1],
         renderInfo,
         guiGraphics,
         renderTypeBuffers
      );
      matrixStack.method_22909();
      return result;
   }

   @Override
   protected void beforeRender(class_4587 matrixStack) {
   }

   @Override
   protected void afterRender(class_4587 matrixStack) {
   }

   @Override
   protected int getIndexLimit() {
      return 9800;
   }

   public static boolean translatePosition(
      class_4587 matrixStack,
      int specW,
      int specH,
      int halfViewW,
      int halfViewH,
      double ps,
      double pc,
      double offx,
      double offy,
      double zoom,
      boolean circle,
      double[] partialTranslate
   ) {
      boolean outOfBounds = false;
      double Y = (pc * offx + ps * offy) * zoom;
      double X = (ps * offx - pc * offy) * zoom;
      double borderedX = X;
      double borderedY = Y;
      if (!circle) {
         if (X > (double)specW) {
            borderedX = (double)specW;
            borderedY = Y * (double)specW / X;
            outOfBounds = true;
         } else if (X < (double)(-specW)) {
            borderedX = (double)(-specW);
            borderedY = -Y * (double)specW / X;
            outOfBounds = true;
         }

         if (borderedY > (double)specH) {
            borderedY = (double)specH;
            borderedX = X * (double)specH / Y;
            outOfBounds = true;
         } else if (borderedY < (double)(-specH)) {
            borderedY = (double)(-specH);
            borderedX = -X * (double)specH / Y;
            outOfBounds = true;
         }

         if (!outOfBounds
            && (borderedX > (double)halfViewW || borderedX < (double)(-halfViewW) || borderedY > (double)halfViewH || borderedY < (double)(-halfViewH))) {
            outOfBounds = true;
         }
      } else {
         double distSquared = X * X + Y * Y;
         double maxDistSquared = (double)(specW * specW);
         if (distSquared > maxDistSquared) {
            double scaleDown = Math.sqrt(maxDistSquared / distSquared);
            borderedX = X * scaleDown;
            borderedY = Y * scaleDown;
            outOfBounds = true;
         }

         if (!outOfBounds && distSquared > (double)(halfViewW * halfViewW)) {
            outOfBounds = true;
         }
      }

      long roundedX = Math.round(borderedX);
      long roundedY = Math.round(borderedY);
      partialTranslate[0] = borderedX - (double)roundedX;
      partialTranslate[1] = borderedY - (double)roundedY;
      matrixStack.method_46416((float)roundedX, (float)roundedY, 0.0F);
      return outOfBounds;
   }

   public static final class Builder extends xaero.hud.minimap.element.render.MinimapElementRendererHandler.Builder {
      public xaero.common.minimap.element.render.over.MinimapElementOverMapRendererHandler build() {
         return (xaero.common.minimap.element.render.over.MinimapElementOverMapRendererHandler)super.build();
      }

      protected xaero.common.minimap.element.render.over.MinimapElementOverMapRendererHandler buildInternally(List<MinimapElementRenderer<?, ?>> renderers) {
         return new xaero.common.minimap.element.render.over.MinimapElementOverMapRendererHandler(HudMod.INSTANCE, renderers, new double[2]);
      }

      protected MinimapElementOverMapRendererHandler.Builder setDefault() {
         super.setDefault();
         return this;
      }

      public static MinimapElementOverMapRendererHandler.Builder begin() {
         return new MinimapElementOverMapRendererHandler.Builder().setDefault();
      }
   }
}
