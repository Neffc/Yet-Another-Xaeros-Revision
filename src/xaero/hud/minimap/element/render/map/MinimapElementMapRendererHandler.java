package xaero.hud.minimap.element.render.map;

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

public class MinimapElementMapRendererHandler extends MinimapElementRendererHandler {
   private float halfWView;

   protected MinimapElementMapRendererHandler(HudMod modMain, List<MinimapElementRenderer<?, ?>> renderers) {
      super(modMain, renderers, MinimapElementRenderLocation.IN_MINIMAP, 19490);
   }

   public void prepareRender(float halfWView) {
      this.halfWView = halfWView;
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
      double offz = elementReader.getRenderZ(element, context, partialTicks) / playerDimDiv - renderPos.field_1350;
      matrixStack.method_22903();
      double zoomedOffX = offx * zoom;
      double zoomedOffZ = offz * zoom;
      double translateX = ps * zoomedOffX - pc * zoomedOffZ;
      double translateY = pc * zoomedOffX + ps * zoomedOffZ;
      int roundedX = (int)Math.round(translateX);
      int roundedY = (int)Math.round(translateY);
      boolean outOfBounds = (float)Math.abs(roundedX) > this.halfWView || (float)Math.abs(roundedY) > this.halfWView;
      double partialX = translateX - (double)roundedX;
      double partialY = translateY - (double)roundedY;
      matrixStack.method_46416((float)roundedX, (float)roundedY, 0.0F);
      boolean result = renderer.renderElement(element, false, outOfBounds, optionalDepth, 1.0F, partialX, partialY, renderInfo, guiGraphics, renderTypeBuffers);
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
