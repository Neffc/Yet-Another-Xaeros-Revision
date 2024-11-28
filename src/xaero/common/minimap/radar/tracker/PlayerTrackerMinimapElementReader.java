package xaero.common.minimap.radar.tracker;

import net.minecraft.class_310;
import net.minecraft.class_640;
import xaero.hud.minimap.element.render.MinimapElementReader;
import xaero.hud.minimap.element.render.MinimapElementRenderLocation;

public class PlayerTrackerMinimapElementReader extends MinimapElementReader<PlayerTrackerMinimapElement<?>, PlayerTrackerMinimapElementRenderContext> {
   public boolean isHidden(PlayerTrackerMinimapElement<?> element, PlayerTrackerMinimapElementRenderContext context) {
      return class_310.method_1551().field_1687.method_27983() != element.getDimension() && context.mapDimId != element.getDimension();
   }

   public double getRenderX(PlayerTrackerMinimapElement<?> element, PlayerTrackerMinimapElementRenderContext context, float partialTicks) {
      return class_310.method_1551().field_1687.method_27983() != element.getDimension() ? element.getX() * context.mapDimDiv : element.getX();
   }

   public double getRenderY(PlayerTrackerMinimapElement<?> element, PlayerTrackerMinimapElementRenderContext context, float partialTicks) {
      return element.getY();
   }

   public double getRenderZ(PlayerTrackerMinimapElement<?> element, PlayerTrackerMinimapElementRenderContext context, float partialTicks) {
      return class_310.method_1551().field_1687.method_27983() != element.getDimension() ? element.getZ() * context.mapDimDiv : element.getZ();
   }

   public int getInteractionBoxLeft(PlayerTrackerMinimapElement<?> element, PlayerTrackerMinimapElementRenderContext context, float partialTicks) {
      return -10;
   }

   public int getInteractionBoxRight(PlayerTrackerMinimapElement<?> element, PlayerTrackerMinimapElementRenderContext context, float partialTicks) {
      return 10;
   }

   public int getInteractionBoxTop(PlayerTrackerMinimapElement<?> element, PlayerTrackerMinimapElementRenderContext context, float partialTicks) {
      return -10;
   }

   public int getInteractionBoxBottom(PlayerTrackerMinimapElement<?> element, PlayerTrackerMinimapElementRenderContext context, float partialTicks) {
      return 10;
   }

   public int getRenderBoxLeft(PlayerTrackerMinimapElement<?> element, PlayerTrackerMinimapElementRenderContext context, float partialTicks) {
      return -20;
   }

   public int getRenderBoxRight(PlayerTrackerMinimapElement<?> element, PlayerTrackerMinimapElementRenderContext context, float partialTicks) {
      return 20;
   }

   public int getRenderBoxTop(PlayerTrackerMinimapElement<?> element, PlayerTrackerMinimapElementRenderContext context, float partialTicks) {
      return -20;
   }

   public int getRenderBoxBottom(PlayerTrackerMinimapElement<?> element, PlayerTrackerMinimapElementRenderContext context, float partialTicks) {
      return 20;
   }

   public int getLeftSideLength(PlayerTrackerMinimapElement<?> element, class_310 mc) {
      class_640 info = class_310.method_1551().method_1562().method_2871(element.getPlayerId());
      return info == null ? 9 : 9 + mc.field_1772.method_1727(info.method_2966().getName());
   }

   public String getMenuName(PlayerTrackerMinimapElement<?> element) {
      class_640 info = class_310.method_1551().method_1562().method_2871(element.getPlayerId());
      return info == null ? element.getPlayerId() + "" : info.method_2966().getName();
   }

   public String getFilterName(PlayerTrackerMinimapElement<?> element) {
      return this.getMenuName(element);
   }

   public int getMenuTextFillLeftPadding(PlayerTrackerMinimapElement<?> element) {
      return 0;
   }

   public int getRightClickTitleBackgroundColor(PlayerTrackerMinimapElement<?> element) {
      return -11184641;
   }

   @Override
   public boolean shouldScaleBoxWithOptionalScale() {
      return true;
   }

   public boolean isInteractable(MinimapElementRenderLocation location, PlayerTrackerMinimapElement<?> element) {
      return true;
   }
}
