package xaero.common.minimap.element.render.map;

import java.util.List;
import net.minecraft.class_1297;
import net.minecraft.class_1657;
import net.minecraft.class_276;
import net.minecraft.class_327;
import net.minecraft.class_332;
import net.minecraft.class_4587;
import net.minecraft.class_4597.class_4598;
import xaero.common.AXaeroMinimap;
import xaero.common.graphics.renderer.multitexture.MultiTextureRenderTypeRendererProvider;
import xaero.common.minimap.element.render.MinimapElementReader;
import xaero.common.minimap.element.render.MinimapElementRenderer;
import xaero.common.minimap.element.render.MinimapElementRendererHandler;
import xaero.common.minimap.render.MinimapRendererHelper;

public final class MinimapElementMapRendererHandler extends MinimapElementRendererHandler {
   private float halfWView;

   protected MinimapElementMapRendererHandler(List<MinimapElementRenderer<?, ?>> renderers) {
      super(renderers, 0);
   }

   public void render(
      class_332 guiGraphics,
      class_1297 renderEntity,
      class_1657 player,
      double renderX,
      double renderY,
      double renderZ,
      double ps,
      double pc,
      double zoom,
      boolean cave,
      float partialTicks,
      class_276 framebuffer,
      AXaeroMinimap modMain,
      MinimapRendererHelper helper,
      class_4598 renderTypeBuffers,
      class_327 font,
      MultiTextureRenderTypeRendererProvider multiTextureRenderTypeRenderers,
      float halfWView
   ) {
      this.halfWView = halfWView;
      super.render(
         guiGraphics,
         renderEntity,
         player,
         renderX,
         renderY,
         renderZ,
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
      double offx = elementReader.getRenderX(element, context, partialTicks) - renderX;
      double offz = elementReader.getRenderZ(element, context, partialTicks) - renderZ;
      matrixStack.method_22903();
      double zoomedOffX = offx * zoom;
      double zoomedOffZ = offz * zoom;
      double translateX = ps * zoomedOffX - pc * zoomedOffZ;
      double translateY = pc * zoomedOffX + ps * zoomedOffZ;
      int roundedX = (int)Math.round(translateX);
      int roundedY = (int)Math.round(translateY);
      boolean outOfBounds = (float)Math.abs(roundedX) > this.halfWView || Math.abs(renderY) > (double)this.halfWView;
      double partialX = translateX - (double)roundedX;
      double partialY = translateY - (double)roundedY;
      matrixStack.method_46416((float)roundedX, (float)roundedY, 0.0F);
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
         1.0F,
         element,
         partialX,
         partialY,
         cave,
         partialTicks
      );
      matrixStack.method_22909();
      return result;
   }

   @Override
   protected int getIndexLimit() {
      return 19490;
   }

   @Override
   protected void beforeRender(class_4587 matrixStack) {
   }

   @Override
   protected void afterRender(class_4587 matrixStack) {
   }

   public static final class Builder extends MinimapElementRendererHandler.Builder {
      public MinimapElementMapRendererHandler build() {
         return (MinimapElementMapRendererHandler)super.build();
      }

      protected MinimapElementMapRendererHandler buildInternally(List<MinimapElementRenderer<?, ?>> renderers) {
         return new MinimapElementMapRendererHandler(renderers);
      }

      protected MinimapElementMapRendererHandler.Builder setDefault() {
         super.setDefault();
         return this;
      }

      public static MinimapElementMapRendererHandler.Builder begin() {
         return new MinimapElementMapRendererHandler.Builder().setDefault();
      }
   }
}
