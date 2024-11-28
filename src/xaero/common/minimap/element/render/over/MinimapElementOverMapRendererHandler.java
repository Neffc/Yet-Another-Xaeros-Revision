package xaero.common.minimap.element.render.over;

import java.util.List;
import net.minecraft.class_1297;
import net.minecraft.class_1657;
import net.minecraft.class_276;
import net.minecraft.class_327;
import net.minecraft.class_332;
import net.minecraft.class_4587;
import net.minecraft.class_4597.class_4598;
import xaero.common.IXaeroMinimap;
import xaero.common.graphics.renderer.multitexture.MultiTextureRenderTypeRendererProvider;
import xaero.common.minimap.element.render.MinimapElementReader;
import xaero.common.minimap.element.render.MinimapElementRenderer;
import xaero.common.minimap.element.render.MinimapElementRendererHandler;
import xaero.common.minimap.render.MinimapRendererHelper;

public class MinimapElementOverMapRendererHandler extends MinimapElementRendererHandler {
   private int halfViewW;
   private int halfViewH;
   private int specW;
   private int specH;
   private boolean circle;
   private float optionalScale;
   private final double[] partialTranslate;

   protected MinimapElementOverMapRendererHandler(List<MinimapElementRenderer<?, ?>> renderers, double[] partialTranslate) {
      super(renderers, 1);
      this.partialTranslate = partialTranslate;
   }

   public void render(
      class_332 guiGraphics,
      class_1297 renderEntity,
      class_1657 player,
      double renderX,
      double renderY,
      double renderZ,
      double playerDimDiv,
      double ps,
      double pc,
      double zoom,
      boolean cave,
      float partialTicks,
      class_276 framebuffer,
      IXaeroMinimap modMain,
      MinimapRendererHelper helper,
      class_4598 renderTypeBuffers,
      class_327 font,
      MultiTextureRenderTypeRendererProvider multiTextureRenderTypeRenderers,
      int specW,
      int specH,
      int halfViewW,
      int halfViewH,
      boolean circle,
      float minimapScale
   ) {
      this.specW = specW;
      this.specH = specH;
      this.halfViewW = halfViewW;
      this.halfViewH = halfViewH;
      this.circle = circle;
      this.optionalScale = minimapScale;
      super.render(
         guiGraphics,
         renderEntity,
         player,
         renderX,
         renderY,
         renderZ,
         playerDimDiv,
         ps,
         pc,
         zoom,
         cave,
         partialTicks,
         framebuffer,
         modMain,
         helper,
         renderTypeBuffers,
         font,
         multiTextureRenderTypeRenderers
      );
   }

   @Override
   protected <E, RRC, RR extends MinimapElementRenderer<E, RRC>> boolean transformAndRenderForRenderer(
      RR renderer,
      class_332 guiGraphics,
      class_4598 renderTypeBuffers,
      class_327 font,
      class_276 framebuffer,
      MinimapRendererHelper helper,
      class_1297 renderEntity,
      class_1657 player,
      double renderX,
      double renderY,
      double renderZ,
      double playerDimDiv,
      double ps,
      double pc,
      double zoom,
      boolean cave,
      float partialTicks,
      int elementIndex,
      double optionalDepth,
      E element,
      MinimapElementReader<E, RRC> elementReader,
      RRC context
   ) {
      class_4587 matrixStack = guiGraphics.method_51448();
      double offx = elementReader.getRenderX(element, context, partialTicks) / playerDimDiv - renderX;
      double offy = elementReader.getRenderZ(element, context, partialTicks) / playerDimDiv - renderZ;
      matrixStack.method_22903();
      boolean outOfBounds = translatePosition(
         matrixStack, this.specW, this.specH, this.halfViewW, this.halfViewH, ps, pc, offx, offy, zoom, this.circle, this.partialTranslate
      );
      boolean result = renderer.renderElement(
         this.location,
         false,
         outOfBounds,
         guiGraphics,
         renderTypeBuffers,
         font,
         framebuffer,
         helper,
         renderEntity,
         player,
         renderX,
         renderY,
         renderZ,
         elementIndex,
         optionalDepth,
         this.optionalScale,
         element,
         this.partialTranslate[0],
         this.partialTranslate[1],
         cave,
         partialTicks
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

   public static final class Builder extends MinimapElementRendererHandler.Builder {
      public MinimapElementOverMapRendererHandler build() {
         return (MinimapElementOverMapRendererHandler)super.build();
      }

      protected MinimapElementOverMapRendererHandler buildInternally(List<MinimapElementRenderer<?, ?>> renderers) {
         return new MinimapElementOverMapRendererHandler(renderers, new double[2]);
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
