package xaero.hud.minimap.waypoint.render;

@Deprecated
public abstract class WaypointsGuiRenderer extends WaypointMapRenderer {
   protected WaypointsGuiRenderer(WaypointReader elementReader, WaypointRenderProvider provider, WaypointMapRenderContext context) {
      super(elementReader, provider, context);
   }
}
