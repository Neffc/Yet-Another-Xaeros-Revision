package xaero.common.minimap.waypoints.render;

import java.util.Iterator;
import xaero.common.minimap.element.render.MinimapElementRenderProvider;
import xaero.common.minimap.waypoints.Waypoint;

public class WaypointRenderProvider extends MinimapElementRenderProvider<Waypoint, WaypointGuiRenderContext> {
   private Iterator<Waypoint> iterator;

   public void begin(int location, WaypointGuiRenderContext context) {
      this.iterator = context.sortingList.stream().filter(context.filter).sorted().iterator();
   }

   public boolean hasNext(int location, WaypointGuiRenderContext context) {
      return this.iterator.hasNext();
   }

   public Waypoint getNext(int location, WaypointGuiRenderContext context) {
      return this.iterator.next();
   }

   public void end(int location, WaypointGuiRenderContext context) {
      this.iterator = null;
   }
}
