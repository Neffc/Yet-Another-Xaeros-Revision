package xaero.common;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import net.minecraft.class_310;
import net.minecraft.class_634;
import net.minecraft.class_746;
import xaero.common.cache.BlockStateShortShapeCache;
import xaero.common.controls.ControlsHandler;
import xaero.common.controls.event.KeyEventHandler;
import xaero.common.core.IXaeroMinimapClientPlayNetHandler;
import xaero.common.core.XaeroMinimapCore;
import xaero.common.graphics.renderer.multitexture.MultiTextureRenderTypeRendererProvider;
import xaero.common.interfaces.Interface;
import xaero.common.interfaces.InterfaceInstance;
import xaero.common.minimap.MinimapProcessor;
import xaero.common.minimap.highlight.HighlighterRegistry;
import xaero.common.minimap.radar.MinimapRadar;
import xaero.common.minimap.radar.tracker.synced.ClientSyncedTrackedPlayerManager;
import xaero.common.minimap.waypoints.WaypointSharingHandler;
import xaero.common.minimap.waypoints.WaypointsManager;
import xaero.common.minimap.waypoints.render.WaypointsGuiRenderer;
import xaero.common.minimap.waypoints.render.WaypointsIngameRenderer;
import xaero.common.minimap.write.MinimapWriter;

public class XaeroMinimapSession {
   protected AXaeroMinimap modMain;
   protected WaypointsManager waypointsManager;
   protected WaypointSharingHandler waypointSharing;
   protected ControlsHandler controls;
   protected KeyEventHandler keyEventHandler;
   protected WaypointsGuiRenderer waypointsGuiRenderer;
   protected WaypointsIngameRenderer waypointsIngameRenderer;
   protected MinimapProcessor minimap;
   protected Map<Interface, InterfaceInstance> interfaceInstances;
   private MultiTextureRenderTypeRendererProvider multiTextureRenderTypeRenderers;
   private boolean usable;

   public XaeroMinimapSession(AXaeroMinimap modMain) {
      this.modMain = modMain;
   }

   public void init(class_634 connection) throws IOException {
      this.waypointsManager = new WaypointsManager(this.modMain, this);
      this.modMain.getSettings().loadWaypointsFromAllSources(this.waypointsManager, connection);
      this.waypointSharing = new WaypointSharingHandler(this.modMain, this);
      this.keyEventHandler = new KeyEventHandler();
      HighlighterRegistry highlighterRegistry = new HighlighterRegistry();
      if (this.modMain.getSupportMods().worldmap()) {
         this.modMain.getSupportMods().worldmapSupport.registerHighlighters(highlighterRegistry);
      }

      if (this.modMain.getSupportMods().pac()) {
         this.modMain.getSupportMods().xaeroPac.registerHighlighters(highlighterRegistry);
      }

      highlighterRegistry.end();
      MinimapWriter minimapWriter = new MinimapWriter(this.modMain, this, new BlockStateShortShapeCache(this.modMain), highlighterRegistry);
      MinimapRadar entityRadar = new MinimapRadar(this.modMain, this, this.modMain.getEntityRadarCategoryManager());
      ClientSyncedTrackedPlayerManager clientSyncedTrackedPlayerManager = new ClientSyncedTrackedPlayerManager();
      this.minimap = new MinimapProcessor(this.modMain, this, minimapWriter, entityRadar, clientSyncedTrackedPlayerManager);
      this.interfaceInstances = new HashMap<>();
      Iterator<Interface> interfaces = this.modMain.getInterfaces().getInterfaceIterator();

      while (interfaces.hasNext()) {
         Interface inter = interfaces.next();
         this.interfaceInstances.put(inter, inter.createInterfaceInstance(this));
      }

      this.multiTextureRenderTypeRenderers = new MultiTextureRenderTypeRendererProvider(2);
      this.usable = true;
      MinimapLogs.LOGGER.info("New minimap session initialized!");
   }

   public void cleanup() {
      try {
         this.minimap.cleanup();
         Iterator<Interface> interfaces = this.modMain.getInterfaces().getInterfaceIterator();

         while (interfaces.hasNext()) {
            Interface inter = interfaces.next();
            this.interfaceInstances.get(inter).cleanup();
         }

         MinimapLogs.LOGGER.info("Minimap session finalized.");
      } catch (Throwable var3) {
         MinimapLogs.LOGGER.error("Minimap session failed to finalize properly.", var3);
      }

      this.usable = false;
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

   public Map<Interface, InterfaceInstance> getInterfaceInstances() {
      return this.interfaceInstances;
   }

   public static XaeroMinimapSession getCurrentSession() {
      XaeroMinimapSession session = getForPlayer(class_310.method_1551().field_1724);
      if (session == null && XaeroMinimapCore.currentSession != null && XaeroMinimapCore.currentSession.usable) {
         session = XaeroMinimapCore.currentSession;
      }

      return session;
   }

   public static XaeroMinimapSession getForPlayer(class_746 player) {
      return player != null && player.field_3944 != null ? ((IXaeroMinimapClientPlayNetHandler)player.field_3944).getXaero_minimapSession() : null;
   }

   public MultiTextureRenderTypeRendererProvider getMultiTextureRenderTypeRenderers() {
      return this.multiTextureRenderTypeRenderers;
   }

   public AXaeroMinimap getModMain() {
      return this.modMain;
   }
}
