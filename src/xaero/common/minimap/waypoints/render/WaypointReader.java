package xaero.common.minimap.waypoints.render;

import xaero.common.minimap.waypoints.Waypoint;
import xaero.hud.minimap.element.render.MinimapElementRenderLocation;

@Deprecated
public class WaypointReader extends xaero.hud.minimap.waypoint.render.WaypointReader {
   @Deprecated
   public boolean isInteractable(int location, Waypoint element) {
      return this.isInteractable(MinimapElementRenderLocation.fromIndex(location), element);
   }

   @Deprecated
   public float getBoxScale(int location, Waypoint element, xaero.hud.minimap.waypoint.render.WaypointGuiRenderContext context) {
      return this.getBoxScale(MinimapElementRenderLocation.fromIndex(location), element, context);
   }
}
