package xaero.hud.minimap.waypoint.render;

import xaero.common.minimap.waypoints.Waypoint;
import xaero.hud.minimap.element.render.MinimapElementRenderLocation;

@Deprecated
public class WaypointReader extends WaypointMapRenderReader {
   @Deprecated
   public boolean isInteractable(MinimapElementRenderLocation location, Waypoint element) {
      return false;
   }

   @Deprecated
   public float getBoxScale(MinimapElementRenderLocation location, Waypoint element, WaypointMapRenderContext context) {
      return 1.0F;
   }
}
