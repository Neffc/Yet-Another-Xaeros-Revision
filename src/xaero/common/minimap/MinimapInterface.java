package xaero.common.minimap;

import java.io.IOException;
import xaero.common.HudMod;
import xaero.common.minimap.element.render.over.MinimapElementOverMapRendererHandler;
import xaero.common.minimap.info.InfoDisplayManager;
import xaero.common.minimap.info.render.InfoDisplayRenderer;
import xaero.common.minimap.render.MinimapFBORenderer;
import xaero.common.minimap.render.MinimapSafeModeRenderer;
import xaero.common.minimap.waypoints.render.WaypointsGuiRenderer;
import xaero.common.minimap.waypoints.render.WaypointsIngameRenderer;
import xaero.hud.minimap.Minimap;
import xaero.hud.minimap.compass.render.CompassRenderer;

@Deprecated
public class MinimapInterface extends Minimap {
   public MinimapInterface(HudMod modMain) throws IOException {
      super(modMain);
   }

   @Deprecated
   @Override
   public WaypointsGuiRenderer getWaypointsGuiRenderer() {
      return super.getWaypointsGuiRenderer();
   }

   @Deprecated
   @Override
   public WaypointsIngameRenderer getWaypointsIngameRenderer() {
      return super.getWaypointsIngameRenderer();
   }

   @Deprecated
   @Override
   public MinimapFBORenderer getMinimapFBORenderer() {
      return super.getMinimapFBORenderer();
   }

   @Deprecated
   @Override
   public MinimapSafeModeRenderer getMinimapSafeModeRenderer() {
      return super.getMinimapSafeModeRenderer();
   }

   @Deprecated
   public MinimapElementOverMapRendererHandler getOverMapRendererHandler() {
      return (MinimapElementOverMapRendererHandler)super.getOverMapRendererHandler();
   }

   @Deprecated
   @Override
   public boolean usingFBO() {
      return super.usingFBO();
   }

   @Deprecated
   @Override
   public CompassRenderer getCompassRenderer() {
      return super.getCompassRenderer();
   }

   @Deprecated
   @Override
   public InfoDisplayRenderer getInfoDisplayRenderer() {
      return super.getInfoDisplayRenderer();
   }

   @Deprecated
   @Override
   public InfoDisplayManager getInfoDisplayManager() {
      return super.getInfoDisplayManager();
   }
}
