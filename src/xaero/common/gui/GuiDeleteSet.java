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
import xaero.hud.minimap.world.MinimapWorldManager;
import xaero.hud.path.XaeroPath;

public class GuiDeleteSet extends class_410 {
   public GuiDeleteSet(
      String setName, XaeroPath worldPath, String name, class_437 parent, class_437 escapeScreen, IXaeroMinimap modMain, MinimapSession session
   ) {
      super(
         result -> confirmDeleteSet(result, worldPath, name, parent, escapeScreen, modMain, session),
         class_2561.method_43470(class_1074.method_4662("gui.xaero_delete_set_message", new Object[0]) + ": " + setName.replace("§§", ":") + "?"),
         class_2561.method_43471("gui.xaero_delete_set_message2")
      );
   }

   private static void confirmDeleteSet(
      boolean p_confirmResult_1_, XaeroPath worldPath, String name, class_437 parent, class_437 escapeScreen, IXaeroMinimap modMain, MinimapSession session
   ) {
      if (p_confirmResult_1_) {
         MinimapWorldManager waypointsManager = session.getWorldManager();
         waypointsManager.getWorld(worldPath).removeWaypointSet(name);
         waypointsManager.getWorld(worldPath).setCurrentWaypointSetId("gui.xaero_default");

         try {
            session.getWorldManagerIO().saveWorld(waypointsManager.getWorld(worldPath));
         } catch (IOException var9) {
            MinimapLogs.LOGGER.error("suppressed exception", var9);
         }

         class_310.method_1551().method_1507(new GuiWaypoints((HudMod)modMain, session, ((GuiWaypoints)parent).parent, escapeScreen));
      } else {
         class_310.method_1551().method_1507(parent);
      }
   }
}
