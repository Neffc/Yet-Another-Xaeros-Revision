package xaero.hud.minimap.element.render.map;

import java.util.List;
import net.minecraft.class_243;
import net.minecraft.class_332;
import net.minecraft.class_4587;
import net.minecraft.class_4597.class_4598;
import xaero.common.HudMod;
import xaero.common.minimap.element.render.MinimapElementRendererHandler;
import xaero.hud.minimap.element.render.MinimapElementRenderInfo;
import xaero.hud.minimap.element.render.MinimapElementRenderLocation;
import xaero.hud.minimap.element.render.MinimapElementRenderer;

public class MinimapElementMapRendererHandler extends MinimapElementRendererHandler {
   private double ps;
   private double pc;
   private double zoom;
   private float halfWView;

   protected MinimapElementMapRendererHandler(HudMod modMain, List<MinimapElementRenderer<?, ?>> renderers) {
      super(modMain, renderers, MinimapElementRenderLocation.IN_MINIMAP, 19490);
   }

   public void prepareRender(double ps, double pc, double zoom, float halfWView) {
      this.ps = ps;
      this.pc = pc;
      this.zoom = zoom;
      this.halfWView = halfWView;
   }

   @Override
   protected <E, RRC, RR extends MinimapElementRenderer<E, RRC>> boolean transformAndRenderForRenderer(
      E element,
      double elementX,
      double elementY,
      double elementZ,
      RR renderer,
      RRC context,
      int elementIndex,
      double optionalDepth,
      MinimapElementRenderInfo renderInfo,
      class_332 guiGraphics,
      class_4598 vanillaBufferSource
   ) {
      class_4587 matrixStack = guiGraphics.method_51448();
      class_243 renderPos = renderInfo.renderPos;
      double offx = elementX - renderPos.field_1352;
      double offz = elementZ - renderPos.field_1350;
      matrixStack.method_22903();
      double zoomedOffX = offx * this.zoom;
      double zoomedOffZ = offz * this.zoom;
      double translateX = this.ps * zoomedOffX - this.pc * zoomedOffZ;
      double translateY = this.pc * zoomedOffX + this.ps * zoomedOffZ;
      int roundedX = (int)Math.round(translateX);
      int roundedY = (int)Math.round(translateY);
      boolean outOfBounds = (float)Math.abs(roundedX) > this.halfWView || (float)Math.abs(roundedY) > this.halfWView;
      double partialX = translateX - (double)roundedX;
      double partialY = translateY - (double)roundedY;
      matrixStack.method_46416((float)roundedX, (float)roundedY, 0.0F);
      boolean result = renderer.renderElement(
         element, false, outOfBounds, optionalDepth, 1.0F, partialX, partialY, renderInfo, guiGraphics, vanillaBufferSource
      );
      matrixStack.method_22909();
      return result;
   }

   @Override
   protected void beforeRender(class_332 guiGraphics, MinimapElementRenderInfo renderInfo, class_4598 vanillaBufferSource) {
   }

   @Override
   protected void afterRender(class_332 guiGraphics, MinimapElementRenderInfo renderInfo, class_4598 vanillaBufferSource) {
   }

   public static final class Builder extends xaero.hud.minimap.element.render.MinimapElementRendererHandler.Builder {
      public MinimapElementMapRendererHandler build() {
         return (MinimapElementMapRendererHandler)super.build();
      }

      protected MinimapElementMapRendererHandler buildInternally(List<MinimapElementRenderer<?, ?>> renderers) {
         return new xaero.common.minimap.element.render.map.MinimapElementMapRendererHandler(HudMod.INSTANCE, renderers);
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
