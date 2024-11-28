package xaero.common.mods;

import com.mojang.blaze3d.systems.RenderSystem;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.IntConsumer;
import net.minecraft.class_1657;
import net.minecraft.class_1937;
import net.minecraft.class_310;
import net.minecraft.class_437;
import net.minecraft.class_4587;
import net.minecraft.class_4588;
import net.minecraft.class_5321;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL14;
import xaero.common.AXaeroMinimap;
import xaero.common.XaeroMinimapSession;
import xaero.common.effect.Effects;
import xaero.common.graphics.renderer.multitexture.MultiTextureRenderTypeRenderer;
import xaero.common.graphics.renderer.multitexture.MultiTextureRenderTypeRendererProvider;
import xaero.common.gui.ScreenBase;
import xaero.common.minimap.highlight.HighlighterRegistry;
import xaero.common.minimap.region.MinimapTile;
import xaero.common.minimap.render.MinimapRendererHelper;
import xaero.common.minimap.render.radar.element.RadarRenderer;
import xaero.common.minimap.waypoints.WaypointWorld;
import xaero.map.MapProcessor;
import xaero.map.WorldMap;
import xaero.map.WorldMapSession;
import xaero.map.graphics.CustomRenderTypes;
import xaero.map.graphics.shader.MapShaders;
import xaero.map.gui.GuiMap;
import xaero.map.gui.GuiWorldMapSettings;
import xaero.map.misc.Misc;
import xaero.map.region.LeveledRegion;
import xaero.map.region.MapRegion;
import xaero.map.region.MapTileChunk;
import xaero.map.region.texture.LeafRegionTexture;
import xaero.map.settings.ModOptions;
import xaero.map.world.MapDimension;

public class SupportXaeroWorldmap {
   public static int WORLDMAP_COMPATIBILITY_VERSION = 19;
   public int compatibilityVersion;
   private static final HashMap<MapTileChunk, Long> seedsUsed = new HashMap<>();
   public static final int black = -16777216;
   public static final int slime = -2142047936;
   private AXaeroMinimap modMain;
   private int destinationCaving = Integer.MAX_VALUE;
   private long lastDestinationCavingSwitch;
   private int previousRenderedCaveLayer = Integer.MAX_VALUE;
   private int lastRenderedCaveLayer = Integer.MAX_VALUE;
   private ArrayList<MapRegion> regionBuffer = new ArrayList<>();

   public SupportXaeroWorldmap(AXaeroMinimap modMain) {
      this.modMain = modMain;

      try {
         this.compatibilityVersion = WorldMap.MINIMAP_COMPATIBILITY_VERSION;
      } catch (NoSuchFieldError var3) {
      }

      if (this.compatibilityVersion < 3) {
         throw new RuntimeException("Xaero's World Map 1.11.0 or newer required!");
      }
   }

   public void drawMinimap(
      XaeroMinimapSession minimapSession,
      class_4587 matrixStack,
      MinimapRendererHelper helper,
      int xFloored,
      int zFloored,
      int minViewX,
      int minViewZ,
      int maxViewX,
      int maxViewZ,
      boolean zooming,
      double zoom,
      class_4588 overlayBufferBuilder,
      MultiTextureRenderTypeRendererProvider multiTextureRenderTypeRenderers
   ) {
      WorldMapSession worldmapSession = WorldMapSession.getCurrentSession();
      if (worldmapSession != null) {
         MapProcessor mapProcessor = worldmapSession.getMapProcessor();
         synchronized (mapProcessor.renderThreadPauseSync) {
            if (!mapProcessor.isRenderingPaused()) {
               if (mapProcessor.getCurrentDimension() == null) {
                  return;
               }

               int compatibilityVersion = this.compatibilityVersion;
               String worldString = mapProcessor.getCurrentWorldId();
               if (worldString == null) {
                  return;
               }

               MapShaders.ensureShaders();
               int mapX = xFloored >> 4;
               int mapZ = zFloored >> 4;
               int chunkX = mapX >> 2;
               int chunkZ = mapZ >> 2;
               int tileX = mapX & 3;
               int tileZ = mapZ & 3;
               int insideX = xFloored & 15;
               int insideZ = zFloored & 15;
               RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
               RenderSystem.enableBlend();
               int minX = (mapX >> 2) - 4;
               int maxX = (mapX >> 2) + 4;
               int minZ = (mapZ >> 2) - 4;
               int maxZ = (mapZ >> 2) + 4;
               int globalRegionCacheHashCode = WorldMap.settings.getRegionCacheHashCode();
               boolean reloadEverything = WorldMap.settings.reloadEverything;
               int globalReloadVersion = WorldMap.settings.reloadVersion;
               boolean slimeChunks = this.modMain.getSettings().getSlimeChunks(minimapSession.getWaypointsManager());
               mapProcessor.updateCaveStart();
               int renderedCaveLayer = mapProcessor.getCurrentCaveLayer();
               int globalCaveStart = mapProcessor.getMapWorld().getCurrentDimension().getLayeredMapRegions().getLayer(renderedCaveLayer).getCaveStart();
               int globalCaveDepth = WorldMap.settings.caveModeDepth;
               float brightness = this.getMinimapBrightness();
               if (renderedCaveLayer != this.lastRenderedCaveLayer) {
                  this.previousRenderedCaveLayer = this.lastRenderedCaveLayer;
               }

               class_1657 player = class_310.method_1551().field_1724;
               boolean noCaveMaps = player.method_6059(Effects.NO_CAVE_MAPS) || player.method_6059(Effects.NO_CAVE_MAPS_HARMFUL);
               boolean playerIsMoving = player.field_6014 != player.method_23317()
                  || player.field_6036 != player.method_23318()
                  || player.field_5969 != player.method_23321();
               boolean shouldRequestLoading = true;
               Object nextToLoadObj = null;
               shouldRequestLoading = false;
               LeveledRegion<?> nextToLoad = mapProcessor.getMapSaveLoad().getNextToLoadByViewing();
               nextToLoadObj = nextToLoad;
               if (nextToLoad != null) {
                  synchronized (nextToLoad) {
                     if (!nextToLoad.reloadHasBeenRequested()
                        && !nextToLoad.hasRemovableSourceData()
                        && (!(nextToLoad instanceof MapRegion) || !((MapRegion)nextToLoad).isRefreshing())) {
                        shouldRequestLoading = true;
                     }
                  }
               } else {
                  shouldRequestLoading = true;
               }

               this.regionBuffer.clear();
               int comparisonChunkX = (player.method_24515().method_10263() >> 4) - 16;
               int comparisonChunkZ = (player.method_24515().method_10260() >> 4) - 16;
               LeveledRegion.setComparison(comparisonChunkX, comparisonChunkZ, 0, comparisonChunkX, comparisonChunkZ);
               MultiTextureRenderTypeRenderer mapWithLightRenderer = null;
               MultiTextureRenderTypeRenderer mapNoLightRenderer = null;
               Runnable finalizer = null;
               IntConsumer binder;
               IntConsumer shaderBinder;
               if (zooming) {
                  binder = t -> {
                     MultiTextureRenderTypeRendererProvider.defaultTextureBind(t);
                     GL11.glTexParameteri(3553, 10240, 9729);
                  };
                  shaderBinder = t -> {
                     RenderSystem.setShaderTexture(0, t);
                     MultiTextureRenderTypeRendererProvider.defaultTextureBind(t);
                     GL11.glTexParameteri(3553, 10240, 9729);
                  };
                  finalizer = () -> GL11.glTexParameteri(3553, 10240, 9728);
               } else {
                  binder = MultiTextureRenderTypeRendererProvider::defaultTextureBind;
                  shaderBinder = t -> RenderSystem.setShaderTexture(0, t);
               }

               mapWithLightRenderer = multiTextureRenderTypeRenderers.getRenderer(shaderBinder, binder, finalizer, CustomRenderTypes.MAP);
               mapNoLightRenderer = multiTextureRenderTypeRenderers.getRenderer(shaderBinder, binder, finalizer, CustomRenderTypes.MAP);
               WaypointWorld world = minimapSession.getWaypointsManager().getAutoWorld();
               Long seed = slimeChunks && world != null ? this.modMain.getSettings().getSlimeChunksSeed(world.getFullId()) : null;
               this.renderChunks(
                  matrixStack,
                  minX,
                  maxX,
                  minZ,
                  maxZ,
                  minViewX,
                  maxViewX,
                  minViewZ,
                  maxViewZ,
                  mapProcessor,
                  renderedCaveLayer,
                  shouldRequestLoading,
                  reloadEverything,
                  globalReloadVersion,
                  globalRegionCacheHashCode,
                  globalCaveStart,
                  globalCaveDepth,
                  playerIsMoving,
                  noCaveMaps,
                  slimeChunks,
                  chunkX,
                  chunkZ,
                  tileX,
                  tileZ,
                  insideX,
                  insideZ,
                  seed,
                  mapWithLightRenderer,
                  mapNoLightRenderer,
                  helper,
                  overlayBufferBuilder
               );
               MapShaders.WORLD_MAP.setBrightness(brightness);
               MapShaders.WORLD_MAP.setWithLight(true);
               multiTextureRenderTypeRenderers.draw(mapWithLightRenderer);
               MapShaders.WORLD_MAP.setWithLight(false);
               multiTextureRenderTypeRenderers.draw(mapNoLightRenderer);
               GL14.glBlendFuncSeparate(770, 771, 1, 0);
               RenderSystem.disableBlend();
               this.lastRenderedCaveLayer = renderedCaveLayer;
               int toRequest = 1;
               int counter = 0;

               for (int i = 0; i < this.regionBuffer.size() && counter < toRequest; i++) {
                  MapRegion region = this.regionBuffer.get(i);
                  if (region != nextToLoadObj || this.regionBuffer.size() <= 1) {
                     synchronized (region) {
                        if (!region.reloadHasBeenRequested()
                           && !region.recacheHasBeenRequested()
                           && (!(region instanceof MapRegion) || !region.isRefreshing())
                           && (region.getLoadState() == 0 || region.getLoadState() == 4 || region.getLoadState() == 2 && region.isBeingWritten())) {
                           if (region.getLoadState() == 2) {
                              region.requestRefresh(mapProcessor);
                           } else {
                              mapProcessor.getMapSaveLoad().requestLoad(region, "Minimap sorted", false);
                           }

                           if (counter == 0) {
                              mapProcessor.getMapSaveLoad().setNextToLoadByViewing(region);
                           }

                           counter++;
                           if (region.getLoadState() == 4) {
                              break;
                           }
                        }
                     }
                  }
               }
            }
         }
      }
   }

   private void renderChunks(
      class_4587 matrixStack,
      int minX,
      int maxX,
      int minZ,
      int maxZ,
      int minViewX,
      int maxViewX,
      int minViewZ,
      int maxViewZ,
      MapProcessor mapProcessor,
      int renderedCaveLayer,
      boolean shouldRequestLoading,
      boolean reloadEverything,
      int globalReloadVersion,
      int globalRegionCacheHashCode,
      int globalCaveStart,
      int globalCaveDepth,
      boolean playerIsMoving,
      boolean noCaveMaps,
      boolean slimeChunks,
      int chunkX,
      int chunkZ,
      int tileX,
      int tileZ,
      int insideX,
      int insideZ,
      Long seed,
      MultiTextureRenderTypeRenderer mapWithLightRenderer,
      MultiTextureRenderTypeRenderer mapNoLightRenderer,
      MinimapRendererHelper helper,
      class_4588 overlayBufferBuilder
   ) {
      MapRegion prevRegion = null;
      Matrix4f matrix = matrixStack.method_23760().method_23761();

      for (int i = minX; i <= maxX; i++) {
         for (int j = minZ; j <= maxZ; j++) {
            MapRegion region = mapProcessor.getMapRegion(renderedCaveLayer, i >> 3, j >> 3, mapProcessor.regionExists(renderedCaveLayer, i >> 3, j >> 3));
            if (region != null && region != prevRegion) {
               synchronized (region) {
                  int regionHashCode = region.getCacheHashCode();
                  int regionReloadVersion = region.getReloadVersion();
                  if (shouldRequestLoading
                     && !region.recacheHasBeenRequested()
                     && !region.reloadHasBeenRequested()
                     && (!(region instanceof MapRegion) || !region.isRefreshing())
                     && (
                        region.getLoadState() == 0
                           || (region.getLoadState() == 4 || region.getLoadState() == 2 && region.isBeingWritten())
                              && (
                                 reloadEverything && regionReloadVersion != globalReloadVersion
                                    || regionHashCode != globalRegionCacheHashCode
                                    || !playerIsMoving && region.caveStartOutdated(globalCaveStart, globalCaveDepth)
                                    || region.getVersion() != mapProcessor.getGlobalVersion()
                                    || (region.isMetaLoaded() || region.getLoadState() != 0 || !region.hasHadTerrain())
                                       && region.getHighlightsHash()
                                          != region.getDim().getHighlightHandler().getRegionHash(region.getRegionX(), region.getRegionZ())
                                    || region.getLoadState() != 2 && region.shouldCache()
                              )
                     )
                     && !this.regionBuffer.contains(region)) {
                     region.calculateSortingChunkDistance();
                     Misc.addToListOfSmallest(10, this.regionBuffer, region);
                  }
               }
            }

            prevRegion = region;
            if (i >= minViewX && i <= maxViewX && j >= minViewZ && j <= maxViewZ) {
               MapTileChunk chunk = region == null ? null : region.getChunk(i & 7, j & 7);
               boolean chunkIsVisible = chunk != null && chunk.getLeafTexture().getGlColorTexture() != -1;
               if (!chunkIsVisible && (!noCaveMaps || this.previousRenderedCaveLayer == Integer.MAX_VALUE)) {
                  MapRegion previousLayerRegion = mapProcessor.getMapRegion(this.previousRenderedCaveLayer, i >> 3, j >> 3, false);
                  if (previousLayerRegion != null) {
                     MapTileChunk previousLayerChunk = previousLayerRegion.getChunk(i & 7, j & 7);
                     if (previousLayerChunk != null && previousLayerChunk.getLeafTexture().getGlColorTexture() != -1) {
                        region = previousLayerRegion;
                        chunk = previousLayerChunk;
                        chunkIsVisible = true;
                     }
                  }
               }

               if (chunkIsVisible) {
                  this.bumpLoadedRegion(mapProcessor, region);
                  GL11.glTexParameterf(3553, 33082, 0.0F);
                  int drawX = 64 * (chunk.getX() - chunkX) - 16 * tileX - insideX;
                  int drawZ = 64 * (chunk.getZ() - chunkZ) - 16 * tileZ - insideZ;
                  this.prepareMapTexturedRect(matrix, (float)drawX, (float)drawZ, 0, 0, 64.0F, 64.0F, chunk, mapNoLightRenderer, mapWithLightRenderer, helper);
                  if (slimeChunks) {
                     this.renderSlimeChunks(chunk, seed, drawX, drawZ, matrixStack, helper, overlayBufferBuilder);
                  }
               }
            }
         }
      }
   }

   public void bumpLoadedRegion(MapProcessor mapProcessor, MapRegion region) {
      if (!mapProcessor.isUploadingPaused() && region.isLoaded()) {
         mapProcessor.getMapWorld().getCurrentDimension().getLayeredMapRegions().bumpLoadedRegion(region);
      }
   }

   public void renderSlimeChunks(
      MapTileChunk chunk, Long seed, int drawX, int drawZ, class_4587 matrixStack, MinimapRendererHelper helper, class_4588 overlayBufferBuilder
   ) {
      Long savedSeed = seedsUsed.get(chunk);
      boolean newSeed = seed == null && savedSeed != null || seed != null && !seed.equals(savedSeed);
      if (newSeed) {
         seedsUsed.put(chunk, seed);
      }

      for (int t = 0; t < 16; t++) {
         if (newSeed || (chunk.getTileGridsCache()[t % 4][t / 4] & 1) == 0) {
            chunk.getTileGridsCache()[t % 4][t / 4] = (byte)(
               1 | (MinimapTile.isSlimeChunk(this.modMain.getSettings(), chunk.getX() * 4 + t % 4, chunk.getZ() * 4 + t / 4, seed) ? 2 : 0)
            );
         }

         if ((chunk.getTileGridsCache()[t % 4][t / 4] & 2) != 0) {
            int slimeDrawX = drawX + 16 * (t % 4);
            int slimeDrawZ = drawZ + 16 * (t / 4);
            helper.addColoredRectToExistingBuffer(
               matrixStack.method_23760().method_23761(), overlayBufferBuilder, (float)slimeDrawX, (float)slimeDrawZ, 16, 16, -2142047936
            );
         }
      }
   }

   public boolean getWorldMapWaypoints() {
      return WorldMap.settings.waypoints;
   }

   public int getWorldMapColours() {
      return WorldMap.settings.colours;
   }

   public boolean getWorldMapFlowers() {
      return WorldMap.settings.flowers;
   }

   public boolean getWorldMapTerrainDepth() {
      return WorldMap.settings.terrainDepth;
   }

   public int getWorldMapTerrainSlopes() {
      return WorldMap.settings.terrainSlopes;
   }

   public boolean getWorldMapBiomeColorsVanillaMode() {
      return WorldMap.settings.biomeColorsVanillaMode;
   }

   public boolean getWorldMapIgnoreHeightmaps() {
      return WorldMap.settings.getClientBooleanValue(ModOptions.IGNORE_HEIGHTMAPS);
   }

   public String tryToGetMultiworldId(class_5321<class_1937> dimId) {
      WorldMapSession worldmapSession = WorldMapSession.getCurrentSession();
      MapProcessor mapProcessor = worldmapSession.getMapProcessor();
      synchronized (mapProcessor.uiPauseSync) {
         return mapProcessor.isUIPaused() ? null : this.getMultiworldIdUnsynced(mapProcessor, dimId);
      }
   }

   private String getMultiworldIdUnsynced(MapProcessor mapProcessor, class_5321<class_1937> dimId) {
      MapDimension mapDim = mapProcessor.isMapWorldUsable() && !mapProcessor.isWaitingForWorldUpdate()
         ? mapProcessor.getMapWorld().createDimensionUnsynced(dimId)
         : null;
      return mapDim == null ? null : (!mapDim.currentMultiworldWritable ? "minimap" : mapDim.getCurrentMultiworld());
   }

   public List<String> getPotentialMultiworldIds(class_5321<class_1937> dimId) {
      WorldMapSession worldmapSession = WorldMapSession.getCurrentSession();
      MapProcessor mapProcessor = worldmapSession.getMapProcessor();
      synchronized (mapProcessor.uiSync) {
         MapDimension mapDim = mapProcessor.getMapWorld().createDimensionUnsynced(dimId);
         return mapDim != null && (mapProcessor.isWaitingForWorldUpdate() || !mapDim.currentMultiworldWritable) ? mapDim.getMultiworldIdsCopy() : null;
      }
   }

   public List<String> getMultiworldIds(class_5321<class_1937> dimId) {
      WorldMapSession worldmapSession = WorldMapSession.getCurrentSession();
      MapProcessor mapProcessor = worldmapSession.getMapProcessor();
      synchronized (mapProcessor.uiSync) {
         MapDimension mapDim = mapProcessor.getMapWorld().createDimensionUnsynced(dimId);
         return mapDim == null ? null : mapDim.getMultiworldIdsCopy();
      }
   }

   public String tryToGetMultiworldName(class_5321<class_1937> dimId, String multiworldId) {
      WorldMapSession worldmapSession = WorldMapSession.getCurrentSession();
      MapProcessor mapProcessor = worldmapSession.getMapProcessor();
      synchronized (mapProcessor.uiPauseSync) {
         return mapProcessor.isUIPaused() ? null : this.getMultiworldNameUnsynced(mapProcessor, dimId, multiworldId);
      }
   }

   private String getMultiworldNameUnsynced(MapProcessor mapProcessor, class_5321<class_1937> dimId, String multiworldId) {
      MapDimension mapDim = !mapProcessor.isMapWorldUsable() ? null : mapProcessor.getMapWorld().createDimensionUnsynced(dimId);
      return mapDim == null ? null : mapDim.getMultiworldName(multiworldId);
   }

   public void openSettings() {
      class_437 current = class_310.method_1551().field_1755;
      class_437 currentEscScreen = current instanceof ScreenBase ? ((ScreenBase)current).escape : null;
      class_310.method_1551().method_1507(new GuiWorldMapSettings(current, currentEscScreen));
   }

   public float getMinimapBrightness() {
      WorldMapSession worldmapSession = WorldMapSession.getCurrentSession();
      if (worldmapSession == null) {
         return 1.0F;
      } else {
         MapProcessor mapProcessor = worldmapSession.getMapProcessor();
         return mapProcessor.getBrightness(this.modMain.getSettings().getLighting());
      }
   }

   public boolean screenShouldSkipWorldRender(class_437 screen) {
      return Misc.screenShouldSkipWorldRender(screen, false);
   }

   public void prepareMapTexturedRect(
      Matrix4f matrix,
      float x,
      float y,
      int textureX,
      int textureY,
      float width,
      float height,
      MapTileChunk chunk,
      MultiTextureRenderTypeRenderer noLightRenderer,
      MultiTextureRenderTypeRenderer withLightrenderer,
      MinimapRendererHelper helper
   ) {
      LeafRegionTexture texture = chunk.getLeafTexture();
      int textureId = texture.getGlColorTexture();
      if (textureId != -1) {
         helper.prepareMyTexturedModalRect(
            matrix, x, y, textureX, (int)height, width, height, -height, 64.0F, textureId, texture.getTextureHasLight() ? withLightrenderer : noLightRenderer
         );
      }
   }

   public boolean getAdjustHeightForCarpetLikeBlocks() {
      return WorldMap.settings.adjustHeightForCarpetLikeBlocks;
   }

   public void registerHighlighters(HighlighterRegistry highlighterRegistry) {
      xaero.map.mods.SupportMods.xaeroMinimap.registerMinimapHighlighters(highlighterRegistry);
   }

   public void createRadarRenderWrapper(RadarRenderer radarRenderer) {
      xaero.map.mods.SupportMods.xaeroMinimap.createRadarRendererWrapper(radarRenderer);
   }

   public boolean worldMapIsRenderingRadar() {
      return WorldMap.settings.minimapRadar;
   }

   public boolean getPartialYTeleport() {
      return WorldMap.settings.partialYTeleportation;
   }

   public boolean isStainedGlassDisplayed() {
      return WorldMap.settings.displayStainedGlass;
   }

   public boolean isMultiplayerMap() {
      WorldMapSession worldmapSession = WorldMapSession.getCurrentSession();
      if (worldmapSession == null) {
         return false;
      } else {
         MapProcessor mapProcessor = worldmapSession.getMapProcessor();
         return mapProcessor.getMapWorld().isMultiplayer();
      }
   }

   public int getManualCaveStart() {
      return WorldMap.settings.caveModeStart == Integer.MAX_VALUE ? Integer.MAX_VALUE : WorldMap.settings.caveModeStart;
   }

   public boolean hasEnabledCaveLayers() {
      return this.getCaveModeType() == 1;
   }

   public int getCaveModeType() {
      if (!WorldMap.settings.isCaveMapsAllowed()) {
         return 0;
      } else {
         WorldMapSession worldmapSession = WorldMapSession.getCurrentSession();
         if (worldmapSession == null) {
            return WorldMap.settings.defaultCaveModeType;
         } else {
            MapProcessor mapProcessor = worldmapSession.getMapProcessor();
            synchronized (mapProcessor.uiPauseSync) {
               if (mapProcessor.isUIPaused()) {
                  return WorldMap.settings.defaultCaveModeType;
               }

               MapDimension mapDim = mapProcessor.getMapWorld().getCurrentDimension();
               if (mapDim != null) {
                  return mapDim.getCaveModeType();
               }
            }

            return WorldMap.settings.defaultCaveModeType;
         }
      }
   }

   public void openScreenForOption(xaero.common.settings.ModOptions option) {
      if (class_310.method_1551().field_1687 != null) {
         class_437 current = class_310.method_1551().field_1755;
         class_437 currentEscScreen = current instanceof ScreenBase ? ((ScreenBase)current).escape : null;
         if (currentEscScreen instanceof GuiMap) {
            currentEscScreen = null;
         }

         WorldMapSession worldmapSession = WorldMapSession.getCurrentSession();
         MapProcessor mapProcessor = worldmapSession.getMapProcessor();
         class_437 screen = new GuiMap(current, currentEscScreen, mapProcessor, class_310.method_1551().method_1560());
         if (option == xaero.common.settings.ModOptions.MANUAL_CAVE_MODE_START) {
            ((GuiMap)screen).enableCaveModeOptions();
         }

         class_310.method_1551().method_1507(screen);
      }
   }

   public int getCaveModeDepth() {
      return WorldMap.settings.caveModeDepth;
   }

   public boolean isLegibleCaveMaps() {
      return WorldMap.settings.legibleCaveMaps;
   }

   public boolean getBiomeBlending() {
      return WorldMap.settings.biomeBlending;
   }

   public boolean hasTrackedPlayerSystemSupport() {
      return this.compatibilityVersion >= 22;
   }

   public void confirmPlayerRadarRender(class_1657 e) {
      if (this.hasTrackedPlayerSystemSupport()) {
         if (WorldMap.trackedPlayerRenderer.getCollector().playerExists(e.method_5667())) {
            WorldMap.trackedPlayerRenderer.getCollector().confirmPlayerRadarRender(e);
         }
      } else {
         if (xaero.map.mods.SupportMods.xaeroPac.playerExists(e.method_5667())) {
            xaero.map.mods.SupportMods.xaeroPac.confirmPlayerRadarRender(e);
         }
      }
   }

   public boolean getDisplayClaims() {
      return WorldMap.settings.displayClaims;
   }

   public int getClaimsBorderOpacity() {
      return WorldMap.settings.claimsBorderOpacity;
   }

   public int getClaimsFillOpacity() {
      return WorldMap.settings.claimsFillOpacity;
   }

   public void toggleChunkClaims() {
      WorldMap.settings.setOptionValue(ModOptions.PAC_CLAIMS, !(Boolean)WorldMap.settings.getOptionValue(ModOptions.PAC_CLAIMS));
   }

   public boolean supportsPacPlayerRadarFilter() {
      return this.compatibilityVersion >= 21;
   }
}
