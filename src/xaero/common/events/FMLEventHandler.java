package xaero.common.events;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_1657;
import net.minecraft.class_310;
import xaero.common.AXaeroMinimap;
import xaero.common.XaeroMinimapSession;
import xaero.common.minimap.MinimapProcessor;
import xaero.common.minimap.waypoints.WaypointsManager;

public class FMLEventHandler {
   private AXaeroMinimap modMain;

   public FMLEventHandler(AXaeroMinimap modMain) {
      this.modMain = modMain;
   }

   public void handleClientTickStart() {
      XaeroMinimapSession minimapSession = XaeroMinimapSession.getCurrentSession();
      if (minimapSession != null) {
         MinimapProcessor minimap = minimapSession.getMinimapProcessor();
         minimap.onClientTick();
         if (class_310.method_1551().field_1755 == null) {
            minimapSession.getKeyEventHandler().onKeyInput(class_310.method_1551(), this.modMain, minimapSession);
         }
      }
   }

   @Environment(EnvType.CLIENT)
   public void handlePlayerTickStart(class_1657 player) {
      if (player == class_310.method_1551().field_1724) {
         XaeroMinimapSession minimapSession = XaeroMinimapSession.getCurrentSession();
         if (minimapSession != null) {
            try {
               MinimapProcessor minimap = minimapSession.getMinimapProcessor();
               WaypointsManager waypointsManager = minimapSession.getWaypointsManager();
               waypointsManager.updateWorldIds();
               minimap.onPlayerTick();
               waypointsManager.updateWaypoints();
               class_310 mc = class_310.method_1551();
               minimapSession.getKeyEventHandler().handleEvents(mc, minimapSession);
               this.playerTickPostOverridable(minimapSession);
            } catch (Throwable var6) {
               this.modMain.getInterfaces().getMinimapInterface().setCrashedWith(var6);
            }
         }
      }
   }

   protected void playerTickPostOverridable(XaeroMinimapSession minimapSession) {
   }

   public void handleRenderTickStart() {
      if (class_310.method_1551().field_1724 != null) {
         this.modMain.getInterfaces().getMinimapInterface().checkCrashes();
         XaeroMinimapSession minimapSession = XaeroMinimapSession.getCurrentSession();
         if (minimapSession != null) {
            MinimapProcessor minimap = minimapSession.getMinimapProcessor();
            minimap.getMinimapWriter().onRender();
         }
      }
   }
}
