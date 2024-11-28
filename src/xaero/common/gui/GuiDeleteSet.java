package xaero.common.gui;

import java.io.IOException;
import net.minecraft.class_1074;
import net.minecraft.class_2561;
import net.minecraft.class_310;
import net.minecraft.class_410;
import net.minecraft.class_437;
import xaero.common.IXaeroMinimap;
import xaero.common.XaeroMinimapSession;
import xaero.common.minimap.waypoints.WaypointsManager;
import xaero.hud.minimap.MinimapLogs;

public class GuiDeleteSet extends class_410 {
   public GuiDeleteSet(
      String setName,
      String container,
      String world,
      String name,
      class_437 parent,
      class_437 escapeScreen,
      IXaeroMinimap modMain,
      XaeroMinimapSession minimapSession
   ) {
      super(
         result -> confirmDeleteSet(result, container, world, name, parent, escapeScreen, modMain, minimapSession),
         class_2561.method_43470(class_1074.method_4662("gui.xaero_delete_set_message", new Object[0]) + ": " + setName.replace("§§", ":") + "?"),
         class_2561.method_43471("gui.xaero_delete_set_message2")
      );
   }

   private static void confirmDeleteSet(
      boolean p_confirmResult_1_,
      String container,
      String world,
      String name,
      class_437 parent,
      class_437 escapeScreen,
      IXaeroMinimap modMain,
      XaeroMinimapSession minimapSession
   ) {
      if (p_confirmResult_1_) {
         WaypointsManager waypointsManager = minimapSession.getWaypointsManager();
         waypointsManager.getWorld(container, world).getSets().remove(name);
         waypointsManager.getWorld(container, world).setCurrent("gui.xaero_default");
         waypointsManager.updateWaypoints();

         try {
            modMain.getSettings().saveWaypoints(waypointsManager.getWorld(container, world));
         } catch (IOException var10) {
            MinimapLogs.LOGGER.error("suppressed exception", var10);
         }

         class_310.method_1551().method_1507(new GuiWaypoints(modMain, minimapSession, ((GuiWaypoints)parent).parent, escapeScreen));
      } else {
         class_310.method_1551().method_1507(parent);
      }
   }
}
