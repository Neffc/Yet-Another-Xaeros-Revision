package xaero.common.minimap.waypoints.render;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import net.minecraft.class_4588;
import xaero.common.minimap.waypoints.Waypoint;
import xaero.common.settings.ModSettings;

public class WaypointGuiRenderContext {
   public ModSettings settings;
   public class_4588 waypointBackgroundConsumer;
   public final List<Waypoint> sortingList = new ArrayList<>();
   public Predicate<Waypoint> filter;
   public final WaypointFilterParams filterParams = new WaypointFilterParams();
   public double dimDiv;
   public boolean deleteReachedDeathpoints;
   public int scale;
}
