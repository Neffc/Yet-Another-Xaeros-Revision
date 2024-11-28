package xaero.hud.minimap.waypoint.render;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import net.minecraft.class_4588;
import xaero.common.minimap.render.MinimapRendererHelper;
import xaero.common.minimap.waypoints.Waypoint;
import xaero.common.settings.ModSettings;

public class WaypointGuiRenderContext {
   public final List<Waypoint> sortingList = new ArrayList<>();
   public final WaypointFilterParams filterParams = new xaero.common.minimap.waypoints.render.WaypointFilterParams();
   public ModSettings settings;
   public class_4588 waypointBackgroundConsumer;
   public Predicate<Waypoint> filter;
   public double dimDiv;
   public boolean deleteReachedDeathpoints;
   public int scale;
   public MinimapRendererHelper helper;
}
