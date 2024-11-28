package xaero.common.minimap.write;

import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import javax.imageio.ImageIO;
import net.minecraft.class_1047;
import net.minecraft.class_1058;
import net.minecraft.class_1059;
import net.minecraft.class_1087;
import net.minecraft.class_1297;
import net.minecraft.class_1921;
import net.minecraft.class_1937;
import net.minecraft.class_1944;
import net.minecraft.class_2189;
import net.minecraft.class_2246;
import net.minecraft.class_2248;
import net.minecraft.class_2261;
import net.minecraft.class_2286;
import net.minecraft.class_2320;
import net.minecraft.class_2338;
import net.minecraft.class_2350;
import net.minecraft.class_2356;
import net.minecraft.class_2368;
import net.minecraft.class_2378;
import net.minecraft.class_2386;
import net.minecraft.class_2404;
import net.minecraft.class_2462;
import net.minecraft.class_2464;
import net.minecraft.class_2504;
import net.minecraft.class_2506;
import net.minecraft.class_2521;
import net.minecraft.class_2586;
import net.minecraft.class_2680;
import net.minecraft.class_2688;
import net.minecraft.class_2791;
import net.minecraft.class_2806;
import net.minecraft.class_2812;
import net.minecraft.class_2818;
import net.minecraft.class_2826;
import net.minecraft.class_2960;
import net.minecraft.class_310;
import net.minecraft.class_3298;
import net.minecraft.class_3481;
import net.minecraft.class_3610;
import net.minecraft.class_3619;
import net.minecraft.class_3620;
import net.minecraft.class_4696;
import net.minecraft.class_5321;
import net.minecraft.class_5819;
import net.minecraft.class_638;
import net.minecraft.class_773;
import net.minecraft.class_777;
import net.minecraft.class_7924;
import net.minecraft.class_2338.class_2339;
import net.minecraft.class_2902.class_2903;
import org.lwjgl.opengl.GL11;
import xaero.common.AXaeroMinimap;
import xaero.common.MinimapLogs;
import xaero.common.XaeroMinimapSession;
import xaero.common.cache.BlockStateShortShapeCache;
import xaero.common.core.XaeroMinimapCore;
import xaero.common.effect.Effects;
import xaero.common.exception.SilentException;
import xaero.common.minimap.MinimapInterface;
import xaero.common.minimap.MinimapProcessor;
import xaero.common.minimap.highlight.DimensionHighlighterHandler;
import xaero.common.minimap.highlight.HighlighterRegistry;
import xaero.common.minimap.mcworld.MinimapClientWorldData;
import xaero.common.minimap.mcworld.MinimapClientWorldDataHelper;
import xaero.common.minimap.region.MinimapChunk;
import xaero.common.minimap.region.MinimapTile;
import xaero.common.minimap.waypoints.WaypointWorld;
import xaero.common.minimap.write.biome.BiomeBlendCalculator;
import xaero.common.misc.CachedFunction;
import xaero.common.misc.Misc;
import xaero.common.misc.OptimizedMath;
import xaero.common.mods.SupportMods;
import xaero.common.settings.ModSettings;

public class MinimapWriter {
   private static final int VOID_COLOR = -16121833;
   private static final float DEFAULT_AMBIENT_LIGHT = 0.7F;
   private static final float DEFAULT_AMBIENT_LIGHT_COLORED = 0.2F;
   private static final float DEFAULT_AMBIENT_LIGHT_WHITE = 0.5F;
   private static final float DEFAULT_MAX_DIRECT_LIGHT = 0.6666667F;
   private static final float GLOWING_MAX_DIRECT_LIGHT = 0.22222224F;
   private static final String[] dimensionsToIgnore = new String[]{"FZHammer"};
   private static final int UPDATE_EVERY_RUNS = 5;
   private static final int MAXIMUM_OVERLAYS = 5;
   public static final int SUN_MINIMUM = 9;
   public static final int NO_Y_VALUE = Integer.MAX_VALUE;
   private static final int MAX_TRANSPARENCY_BLEND_DEPTH = 5;
   private AXaeroMinimap modMain;
   private XaeroMinimapSession minimapSession;
   private MinimapWriterHelper helper;
   private MinimapInterface minimapInterface;
   private class_5819 usedRandom = class_5819.method_43049(0L);
   private int loadingSideInChunks;
   private int updateRadius;
   private MinimapChunk[][] loadingBlocks;
   private int loadingMapChunkX;
   private int loadingMapChunkZ;
   private int loadingStartX;
   private int loadingStartZ;
   private int loadingEndX;
   private int loadingEndZ;
   private int loadingCaving;
   private int loadingLevels;
   private boolean loadingLighting;
   private float loadingSingleLevelBrightness;
   private int loadingTerrainSlopes;
   private boolean loadingTerrainDepth;
   private boolean loadingRedstone;
   private int loadingColours;
   private boolean loadingTransparency;
   private boolean loadingBiomesVanillaMode;
   private class_5321<class_1937> loadingDimension;
   private boolean loadingIgnoreHeightmaps;
   private int loadingCaveMapsDepth;
   public int loadingLightOverlayType;
   public int loadingLightOverlayMaxLight;
   public int loadingLightOverlayMinLight;
   public int loadingLightOverlayColor;
   private boolean loadingFlowers;
   private boolean loadingAdjustHeightForCarpetLikeBlocks;
   private boolean loadingStainedGlass;
   private boolean loadingLegibleCaveMode;
   private boolean loadingBiomeBlending;
   private boolean loadingNonWorldMap;
   private Long loadingSlimeSeed;
   private int loadingHighlightVersion;
   private int loadedSideInChunks;
   private MinimapChunk[][] loadedBlocks;
   private int loadedMapChunkX;
   private int loadedMapChunkZ;
   private int loadedCaving;
   private int prevLoadedCaving;
   private int loadedLevels;
   private boolean loadedLighting;
   private int loadedTerrainSlopes;
   private boolean loadedTerrainDepth;
   private boolean loadedRedstone;
   private int loadedColours;
   private boolean loadedTransparency;
   private boolean loadedBiomesVanillaMode;
   private class_5321<class_1937> loadedDimension;
   private boolean loadedIgnoreHeightmaps;
   private int loadedCaveMapsDepth;
   public int loadedLightOverlayType;
   public int loadedLightOverlayMaxLight;
   public int loadedLightOverlayMinLight;
   public int loadedLightOverlayColor;
   private boolean loadedFlowers;
   private boolean loadedAdjustHeightForCarpetLikeBlocks;
   private boolean loadedStainedGlass;
   private boolean loadedLegibleCaveMode;
   private boolean loadedBiomeBlending;
   private boolean loadedNonWorldMap;
   private Long loadedSlimeSeed;
   private int loadedHighlightVersion;
   private long loadedTime;
   private boolean settingsChanged;
   private ArrayList<Long> detectedChunkChanges;
   private int workingFrameCount;
   private long framesFreedTime = -1L;
   public long writeFreeSinceLastWrite = -1L;
   private int writeFreeSizeTiles;
   private int writeFreeFullUpdateTargetTime;
   private int updateChunkX;
   private int updateChunkZ;
   private int tileInsideX;
   private int tileInsideZ;
   private int runNumber;
   private boolean previousShouldLoad;
   private int lastCaving;
   private boolean clearBlockColours;
   private final HashMap<String, Integer> textureColours;
   private final HashMap<Integer, Integer> blockColours;
   private final CachedFunction<class_2688<?, ?>, Boolean> transparentCache;
   private final CachedFunction<class_2680, Boolean> glowingCache;
   private long lastWrite = -1L;
   private long lastWriteTry = -1L;
   private boolean forcedRefresh;
   private MinimapChunk oldChunk;
   private class_2680 lastBlockStateForTextureColor = null;
   private int lastBlockStateForTextureColorResult = -1;
   private CachedFunction<class_3610, class_2680> fluidToBlock;
   private int updates;
   private int loads;
   private long before;
   private int processingTime;
   public boolean debugTotalTime = false;
   public long minTime = -1L;
   public long maxTime = -1L;
   public long totalTime;
   public long totalRuns;
   public long lastDebugTime = -1L;
   public long minTimeDebug;
   public long maxTimeDebug;
   public long averageTimeDebug;
   private long currentComparisonCode;
   private final List<Integer> pixelTransparentSizes;
   private final List<class_2680> pixelBlockStates;
   private final List<Integer> pixelBlockLights;
   private int firstBlockY;
   boolean isglowing;
   private final int[] underRed;
   private final int[] underGreen;
   private final int[] underBlue;
   private int sun;
   private float currentTransparencyMultiplier;
   private int blockY;
   private int blockColor;
   private final int[] red;
   private final int[] green;
   private final int[] blue;
   private final float[] brightness;
   private final float[] postBrightness;
   private final int[] tempColor;
   private boolean underair;
   private class_2680 previousTransparentState;
   private int firstTransparentStateY;
   private final class_2339 mutableBlockPos;
   private final class_2339 mutableBlockPos2;
   private final class_2339 mutableBlockPos3;
   private final int[][] intUpdateArrayBuffers;
   private ArrayList<class_2680> buggedStates;
   private final class_310 mc;
   private final BiomeBlendCalculator biomeBlendCalculator;
   private final BlockStateShortShapeCache blockStateShortShapeCache;
   private final HighlighterRegistry highlighterRegistry;
   private class_1937 prevWorld;
   private DimensionHighlighterHandler dimensionHighlightHandler;

   public MinimapWriter(
      AXaeroMinimap modMain, XaeroMinimapSession minimapSession, BlockStateShortShapeCache blockStateShortShapeCache, HighlighterRegistry highlighterRegistry
   ) {
      this.modMain = modMain;
      this.minimapSession = minimapSession;
      this.loadingSideInChunks = 9;
      this.updateRadius = 16;
      this.prevLoadedCaving = this.loadedCaving = this.loadingCaving = Integer.MAX_VALUE;
      this.lastCaving = Integer.MAX_VALUE;
      this.textureColours = new HashMap<>();
      this.blockColours = new HashMap<>();
      this.loadedCaving = Integer.MAX_VALUE;
      this.red = new int[5];
      this.green = new int[5];
      this.blue = new int[5];
      this.underRed = new int[5];
      this.underGreen = new int[5];
      this.underBlue = new int[5];
      this.brightness = new float[5];
      this.postBrightness = new float[5];
      this.tempColor = new int[3];
      this.helper = new MinimapWriterHelper();
      this.mutableBlockPos = new class_2339();
      this.mutableBlockPos2 = new class_2339();
      this.mutableBlockPos3 = new class_2339();
      this.intUpdateArrayBuffers = new int[5][4096];
      this.pixelBlockStates = new ArrayList<>();
      this.pixelTransparentSizes = new ArrayList<>();
      this.pixelBlockLights = new ArrayList<>();
      this.transparentCache = new CachedFunction<>(
         state -> {
            if (!(state instanceof class_2680 blockState)) {
               class_3610 fluidState = (class_3610)state;
               return class_4696.method_23680(fluidState) == class_1921.method_23583();
            } else {
               return blockState.method_26204() instanceof class_2189
                  || blockState.method_26204() instanceof class_2368
                  || class_4696.method_23679(blockState) == class_1921.method_23583();
            }
         }
      );
      this.glowingCache = new CachedFunction<>(state -> {
         try {
            return state.method_26213() > 0;
         } catch (Exception var2) {
            return false;
         }
      });
      this.minimapInterface = modMain.getInterfaces().getMinimapInterface();
      this.buggedStates = new ArrayList<>();
      this.detectedChunkChanges = new ArrayList<>();
      this.mc = class_310.method_1551();
      this.biomeBlendCalculator = new BiomeBlendCalculator();
      this.blockStateShortShapeCache = blockStateShortShapeCache;
      this.fluidToBlock = new CachedFunction<>(class_3610::method_15759);
      this.highlighterRegistry = highlighterRegistry;
   }

   public void setupDimensionHighlightHandler(class_5321<class_1937> dimension) {
      this.dimensionHighlightHandler = new DimensionHighlighterHandler(dimension, this.highlighterRegistry, this);
   }

   private void updateTimeDebug(long before) {
      if (this.debugTotalTime) {
         long debugPassed = System.nanoTime() - before;
         this.totalTime += debugPassed;
         this.totalRuns++;
         if (debugPassed > this.maxTime) {
            this.maxTime = debugPassed;
         }

         if (this.minTime == -1L || debugPassed < this.minTime) {
            this.minTime = debugPassed;
         }

         long time = System.currentTimeMillis();
         if (this.lastDebugTime == -1L) {
            this.lastDebugTime = time;
         } else if (time - this.lastDebugTime > 1000L) {
            this.maxTimeDebug = this.maxTime;
            this.minTimeDebug = this.minTime;
            this.averageTimeDebug = this.totalTime / this.totalRuns;
            this.maxTime = -1L;
            this.minTime = -1L;
            this.totalTime = 0L;
            this.totalRuns = 0L;
            this.lastDebugTime = time;
         }
      }
   }

   public void onRender() {
      if (ModSettings.canEditIngameSettings()) {
         long before = System.nanoTime();
         MinimapProcessor minimapProcessor = this.minimapSession.getMinimapProcessor();

         try {
            class_1297 player = class_310.method_1551().method_1560();
            if (player == null) {
               return;
            }

            class_1937 world = player.method_37908();
            if (world != this.prevWorld) {
               if (world != null) {
                  this.setupDimensionHighlightHandler(world.method_27983());
               } else {
                  this.dimensionHighlightHandler = null;
               }

               this.loadedDimension = null;
               this.tileInsideX = this.tileInsideZ = this.updateChunkX = this.updateChunkZ = 0;
               this.prevWorld = world;
               if (this.modMain.getSupportMods().framedBlocks()) {
                  this.modMain.getSupportMods().supportFramedBlocks.onWorldChange();
               }
            }

            double playerX = player.method_23317();
            double playerY = player.method_23318();
            double playerZ = player.method_23321();
            if (this.modMain.getSettings() == null || !this.modMain.getSettings().getMinimap() || world == null) {
               this.updateTimeDebug(before);
               return;
            }

            int cavingDestination = this.getCaving(minimapProcessor.isManualCaveMode(), playerX, playerY, playerZ, world);
            boolean attemptUsingWorldMapChunks = this.modMain.getSupportMods().shouldUseWorldMapChunks()
               && (cavingDestination == Integer.MAX_VALUE || this.modMain.getSupportMods().shouldUseWorldMapCaveChunks())
               && this.modMain.getSettings().lightOverlayType <= 0;
            boolean shouldLoad = !this.ignoreWorld(world)
               && (
                  !attemptUsingWorldMapChunks
                     || this.loadedNonWorldMap
                     || this.loadingNonWorldMap
                     || this.loadedCaving != cavingDestination
                     || this.loadedCaving != this.loadingCaving
               );
            if (shouldLoad != this.previousShouldLoad) {
               this.tileInsideX = this.tileInsideZ = this.updateChunkX = this.updateChunkZ = 0;
               this.previousShouldLoad = shouldLoad;
            }

            if (!shouldLoad) {
               this.updateTimeDebug(before);
               return;
            }

            XaeroMinimapCore.ensureField();
            int lengthX = Math.min(this.loadingSideInChunks, this.loadingEndX - this.loadingStartX + 1);
            int lengthZ = Math.min(this.loadingSideInChunks, this.loadingEndZ - this.loadingStartZ + 1);
            if (this.lastWriteTry == -1L) {
               lengthX = 3;
               lengthZ = 3;
            } else {
               if (lengthX > this.loadingSideInChunks) {
                  lengthX = this.loadingSideInChunks;
               }

               if (lengthZ > this.loadingSideInChunks) {
                  lengthZ = this.loadingSideInChunks;
               }
            }

            int sizeTileChunks = lengthX * lengthZ;
            int sizeTiles = sizeTileChunks * 4 * 4;
            int sizeBasedTargetTime = sizeTiles * 1000 / 1500;
            int fullUpdateTargetTime = Math.max(100, sizeBasedTargetTime);
            long time = System.currentTimeMillis();
            long passed = this.lastWrite == -1L ? 0L : time - this.lastWrite;
            if (this.lastWriteTry == -1L
               || this.writeFreeSizeTiles != sizeTiles
               || this.writeFreeFullUpdateTargetTime != fullUpdateTargetTime
               || this.workingFrameCount > 30) {
               this.framesFreedTime = time;
               this.writeFreeSizeTiles = sizeTiles;
               this.writeFreeFullUpdateTargetTime = fullUpdateTargetTime;
               this.workingFrameCount = 0;
            }

            long sinceLastWrite = Math.min(passed, this.writeFreeSinceLastWrite);
            if (this.framesFreedTime != -1L) {
               sinceLastWrite = time - this.framesFreedTime;
            }

            long tilesToUpdate = Math.min(sinceLastWrite * (long)sizeTiles / (long)fullUpdateTargetTime, 100L);
            if (this.lastWrite == -1L || tilesToUpdate != 0L) {
               this.lastWrite = time;
            }

            int flickeringTimer = this.modMain.getSettings().caveModeToggleTimer;
            if (tilesToUpdate != 0L) {
               if (this.framesFreedTime == -1L) {
                  int timeLimit = (int)(Math.min(sinceLastWrite, 50L) * 86960L);
                  long writeStartNano = System.nanoTime();
                  if (cavingDestination == Integer.MAX_VALUE != (this.loadingCaving == Integer.MAX_VALUE)
                     || attemptUsingWorldMapChunks == this.loadingNonWorldMap) {
                     this.tileInsideX = this.tileInsideZ = this.updateChunkX = this.updateChunkZ = 0;
                     this.loadedTime = time;
                  }

                  for (int i = 0; (long)i < tilesToUpdate && !this.beforeWriting(attemptUsingWorldMapChunks, cavingDestination, flickeringTimer, time); i++) {
                     if (this.writeChunk(minimapProcessor, playerX, playerY, playerZ, world, cavingDestination, attemptUsingWorldMapChunks)) {
                        i--;
                     }

                     if (System.nanoTime() - writeStartNano >= (long)timeLimit) {
                        break;
                     }
                  }

                  this.workingFrameCount++;
               } else {
                  this.writeFreeSinceLastWrite = sinceLastWrite;
                  this.framesFreedTime = -1L;
               }
            }

            this.lastWriteTry = time;
         } catch (Throwable var34) {
            this.minimapInterface.setCrashedWith(var34);
         }

         this.updateTimeDebug(before);
      }
   }

   private boolean beforeWriting(boolean attemptUsingWorldMapChunks, int cavingDestination, int flickeringTimer, long time) {
      if (this.tileInsideX == 0 && this.tileInsideZ == 0 && this.updateChunkX == 0 && this.updateChunkZ == 0 && attemptUsingWorldMapChunks) {
         this.loadingCaving = cavingDestination;
         this.loadingNonWorldMap = false;
         if (this.loadedCaving == Integer.MAX_VALUE == (this.loadingCaving == Integer.MAX_VALUE)
            || this.loadedTime == 0L
            || time - this.loadedTime >= (long)flickeringTimer) {
            this.loadedCaving = this.loadingCaving;
            this.loadedNonWorldMap = false;
         }

         if (!this.loadedNonWorldMap) {
            this.detectedChunkChanges.clear();
         }

         return true;
      } else {
         return this.tileInsideX == 3
            && this.tileInsideZ == 3
            && this.updateChunkX == this.loadingSideInChunks - 1
            && this.updateChunkZ == this.loadingSideInChunks - 1
            && this.loadingCaving == Integer.MAX_VALUE != (this.loadedCaving == Integer.MAX_VALUE)
            && this.loadedTime != 0L
            && time - this.loadedTime < (long)flickeringTimer;
      }
   }

   private boolean writeChunk(
      MinimapProcessor minimapProcessor,
      double playerX,
      double playerY,
      double playerZ,
      class_1937 world,
      int cavingDestination,
      boolean attemptUsingWorldMapChunks
   ) {
      long processStart = System.nanoTime();
      if (this.tileInsideX == 0 && this.tileInsideZ == 0) {
         if (this.updateChunkX == 0 && this.updateChunkZ == 0) {
            this.settingsChanged = false;
            if (this.clearBlockColours) {
               this.settingsChanged = true;
               this.clearBlockColours = false;
               if (!this.blockColours.isEmpty()) {
                  this.blockColours.clear();
                  this.textureColours.clear();
                  this.lastBlockStateForTextureColor = null;
                  this.lastBlockStateForTextureColorResult = -1;
                  MinimapLogs.LOGGER.info("Minimap block colour cache cleaned.");
               }
            }

            this.loadingSideInChunks = this.getLoadSide();
            this.updateRadius = this.getUpdateRadiusInChunks();
            int playerXInt = OptimizedMath.myFloor(playerX);
            int playerZInt = OptimizedMath.myFloor(playerZ);
            this.loadingMapChunkX = this.getMapCoord(this.loadingSideInChunks, playerXInt);
            this.loadingMapChunkZ = this.getMapCoord(this.loadingSideInChunks, playerZInt);
            int loadDistance = (Integer)this.mc.field_1690.method_42503().method_41753();
            int playerTileX = playerXInt >> 4;
            int playerTileZ = playerZInt >> 4;
            int globalStartX = playerTileX - loadDistance >> 2;
            int globalStartZ = playerTileZ - loadDistance >> 2;
            int globalEndX = playerTileX + loadDistance >> 2;
            int globalEndZ = playerTileZ + loadDistance >> 2;
            this.loadingStartX = globalStartX - this.loadingMapChunkX;
            this.loadingStartZ = globalStartZ - this.loadingMapChunkZ;
            this.loadingEndX = globalEndX - this.loadingMapChunkX;
            this.loadingEndZ = globalEndZ - this.loadingMapChunkZ;
            this.loadingCaving = cavingDestination;
            if (this.loadingCaving != Integer.MAX_VALUE || this.loadedCaving != Integer.MAX_VALUE) {
               int maxDistance = 2;
               if (this.loadingCaving != Integer.MAX_VALUE && this.loadedCaving == Integer.MAX_VALUE) {
                  maxDistance = 1;
               }

               this.loadingStartX = Math.max(this.loadingStartX, this.loadingSideInChunks / 2 - maxDistance);
               this.loadingStartZ = Math.max(this.loadingStartZ, this.loadingSideInChunks / 2 - maxDistance);
               this.loadingEndX = Math.min(this.loadingEndX, this.loadingSideInChunks / 2 + maxDistance);
               this.loadingEndZ = Math.min(this.loadingEndZ, this.loadingSideInChunks / 2 + maxDistance);
            }

            if (this.loadingCaving != this.loadedCaving || this.loadingCaving != Integer.MAX_VALUE && this.prevLoadedCaving == Integer.MAX_VALUE) {
               this.runNumber = 0;
            }

            this.loadingLighting = this.modMain.getSettings().getLighting();
            this.loadingLevels = this.loadingLighting ? 5 : 1;
            this.loadingSingleLevelBrightness = 1.0F;
            this.loadingLegibleCaveMode = this.modMain.getSettings().isLegibleCaveMaps();
            this.loadingBiomeBlending = this.modMain.getSettings().getBiomeBlending();
            if (!((class_638)world).method_28103().method_28114() && (!this.loadingLegibleCaveMode || this.loadingCaving == Integer.MAX_VALUE)) {
               if (this.loadingLighting && !world.method_8597().comp_642()) {
                  this.loadingLevels = 1;
                  this.loadingSingleLevelBrightness = this.minimapInterface.getMinimapFBORenderer().getSunBrightness(minimapProcessor, true);
               }
            } else {
               this.loadingLighting = false;
               this.loadingLevels = 1;
            }

            this.loadingTerrainSlopes = this.modMain.getSettings().getTerrainSlopes();
            this.loadingTerrainDepth = this.modMain.getSettings().getTerrainDepth();
            this.loadingRedstone = this.modMain.getSettings().getDisplayRedstone();
            this.loadingColours = this.modMain.getSettings().getBlockColours();
            this.loadingTransparency = this.modMain.getSettings().blockTransparency;
            this.loadingBiomesVanillaMode = this.modMain.getSettings().getBiomeColorsVanillaMode();
            this.loadingDimension = world.method_27983();
            this.loadingCaveMapsDepth = this.modMain.getSettings().getCaveMapsDepth();
            this.loadingIgnoreHeightmaps = this.modMain.getSettings().isIgnoreHeightmaps();
            this.loadingLightOverlayColor = this.modMain.getSettings().lightOverlayColor;
            this.loadingLightOverlayMaxLight = this.modMain.getSettings().lightOverlayMaxLight;
            this.loadingLightOverlayMinLight = this.modMain.getSettings().lightOverlayMinLight;
            this.loadingLightOverlayType = this.modMain.getSettings().lightOverlayType;
            this.loadingFlowers = this.modMain.getSettings().getShowFlowers();
            this.loadingAdjustHeightForCarpetLikeBlocks = this.modMain.getSettings().getAdjustHeightForCarpetLikeBlocks();
            WaypointWorld waypointWorld = this.minimapSession.getWaypointsManager().getAutoWorld();
            if (waypointWorld != null) {
               this.loadingSlimeSeed = this.modMain.getSettings().getSlimeChunksSeed(waypointWorld.getFullId());
            }

            this.loadingHighlightVersion = this.dimensionHighlightHandler.getVersion();
            this.loadingStainedGlass = this.modMain.getSettings().isStainedGlassDisplayed();
            this.loadingNonWorldMap = true;
            this.settingsChanged = this.settingsChanged || this.loadedDimension != this.loadingDimension;
            this.settingsChanged = this.settingsChanged || this.loadedTerrainSlopes != this.loadingTerrainSlopes;
            this.settingsChanged = this.settingsChanged || this.loadedTerrainDepth != this.loadingTerrainDepth;
            this.settingsChanged = this.settingsChanged || this.loadedRedstone != this.loadingRedstone;
            this.settingsChanged = this.settingsChanged || this.loadedColours != this.loadingColours;
            this.settingsChanged = this.settingsChanged || this.loadedTransparency != this.loadingTransparency;
            this.settingsChanged = this.settingsChanged || this.loadingBiomesVanillaMode != this.loadedBiomesVanillaMode;
            this.settingsChanged = this.settingsChanged || this.loadingCaveMapsDepth != this.loadedCaveMapsDepth;
            this.settingsChanged = this.settingsChanged || this.loadingIgnoreHeightmaps != this.loadedIgnoreHeightmaps;
            this.settingsChanged = this.settingsChanged || this.loadingLightOverlayColor != this.loadedLightOverlayColor;
            this.settingsChanged = this.settingsChanged || this.loadingLightOverlayMaxLight != this.loadedLightOverlayMaxLight;
            this.settingsChanged = this.settingsChanged || this.loadingLightOverlayMinLight != this.loadedLightOverlayMinLight;
            this.settingsChanged = this.settingsChanged || this.loadingLightOverlayType != this.loadedLightOverlayType;
            this.settingsChanged = this.settingsChanged || this.loadingFlowers != this.loadedFlowers;
            this.settingsChanged = this.settingsChanged || this.loadingAdjustHeightForCarpetLikeBlocks != this.loadedAdjustHeightForCarpetLikeBlocks;
            this.settingsChanged = this.settingsChanged || this.loadingCaving == Integer.MAX_VALUE != (this.loadedCaving == Integer.MAX_VALUE);
            this.settingsChanged = this.settingsChanged || !Objects.equals(this.loadingSlimeSeed, this.loadedSlimeSeed);
            this.settingsChanged = this.settingsChanged || this.loadingHighlightVersion != this.loadedHighlightVersion;
            this.settingsChanged = this.settingsChanged || this.loadingStainedGlass != this.loadedStainedGlass;
            this.settingsChanged = this.settingsChanged || this.loadingLegibleCaveMode != this.loadedLegibleCaveMode;
            this.settingsChanged = this.settingsChanged || this.loadingLighting != this.loadedLighting;
            this.settingsChanged = this.settingsChanged || this.loadingBiomeBlending != this.loadedBiomeBlending;
            this.settingsChanged = this.settingsChanged || this.loadingNonWorldMap != this.loadedNonWorldMap;
            if (this.loadingBlocks == null || this.loadingBlocks.length != this.loadingSideInChunks) {
               this.loadingBlocks = new MinimapChunk[this.loadingSideInChunks][this.loadingSideInChunks];
            }

            if (this.minimapInterface.usingFBO() && minimapProcessor.isToResetImage()) {
               this.forcedRefresh = true;
               minimapProcessor.setToResetImage(false);
            }

            this.biomeBlendCalculator.prepare(world, this.loadingBiomeBlending);
         }

         this.oldChunk = null;
         if (this.loadedBlocks != null) {
            int updateChunkXOld = this.loadingMapChunkX + this.updateChunkX - this.loadedMapChunkX;
            int updateChunkZOld = this.loadingMapChunkZ + this.updateChunkZ - this.loadedMapChunkZ;
            if (updateChunkXOld > -1 && updateChunkXOld < this.loadedBlocks.length && updateChunkZOld > -1 && updateChunkZOld < this.loadedBlocks.length) {
               this.oldChunk = this.loadedBlocks[updateChunkXOld][updateChunkZOld];
            }
         }
      }

      MinimapChunk mchunk = this.loadingBlocks[this.updateChunkX][this.updateChunkZ];
      if (mchunk == null) {
         mchunk = this.loadingBlocks[this.updateChunkX][this.updateChunkZ] = new MinimapChunk(
            this.loadingMapChunkX + this.updateChunkX, this.loadingMapChunkZ + this.updateChunkZ
         );
      } else if (this.tileInsideX == 0 && this.tileInsideZ == 0) {
         mchunk.reset(this.loadingMapChunkX + this.updateChunkX, this.loadingMapChunkZ + this.updateChunkZ);
      }

      boolean onlyLoad = this.runNumber % 5 != 0 && this.loadingLightOverlayType <= 0;
      boolean outsideRender = this.updateChunkX < this.loadingStartX
         || this.updateChunkX > this.loadingEndX
         || this.updateChunkZ < this.loadingStartZ
         || this.updateChunkZ > this.loadingEndZ;
      MinimapChunk topChunk = this.updateChunkZ > 0 ? this.loadingBlocks[this.updateChunkX][this.updateChunkZ - 1] : null;
      MinimapChunk topLeftChunk = this.updateChunkX > 0 && this.updateChunkZ > 0 ? this.loadingBlocks[this.updateChunkX - 1][this.updateChunkZ - 1] : null;
      MinimapChunk leftChunk = this.updateChunkX > 0 ? this.loadingBlocks[this.updateChunkX - 1][this.updateChunkZ] : null;
      boolean wasProperWrite = this.writeTile(
         minimapProcessor,
         playerX,
         playerY,
         playerZ,
         world,
         mchunk,
         this.oldChunk,
         topChunk,
         topLeftChunk,
         leftChunk,
         this.updateChunkX,
         this.updateChunkZ,
         this.tileInsideX,
         this.tileInsideZ,
         onlyLoad,
         outsideRender
      );
      if (this.loadingLightOverlayType > 0 && this.loadedLightOverlayType > 0) {
         onlyLoad = true;
      }

      this.tileInsideZ++;
      if (this.tileInsideZ >= 4) {
         this.tileInsideZ = 0;
         this.tileInsideX++;
         if (this.tileInsideX >= 4) {
            this.tileInsideX = 0;
            mchunk = this.loadingBlocks[this.updateChunkX][this.updateChunkZ];
            if (this.minimapInterface.usingFBO() && mchunk.isHasSomething() && mchunk.isChanged()) {
               mchunk.updateBuffers(this.loadingLevels, this.intUpdateArrayBuffers);
               mchunk.setChanged(false);
            }

            mchunk.setLevelsBuffered(this.loadingLevels);
            if (this.updateChunkX == this.loadingSideInChunks - 1 && this.updateChunkZ == this.loadingSideInChunks - 1) {
               if (this.runNumber % 5 == 0 && !MinimapTile.recycled.isEmpty()) {
                  MinimapTile.recycled.subList(MinimapTile.recycled.size() / 2, MinimapTile.recycled.size()).clear();
               }

               if (this.loadedBlocks != null) {
                  for (int i = 0; i < this.loadedBlocks.length; i++) {
                     for (int j = 0; j < this.loadedBlocks.length; j++) {
                        MinimapChunk m = this.loadedBlocks[i][j];
                        MinimapChunk lm = null;
                        if (m != null) {
                           m.recycleTiles();
                           int loadingX = this.loadedMapChunkX + i - this.loadingMapChunkX;
                           int loadingZ = this.loadedMapChunkZ + j - this.loadingMapChunkZ;
                           if (loadingX > -1 && loadingZ > -1 && loadingX < this.loadingSideInChunks && loadingZ < this.loadingSideInChunks) {
                              lm = this.loadingBlocks[loadingX][loadingZ];
                           }

                           boolean shouldTransfer = m.getLevelsBuffered() == this.loadingLevels && lm != null;
                           if (shouldTransfer) {
                              synchronized (m) {
                                 m.setBlockTextureUpload(true);
                              }
                           }

                           for (int l = 0; l < m.getLevelsBuffered(); l++) {
                              if (m.getGlTexture(l) != 0) {
                                 if (shouldTransfer) {
                                    lm.setGlTexture(l, m.getGlTexture(l));
                                 } else {
                                    GL11.glDeleteTextures(m.getGlTexture(l));
                                 }
                              }

                              if (shouldTransfer && !lm.isRefreshRequired(l) && m.isRefreshRequired(l)) {
                                 lm.copyBuffer(l, m.getBuffer(l));
                                 lm.setRefreshRequired(l, true);
                                 m.setRefreshRequired(l, false);
                              }
                           }
                        }
                     }
                  }
               }

               synchronized (this) {
                  MinimapChunk[][] bu = this.loadedBlocks;
                  this.loadedSideInChunks = this.loadingSideInChunks;
                  this.loadedBlocks = this.loadingBlocks;
                  this.loadingBlocks = bu;
                  this.loadedMapChunkX = this.loadingMapChunkX;
                  this.loadedMapChunkZ = this.loadingMapChunkZ;
                  this.loadedLevels = this.loadingLevels;
                  this.loadedLighting = this.loadingLighting;
                  this.loadedTerrainSlopes = this.loadingTerrainSlopes;
                  this.loadedTerrainDepth = this.loadingTerrainDepth;
                  this.loadedRedstone = this.loadingRedstone;
                  this.loadedColours = this.loadingColours;
                  this.loadedTransparency = this.loadingTransparency;
                  this.loadedBiomesVanillaMode = this.loadingBiomesVanillaMode;
                  this.loadedDimension = this.loadingDimension;
                  this.loadedCaveMapsDepth = this.loadingCaveMapsDepth;
                  this.loadedIgnoreHeightmaps = this.loadingIgnoreHeightmaps;
                  this.loadedLightOverlayColor = this.loadingLightOverlayColor;
                  this.loadedLightOverlayMaxLight = this.loadingLightOverlayMaxLight;
                  this.loadedLightOverlayMinLight = this.loadingLightOverlayMinLight;
                  this.loadedLightOverlayType = this.loadingLightOverlayType;
                  this.loadedFlowers = this.loadingFlowers;
                  this.loadedAdjustHeightForCarpetLikeBlocks = this.loadingAdjustHeightForCarpetLikeBlocks;
                  this.loadedSlimeSeed = this.loadingSlimeSeed;
                  this.loadedHighlightVersion = this.loadingHighlightVersion;
                  this.loadedStainedGlass = this.loadingStainedGlass;
                  this.loadedLegibleCaveMode = this.loadingLegibleCaveMode;
                  this.loadedBiomeBlending = this.loadingBiomeBlending;
                  this.loadedTime = System.currentTimeMillis();
               }

               this.detectedChunkChanges.clear();
               this.prevLoadedCaving = this.loadedCaving;
               this.loadedCaving = this.loadingCaving;
               this.loadedNonWorldMap = true;
               this.forcedRefresh = false;
               this.runNumber++;
            }

            this.updateChunkZ++;
            if (this.updateChunkZ >= this.loadingSideInChunks) {
               this.updateChunkZ = 0;
               this.updateChunkX = (this.updateChunkX + 1) % this.loadingSideInChunks;
            }
         }
      }

      int passed = (int)(System.nanoTime() - processStart);
      return outsideRender && !wasProperWrite;
   }

   private boolean writeTile(
      MinimapProcessor minimapProcessor,
      double playerX,
      double playerY,
      double playerZ,
      class_1937 world,
      MinimapChunk mchunk,
      MinimapChunk oldChunk,
      MinimapChunk topChunk,
      MinimapChunk topLeftChunk,
      MinimapChunk leftChunk,
      int canvasX,
      int canvasZ,
      int insideX,
      int insideZ,
      boolean onlyLoad,
      boolean outsideRender
   ) {
      int tileX = mchunk.getX() * 4 + insideX;
      int tileZ = mchunk.getZ() * 4 + insideZ;
      int halfSide = this.loadingSideInChunks / 2;
      int tileFromCenterX = canvasX - halfSide;
      int tileFromCenterZ = canvasZ - halfSide;
      MinimapTile oldTile = null;
      if (oldChunk != null) {
         oldTile = oldChunk.getTile(insideX, insideZ);
      }

      class_2818 bchunk = (class_2818)world.method_8402(tileX, tileZ, class_2806.field_12803, false);
      boolean neighborsLoaded = true;

      for (int i = -1; i < 2; i++) {
         for (int j = -1; j < 2; j++) {
            if (i != 0 || j != 0) {
               class_2791 nChunk = world.method_8497(tileX + i, tileZ + j);
               if (nChunk == null || nChunk instanceof class_2812) {
                  neighborsLoaded = false;
                  break;
               }
            }
         }
      }

      boolean chunkUpdated = false;
      boolean chunkIsClean = true;

      try {
         chunkIsClean = bchunk == null || (Boolean)XaeroMinimapCore.chunkCleanField.get(bchunk);
      } catch (IllegalAccessException | IllegalArgumentException var95) {
         throw new RuntimeException(var95);
      }

      chunkUpdated = !chunkIsClean;
      if (chunkIsClean && bchunk != null && this.detectedChunkChanges.contains(Misc.getChunkPosAsLong(bchunk))) {
         chunkUpdated = true;
      }

      chunkUpdated = chunkUpdated || oldTile == null || oldTile.caveLevel != this.loadingCaving;
      boolean effectivelyUnloaded = bchunk == null || bchunk instanceof class_2812 || !neighborsLoaded;
      int mRegionX = mchunk.getX() >> 3;
      int mRegionZ = mchunk.getZ() >> 3;
      int mChunkInsideX = mchunk.getX() & 7;
      int mChunkInsideZ = mchunk.getZ() & 7;
      boolean needsHighlights = this.dimensionHighlightHandler
         .shouldApplyTileChunkHighlights(mRegionX, mRegionZ, mChunkInsideX, mChunkInsideZ, !effectivelyUnloaded);
      if ((needsHighlights && oldTile == null || !outsideRender && !effectivelyUnloaded)
         && (
            chunkUpdated
                  && !onlyLoad
                  && tileFromCenterX <= this.updateRadius
                  && tileFromCenterZ <= this.updateRadius
                  && tileFromCenterX >= -this.updateRadius
                  && tileFromCenterZ >= -this.updateRadius
               || oldTile == null
               || !oldTile.isSuccess()
               || oldChunk.getLevelsBuffered() != this.loadingLevels
               || this.settingsChanged
         )) {
         if (oldTile != null && oldChunk.getLevelsBuffered() != this.loadingLevels) {
            oldTile = null;
         }

         MinimapTile tile = null;
         class_2903 typeWorldSurface = class_2903.field_13202;
         MinimapClientWorldData worldData = MinimapClientWorldDataHelper.getWorldData((class_638)world);
         float shadowR = worldData.shadowR;
         float shadowG = worldData.shadowG;
         float shadowB = worldData.shadowB;
         int playerYi = (int)playerY;
         boolean sameCaveLevel = oldTile != null && this.loadingCaving == oldTile.caveLevel;
         boolean sameHighlights = oldTile != null
            && this.loadedDimension == this.loadingDimension
            && this.loadingHighlightVersion == oldTile.getHighlightVersion();
         boolean settingsChanged = this.settingsChanged;
         int loadingCaving = this.loadingCaving;
         int loadingLevels = this.loadingLevels;
         boolean loadingLighting = this.loadingLighting;
         float loadingSingleLevelBrightness = this.loadingSingleLevelBrightness;
         int loadingTerrainSlopes = this.loadingTerrainSlopes;
         boolean loadingTerrainDepth = this.loadingTerrainDepth;
         List<Integer> pixelTransparentSizes = this.pixelTransparentSizes;
         List<class_2680> pixelBlockStates = this.pixelBlockStates;
         List<Integer> pixelBlockLights = this.pixelBlockLights;
         int[] underRed = this.underRed;
         int[] underGreen = this.underGreen;
         int[] underBlue = this.underBlue;
         float[] postBrightness = this.postBrightness;
         float[] brightness = this.brightness;
         int[] red = this.red;
         int[] green = this.green;
         int[] blue = this.blue;
         int[] tempColor = this.tempColor;
         boolean loadingIgnoreHeightmaps = this.loadingIgnoreHeightmaps;
         int loadingCaveMapsDepth = this.loadingCaveMapsDepth;
         class_2339 mutableBlockPos = this.mutableBlockPos;
         class_2339 mutableBlockPos2 = this.mutableBlockPos2;
         class_2339 mutableBlockPos3 = this.mutableBlockPos3;
         Long loadingSlimeSeed = this.loadingSlimeSeed;
         int loadedLevels = this.loadedLevels;
         AXaeroMinimap modMain = this.modMain;
         MinimapWriterHelper helper = this.helper;
         int loadingColours = this.loadingColours;
         boolean loadingRedstone = this.loadingRedstone;
         boolean loadingTransparency = this.loadingTransparency;
         int loadingLightOverlayType = this.loadingLightOverlayType;
         int loadingLightOverlayMaxLight = this.loadingLightOverlayMaxLight;
         int loadingLightOverlayMinLight = this.loadingLightOverlayMinLight;
         int loadingLightOverlayColor = this.loadingLightOverlayColor;
         boolean loadingFlowers = this.loadingFlowers;
         boolean adjustHeightForCarpetLikeBlocks = this.loadingAdjustHeightForCarpetLikeBlocks;
         boolean loadingStainedGlass = this.loadingStainedGlass;
         boolean loadingLegibleCaveMode = this.loadingLegibleCaveMode;
         boolean framedBlocksExist = modMain.getSupportMods().framedBlocks();
         int[] highlights = oldTile != null && needsHighlights ? oldTile.getHighlights() : null;
         if (needsHighlights && (highlights == null || !sameHighlights)) {
            highlights = this.dimensionHighlightHandler.applyChunkHighlightColors(tileX, tileZ);
         }

         if (effectivelyUnloaded) {
            tile = MinimapTile.getANewTile(modMain.getSettings(), tileX, tileZ, loadingSlimeSeed);

            for (int l = 0; l < loadingLevels; l++) {
               float overlayBrightness = this.getBlockBrightness(
                  9.0F, loadingCaving == Integer.MAX_VALUE || !loadingLighting && !loadingLegibleCaveMode ? 15 : 0, l, 0
               );

               for (int blockX = 0; blockX < 16; blockX++) {
                  for (int blockZ = 0; blockZ < 16; blockZ++) {
                     int highlight = highlights[blockZ << 4 | blockX];
                     int hlRed = highlight >> 8 & 0xFF;
                     int hlGreen = highlight >> 16 & 0xFF;
                     int hlBlue = highlight >> 24 & 0xFF;
                     tile.setRGB(
                        l,
                        blockX,
                        blockZ,
                        (int)((float)hlRed * overlayBrightness),
                        (int)((float)hlGreen * overlayBrightness),
                        (int)((float)hlBlue * overlayBrightness)
                     );
                     tile.setCode(blockX, blockZ, 0L, (byte)0, (byte)0, (byte)0, (byte)0);
                  }
               }
            }

            mchunk.setTile(this.tileInsideX, this.tileInsideZ, tile);
            tile.setSuccess(false);
            tile.setHasSomething(true);
            mchunk.setHasSomething(true);
            mchunk.setChanged(true);
         } else {
            int sectionBasedHeight = loadingIgnoreHeightmaps ? this.getSectionBasedHeight(bchunk, 64) : 0;

            for (int blockX = 0; blockX < 16; blockX++) {
               for (int blockZ = 0; blockZ < 16; blockZ++) {
                  tile = this.loadBlockColor(
                     playerYi,
                     world,
                     blockX,
                     blockZ,
                     bchunk,
                     tileX,
                     tileZ,
                     insideX,
                     insideZ,
                     sectionBasedHeight,
                     typeWorldSurface,
                     oldTile,
                     mchunk,
                     topChunk,
                     topLeftChunk,
                     leftChunk,
                     shadowR,
                     shadowG,
                     shadowB,
                     sameCaveLevel,
                     sameHighlights,
                     canvasX,
                     canvasZ,
                     !needsHighlights ? 0 : highlights[blockZ << 4 | blockX],
                     settingsChanged,
                     loadingCaving,
                     loadingLevels,
                     loadingLighting,
                     loadingSingleLevelBrightness,
                     loadingTerrainSlopes,
                     loadingTerrainDepth,
                     pixelTransparentSizes,
                     pixelBlockStates,
                     pixelBlockLights,
                     underRed,
                     underGreen,
                     underBlue,
                     postBrightness,
                     brightness,
                     red,
                     green,
                     blue,
                     tempColor,
                     loadingIgnoreHeightmaps,
                     loadingCaveMapsDepth,
                     mutableBlockPos,
                     mutableBlockPos2,
                     loadingSlimeSeed,
                     loadedLevels,
                     modMain,
                     helper,
                     loadingColours,
                     loadingRedstone,
                     loadingTransparency,
                     loadingLightOverlayType,
                     loadingLightOverlayMaxLight,
                     loadingLightOverlayMinLight,
                     loadingLightOverlayColor,
                     loadingFlowers,
                     adjustHeightForCarpetLikeBlocks,
                     loadingStainedGlass,
                     loadingLegibleCaveMode,
                     mutableBlockPos3,
                     framedBlocksExist
                  );
               }
            }

            tile.setHasTerrain(true);
         }

         tile.caveLevel = loadingCaving;
         tile.setHighlights(highlights);
         tile.setHighlightVersion(this.loadingHighlightVersion);
         if (!chunkIsClean) {
            long chunkPosLong = Misc.getChunkPosAsLong(bchunk);
            if (!this.detectedChunkChanges.contains(chunkPosLong)) {
               this.detectedChunkChanges.add(chunkPosLong);
            }

            try {
               XaeroMinimapCore.chunkCleanField.set(bchunk, true);
            } catch (IllegalAccessException | IllegalArgumentException var94) {
               throw new RuntimeException(var94);
            }
         }

         return true;
      } else {
         if (oldTile != null && oldChunk.getLevelsBuffered() == this.loadingLevels && !this.settingsChanged) {
            mchunk.setTile(insideX, insideZ, oldTile);
            oldTile.setWasTransfered(true);
            if (oldTile.isHasSomething()) {
               mchunk.setHasSomething(true);
            }

            if (this.forcedRefresh) {
               mchunk.setChanged(true);
            }
         }

         return false;
      }
   }

   public MinimapTile loadBlockColor(
      int playerYi,
      class_1937 world,
      int insideX,
      int insideZ,
      class_2818 bchunk,
      int tileX,
      int tileZ,
      int tileInsideX,
      int tileInsideZ,
      int sectionBasedHeight,
      class_2903 typeWorldSurface,
      MinimapTile oldTile,
      MinimapChunk mchunk,
      MinimapChunk topChunk,
      MinimapChunk topLeftChunk,
      MinimapChunk leftChunk,
      float shadowR,
      float shadowG,
      float shadowB,
      boolean sameCaveLevel,
      boolean sameHighlights,
      int canvasX,
      int canvasZ,
      int highlight,
      boolean settingsChanged,
      int loadingCaving,
      int loadingLevels,
      boolean loadingLighting,
      float loadingSingleLevelBrightness,
      int loadingTerrainSlopes,
      boolean loadingTerrainDepth,
      List<Integer> pixelTransparentSizes,
      List<class_2680> pixelBlockStates,
      List<Integer> pixelBlockLights,
      int[] underRed,
      int[] underGreen,
      int[] underBlue,
      float[] postBrightness,
      float[] brightness,
      int[] red,
      int[] green,
      int[] blue,
      int[] tempColor,
      boolean loadingIgnoreHeightmaps,
      int loadingCaveMapsDepth,
      class_2339 mutableBlockPos,
      class_2339 mutableBlockPos2,
      Long loadingSlimeSeed,
      int loadedLevels,
      AXaeroMinimap modMain,
      MinimapWriterHelper helper,
      int loadingColours,
      boolean loadingRedstone,
      boolean loadingTransparency,
      int loadingLightOverlayType,
      int loadingLightOverlayMaxLight,
      int loadingLightOverlayMinLight,
      int loadingLightOverlayColor,
      boolean loadingFlowers,
      boolean adjustHeightForCarpetLikeBlocks,
      boolean loadingStainedGlass,
      boolean loadingLegibleCaveMode,
      class_2339 mutableBlockPos3,
      boolean framedBlocksExist
   ) {
      int worldBottomY = world.method_31607();
      int highY;
      if (loadingCaving != Integer.MAX_VALUE) {
         highY = loadingCaving;
      } else {
         int height = bchunk.method_12005(typeWorldSurface, insideX, insideZ);
         if (!loadingIgnoreHeightmaps && height >= worldBottomY) {
            highY = height;
         } else {
            highY = sectionBasedHeight;
         }
      }

      if (highY >= world.method_31600()) {
         highY = world.method_31600() - 1;
      }

      int bottom = loadingCaving != Integer.MAX_VALUE ? highY + 1 - loadingCaveMapsDepth : worldBottomY;
      int lowY = bottom;
      if (bottom < worldBottomY) {
         lowY = worldBottomY;
      }

      pixelTransparentSizes.clear();
      pixelBlockStates.clear();
      pixelBlockLights.clear();
      this.currentComparisonCode = 0L;
      byte currentComparisonCodeAdd = 0;
      byte currentComparisonCodeAdd2 = 0;
      this.blockY = 0;

      for (int i = 0; i < loadingLevels; i++) {
         underRed[i] = 0;
         underGreen[i] = 0;
         underBlue[i] = 0;
      }

      this.currentTransparencyMultiplier = 1.0F;
      this.sun = 15;
      this.blockColor = 0;
      this.isglowing = false;
      double secondaryBR = 1.0;
      double secondaryBG = 1.0;
      double secondaryBB = 1.0;
      class_2248 block = this.findBlock(
         world,
         bchunk,
         insideX,
         insideZ,
         highY,
         lowY,
         loadingCaving,
         loadingRedstone,
         mutableBlockPos,
         mutableBlockPos2,
         loadingColours,
         loadingTransparency,
         pixelBlockLights,
         pixelBlockStates,
         loadingLevels,
         loadingLighting,
         pixelTransparentSizes,
         loadingFlowers,
         loadingStainedGlass,
         mutableBlockPos3,
         framedBlocksExist
      );
      class_2680 state = pixelBlockStates.isEmpty() ? null : pixelBlockStates.get(pixelBlockStates.size() - 1);
      if (adjustHeightForCarpetLikeBlocks && state != null && this.blockStateShortShapeCache.isShort(state)) {
         this.blockY--;
      }

      boolean isglowing = this.isglowing;
      int blockY = this.blockY;
      long currentComparisonCode = this.currentComparisonCode;
      boolean success = true;
      int prevHeight = Integer.MAX_VALUE;
      int prevHeightDiagonal = Integer.MAX_VALUE;
      int prevInsideX = insideX - 1;
      int prevInsideZ = insideZ - 1;
      boolean xEdge = prevInsideX < 0;
      boolean zEdge = prevInsideZ < 0;
      MinimapTile tile = mchunk.getTile(tileInsideX, tileInsideZ);
      MinimapTile prevHeightSrc = tile;
      MinimapTile prevHeightDiagonalSrc = tile;
      if (zEdge) {
         prevInsideZ = 15;
         if (tileInsideZ > 0) {
            prevHeightSrc = mchunk.getTile(tileInsideX, tileInsideZ - 1);
         } else if (topChunk != null) {
            prevHeightSrc = topChunk.getTile(tileInsideX, 3);
         }
      }

      if (xEdge) {
         prevInsideX = 15;
         if (zEdge) {
            if (tileInsideZ > 0 && tileInsideX > 0) {
               prevHeightDiagonalSrc = mchunk.getTile(tileInsideX - 1, tileInsideZ - 1);
            } else if (tileInsideX == 0 && tileInsideZ == 0) {
               if (topLeftChunk != null) {
                  prevHeightDiagonalSrc = topLeftChunk.getTile(3, 3);
               }
            } else if (tileInsideX == 0) {
               if (leftChunk != null) {
                  prevHeightDiagonalSrc = leftChunk.getTile(3, tileInsideZ - 1);
               }
            } else if (topChunk != null) {
               prevHeightDiagonalSrc = topChunk.getTile(tileInsideX - 1, 3);
            }
         } else if (tileInsideX > 0) {
            prevHeightDiagonalSrc = mchunk.getTile(tileInsideX - 1, tileInsideZ);
         } else if (leftChunk != null) {
            prevHeightDiagonalSrc = leftChunk.getTile(3, tileInsideZ);
         }
      } else {
         prevHeightDiagonalSrc = prevHeightSrc;
      }

      if (prevHeightSrc != null && (prevHeightSrc == tile || prevHeightSrc.hasTerrain())) {
         prevHeight = prevHeightSrc.getHeight(insideX, prevInsideZ);
         if (prevHeightSrc != tile && prevHeightSrc.caveLevel != loadingCaving) {
            success = false;
         }
      } else if (zEdge) {
         prevHeight = blockY;
         if (pixelTransparentSizes.isEmpty()) {
            try {
               class_2791 prevChunk = world.method_8497(tileX, tileZ - 1);
               if (prevChunk != null) {
                  prevHeight = prevChunk.method_12005(typeWorldSurface, insideX, prevInsideZ);
               }
            } catch (IllegalStateException var116) {
            }
         }

         success = false;
      }

      if (prevHeightDiagonalSrc == null || prevHeightDiagonalSrc != tile && !prevHeightDiagonalSrc.hasTerrain()) {
         if (xEdge || zEdge) {
            prevHeightDiagonal = blockY;
            if (pixelTransparentSizes.isEmpty()) {
               try {
                  class_2791 prevChunk = xEdge && zEdge
                     ? world.method_8497(tileX - 1, tileZ - 1)
                     : (zEdge ? world.method_8497(tileX, tileZ - 1) : world.method_8497(tileX - 1, tileZ));
                  if (prevChunk != null) {
                     prevHeightDiagonal = prevChunk.method_12005(typeWorldSurface, prevInsideX, prevInsideZ);
                  }
               } catch (IllegalStateException var117) {
               }
            }

            success = false;
         }
      } else {
         prevHeightDiagonal = prevHeightDiagonalSrc.getHeight(prevInsideX, prevInsideZ);
         if (prevHeightDiagonalSrc != tile && prevHeightDiagonalSrc.caveLevel != loadingCaving) {
            success = false;
         }
      }

      int verticalSlope = 0;
      int diagonalSlope = 0;
      if (loadingTerrainSlopes > 0) {
         if (prevHeight != Integer.MAX_VALUE) {
            verticalSlope = Math.max(-128, Math.min(127, blockY - prevHeight));
         }

         if (prevHeightDiagonal != Integer.MAX_VALUE) {
            diagonalSlope = Math.max(-128, Math.min(127, blockY - prevHeightDiagonal));
         }
      }

      for (int i = 0; i < pixelBlockLights.size(); i++) {
         int l = pixelBlockLights.get(i);
         if (i <= 1) {
            currentComparisonCodeAdd = (byte)(currentComparisonCodeAdd | l << 4 * i + 1);
         }

         if (i >= 1) {
            currentComparisonCode |= (long)(l << 4 * (i - 1) >> 3);
         }
      }

      int add2Calculation = 17;

      for (int i = 0; i < pixelTransparentSizes.size(); i++) {
         add2Calculation = add2Calculation * 37 + pixelTransparentSizes.get(i);
      }

      currentComparisonCodeAdd = (byte)(currentComparisonCodeAdd | add2Calculation >> 8 & 1);
      currentComparisonCodeAdd2 = (byte)add2Calculation;
      boolean reuseColour = !settingsChanged
         && sameCaveLevel
         && sameHighlights
         && !oldTile.pixelChanged(
            insideX, insideZ, currentComparisonCode, currentComparisonCodeAdd, currentComparisonCodeAdd2, (byte)verticalSlope, (byte)diagonalSlope
         );
      if (!reuseColour) {
         if (highlight != 0 && block == null) {
            this.sun = 0;
         }

         int firstSun = this.sun;
         boolean hasTransparentLayer = highlight != 0 || !pixelTransparentSizes.isEmpty();
         if (hasTransparentLayer && firstSun != 15) {
            this.sun = 15;
         }

         if (highlight != 0) {
            int hlRed = highlight >> 8 & 0xFF;
            int hlGreen = highlight >> 16 & 0xFF;
            int hlBlue = highlight >> 24 & 0xFF;
            int hlAlpha = highlight & 0xFF;
            float hlAlphaFloat = (float)hlAlpha / 255.0F;
            this.applyTransparentLayer(hlRed, hlGreen, hlBlue, hlAlphaFloat, true);
         }

         boolean legibleCaveMode = loadingLegibleCaveMode && loadingCaving != Integer.MAX_VALUE;
         this.calculateBlockColors(
            world,
            bchunk,
            insideX,
            insideZ,
            mutableBlockPos2,
            pixelTransparentSizes,
            pixelBlockStates,
            pixelBlockLights,
            loadingColours,
            loadingLightOverlayColor,
            loadingCaving,
            loadingLevels,
            loadingLighting,
            loadingSingleLevelBrightness,
            legibleCaveMode
         );
         int blockColor = this.blockColor;
         float currentTransparencyMultiplier = this.currentTransparencyMultiplier;
         int sun = this.sun;
         if (block == null) {
            sun = 15;
         }

         isglowing = block != null && isglowing;
         int topLight = pixelBlockLights.isEmpty() ? 0 : pixelBlockLights.get(0);
         int cr = blockColor >> 16 & 0xFF;
         int cg = blockColor >> 8 & 0xFF;
         int cb = blockColor & 0xFF;
         if (isglowing) {
            helper.getGlowingColour(cr, cg, cb, tempColor);
            cr = tempColor[0];
            cg = tempColor[1];
            cb = tempColor[2];
            if (hasTransparentLayer && pixelTransparentSizes.isEmpty()) {
               topLight = 15;
            }
         }

         if (!isglowing || hasTransparentLayer) {
            int blockLight = pixelBlockLights.isEmpty() ? 0 : pixelBlockLights.get(pixelBlockLights.size() - 1);
            int firstBlockY = this.firstBlockY;

            for (int i = 0; i < loadingLevels; i++) {
               postBrightness[i] = 1.0F;
               if (legibleCaveMode) {
                  if (!isglowing) {
                     brightness[i] = block == null ? 1.0F : (1.0F + (float)blockY - (float)bottom) / (float)(1 + highY - bottom);
                  }

                  if (hasTransparentLayer) {
                     float transparentLayerCaveBrightness = block == null && pixelTransparentSizes.isEmpty()
                        ? this.getFixedSkyLightBlockBrightness(9.0F, 0.0F, 0)
                        : (1.0F + (float)firstBlockY - (float)bottom) / (float)(1 + highY - bottom);
                     underRed[i] = (int)((float)underRed[i] * transparentLayerCaveBrightness);
                     underGreen[i] = (int)((float)underGreen[i] * transparentLayerCaveBrightness);
                     underBlue[i] = (int)((float)underBlue[i] * transparentLayerCaveBrightness);
                  }
               } else {
                  if (!isglowing) {
                     if (!hasTransparentLayer) {
                        if (block == null) {
                           brightness[i] = 1.0F;
                        } else {
                           brightness[i] = loadingLevels != 1
                              ? this.getBlockBrightness(9.0F, sun, i, blockLight)
                              : this.getFixedSkyLightBlockBrightness(9.0F, loadingSingleLevelBrightness, blockLight);
                        }
                     } else {
                        brightness[i] = this.getBlockBrightness(9.0F, sun, 0, blockLight);
                     }
                  }

                  if (hasTransparentLayer) {
                     postBrightness[i] = loadingLevels != 1
                        ? this.getBlockBrightness(9.0F, firstSun, i, topLight)
                        : this.getFixedSkyLightBlockBrightness(9.0F, loadingSingleLevelBrightness, topLight);
                  }
               }
            }
         }

         float depthBrightness = 1.0F;
         if (block != null && !isglowing && loadingTerrainDepth && !legibleCaveMode) {
            if (loadingCaving != Integer.MAX_VALUE) {
               depthBrightness = 0.7F + 0.3F * (float)(blockY - bottom) / (float)(highY - bottom);
            } else {
               depthBrightness = (float)blockY / 63.0F;
            }

            float max = loadingTerrainSlopes >= 2 ? 1.0F : 1.15F;
            float min = loadingTerrainSlopes >= 2 ? 0.9F : 0.7F;
            if (depthBrightness > max) {
               depthBrightness = max;
            } else if (depthBrightness < min) {
               depthBrightness = min;
            }
         }

         if (block != null && loadingTerrainSlopes > 0) {
            if (loadingTerrainSlopes == 1) {
               if (!isglowing) {
                  if (verticalSlope > 0) {
                     depthBrightness = (float)((double)depthBrightness * 1.15);
                  } else if (verticalSlope < 0) {
                     depthBrightness = (float)((double)depthBrightness * 0.85);
                  }
               }
            } else {
               float ambientLightColored = 0.2F;
               float ambientLightWhite = 0.5F;
               float maxDirectLight = 0.6666667F;
               if (isglowing) {
                  ambientLightColored = 0.0F;
                  ambientLightWhite = 1.0F;
                  maxDirectLight = 0.22222224F;
               }

               float cos = 0.0F;
               if (loadingTerrainSlopes == 2) {
                  float crossZ = (float)(-verticalSlope);
                  if (crossZ < 1.0F) {
                     if (verticalSlope == 1 && diagonalSlope == 1) {
                        cos = 1.0F;
                     } else {
                        float crossX = (float)(verticalSlope - diagonalSlope);
                        float cast = 1.0F - crossZ;
                        float crossMagnitude = (float)Math.sqrt((double)(crossX * crossX + 1.0F + crossZ * crossZ));
                        cos = (float)((double)(cast / crossMagnitude) / Math.sqrt(2.0));
                     }
                  }
               } else if (verticalSlope >= 0) {
                  if (verticalSlope == 1) {
                     cos = 1.0F;
                  } else {
                     float surfaceDirectionMagnitude = (float)Math.sqrt((double)(verticalSlope * verticalSlope + 1));
                     float castToMostLit = (float)(verticalSlope + 1);
                     cos = (float)((double)(castToMostLit / surfaceDirectionMagnitude) / Math.sqrt(2.0));
                  }
               }

               float directLightClamped = 0.0F;
               if (cos == 1.0F) {
                  directLightClamped = maxDirectLight;
               } else if (cos > 0.0F) {
                  directLightClamped = (float)Math.ceil((double)(cos * 10.0F)) / 10.0F * maxDirectLight * 0.88388F;
               }

               float whiteLight = ambientLightWhite + directLightClamped;
               secondaryBR *= (double)(shadowR * ambientLightColored + whiteLight);
               secondaryBG *= (double)(shadowG * ambientLightColored + whiteLight);
               secondaryBB *= (double)(shadowB * ambientLightColored + whiteLight);
            }
         }

         secondaryBR *= (double)depthBrightness;
         secondaryBG *= (double)depthBrightness;
         secondaryBB *= (double)depthBrightness;
         if (loadingLightOverlayType > 0) {
            int blockLight = pixelBlockLights.isEmpty() ? 0 : pixelBlockLights.get(0);
            int testLight = loadingLightOverlayType == 1 ? blockLight : (loadingLightOverlayType == 2 ? firstSun : Math.max(blockLight, firstSun));
            if (testLight >= loadingLightOverlayMinLight && testLight <= loadingLightOverlayMaxLight) {
               int overlayColor = ModSettings.COLORS[loadingLightOverlayColor];
               int overlayRed = (overlayColor >> 16 & 0xFF) * 2 / 3;
               int overlayGreen = (overlayColor >> 8 & 0xFF) * 2 / 3;
               int overlayBlue = (overlayColor & 0xFF) * 2 / 3;

               for (int ix = 0; ix < loadingLevels; ix++) {
                  float destColorScale = (isglowing ? 1.0F : postBrightness[ix]) / 3.0F;
                  underRed[ix] = (int)((float)underRed[ix] * destColorScale);
                  underGreen[ix] = (int)((float)underGreen[ix] * destColorScale);
                  underBlue[ix] = (int)((float)underBlue[ix] * destColorScale);
                  brightness[ix] *= destColorScale;
                  postBrightness[ix] = 1.0F;
                  underRed[ix] += overlayRed;
                  underGreen[ix] += overlayGreen;
                  underBlue[ix] += overlayBlue;
               }

               if (isglowing) {
                  secondaryBR /= 3.0;
                  secondaryBG /= 3.0;
                  secondaryBB /= 3.0;
               }
            }
         }

         for (int ix = 0; ix < loadingLevels; ix++) {
            float b;
            if (isglowing) {
               b = 1.0F;
               if (!hasTransparentLayer) {
                  postBrightness[ix] = 1.0F;
               }
            } else {
               b = brightness[ix];
            }

            red[ix] = (int)(((double)((float)cr * b) * secondaryBR * (double)currentTransparencyMultiplier + (double)underRed[ix]) * (double)postBrightness[ix]);
            if (red[ix] > 255) {
               red[ix] = 255;
            }

            green[ix] = (int)(
               ((double)((float)cg * b) * secondaryBG * (double)currentTransparencyMultiplier + (double)underGreen[ix]) * (double)postBrightness[ix]
            );
            if (green[ix] > 255) {
               green[ix] = 255;
            }

            blue[ix] = (int)(
               ((double)((float)cb * b) * secondaryBB * (double)currentTransparencyMultiplier + (double)underBlue[ix]) * (double)postBrightness[ix]
            );
            if (blue[ix] > 255) {
               blue[ix] = 255;
            }
         }
      } else {
         for (int ix = 0; ix < loadingLevels; ix++) {
            red[ix] = oldTile.getRed(ix, insideX, insideZ);
            green[ix] = oldTile.getGreen(ix, insideX, insideZ);
            blue[ix] = oldTile.getBlue(ix, insideX, insideZ);
         }
      }

      if (tile == null) {
         tile = MinimapTile.getANewTile(modMain.getSettings(), tileX, tileZ, loadingSlimeSeed);
         mchunk.setTile(tileInsideX, tileInsideZ, tile);
      }

      if (this.notEmptyColor(red, green, blue)) {
         tile.setHasSomething(true);
         mchunk.setHasSomething(true);
      }

      tile.setHeight(insideX, insideZ, blockY);
      tile.setCode(insideX, insideZ, currentComparisonCode, currentComparisonCodeAdd, currentComparisonCodeAdd2, (byte)verticalSlope, (byte)diagonalSlope);
      if (tile.isSuccess()) {
         tile.setSuccess(success);
      }

      if (oldTile != null) {
         int oldTileDarkestLevel = loadedLevels - 1;
         int tileDarkestLevel = loadingLevels - 1;
         if (oldTile.getRed(oldTileDarkestLevel, insideX, insideZ) != red[tileDarkestLevel]
            || oldTile.getGreen(oldTileDarkestLevel, insideX, insideZ) != green[tileDarkestLevel]
            || oldTile.getBlue(oldTileDarkestLevel, insideX, insideZ) != blue[tileDarkestLevel]) {
            mchunk.setChanged(true);
         }
      } else {
         mchunk.setChanged(true);
      }

      for (int ix = 0; ix < loadingLevels; ix++) {
         tile.setRGB(ix, insideX, insideZ, red[ix], green[ix], blue[ix]);
      }

      return tile;
   }

   private class_2680 unpackFramedBlocks(class_2680 original, class_1937 world, class_2338 globalPos) {
      if (original.method_26204() instanceof class_2189) {
         return original;
      } else {
         class_2680 result = original;
         SupportMods supportMods = this.modMain.getSupportMods();
         if (supportMods.supportFramedBlocks.isFrameBlock(world, null, original)) {
            class_2586 tileEntity = world.method_8321(globalPos);
            if (tileEntity != null) {
               result = supportMods.supportFramedBlocks.unpackFramedBlock(world, null, original, tileEntity);
               if (result == null || result.method_26204() instanceof class_2189) {
                  result = original;
               }
            }
         }

         return result;
      }
   }

   public class_2248 findBlock(
      class_1937 world,
      class_2818 bchunk,
      int insideX,
      int insideZ,
      int highY,
      int lowY,
      int loadingCaving,
      boolean loadingRedstone,
      class_2339 mutableBlockPos,
      class_2339 mutableBlockPos2,
      int loadingColours,
      boolean loadingTransparency,
      List<Integer> pixelBlockLights,
      List<class_2680> pixelBlockStates,
      int loadingLevels,
      boolean loadingLighting,
      List<Integer> pixelTransparentSizes,
      boolean loadingFlowers,
      boolean loadingStainedGlass,
      class_2339 mutableBlockPos3,
      boolean framedBlocksExist
   ) {
      this.underair = loadingCaving == Integer.MAX_VALUE;
      this.previousTransparentState = null;
      if (highY != Integer.MAX_VALUE && lowY != Integer.MAX_VALUE) {
         boolean shouldExtendTillTheBottom = false;
         int transparentSkipY = 0;

         for (int i = highY; i >= lowY; i = shouldExtendTillTheBottom ? transparentSkipY : i - 1) {
            mutableBlockPos.method_10103(insideX, i, insideZ);
            class_2338 globalPos = mutableBlockPos2.method_10103(
               (bchunk.method_12004().field_9181 << 4) + insideX, i, (bchunk.method_12004().field_9180 << 4) + insideZ
            );
            class_2680 state = bchunk.method_8320(mutableBlockPos);
            if (state == null) {
               state = class_2246.field_10124.method_9564();
            }

            if (framedBlocksExist) {
               state = this.unpackFramedBlocks(state, world, globalPos);
            }

            class_3610 fluidFluidState = state.method_26227();
            shouldExtendTillTheBottom = !shouldExtendTillTheBottom && !pixelBlockStates.isEmpty() && this.firstTransparentStateY - i >= 5;
            if (shouldExtendTillTheBottom) {
               for (transparentSkipY = i - 1; transparentSkipY >= lowY; transparentSkipY--) {
                  class_2680 traceState = bchunk.method_8320(mutableBlockPos3.method_10103(insideX, transparentSkipY, insideZ));
                  if (traceState == null) {
                     traceState = class_2246.field_10124.method_9564();
                  }

                  class_3610 traceFluidState = traceState.method_26227();
                  if (!traceFluidState.method_15769()) {
                     if (!this.isTransparent(traceFluidState)) {
                        break;
                     }

                     if (!(traceState.method_26204() instanceof class_2189)
                        && traceState.method_26204() == this.fluidToBlock.apply(traceFluidState).method_26204()) {
                        continue;
                     }
                  }

                  if (!this.isTransparent(traceState)) {
                     break;
                  }
               }
            }

            if (!fluidFluidState.method_15769()) {
               this.underair = true;
               class_2680 fluidState = this.fluidToBlock.apply(fluidFluidState);
               if (this.findBlockHelp(
                  world,
                  bchunk,
                  insideX,
                  i,
                  insideZ,
                  fluidState,
                  fluidFluidState,
                  transparentSkipY,
                  shouldExtendTillTheBottom,
                  loadingCaving,
                  loadingRedstone,
                  mutableBlockPos,
                  mutableBlockPos2,
                  loadingColours,
                  loadingTransparency,
                  pixelBlockLights,
                  pixelBlockStates,
                  loadingLevels,
                  loadingLighting,
                  pixelTransparentSizes,
                  loadingFlowers,
                  loadingStainedGlass
               )) {
                  return fluidState.method_26204();
               }

               if (!(state.method_26204() instanceof class_2189) && state.method_26204() == this.fluidToBlock.apply(fluidFluidState).method_26204()) {
                  continue;
               }
            }

            if ((state.method_26204() instanceof class_2189 || this.underair)
               && this.findBlockHelp(
                  world,
                  bchunk,
                  insideX,
                  i,
                  insideZ,
                  state,
                  null,
                  transparentSkipY,
                  shouldExtendTillTheBottom,
                  loadingCaving,
                  loadingRedstone,
                  mutableBlockPos,
                  mutableBlockPos2,
                  loadingColours,
                  loadingTransparency,
                  pixelBlockLights,
                  pixelBlockStates,
                  loadingLevels,
                  loadingLighting,
                  pixelTransparentSizes,
                  loadingFlowers,
                  loadingStainedGlass
               )) {
               return state.method_26204();
            }
         }

         return null;
      } else {
         return null;
      }
   }

   private boolean findBlockHelp(
      class_1937 world,
      class_2791 bchunk,
      int insideX,
      int i,
      int insideZ,
      class_2680 state,
      class_3610 fluidFluidState,
      int transparentSkipY,
      boolean shouldExtendTillTheBottom,
      int loadingCaving,
      boolean loadingRedstone,
      class_2339 mutableBlockPos,
      class_2339 mutableBlockPos2,
      int loadingColours,
      boolean loadingTransparency,
      List<Integer> pixelBlockLights,
      List<class_2680> pixelBlockStates,
      int loadingLevels,
      boolean loadingLighting,
      List<Integer> pixelTransparentSizes,
      boolean loadingFlowers,
      boolean loadingStainedGlass
   ) {
      class_2248 got = state.method_26204();
      if (!(got instanceof class_2189)) {
         boolean isRedstone = false;
         if (!(got instanceof class_2404) && state.method_26217() == class_2464.field_11455) {
            return false;
         } else if (got == class_2246.field_10336) {
            return false;
         } else if (got == class_2246.field_10479) {
            return false;
         } else if (got != class_2246.field_10033
            && got != class_2246.field_10285
            && (loadingStainedGlass || !(got instanceof class_2506) && !(got instanceof class_2504))) {
            boolean isFlower = got instanceof class_2521
               || got instanceof class_2356
               || got instanceof class_2261 && state.method_26164(class_3481.field_20339);
            if (got instanceof class_2320 && !isFlower) {
               return false;
            } else if (isFlower && !loadingFlowers) {
               return false;
            } else {
               isRedstone = got == class_2246.field_10523 || got == class_2246.field_10091 || got instanceof class_2462 || got instanceof class_2286;
               if (!loadingRedstone && isRedstone) {
                  return false;
               } else if (this.buggedStates.contains(state)) {
                  return false;
               } else {
                  this.blockY = i;
                  class_2338 globalPos = mutableBlockPos2;
                  class_3620 mapColor = null;

                  try {
                     mapColor = state.method_26205(world, globalPos);
                  } catch (Throwable var31) {
                     this.buggedStates.add(state);
                     MinimapLogs.LOGGER
                        .info(
                           "Broken vanilla map color definition found: "
                              + ((class_2378)world.method_30349().method_33310(class_7924.field_41254).get()).method_10221(got)
                        );
                  }

                  boolean isTransparent = loadingTransparency
                     && (state == this.previousTransparentState || this.isTransparent((class_2688<?, ?>)(fluidFluidState == null ? state : fluidFluidState)));
                  if ((isTransparent || isRedstone) && loadingColours != 1 || mapColor != null && mapColor.field_16011 != 0) {
                     if (!this.underair) {
                        return !isTransparent;
                     } else {
                        class_2338 lightPos = mutableBlockPos.method_10103(
                           mutableBlockPos2.method_10263(), mutableBlockPos2.method_10264() + 1, mutableBlockPos2.method_10260()
                        );
                        if (this.currentComparisonCode == 0L) {
                           this.firstBlockY = i;
                           if (loadingLighting && loadingCaving != Integer.MAX_VALUE) {
                              this.sun = world.method_8314(class_1944.field_9284, lightPos);
                           }
                        }

                        if (loadingTransparency && isTransparent) {
                           if (!shouldExtendTillTheBottom && pixelBlockStates.size() < 5 && state != this.previousTransparentState) {
                              if (pixelBlockStates.isEmpty()) {
                                 this.firstTransparentStateY = i;
                              }

                              this.currentComparisonCode = this.currentComparisonCode + ((long)class_2248.method_9507(state) & 4294967295L);
                              pixelBlockStates.add(state);
                              pixelTransparentSizes.add(1);
                              pixelBlockLights.add(!loadingLighting ? 0 : world.method_8314(class_1944.field_9282, lightPos));
                              this.previousTransparentState = state;
                           } else {
                              int depthToAdd = 1;
                              if (shouldExtendTillTheBottom) {
                                 depthToAdd = i - transparentSkipY;
                              }

                              pixelTransparentSizes.set(
                                 pixelTransparentSizes.size() - 1, pixelTransparentSizes.get(pixelTransparentSizes.size() - 1) + depthToAdd
                              );
                           }

                           return false;
                        } else {
                           this.currentComparisonCode = this.currentComparisonCode + ((long)class_2248.method_9507(state) & 4294967295L);
                           this.currentComparisonCode <<= 29;
                           this.currentComparisonCode |= ((long)i & 4095L) << 17;
                           pixelBlockLights.add(!loadingLighting ? 0 : world.method_8314(class_1944.field_9282, lightPos));
                           pixelBlockStates.add(state);
                           this.isglowing = this.isGlowing(state, world, mutableBlockPos2);
                           return true;
                        }
                     }
                  } else {
                     return false;
                  }
               }
            }
         } else {
            return false;
         }
      } else {
         if (got instanceof class_2189) {
            this.underair = true;
         }

         return false;
      }
   }

   private void calculateBlockColors(
      class_1937 world,
      class_2818 bchunk,
      int insideX,
      int insideZ,
      class_2339 mutableBlockPos2,
      List<Integer> pixelTransparentSizes,
      List<class_2680> pixelBlockStates,
      List<Integer> pixelBlockLights,
      int loadingColours,
      int loadingLightOverlayColor,
      int loadingCaving,
      int loadingLevels,
      boolean loadingLighting,
      float loadingSingleLevelBrightness,
      boolean legibleCaveMaps
   ) {
      int firstBlockY = this.firstBlockY;
      class_2339 globalPos = mutableBlockPos2.method_10103(
         bchunk.method_12004().field_9181 * 16 + insideX, firstBlockY, bchunk.method_12004().field_9180 * 16 + insideZ
      );
      if (!pixelTransparentSizes.isEmpty()) {
         for (int i = 0; i < pixelTransparentSizes.size(); i++) {
            class_2680 state = pixelBlockStates.get(i);
            class_2248 b = state.method_26204();
            int size = pixelTransparentSizes.get(i);
            int opacity = state.method_26193(bchunk.method_12200(), globalPos);
            this.applyTransparentLayer(
               world, bchunk, b, state, opacity * size, globalPos, pixelBlockLights.get(i), loadingLighting, loadingSingleLevelBrightness, legibleCaveMaps
            );
            int nextY = globalPos.method_10264() - size;
            globalPos.method_33098(nextY);
         }
      }

      if (pixelBlockStates.size() > pixelTransparentSizes.size()) {
         class_2680 state = pixelBlockStates.get(pixelBlockStates.size() - 1);
         class_2248 b = state.method_26204();
         int color;
         if (loadingColours == 1) {
            class_3620 minimapColor = state.method_26205(world, globalPos);
            color = minimapColor.field_16011;
         } else {
            color = this.loadBlockColourFromTexture(world, state, b, globalPos, true);
         }

         this.blockColor = this.addBlockColorMultipliers(color, state, world, globalPos);
      } else {
         this.blockColor = loadingCaving != Integer.MAX_VALUE ? 0 : -16121833;
      }
   }

   private boolean isTransparent(class_2688<?, ?> state) {
      return this.transparentCache.apply(state);
   }

   private boolean isGlowing(class_2680 state, class_1937 world, class_2338 pos) {
      return this.glowingCache.apply(state);
   }

   private void applyTransparentLayer(
      class_1937 world,
      class_2818 bchunk,
      class_2248 b,
      class_2680 state,
      int opacity,
      class_2338 globalPos,
      int blockLight,
      boolean lighting,
      float loadingSingleLevelBrightness,
      boolean legibleCaveMaps
   ) {
      int red = 0;
      int green = 0;
      int blue = 0;
      float vanillaTransparency = b instanceof class_2404 ? 0.75F : (b instanceof class_2386 ? 0.85F : 0.5F);
      int color;
      if (this.loadingColours == 0) {
         color = this.loadBlockColourFromTexture(world, state, b, globalPos, true);
      } else {
         color = state.method_26205(world, globalPos).field_16011;
         color = (int)(255.0F * vanillaTransparency) << 24 | color & 16777215;
      }

      color = this.addBlockColorMultipliers(color, state, world, globalPos);
      red = color >> 16 & 0xFF;
      green = color >> 8 & 0xFF;
      blue = color & 0xFF;
      float transparency = (float)(color >> 24 & 0xFF) / 255.0F;
      if (transparency == 0.0F) {
         transparency = vanillaTransparency;
      }

      if (this.isGlowing(state, bchunk.method_12200(), globalPos)) {
         this.helper.getGlowingColour(red, green, blue, this.tempColor);
         red = this.tempColor[0];
         green = this.tempColor[1];
         blue = this.tempColor[2];
      }

      float brightness = legibleCaveMaps
         ? 1.0F
         : (
            lighting
               ? this.getBlockBrightness(9.0F, this.sun, 0, blockLight)
               : this.getFixedSkyLightBlockBrightness(9.0F, loadingSingleLevelBrightness, blockLight)
         );
      this.applyTransparentLayer(red, green, blue, transparency * brightness, false);
      this.sun -= opacity;
      if (this.sun < 0) {
         this.sun = 0;
      }
   }

   private void applyTransparentLayer(int red, int green, int blue, float transparency, boolean premultiplied) {
      float overlayIntensity = this.currentTransparencyMultiplier * (premultiplied ? 1.0F : transparency);

      for (int i = 0; i < this.loadingLevels; i++) {
         this.underRed[i] = (int)((float)this.underRed[i] + (float)red * overlayIntensity);
         this.underGreen[i] = (int)((float)this.underGreen[i] + (float)green * overlayIntensity);
         this.underBlue[i] = (int)((float)this.underBlue[i] + (float)blue * overlayIntensity);
      }

      this.currentTransparencyMultiplier *= 1.0F - transparency;
   }

   private int loadBlockColourFromTexture(class_1937 world, class_2680 state, class_2248 b, class_2338 pos, boolean convert) {
      if (state == this.lastBlockStateForTextureColor) {
         return this.lastBlockStateForTextureColorResult;
      } else {
         int stateHash = class_2248.method_9507(state);
         Integer c = this.blockColours.get(stateHash);
         int red = 0;
         int green = 0;
         int blue = 0;
         int alpha = 0;
         if (c == null) {
            String name = null;

            try {
               List<class_777> upQuads = null;
               class_773 bms = class_310.method_1551().method_1541().method_3351();
               class_1087 model = bms.method_3335(state);
               if (convert) {
                  upQuads = model.method_4707(state, class_2350.field_11036, this.usedRandom);
               }

               class_1058 missingTexture = class_310.method_1551().method_1554().method_24153(class_1059.field_5275).method_4608(class_1047.method_4539());
               class_1058 texture;
               if (upQuads != null && !upQuads.isEmpty() && upQuads.get(0).method_35788() != missingTexture) {
                  texture = upQuads.get(0).method_35788();
               } else {
                  texture = bms.method_3339(state);
                  if (texture == missingTexture) {
                     for (int i = class_2350.values().length - 1; i >= 0; i--) {
                        if (i != 1) {
                           List<class_777> quads = model.method_4707(state, class_2350.values()[i], this.usedRandom);
                           if (!quads.isEmpty()) {
                              texture = quads.get(0).method_35788();
                              if (texture != missingTexture) {
                                 break;
                              }
                           }
                        }
                     }
                  }
               }

               if (texture == null) {
                  throw new SilentException("No texture for " + state);
               }

               name = texture.method_45851().method_45816() + ".png";
               c = -1;
               String[] args = name.split(":");
               if (args.length < 2) {
                  args = new String[]{"minecraft", args[0]};
               }

               Integer cachedColour = this.textureColours.get(name);
               if (cachedColour == null) {
                  class_2960 location = new class_2960(args[0], "textures/" + args[1]);
                  class_3298 resource = (class_3298)class_310.method_1551().method_1478().method_14486(location).orElse(null);
                  if (resource == null) {
                     throw new SilentException("No texture " + location);
                  }

                  InputStream input = resource.method_14482();
                  BufferedImage img = ImageIO.read(input);
                  red = 0;
                  green = 0;
                  blue = 0;
                  int total = 0;
                  int ts = Math.min(img.getWidth(), img.getHeight());
                  if (ts > 0) {
                     int diff = Math.max(1, Math.min(4, ts / 8));
                     int parts = ts / diff;
                     Raster raster = img.getData();
                     int[] colorHolder = null;

                     for (int ix = 0; ix < parts; ix++) {
                        for (int j = 0; j < parts; j++) {
                           int rgb;
                           if (img.getColorModel().getNumComponents() < 3) {
                              colorHolder = raster.getPixel(ix * diff, j * diff, colorHolder);
                              int sample = colorHolder[0] & 0xFF;
                              int a = 255;
                              if (colorHolder.length > 1) {
                                 a = colorHolder[1];
                              }

                              rgb = a << 24 | sample << 16 | sample << 8 | sample;
                           } else {
                              rgb = img.getRGB(ix * diff, j * diff);
                           }

                           int a = rgb >> 24 & 0xFF;
                           if (rgb != 0 && a != 0) {
                              red += rgb >> 16 & 0xFF;
                              green += rgb >> 8 & 0xFF;
                              blue += rgb & 0xFF;
                              alpha += a;
                              total++;
                           }
                        }
                     }
                  }

                  input.close();
                  if (total == 0) {
                     total = 1;
                  }

                  red /= total;
                  green /= total;
                  blue /= total;
                  alpha /= total;
                  if (convert && red == 0 && green == 0 && blue == 0) {
                     throw new SilentException("Black texture " + ts);
                  }

                  c = alpha << 24 | red << 16 | green << 8 | blue;
                  this.textureColours.put(name, c);
               } else {
                  c = cachedColour;
               }
            } catch (FileNotFoundException var35) {
               if (convert) {
                  return this.loadBlockColourFromTexture(world, state, b, pos, false);
               }

               MinimapLogs.LOGGER
                  .info("Block file not found: " + ((class_2378)world.method_30349().method_33310(class_7924.field_41254).get()).method_10221(b));
               c = 0;
               if (state != null && state.method_26205(world, pos) != null) {
                  c = state.method_26205(world, pos).field_16011;
               }

               if (name != null) {
                  this.textureColours.put(name, c);
               }
            } catch (Exception var36) {
               MinimapLogs.LOGGER
                  .info(
                     "Exception when loading "
                        + ((class_2378)world.method_30349().method_33310(class_7924.field_41254).get()).method_10221(b)
                        + " texture, using material colour."
                  );
               c = 0;
               if (state.method_26205(world, pos) != null) {
                  c = state.method_26205(world, pos).field_16011;
               }

               if (name != null) {
                  this.textureColours.put(name, c);
               }

               if (var36 instanceof SilentException) {
                  MinimapLogs.LOGGER.info(var36.getMessage());
               } else {
                  MinimapLogs.LOGGER.error("suppressed exception", var36);
               }
            }

            if (c != null) {
               this.blockColours.put(stateHash, c);
            }
         }

         this.lastBlockStateForTextureColor = state;
         this.lastBlockStateForTextureColorResult = c;
         return c;
      }
   }

   private int addBlockColorMultipliers(int c, class_2680 state, class_1937 world, class_2338 pos) {
      if (this.modMain.getSettings().getBlockColours() == 1 && !this.loadingBiomesVanillaMode) {
         return c;
      } else {
         int grassColor = 16777215;

         try {
            grassColor = class_310.method_1551().method_1505().method_1697(state, this.biomeBlendCalculator, pos, 0);
         } catch (Throwable var12) {
            MinimapLogs.LOGGER.error("suppressed exception", var12);
         }

         if (grassColor != -1 && grassColor != 16777215) {
            float rMultiplier = (float)(c >> 16 & 0xFF) / 255.0F;
            float gMultiplier = (float)(c >> 8 & 0xFF) / 255.0F;
            float bMultiplier = (float)(c & 0xFF) / 255.0F;
            int red = (int)((float)(grassColor >> 16 & 0xFF) * rMultiplier);
            int green = (int)((float)(grassColor >> 8 & 0xFF) * gMultiplier);
            int blue = (int)((float)(grassColor & 0xFF) * bMultiplier);
            c = c & 0xFF000000 | red << 16 | green << 8 | blue;
         }

         return c;
      }
   }

   private boolean ignoreWorld(class_1937 world) {
      for (int i = 0; i < dimensionsToIgnore.length; i++) {
         if (dimensionsToIgnore[i].equals(world.method_27983().method_29177().method_12832())) {
            return true;
         }
      }

      return false;
   }

   private int getCaving(boolean manualCaveMode, double playerX, double playerY, double playerZ, class_1937 world) {
      if (!this.modMain.getSettings().getCaveMaps(manualCaveMode)
         || this.mc.field_1724.method_6059(Effects.NO_CAVE_MAPS)
         || this.mc.field_1724.method_6059(Effects.NO_CAVE_MAPS_HARMFUL)) {
         return Integer.MAX_VALUE;
      } else if (this.ignoreWorld(world)) {
         return this.lastCaving;
      } else {
         if (manualCaveMode) {
            int customCaveStart = this.modMain.getSettings().getManualCaveModeStart();
            if (customCaveStart != Integer.MAX_VALUE) {
               return customCaveStart;
            }
         } else if (this.modMain.getSupportMods().worldmap() && this.modMain.getSupportMods().worldmapSupport.shouldPreventAutoCaveMode(world)) {
            return Integer.MAX_VALUE;
         }

         int worldBottomY = world.method_31607();
         int worldTopY = world.method_31600() - 1;
         int y = (int)playerY + 1;
         int defaultCaveStart = y + 3;
         int defaultResult = manualCaveMode ? defaultCaveStart : Integer.MAX_VALUE;
         if (y <= worldTopY && y >= worldBottomY) {
            int x = OptimizedMath.myFloor(playerX);
            int z = OptimizedMath.myFloor(playerZ);
            int roofRadius = this.modMain.getSettings().caveMaps - 1;
            int roofDiameter = 1 + roofRadius * 2;
            int startX = x - roofRadius;
            int startZ = z - roofRadius;
            boolean ignoringHeightmaps = this.modMain.getSettings().isIgnoreHeightmaps();
            int bottom = y;
            int top = Integer.MAX_VALUE;
            class_2791 prevBChunk = null;
            int potentialResult = defaultCaveStart;

            for (int o = 0; o < roofDiameter; o++) {
               label122:
               for (int p = 0; p < roofDiameter; p++) {
                  int currentX = startX + o;
                  int currentZ = startZ + p;
                  this.mutableBlockPos.method_10103(currentX, y, currentZ);
                  class_2791 bchunk = world.method_8497(currentX >> 4, currentZ >> 4);
                  if (bchunk == null) {
                     return defaultResult;
                  }

                  int skyLight = world.method_8314(class_1944.field_9284, this.mutableBlockPos);
                  if (!ignoringHeightmaps) {
                     if (skyLight >= 15) {
                        return defaultResult;
                     }

                     int insideX = currentX & 15;
                     int insideZ = currentZ & 15;
                     top = bchunk.method_12005(class_2903.field_13202, insideX, insideZ);
                  } else if (bchunk != prevBChunk) {
                     class_2826[] sections = bchunk.method_12006();
                     if (sections.length == 0) {
                        return defaultResult;
                     }

                     int playerSection = y - worldBottomY >> 4;
                     boolean foundSomething = false;

                     for (int i = playerSection; i < sections.length; i++) {
                        class_2826 searchedSection = sections[i];
                        if (!searchedSection.method_38292()) {
                           if (!foundSomething) {
                              bottom = Math.max(bottom, worldBottomY + (i << 4));
                              foundSomething = true;
                           }

                           top = worldBottomY + (i << 4) + 15;
                        }
                     }

                     if (!foundSomething) {
                        return defaultResult;
                     }

                     prevBChunk = bchunk;
                  }

                  if (top < worldBottomY) {
                     return defaultResult;
                  }

                  if (top > worldTopY) {
                     top = worldTopY;
                  }

                  for (int ix = bottom; ix <= top; ix++) {
                     this.mutableBlockPos.method_33098(ix);
                     class_2680 state = world.method_8320(this.mutableBlockPos);
                     if (!state.method_26215()
                        && state.method_26223() != class_3619.field_15971
                        && !(state.method_26204() instanceof class_2404)
                        && !state.method_26164(class_3481.field_15503)
                        && !this.isTransparent(state)
                        && state.method_26204() != class_2246.field_10499) {
                        if (o == p && o == roofRadius) {
                           potentialResult = Math.min(ix, defaultCaveStart);
                        }
                        continue label122;
                     }
                  }

                  return defaultResult;
               }
            }

            return this.lastCaving = potentialResult;
         } else {
            return defaultResult;
         }
      }
   }

   public int getSectionBasedHeight(class_2818 bchunk, int startY) {
      class_2826[] sections = bchunk.method_12006();
      if (sections.length == 0) {
         return 0;
      } else {
         int chunkBottomY = bchunk.method_31607();
         int playerSection = Math.min(startY - chunkBottomY >> 4, sections.length - 1);
         if (playerSection < 0) {
            playerSection = 0;
         }

         int result = 0;

         for (int i = playerSection; i < sections.length; i++) {
            class_2826 searchedSection = sections[i];
            if (!searchedSection.method_38292()) {
               result = chunkBottomY + (i << 4) + 15;
            }
         }

         if (playerSection > 0 && result == 0) {
            for (int ix = playerSection - 1; ix >= 0; ix--) {
               class_2826 searchedSection = sections[ix];
               if (!searchedSection.method_38292()) {
                  result = chunkBottomY + (ix << 4) + 15;
                  break;
               }
            }
         }

         return result;
      }
   }

   public int getLoadSide() {
      return 9;
   }

   public int getUpdateRadiusInChunks() {
      return (int)Math.ceil((double)this.loadingSideInChunks / 2.0 / this.minimapSession.getMinimapProcessor().getMinimapZoom());
   }

   public int getMapCoord(int side, int coord) {
      return (coord >> 6) - side / 2;
   }

   public int getLoadedCaving() {
      return this.loadedCaving;
   }

   private boolean notEmptyColor(int[] red, int[] green, int[] blue) {
      return red[0] != 0 || green[0] != 0 || blue[0] != 0;
   }

   public float getFixedSkyLightBlockBrightness(float min, float fixedSun, int blockLight) {
      return (min + Math.max(fixedSun * 15.0F, (float)blockLight)) / (15.0F + min);
   }

   public float getBlockBrightness(float min, int sun, int lightLevel, int blockLight) {
      return (
            min
               + Math.max(
                  (lightLevel != -1 && lightLevel != 0 ? ((float)this.loadingLevels - 1.0F - (float)lightLevel) / ((float)this.loadingLevels - 1.0F) : 1.0F)
                     * (float)sun,
                  (float)blockLight
               )
         )
         / (15.0F + min);
   }

   public int getLoadingMapChunkX() {
      return this.loadingMapChunkX;
   }

   public int getLoadingMapChunkZ() {
      return this.loadingMapChunkZ;
   }

   public int getLoadingSideInChunks() {
      return this.loadingSideInChunks;
   }

   public MinimapChunk[][] getLoadedBlocks() {
      return this.loadedBlocks;
   }

   public int getLoadedMapChunkZ() {
      return this.loadedMapChunkZ;
   }

   public int getLoadedMapChunkX() {
      return this.loadedMapChunkX;
   }

   public int getLoadedLevels() {
      return this.loadedLevels;
   }

   public void setClearBlockColours(boolean clearBlockColours) {
      this.clearBlockColours = clearBlockColours;
   }

   public void cleanup() {
      if (this.loadedBlocks != null) {
         for (int i = 0; i < this.loadedBlocks.length; i++) {
            for (int j = 0; j < this.loadedBlocks.length; j++) {
               MinimapChunk m = this.loadedBlocks[i][j];
               if (m != null) {
                  m.cleanup(this.minimapInterface);
               }
            }
         }
      }
   }

   public void resetShortBlocks() {
      this.blockStateShortShapeCache.reset();
   }

   public DimensionHighlighterHandler getDimensionHighlightHandler() {
      return this.dimensionHighlightHandler;
   }

   public int getLoadedSideInChunks() {
      return this.loadedSideInChunks;
   }

   public boolean isLoadedNonWorldMap() {
      return this.loadedNonWorldMap;
   }
}
