package xaero.common.gui;

import java.io.IOException;
import net.minecraft.class_1074;
import net.minecraft.class_2561;
import net.minecraft.class_310;
import net.minecraft.class_410;
import net.minecraft.class_437;
import xaero.common.AXaeroMinimap;
import xaero.common.MinimapLogs;
import xaero.common.XaeroMinimapSession;
import xaero.common.minimap.waypoints.WaypointSet;

public class GuiClearSet extends class_410 {
   public GuiClearSet(
      String setName,
      String container,
      String world,
      String name,
      GuiWaypoints parent,
      class_437 escapeScreen,
      AXaeroMinimap modMain,
      XaeroMinimapSession minimapSession
   ) {
      super(
         result -> confirmClearSet(result, container, world, name, parent, escapeScreen, modMain, minimapSession),
         class_2561.method_43470(class_1074.method_4662("gui.xaero_clear_set_message", new Object[0]) + ": " + setName.replace("§§", ":") + "?"),
         class_2561.method_43471("gui.xaero_clear_set_message2")
      );
   }

   private static void confirmClearSet(
      boolean p_confirmResult_1_,
      String container,
      String world,
      String name,
      GuiWaypoints parent,
      class_437 escapeScreen,
      AXaeroMinimap modMain,
      XaeroMinimapSession minimapSession
   ) {
      if (p_confirmResult_1_) {
         WaypointSet set = minimapSession.getWaypointsManager().getWorld(container, world).getSets().get(name);
         if (set != null) {
            set.getList().clear();
         }

         try {
            modMain.getSettings().saveWaypoints(minimapSession.getWaypointsManager().getWorld(container, world));
         } catch (IOException var10) {
            MinimapLogs.LOGGER.error("suppressed exception", var10);
         }
      }

      class_310.method_1551().method_1507(new GuiWaypoints(modMain, minimapSession, parent.parent, escapeScreen));
   }
}
