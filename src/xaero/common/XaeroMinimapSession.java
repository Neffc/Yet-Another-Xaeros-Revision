package xaero.common;

import java.io.IOException;
import net.minecraft.class_634;
import net.minecraft.class_746;
import xaero.common.cache.BlockStateShortShapeCache;
import xaero.common.controls.ControlsHandler;
import xaero.common.controls.event.KeyEventHandler;
import xaero.common.minimap.MinimapProcessor;
import xaero.common.minimap.highlight.HighlighterRegistry;
import xaero.common.minimap.radar.MinimapRadar;
import xaero.common.minimap.radar.tracker.synced.ClientSyncedTrackedPlayerManager;
import xaero.common.minimap.waypoints.WaypointSharingHandler;
import xaero.common.minimap.waypoints.WaypointsManager;
import xaero.common.minimap.write.MinimapWriter;
import xaero.hud.HudSession;

@Deprecated
public class XaeroMinimapSession extends HudSession {
   protected WaypointsManager waypointsManager;
   protected WaypointSharingHandler waypointSharing;
   protected MinimapProcessor minimap;

   public XaeroMinimapSession(HudMod modMain) {
      super(modMain);
   }

   @Override
   public void init(class_634 connection) throws IOException {
      this.waypointsManager = new WaypointsManager(this.modMain, this);
      this.modMain.getSettings().loadWaypointsFromAllSources(this.waypointsManager, connection);
      this.waypointSharing = new WaypointSharingHandler(this.modMain, this);
      HighlighterRegistry highlighterRegistry = new HighlighterRegistry();
      if (this.modMain.getSupportMods().worldmap()) {
         this.modMain.getSupportMods().worldmapSupport.registerHighlighters(highlighterRegistry);
      }

      if (this.modMain.getSupportMods().pac()) {
         this.modMain.getSupportMods().xaeroPac.registerHighlighters(highlighterRegistry);
      }

      highlighterRegistry.end();
      MinimapWriter minimapWriter = this.modMain
         .getPlatformContext()
         .createMinimapWriter(this.modMain, this, new BlockStateShortShapeCache(this.modMain), highlighterRegistry);
      MinimapRadar entityRadar = new MinimapRadar(this.modMain, this, this.modMain.getEntityRadarCategoryManager());
      ClientSyncedTrackedPlayerManager clientSyncedTrackedPlayerManager = new ClientSyncedTrackedPlayerManager();
      this.minimap = new MinimapProcessor(this.modMain, this, minimapWriter, entityRadar, clientSyncedTrackedPlayerManager);
      super.init(connection);
   }

   @Override
   protected void cleanup() {
      this.minimap.cleanup();
      super.cleanup();
   }

   public WaypointsManager getWaypointsManager() {
      return this.waypointsManager;
   }

   public WaypointSharingHandler getWaypointSharing() {
      return this.waypointSharing;
   }

   public ControlsHandler getControls() {
      return this.controls;
   }

   public KeyEventHandler getKeyEventHandler() {
      return this.keyEventHandler;
   }

   public MinimapProcessor getMinimapProcessor() {
      return this.minimap;
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
