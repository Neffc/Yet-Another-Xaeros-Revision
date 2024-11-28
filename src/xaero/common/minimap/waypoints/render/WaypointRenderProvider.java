package xaero.common.minimap.waypoints.render;

import xaero.common.minimap.waypoints.Waypoint;
import xaero.hud.minimap.element.render.MinimapElementRenderLocation;
import xaero.hud.minimap.waypoint.render.WaypointMapRenderContext;

@Deprecated
public class WaypointRenderProvider extends xaero.hud.minimap.waypoint.render.WaypointRenderProvider {
   public void begin(int location, WaypointMapRenderContext context) {
      this.begin(MinimapElementRenderLocation.fromIndex(location), context);
   }

   public Waypoint setupContextAndGetNext(int location, WaypointMapRenderContext context) {
      return this.setupContextAndGetNext(MinimapElementRenderLocation.fromIndex(location), context);
   }

   public boolean hasNext(int location, WaypointMapRenderContext context) {
      return this.hasNext(MinimapElementRenderLocation.fromIndex(location), context);
   }

   public Waypoint getNext(int location, WaypointMapRenderContext context) {
      return this.getNext(MinimapElementRenderLocation.fromIndex(location), context);
   }

   public void end(int location, WaypointMapRenderContext context) {
      this.end(MinimapElementRenderLocation.fromIndex(location), context);
   }
}
