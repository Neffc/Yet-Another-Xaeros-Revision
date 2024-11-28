package xaero.common.gui;

import java.io.IOException;
import net.minecraft.class_1074;
import net.minecraft.class_2561;
import net.minecraft.class_310;
import net.minecraft.class_410;
import net.minecraft.class_437;
import xaero.common.HudMod;
import xaero.common.IXaeroMinimap;
import xaero.hud.minimap.MinimapLogs;
import xaero.hud.minimap.module.MinimapSession;
import xaero.hud.minimap.waypoint.set.WaypointSet;
import xaero.hud.path.XaeroPath;

public class GuiClearSet extends class_410 {
   public GuiClearSet(
      String setName, XaeroPath worldPath, String name, GuiWaypoints parent, class_437 escapeScreen, IXaeroMinimap modMain, MinimapSession session
   ) {
      super(
         result -> confirmClearSet(result, worldPath, name, parent, escapeScreen, modMain, session),
         class_2561.method_43470(class_1074.method_4662("gui.xaero_clear_set_message", new Object[0]) + ": " + setName.replace("§§", ":") + "?"),
         class_2561.method_43471("gui.xaero_clear_set_message2")
      );
   }

   private static void confirmClearSet(
      boolean p_confirmResult_1_, XaeroPath worldPath, String name, GuiWaypoints parent, class_437 escapeScreen, IXaeroMinimap modMain, MinimapSession session
   ) {
      if (p_confirmResult_1_) {
         WaypointSet set = session.getWorldManager().getWorld(worldPath).getWaypointSet(name);
         if (set != null) {
            set.clear();
         }

         try {
            session.getWorldManagerIO().saveWorld(session.getWorldManager().getWorld(worldPath));
         } catch (IOException var9) {
            MinimapLogs.LOGGER.error("suppressed exception", var9);
         }
      }

      class_310.method_1551().method_1507(new GuiWaypoints((HudMod)modMain, session, parent.parent, escapeScreen));
   }
}
