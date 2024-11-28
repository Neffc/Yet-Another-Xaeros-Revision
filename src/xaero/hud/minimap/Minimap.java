package xaero.hud.minimap;

import java.io.IOException;
import java.util.ArrayList;
import net.minecraft.class_310;
import xaero.common.IXaeroMinimap;
import xaero.common.minimap.element.render.over.MinimapElementOverMapRendererHandler;
import xaero.common.minimap.info.BuiltInInfoDisplays;
import xaero.common.minimap.info.InfoDisplayIO;
import xaero.common.minimap.info.InfoDisplayManager;
import xaero.common.minimap.info.render.InfoDisplayRenderer;
import xaero.common.minimap.render.MinimapFBORenderer;
import xaero.common.minimap.render.MinimapSafeModeRenderer;
import xaero.common.minimap.waypoints.render.CompassRenderer;
import xaero.common.minimap.waypoints.render.WaypointDeleter;
import xaero.common.minimap.waypoints.render.WaypointsGuiRenderer;
import xaero.common.minimap.waypoints.render.WaypointsIngameRenderer;

public class Minimap {
   private IXaeroMinimap modMain;
   private class_310 mc = class_310.method_1551();
   private WaypointsGuiRenderer waypointsGuiRenderer;
   private WaypointsIngameRenderer waypointsIngameRenderer;
   private MinimapFBORenderer minimapFBORenderer;
   private CompassRenderer compassRenderer;
   private MinimapElementOverMapRendererHandler overMapRendererHandler;
   private final InfoDisplayManager infoDisplayManager;
   private final InfoDisplayIO infoDisplayIO;
   private final InfoDisplayRenderer infoDisplayRenderer;
   private Throwable crashedWith;
   private MinimapSafeModeRenderer minimapSafeModeRenderer;

   public Minimap(IXaeroMinimap modMain) throws IOException {
      this.modMain = modMain;
      WaypointDeleter waypointDeleter = new WaypointDeleter(modMain);
      this.waypointsGuiRenderer = WaypointsGuiRenderer.Builder.begin(modMain).setWaypointDeleter(waypointDeleter).build();
      this.compassRenderer = new CompassRenderer(modMain, this.mc);
      this.overMapRendererHandler = MinimapElementOverMapRendererHandler.Builder.begin().build();
      this.overMapRendererHandler.add(this.waypointsGuiRenderer);
      this.waypointsIngameRenderer = new WaypointsIngameRenderer(modMain, waypointDeleter, this.mc);
      this.minimapFBORenderer = new MinimapFBORenderer(modMain, this.mc, this.waypointsGuiRenderer, this, this.compassRenderer);
      this.minimapSafeModeRenderer = new MinimapSafeModeRenderer(modMain, this.mc, this.waypointsGuiRenderer, this, this.compassRenderer);
      this.infoDisplayManager = new InfoDisplayManager();
      BuiltInInfoDisplays.addToManager(this.infoDisplayManager);
      this.infoDisplayManager.setOrder(new ArrayList<>());
      this.infoDisplayIO = new InfoDisplayIO(this.infoDisplayManager);
      this.infoDisplayRenderer = new InfoDisplayRenderer();
   }

   public Throwable getCrashedWith() {
      return this.crashedWith;
   }

   public void setCrashedWith(Throwable crashedWith) {
      if (this.crashedWith == null) {
         this.crashedWith = crashedWith;
      }
   }

   public void checkCrashes() {
      if (this.crashedWith != null) {
         Throwable crash = this.crashedWith;
         this.crashedWith = null;
         throw new RuntimeException("Xaero's Minimap (" + this.modMain.getVersionID() + ") has crashed! Please report here: bit.ly/XaeroMMIssues", crash);
      }
   }

   public WaypointsGuiRenderer getWaypointsGuiRenderer() {
      return this.waypointsGuiRenderer;
   }

   public WaypointsIngameRenderer getWaypointsIngameRenderer() {
      return this.waypointsIngameRenderer;
   }

   public MinimapFBORenderer getMinimapFBORenderer() {
      return this.minimapFBORenderer;
   }

   public MinimapSafeModeRenderer getMinimapSafeModeRenderer() {
      return this.minimapSafeModeRenderer;
   }

   public MinimapElementOverMapRendererHandler getOverMapRendererHandler() {
      return this.overMapRendererHandler;
   }

   public boolean usingFBO() {
      return this.getMinimapFBORenderer().isLoadedFBO() && !this.modMain.getSettings().mapSafeMode;
   }

   public CompassRenderer getCompassRenderer() {
      return this.compassRenderer;
   }

   public InfoDisplayRenderer getInfoDisplayRenderer() {
      return this.infoDisplayRenderer;
   }

   public InfoDisplayManager getInfoDisplayManager() {
      return this.infoDisplayManager;
   }

   public InfoDisplayIO getInfoDisplayIO() {
      return this.infoDisplayIO;
   }
}
