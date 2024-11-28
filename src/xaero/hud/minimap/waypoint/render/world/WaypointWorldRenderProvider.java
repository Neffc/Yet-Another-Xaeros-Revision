package xaero.hud.minimap.waypoint.render.world;

import xaero.common.minimap.waypoints.Waypoint;
import xaero.hud.minimap.element.render.MinimapElementRenderLocation;
import xaero.hud.minimap.waypoint.render.AbstractWaypointRenderProvider;

public final class WaypointWorldRenderProvider extends AbstractWaypointRenderProvider<WaypointWorldRenderContext> {
   @Deprecated
   public void begin(int location, WaypointWorldRenderContext context) {
      this.begin(MinimapElementRenderLocation.fromIndex(location), context);
   }

   @Deprecated
   public Waypoint setupContextAndGetNext(int location, WaypointWorldRenderContext context) {
      return this.setupContextAndGetNext(MinimapElementRenderLocation.fromIndex(location), context);
   }

   @Deprecated
   public boolean hasNext(int location, WaypointWorldRenderContext context) {
      return this.hasNext(MinimapElementRenderLocation.fromIndex(location), context);
   }

   @Deprecated
   public Waypoint getNext(int location, WaypointWorldRenderContext context) {
      return this.getNext(MinimapElementRenderLocation.fromIndex(location), context);
   }

   @Deprecated
   public void end(int location, WaypointWorldRenderContext context) {
      this.end(MinimapElementRenderLocation.fromIndex(location), context);
   }
}
