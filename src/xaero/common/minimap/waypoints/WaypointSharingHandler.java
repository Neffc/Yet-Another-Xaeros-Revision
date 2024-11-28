package xaero.common.minimap.waypoints;

import net.minecraft.class_437;
import xaero.common.HudMod;
import xaero.hud.minimap.module.MinimapSession;
import xaero.hud.minimap.world.MinimapWorld;

@Deprecated
public class WaypointSharingHandler extends xaero.hud.minimap.waypoint.WaypointSharingHandler {
   public static final String WAYPOINT_OLD_SHARE_PREFIX = "xaero_waypoint:";
   public static final String WAYPOINT_ADD_PREFIX = "xaero_waypoint_add:";
   public static final String WAYPOINT_SHARE_PREFIX = "xaero-waypoint:";

   public WaypointSharingHandler(HudMod modMain, MinimapSession session) {
      super(modMain, session);
   }

   @Deprecated
   public void shareWaypoint(class_437 parent, Waypoint w, WaypointWorld wWorld) {
      this.shareWaypoint(parent, w, (MinimapWorld)wWorld);
   }

   @Deprecated
   @Override
   public void shareWaypoint(class_437 currentScreen, Waypoint waypoint, MinimapWorld minimapWorld) {
      super.shareWaypoint(currentScreen, waypoint, minimapWorld);
   }

   @Deprecated
   @Override
   public void onWaypointReceived(String playerName, String text) {
      super.onWaypointReceived(playerName, text);
   }

   @Deprecated
   @Override
   public void onWaypointAdd(String[] args) {
      super.onWaypointAdd(args);
   }
}
