package xaero.common.minimap.waypoints;

import net.minecraft.class_2561;

public enum WaypointVisibilityType {
   LOCAL(class_2561.method_43471("gui.xaero_waypoints_local")),
   GLOBAL(class_2561.method_43471("gui.xaero_waypoints_global")),
   WORLD_MAP_LOCAL(class_2561.method_43471("gui.xaero_waypoint_visibility_type_world_map_local")),
   WORLD_MAP_GLOBAL(class_2561.method_43471("gui.xaero_waypoint_visibility_type_world_map_global"));

   private final class_2561 translation;

   private WaypointVisibilityType(class_2561 translation) {
      this.translation = translation;
   }

   public class_2561 getTranslation() {
      return this.translation;
   }
}
