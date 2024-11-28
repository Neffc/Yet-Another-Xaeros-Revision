package xaero.common.minimap.waypoints.render;

import xaero.common.minimap.waypoints.Waypoint;
import xaero.hud.minimap.element.render.MinimapElementRenderLocation;

@Deprecated
public class WaypointRenderProvider extends xaero.hud.minimap.waypoint.render.WaypointRenderProvider {
   public void begin(int location, xaero.hud.minimap.waypoint.render.WaypointGuiRenderContext context) {
      this.begin(MinimapElementRenderLocation.fromIndex(location), context);
   }

   public Waypoint setupContextAndGetNext(int location, xaero.hud.minimap.waypoint.render.WaypointGuiRenderContext context) {
      return this.setupContextAndGetNext(MinimapElementRenderLocation.fromIndex(location), context);
   }

   public boolean hasNext(int location, xaero.hud.minimap.waypoint.render.WaypointGuiRenderContext context) {
      return this.hasNext(MinimapElementRenderLocation.fromIndex(location), context);
   }

   public Waypoint getNext(int location, xaero.hud.minimap.waypoint.render.WaypointGuiRenderContext context) {
      return this.getNext(MinimapElementRenderLocation.fromIndex(location), context);
   }

   public void end(int location, xaero.hud.minimap.waypoint.render.WaypointGuiRenderContext context) {
      this.end(MinimapElementRenderLocation.fromIndex(location), context);
   }
}
