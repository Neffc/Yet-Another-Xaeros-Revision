package xaero.hud.minimap;

import java.io.IOException;
import net.minecraft.class_310;
import xaero.common.HudMod;
import xaero.common.minimap.info.InfoDisplayManager;
import xaero.common.minimap.info.render.InfoDisplayRenderer;
import xaero.common.minimap.render.MinimapFBORenderer;
import xaero.common.minimap.render.MinimapSafeModeRenderer;
import xaero.common.minimap.waypoints.render.WaypointsIngameRenderer;
import xaero.hud.minimap.compass.render.CompassRenderer;
import xaero.hud.minimap.element.render.over.MinimapElementOverMapRendererHandler;
import xaero.hud.minimap.info.InfoDisplays;
import xaero.hud.minimap.waypoint.render.WaypointDeleter;
import xaero.hud.minimap.waypoint.render.WaypointsGuiRenderer;

public class Minimap {
   private final HudMod modMain;
   private final class_310 mc = class_310.method_1551();
   private final WaypointsGuiRenderer waypointsGuiRenderer;
   private final WaypointsIngameRenderer waypointsIngameRenderer;
   private final MinimapFBORenderer minimapFBORenderer;
   private final CompassRenderer compassRenderer;
   private final MinimapElementOverMapRendererHandler overMapRendererHandler;
   private final InfoDisplays infoDisplays;
   private Throwable crashedWith;
   private MinimapSafeModeRenderer minimapSafeModeRenderer;

   public Minimap(HudMod modMain) throws IOException {
      this.modMain = modMain;
      WaypointDeleter waypointDeleter = new WaypointDeleter(modMain);
      this.waypointsGuiRenderer = WaypointsGuiRenderer.Builder.begin(modMain).setWaypointDeleter(waypointDeleter).build();
      this.compassRenderer = new CompassRenderer(modMain, this.mc);
      this.overMapRendererHandler = MinimapElementOverMapRendererHandler.Builder.begin().build();
      this.overMapRendererHandler.add(this.waypointsGuiRenderer);
      this.waypointsIngameRenderer = new WaypointsIngameRenderer(modMain, waypointDeleter, this.mc);
      this.minimapFBORenderer = new MinimapFBORenderer(modMain, this.mc, this.waypointsGuiRenderer, this, this.compassRenderer);
      this.minimapSafeModeRenderer = new MinimapSafeModeRenderer(modMain, this.mc, this.waypointsGuiRenderer, this, this.compassRenderer);
      this.infoDisplays = new InfoDisplays();
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

   @Deprecated
   public xaero.common.minimap.waypoints.render.WaypointsGuiRenderer getWaypointsGuiRenderer() {
      return (xaero.common.minimap.waypoints.render.WaypointsGuiRenderer)this.waypointsGuiRenderer;
   }

   public WaypointsGuiRenderer getWaypointGuiRenderer() {
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

   @Deprecated
   public InfoDisplayRenderer getInfoDisplayRenderer() {
      return (InfoDisplayRenderer)this.infoDisplays.getRenderer();
   }

   @Deprecated
   public InfoDisplayManager getInfoDisplayManager() {
      return (InfoDisplayManager)this.infoDisplays.getManager();
   }

   public InfoDisplays getInfoDisplays() {
      return this.infoDisplays;
   }

   public HudMod getModMain() {
      return this.modMain;
   }
}
