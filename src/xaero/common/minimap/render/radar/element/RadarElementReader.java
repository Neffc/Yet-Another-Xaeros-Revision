package xaero.common.minimap.render.radar.element;

import net.minecraft.class_1297;
import net.minecraft.class_310;
import xaero.common.minimap.element.render.MinimapElementReader;
import xaero.hud.minimap.element.render.MinimapElementRenderLocation;

public final class RadarElementReader extends MinimapElementReader<class_1297, RadarRenderContext> {
   public double getRenderX(class_1297 element, RadarRenderContext context, float partialTicks) {
      return context.minimapRadar.getEntityX(element, partialTicks);
   }

   public double getRenderY(class_1297 element, RadarRenderContext context, float partialTicks) {
      return element.method_23318();
   }

   public double getRenderZ(class_1297 element, RadarRenderContext context, float partialTicks) {
      return context.minimapRadar.getEntityZ(element, partialTicks);
   }

   public boolean isHidden(class_1297 element, RadarRenderContext context) {
      return false;
   }

   public int getInteractionBoxLeft(class_1297 element, RadarRenderContext context, float partialTicks) {
      return -16;
   }

   public int getInteractionBoxRight(class_1297 element, RadarRenderContext context, float partialTicks) {
      return 16;
   }

   public int getInteractionBoxTop(class_1297 element, RadarRenderContext context, float partialTicks) {
      return -16;
   }

   public int getInteractionBoxBottom(class_1297 element, RadarRenderContext context, float partialTicks) {
      return 16;
   }

   public int getRenderBoxLeft(class_1297 element, RadarRenderContext context, float partialTicks) {
      return -64;
   }

   public int getRenderBoxRight(class_1297 element, RadarRenderContext context, float partialTicks) {
      return 64;
   }

   public int getRenderBoxTop(class_1297 element, RadarRenderContext context, float partialTicks) {
      return -32;
   }

   public int getRenderBoxBottom(class_1297 element, RadarRenderContext context, float partialTicks) {
      return 32;
   }

   public int getLeftSideLength(class_1297 element, class_310 mc) {
      return 0;
   }

   public String getMenuName(class_1297 element) {
      return "n/a";
   }

   public String getFilterName(class_1297 element) {
      return this.getMenuName(element);
   }

   public int getMenuTextFillLeftPadding(class_1297 element) {
      return 0;
   }

   public int getRightClickTitleBackgroundColor(class_1297 element) {
      return 0;
   }

   @Deprecated
   public float getBoxScale(int location, class_1297 element, RadarRenderContext context) {
      return this.getBoxScale(MinimapElementRenderLocation.fromIndex(location), element, context);
   }

   public float getBoxScale(MinimapElementRenderLocation location, class_1297 element, RadarRenderContext context) {
      return location == MinimapElementRenderLocation.OVER_MINIMAP ? 0.5F : 1.0F;
   }

   public boolean isInteractable(int location, class_1297 element) {
      return this.isInteractable(MinimapElementRenderLocation.fromIndex(location), element);
   }

   public boolean isInteractable(MinimapElementRenderLocation location, class_1297 element) {
      return false;
   }

   @Override
   public boolean shouldScaleBoxWithOptionalScale() {
      return true;
   }
}
