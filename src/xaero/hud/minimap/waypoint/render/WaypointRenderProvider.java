package xaero.hud.minimap.waypoint.render;

import java.util.Iterator;
import xaero.common.minimap.element.render.MinimapElementRenderProvider;
import xaero.common.minimap.waypoints.Waypoint;
import xaero.hud.minimap.element.render.MinimapElementRenderLocation;

public abstract class WaypointRenderProvider extends MinimapElementRenderProvider<Waypoint, WaypointGuiRenderContext> {
   private Iterator<Waypoint> iterator;

   public void begin(MinimapElementRenderLocation location, WaypointGuiRenderContext context) {
      this.iterator = context.sortingList.stream().filter(context.filter).sorted().iterator();
   }

   public boolean hasNext(MinimapElementRenderLocation location, WaypointGuiRenderContext context) {
      return this.iterator.hasNext();
   }

   public Waypoint setupContextAndGetNext(MinimapElementRenderLocation location, WaypointGuiRenderContext context) {
      return this.getNext(location, context);
   }

   public Waypoint getNext(MinimapElementRenderLocation location, WaypointGuiRenderContext context) {
      return this.iterator.next();
   }

   public void end(MinimapElementRenderLocation location, WaypointGuiRenderContext context) {
      this.iterator = null;
   }
}
