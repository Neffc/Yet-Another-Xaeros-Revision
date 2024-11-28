package xaero.hud.minimap.waypoint.render;

import net.minecraft.class_310;
import xaero.common.minimap.element.render.MinimapElementReader;
import xaero.common.minimap.waypoints.Waypoint;
import xaero.hud.minimap.element.render.MinimapElementRenderLocation;
import xaero.hud.minimap.waypoint.WaypointPurpose;

public class WaypointReader extends MinimapElementReader<Waypoint, WaypointGuiRenderContext> {
   public double getRenderX(Waypoint element, WaypointGuiRenderContext context, float partialTicks) {
      return (double)element.getX(context.dimDiv) + 0.5;
   }

   public double getRenderY(Waypoint element, WaypointGuiRenderContext context, float partialTicks) {
      return (double)(element.getY() + 1);
   }

   public double getRenderZ(Waypoint element, WaypointGuiRenderContext context, float partialTicks) {
      return (double)element.getZ(context.dimDiv) + 0.5;
   }

   public boolean isHidden(Waypoint element, WaypointGuiRenderContext context) {
      return false;
   }

   public int getInteractionBoxLeft(Waypoint element, WaypointGuiRenderContext context, float partialTicks) {
      return 0;
   }

   public int getInteractionBoxRight(Waypoint element, WaypointGuiRenderContext context, float partialTicks) {
      return 0;
   }

   public int getInteractionBoxTop(Waypoint element, WaypointGuiRenderContext context, float partialTicks) {
      return 0;
   }

   public int getInteractionBoxBottom(Waypoint element, WaypointGuiRenderContext context, float partialTicks) {
      return 0;
   }

   public int getRenderBoxLeft(Waypoint element, WaypointGuiRenderContext context, float partialTicks) {
      return -this.getRenderBoxRight(element, context, partialTicks);
   }

   public int getRenderBoxRight(Waypoint element, WaypointGuiRenderContext context, float partialTicks) {
      int j = element.getPurpose() == WaypointPurpose.DEATH ? 4 : class_310.method_1551().field_1772.method_1727(element.getInitials()) / 2;
      int addedFrame = j > 4 ? j - 4 : 0;
      return 5 + addedFrame;
   }

   public int getRenderBoxTop(Waypoint element, WaypointGuiRenderContext context, float partialTicks) {
      return this.getRenderBoxLeft(element, context, partialTicks);
   }

   public int getRenderBoxBottom(Waypoint element, WaypointGuiRenderContext context, float partialTicks) {
      return this.getRenderBoxRight(element, context, partialTicks);
   }

   public int getLeftSideLength(Waypoint element, class_310 mc) {
      return 0;
   }

   public String getMenuName(Waypoint element) {
      return "n/a";
   }

   public String getFilterName(Waypoint element) {
      return this.getMenuName(element);
   }

   public int getMenuTextFillLeftPadding(Waypoint element) {
      return 0;
   }

   public int getRightClickTitleBackgroundColor(Waypoint element) {
      return 0;
   }

   @Override
   public boolean shouldScaleBoxWithOptionalScale() {
      return false;
   }

   public boolean isInteractable(MinimapElementRenderLocation location, Waypoint element) {
      return false;
   }

   public float getBoxScale(MinimapElementRenderLocation location, Waypoint element, WaypointGuiRenderContext context) {
      return 1.0F;
   }
}
