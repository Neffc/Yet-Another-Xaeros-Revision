package xaero.common;

import java.io.IOException;
import net.minecraft.class_634;
import net.minecraft.class_746;
import xaero.common.minimap.MinimapProcessor;
import xaero.common.minimap.waypoints.WaypointSharingHandler;
import xaero.common.minimap.waypoints.WaypointsManager;
import xaero.hud.HudSession;
import xaero.hud.minimap.BuiltInHudModules;

@Deprecated
public class XaeroMinimapSession extends HudSession {
   public XaeroMinimapSession(HudMod modMain) {
      super(modMain);
   }

   @Override
   public void init(class_634 connection) throws IOException {
      super.init(connection);
   }

   @Override
   protected void cleanup() {
      super.cleanup();
   }

   public WaypointsManager getWaypointsManager() {
      return (WaypointsManager)BuiltInHudModules.MINIMAP.getCurrentSession();
   }

   public WaypointSharingHandler getWaypointSharing() {
      return (WaypointSharingHandler)BuiltInHudModules.MINIMAP.getCurrentSession().getWaypointSession().getSharing();
   }

   public MinimapProcessor getMinimapProcessor() {
      return BuiltInHudModules.MINIMAP.getCurrentSession().getProcessor();
   }

   public static XaeroMinimapSession getCurrentSession() {
      return (XaeroMinimapSession)HudSession.getCurrentSession();
   }

   public static XaeroMinimapSession getForPlayer(class_746 player) {
      return (XaeroMinimapSession)HudSession.getForPlayer(player);
   }

   public IXaeroMinimap getModMain() {
      return this.getHudMod();
   }
}
