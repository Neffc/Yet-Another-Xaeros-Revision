package xaero.hud.minimap.module;

import java.io.IOException;
import net.minecraft.class_310;
import net.minecraft.class_634;
import xaero.common.HudMod;
import xaero.common.cache.BlockStateShortShapeCache;
import xaero.common.graphics.renderer.multitexture.MultiTextureRenderTypeRendererProvider;
import xaero.common.minimap.MinimapProcessor;
import xaero.common.minimap.highlight.HighlighterRegistry;
import xaero.common.minimap.radar.MinimapRadar;
import xaero.common.minimap.radar.tracker.synced.ClientSyncedTrackedPlayerManager;
import xaero.common.minimap.write.MinimapWriter;
import xaero.hud.minimap.Minimap;
import xaero.hud.minimap.waypoint.WaypointSession;
import xaero.hud.minimap.world.MinimapDimensionHelper;
import xaero.hud.minimap.world.MinimapWorldManager;
import xaero.hud.minimap.world.io.MinimapWorldManagerIO;
import xaero.hud.minimap.world.state.MinimapWorldState;
import xaero.hud.minimap.world.state.MinimapWorldStateUpdater;
import xaero.hud.module.HudModule;
import xaero.hud.module.ModuleSession;

public abstract class MinimapSession extends ModuleSession<MinimapSession> {
   private Minimap minimap;
   private final class_310 mc = class_310.method_1551();
   protected final MinimapProcessor processor;
   protected final MinimapWorldManager worldManager;
   private final MinimapWorldManagerIO worldManagerIO;
   private final MinimapWorldState worldState;
   private final MinimapWorldStateUpdater worldStateUpdater;
   private final MinimapDimensionHelper dimensionHelper;
   private final WaypointSession waypointSession;
   private final MultiTextureRenderTypeRendererProvider multiTextureRenderTypeRenderers;

   public MinimapSession(HudMod modMain, HudModule<MinimapSession> module, class_634 connection) {
      super(modMain, module);
      this.worldManager = new MinimapWorldManager(modMain, this);
      this.worldState = new MinimapWorldState();
      this.worldStateUpdater = new MinimapWorldStateUpdater(modMain, this, connection);
      this.dimensionHelper = new MinimapDimensionHelper();
      this.worldManagerIO = new MinimapWorldManagerIO(modMain);
      this.worldStateUpdater.init();

      try {
         this.worldManagerIO.loadWorldsFromAllSources(this, connection);
      } catch (IOException var8) {
         throw new RuntimeException(var8);
      }

      this.waypointSession = new WaypointSession(modMain, this);
      this.multiTextureRenderTypeRenderers = new MultiTextureRenderTypeRendererProvider(2);
      HighlighterRegistry highlighterRegistry = new HighlighterRegistry();
      if (modMain.getSupportMods().worldmap()) {
         modMain.getSupportMods().worldmapSupport.registerHighlighters(highlighterRegistry);
      }

      if (modMain.getSupportMods().pac()) {
         modMain.getSupportMods().xaeroPac.registerHighlighters(highlighterRegistry);
      }

      highlighterRegistry.end();
      MinimapWriter minimapWriter = modMain.getPlatformContext()
         .createMinimapWriter(this.modMain, this, new BlockStateShortShapeCache(modMain), highlighterRegistry);
      MinimapRadar entityRadar = new MinimapRadar(this.modMain, this, modMain.getEntityRadarCategoryManager());
      ClientSyncedTrackedPlayerManager clientSyncedTrackedPlayerManager = new ClientSyncedTrackedPlayerManager();
      this.processor = new MinimapProcessor(modMain, this, minimapWriter, entityRadar, clientSyncedTrackedPlayerManager);
      this.minimap = modMain.getMinimap();
   }

   @Override
   public void prePotentialRender() {
      try {
         super.prePotentialRender();
         this.getProcessor().checkFBO();
         this.modMain.getTrackedPlayerRenderer().getCollector().update(class_310.method_1551());
      } catch (Throwable var2) {
         this.minimap.setCrashedWith(var2);
         this.minimap.checkCrashes();
      }
   }

   @Override
   public void close() {
      this.processor.cleanup();
   }

   @Override
   public boolean isActive() {
      return this.modMain.getSettings().getMinimap();
   }

   @Override
   public int getWidth(double screenScale) {
      return (int)((double)((float)this.getConfiguredWidth() * this.modMain.getSettings().getMinimapScale()) / screenScale);
   }

   @Override
   public int getHeight(double screenScale) {
      return this.getWidth(screenScale);
   }

   public int getConfiguredWidth() {
      return this.getProcessor().getMinimapSize() / 2 + 18;
   }

   public MinimapProcessor getProcessor() {
      return this.processor;
   }

   public boolean getHideMinimapUnderScreen() {
      return this.modMain.getSettings().hideMinimapUnderScreen;
   }

   public boolean getHideMinimapUnderF3() {
      return this.modMain.getSettings().hideMinimapUnderF3;
   }

   public MultiTextureRenderTypeRendererProvider getMultiTextureRenderTypeRenderers() {
      return this.multiTextureRenderTypeRenderers;
   }

   public WaypointSession getWaypointSession() {
      return this.waypointSession;
   }

   public class_310 getMc() {
      return this.mc;
   }

   public MinimapWorldManager getWorldManager() {
      return this.worldManager;
   }

   public MinimapWorldState getWorldState() {
      return this.worldState;
   }

   public MinimapWorldStateUpdater getWorldStateUpdater() {
      return this.worldStateUpdater;
   }

   public MinimapDimensionHelper getDimensionHelper() {
      return this.dimensionHelper;
   }

   public MinimapWorldManagerIO getWorldManagerIO() {
      return this.worldManagerIO;
   }
}
