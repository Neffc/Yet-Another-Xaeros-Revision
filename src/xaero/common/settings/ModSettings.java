package xaero.common.settings;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import net.minecraft.class_1074;
import net.minecraft.class_1792;
import net.minecraft.class_1937;
import net.minecraft.class_2960;
import net.minecraft.class_304;
import net.minecraft.class_310;
import net.minecraft.class_3532;
import net.minecraft.class_437;
import net.minecraft.class_7923;
import net.minecraft.server.MinecraftServer;
import xaero.common.IXaeroMinimap;
import xaero.common.XaeroMinimapSession;
import xaero.common.category.setting.ObjectCategorySetting;
import xaero.common.gui.GuiSettings;
import xaero.common.gui.GuiSlimeSeed;
import xaero.common.minimap.MinimapProcessor;
import xaero.common.minimap.highlight.DimensionHighlighterHandler;
import xaero.common.minimap.mcworld.MinimapClientWorldDataHelper;
import xaero.common.minimap.radar.category.EntityRadarBackwardsCompatibilityConfig;
import xaero.common.minimap.radar.category.EntityRadarCategory;
import xaero.common.minimap.radar.category.setting.EntityRadarCategorySettings;
import xaero.common.minimap.waypoints.WaypointWorld;
import xaero.common.minimap.waypoints.WaypointsManager;
import xaero.hud.HudSession;
import xaero.hud.minimap.BuiltInHudModules;
import xaero.hud.minimap.MinimapLogs;
import xaero.hud.minimap.info.BuiltInInfoDisplays;
import xaero.hud.minimap.info.InfoDisplayIO;
import xaero.hud.minimap.module.MinimapSession;
import xaero.hud.minimap.world.container.MinimapWorldRootContainer;
import xaero.hud.path.XaeroPath;
import xaero.hud.path.XaeroPathReader;

public class ModSettings {
   public static int defaultSettings;
   public static int ignoreUpdate;
   public static final String format = "§";
   protected IXaeroMinimap modMain;
   private EntityRadarBackwardsCompatibilityConfig entityRadarBackwardsCompatibilityConfig;
   private boolean foundOldRadarSettings;
   public static final String[] ENCHANT_COLORS = new String[]{"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f"};
   public static final String[] ENCHANT_COLOR_NAMES = new String[]{
      "gui.xaero_black",
      "gui.xaero_dark_blue",
      "gui.xaero_dark_green",
      "gui.xaero_dark_aqua",
      "gui.xaero_dark_red",
      "gui.xaero_dark_purple",
      "gui.xaero_gold",
      "gui.xaero_gray",
      "gui.xaero_dark_gray",
      "gui.xaero_blue",
      "gui.xaero_green",
      "gui.xaero_aqua",
      "gui.xaero_red",
      "gui.xaero_purple",
      "gui.xaero_yellow",
      "gui.xaero_white"
   };
   public static final int[] COLORS = new int[]{
      -16777216, -16777046, -16733696, -16733526, -5636096, -5635926, -22016, -5592406, -11184811, -11184641, -11141291, -11141121, -65536, -43521, -171, -1
   };
   public static int serverSettings;
   public static class_304 keyBindZoom = new class_304("gui.xaero_zoom_in", -1, "Xaero's Minimap");
   public static class_304 keyBindZoom1 = new class_304("gui.xaero_zoom_out", -1, "Xaero's Minimap");
   public static class_304 newWaypoint = new class_304("gui.xaero_new_waypoint", 66, "Xaero's Minimap");
   public static class_304 keyWaypoints = new class_304("gui.xaero_waypoints_key", 85, "Xaero's Minimap");
   public static class_304 keyLargeMap = new class_304("gui.xaero_enlarge_map", 90, "Xaero's Minimap");
   public static class_304 keyToggleMap = new class_304("gui.xaero_toggle_map", -1, "Xaero's Minimap");
   public static class_304 keyToggleWaypoints = new class_304("gui.xaero_toggle_waypoints", -1, "Xaero's Minimap");
   public static class_304 keyToggleMapWaypoints = new class_304("gui.xaero_toggle_map_waypoints", -1, "Xaero's Minimap");
   public static class_304 keyToggleSlimes = new class_304("gui.xaero_toggle_slime", -1, "Xaero's Minimap");
   public static class_304 keyToggleGrid = new class_304("gui.xaero_toggle_grid", -1, "Xaero's Minimap");
   public static class_304 keyInstantWaypoint = new class_304("gui.xaero_instant_waypoint", 334, "Xaero's Minimap");
   public static class_304 keySwitchSet = new class_304("gui.xaero_switch_waypoint_set", -1, "Xaero's Minimap");
   public static class_304 keyAllSets = new class_304("gui.xaero_display_all_sets", -1, "Xaero's Minimap");
   public static class_304 keyLightOverlay = new class_304("gui.xaero_toggle_light_overlay", -1, "Xaero's Minimap");
   public static class_304 keyToggleRadar = new class_304("gui.xaero_toggle_entity_radar", -1, "Xaero's Minimap");
   public static class_304 keyReverseEntityRadar = new class_304("gui.xaero_reverse_entity_radar", -1, "Xaero's Minimap");
   public static class_304 keyManualCaveMode = new class_304("gui.xaero_toggle_manual_cave_mode", -1, "Xaero's Minimap");
   public static class_304 keyAlternativeListPlayers = new class_304("gui.xaero_alternative_list_players", -1, "Xaero's Minimap");
   public static class_304 keyToggleTrackedPlayersOnMap = new class_304("gui.xaero_toggle_tracked_players_on_map", -1, "Xaero's Minimap");
   public static class_304 keyToggleTrackedPlayersInWorld = new class_304("gui.xaero_toggle_tracked_players_in_world", -1, "Xaero's Minimap");
   @Deprecated
   public static class_304 keyToggleTrackedPlayers = keyToggleTrackedPlayersOnMap;
   @Deprecated
   public static class_304 keyTogglePacPlayers = keyToggleTrackedPlayersOnMap;
   public static class_304 keyTogglePacChunkClaims = new class_304("gui.xaero_toggle_pac_chunk_claims", -1, "Xaero's Minimap");
   public static String minimapItemId = null;
   public static class_1792 minimapItem = null;
   public int zoom = 0;
   public static final float[] zooms = new float[]{1.0F, 2.0F, 3.0F, 4.0F, 5.0F};
   public int caveMaps = 2;
   public int caveZoom = 1;
   private boolean showWaypoints = true;
   private boolean deathpoints = true;
   private boolean oldDeathpoints = true;
   public int chunkGrid = -1;
   public boolean slimeChunks = false;
   private static HashMap<XaeroPath, Long> serverSlimeSeeds = new HashMap<>();
   private boolean showIngameWaypoints = true;
   private boolean lockNorth = false;
   private boolean antiAliasing = true;
   private boolean displayRedstone = true;
   public boolean mapSafeMode = false;
   public int distance = 1;
   public static final String[] distanceTypes = new String[]{"gui.xaero_off", "gui.xaero_looking_at", "gui.xaero_all"};
   private int blockColours = 0;
   public static final String[] blockColourTypes = new String[]{"gui.xaero_accurate", "gui.xaero_vanilla"};
   private boolean lighting = true;
   public boolean compassOverEverything = true;
   private int minimapSize = 0;
   public double minimapOpacity = 100.0;
   public double waypointsIngameCloseScale = 1.0;
   private int waypointsIngameIconScale = 0;
   private int waypointsIngameDistanceScale = 0;
   private int waypointsIngameNameScale = 0;
   private static final float DEFAULT_SCALE = 0.8F;
   private static final float MINECRAFT_SCALE = 0.02666667F;
   private static final double WAYPOINT_ICON_WORLD_SCALE = 0.021333335F;
   private double dotNameScale = 1.0;
   public static boolean settingsButton = false;
   public static boolean updateNotification = true;
   private boolean showFlowers = true;
   public boolean keepWaypointNames = true;
   private int waypointsDistanceExp = 0;
   public double waypointsDistanceMin = 0.0;
   public String defaultWaypointTPCommandFormat = "/tp @s {x} {y} {z}";
   public String defaultWaypointTPCommandRotationFormat = "/tp @s {x} {y} {z} {yaw} ~";
   public double arrowScale = 1.5;
   public int arrowColour = 0;
   public String[] arrowColourNames = new String[]{
      "gui.xaero_red",
      "gui.xaero_green",
      "gui.xaero_blue",
      "gui.xaero_yellow",
      "gui.xaero_purple",
      "gui.xaero_white",
      "gui.xaero_black",
      "gui.xaero_legacy_color"
   };
   public static float[][] arrowColours = new float[][]{
      {0.8F, 0.1F, 0.1F, 1.0F},
      {0.09F, 0.57F, 0.0F, 1.0F},
      {0.0F, 0.55F, 1.0F, 1.0F},
      {1.0F, 0.93F, 0.0F, 1.0F},
      {0.73F, 0.33F, 0.83F, 1.0F},
      {1.0F, 1.0F, 1.0F, 1.0F},
      {0.0F, 0.0F, 0.0F, 1.0F},
      {0.4588F, 0.0F, 0.0F, 1.0F}
   };
   public boolean smoothDots = true;
   public static final String[] ENTITY_ICONS_OPTIONS = new String[]{"gui.xaero_icons_off", "gui.xaero_icons_list", "gui.xaero_icons_always", "-"};
   private boolean worldMap = true;
   private boolean terrainDepth = true;
   private static final String[] SLOPES_MODES = new String[]{
      "gui.xaero_off", "gui.xaero_slopes_legacy", "gui.xaero_slopes_default_3d", "gui.xaero_slopes_default_2d"
   };
   private int terrainSlopes = 2;
   public int mainEntityAs = 0;
   public boolean blockTransparency = true;
   public int waypointOpacityIngame = 80;
   public int waypointOpacityMap = 90;
   public boolean allowWrongWorldTeleportation = false;
   public int hideWorldNames = 1;
   public boolean openSlimeSettings = true;
   public boolean alwaysShowDistance = false;
   public static final String[] ENTITY_NAMES_OPTIONS = new String[]{"gui.xaero_names_off", "gui.xaero_names_list", "gui.xaero_names_always", "-"};
   private static final String[] SHOW_LIGHT_LEVEL_NAMES = new String[]{
      "gui.xaero_off", "gui.xaero_light_block", "gui.xaero_light_sky", "gui.xaero_light_all", "gui.xaero_light_both2"
   };
   public int renderLayerIndex = 1;
   public boolean crossDimensionalTp = true;
   public boolean differentiateByServerAddress = true;
   private boolean biomeColorsVanillaMode = false;
   public int lookingAtAngle = 10;
   public int lookingAtAngleVertical = 180;
   public boolean centeredEnlarged = false;
   public int zoomOnEnlarged = 0;
   public int minimapTextAlign = 0;
   public boolean waypointsMutualEdit = true;
   private int caveMapsDepth = 30;
   public boolean hideWaypointCoordinates = false;
   public boolean renderAllSets = false;
   public int playerArrowOpacity = 100;
   public boolean waypointsBottom;
   private static final String[] MINIMAP_SHAPES = new String[]{"gui.xaero_minimap_shape_square", "gui.xaero_minimap_shape_circle"};
   public int minimapShape;
   public int lightOverlayType;
   public int lightOverlayMaxLight = 7;
   public int lightOverlayMinLight = 0;
   public int lightOverlayColor = 13;
   public static final String[] DOTS_STYLES = new String[]{"gui.xaero_dots_style_default", "gui.xaero_dots_style_legacy"};
   private int dotsStyle = 0;
   public boolean debugEntityIcons;
   private int uiScale = 0;
   public static final String[] PUSHBOX_OPTIONS = new String[]{"gui.xaero_off", "gui.xaero_pushbox_normal", "gui.xaero_pushbox_screen_height"};
   public int bossHealthPushBox = 1;
   public int potionEffectPushBox = 1;
   public static final String[] FRAME_OPTIONS = new String[]{
      "gui.xaero_minimap_frame_default", "gui.xaero_minimap_frame_colored_thick", "gui.xaero_minimap_frame_colored_thin", "gui.xaero_off"
   };
   public int minimapFrame = 0;
   public int minimapFrameColor = 9;
   public static final String[] COMPASS_OPTIONS = new String[]{"gui.xaero_off", "gui.xaero_minimap_compass_inside_frame", "gui.xaero_minimap_compass_on_frame"};
   public int compassLocation = 1;
   private int compassDirectionScale = 0;
   public int compassColor = 9;
   private int northCompassColor = -1;
   public boolean debugEntityVariantIds;
   private static final String[] MULTIPLE_WAYPOINT_INFO = new String[]{"gui.xaero_off", "gui.xaero_while_sneaking", "gui.xaero_multiple_waypoints_always"};
   public int displayMultipleWaypointInfo = 1;
   private boolean entityRadar = true;
   public boolean adjustHeightForCarpetLikeBlocks = true;
   public int autoConvertWaypointDistanceToKmThreshold = 10000;
   public int waypointDistancePrecision = 1;
   public int mainDotSize = 2;
   public boolean partialYTeleportation = true;
   public boolean deleteReachedDeathpoints = true;
   public boolean hideMinimapUnderScreen = true;
   public boolean hideMinimapUnderF3 = true;
   public boolean manualCaveModeStartAuto = true;
   public int manualCaveModeStart = -1;
   public int chunkGridLineWidth = 1;
   public boolean temporaryWaypointsGlobal = true;
   public boolean keepUnlockedWhenEnlarged = false;
   public boolean enlargedMinimapAToggle = false;
   public static final String[] RADAR_OVER_MAP_OPTIONS = new String[]{
      "gui.xaero_radar_over_map_never", "gui.xaero_radar_over_map_list", "gui.xaero_radar_over_map_always", "-"
   };
   public boolean displayTrackedPlayersOnMap = true;
   public boolean displayTrackedPlayersInWorld = true;
   private boolean displayClaims = true;
   public boolean displayCurrentClaim = true;
   private int claimsFillOpacity = 46;
   private int claimsBorderOpacity = 80;
   public boolean radarHideInvisibleEntities = true;
   private boolean displayStainedGlass = true;
   public int waypointOnMapScale = 0;
   public boolean switchToAutoOnDeath = true;
   public int infoDisplayBackgroundOpacity = 40;
   public int caveModeToggleTimer = 1000;
   private boolean legibleCaveMaps;
   private boolean biomeBlending = true;
   public boolean allowInternetAccess = true;
   public boolean dimensionScaledMaxWaypointDistance = true;
   private int trackedPlayerWorldIconScale;
   private int trackedPlayerWorldNameScale;
   private int trackedPlayerMinimapIconScale;
   private static int[] OLD_MINIMAP_SIZES = new int[]{57, 85, 113, 169};

   public ModSettings(IXaeroMinimap modMain) {
      this.modMain = modMain;
      this.entityRadarBackwardsCompatibilityConfig = new EntityRadarBackwardsCompatibilityConfig();
      defaultSettings = modMain.getVersionID().endsWith("fair") ? 16188159 : Integer.MAX_VALUE;
      if (serverSettings == 0) {
         serverSettings = defaultSettings;
      }
   }

   public boolean isKeyRepeat(class_304 kb) {
      return kb != this.modMain.getSettingsKey()
         && kb != keyWaypoints
         && kb != newWaypoint
         && kb != keyLargeMap
         && kb != keyToggleMap
         && kb != keyToggleWaypoints
         && kb != keyToggleMapWaypoints
         && kb != keyToggleSlimes
         && kb != keyToggleGrid
         && kb != keyInstantWaypoint
         && kb != keySwitchSet
         && kb != keyAllSets
         && kb != keyLightOverlay
         && kb != keyBindZoom
         && kb != keyBindZoom1
         && kb != keyToggleRadar
         && kb != keyReverseEntityRadar
         && kb != keyManualCaveMode
         && kb != keyToggleTrackedPlayersOnMap
         && kb != keyToggleTrackedPlayersInWorld
         && kb != keyTogglePacChunkClaims;
   }

   public boolean getMinimap() {
      return BuiltInHudModules.MINIMAP.isActive()
         && !this.minimapDisabled()
         && (minimapItem == null || class_310.method_1551().field_1724 == null || MinimapProcessor.hasMinimapItem(class_310.method_1551().field_1724));
   }

   public boolean getCaveMaps(boolean manualCaveMode) {
      return (manualCaveMode || this.caveMaps > 0) && !this.caveMapsDisabled();
   }

   public boolean getShowWaypoints() {
      return this.showWaypoints && !this.showWaypointsDisabled();
   }

   public boolean getDeathpoints() {
      return this.deathpoints && !this.deathpointsDisabled();
   }

   public boolean getOldDeathpoints() {
      return this.oldDeathpoints;
   }

   public void setSlimeChunksSeed(long seed, XaeroPath fullWorldID) {
      serverSlimeSeeds.put(fullWorldID, seed);
   }

   public Long getSlimeChunksSeed(XaeroPath fullWorldID) {
      MinecraftServer sp = class_310.method_1551().method_1576();
      if (sp == null) {
         return serverSlimeSeeds.get(fullWorldID);
      } else {
         try {
            if (class_310.method_1551().field_1687.method_27983() != class_1937.field_25179) {
               return null;
            }
         } catch (ArrayIndexOutOfBoundsException var5) {
            return null;
         }

         long seed = sp.method_3847(class_1937.field_25179).method_8412();
         return seed;
      }
   }

   public boolean customSlimeSeedNeeded(HudSession minimapSession) {
      return !(class_310.method_1551().field_1755 instanceof GuiSlimeSeed) && class_310.method_1551().method_1576() == null && minimapSession != null;
   }

   @Deprecated
   public boolean getSlimeChunks(WaypointsManager waypointsManager) {
      return this.getSlimeChunks((MinimapSession)waypointsManager);
   }

   public boolean getSlimeChunks(MinimapSession session) {
      return this.slimeChunks
         && (class_310.method_1551().method_1576() != null || this.getSlimeChunksSeed(session.getWorldState().getCurrentWorldPath()) != null);
   }

   public boolean getShowIngameWaypoints() {
      return this.showIngameWaypoints
         && !this.showWaypointsDisabled()
         && (minimapItem == null || class_310.method_1551().field_1724 == null || MinimapProcessor.hasMinimapItem(class_310.method_1551().field_1724));
   }

   @Deprecated
   public boolean waypointsGUI(WaypointsManager waypointsManager) {
      return this.waypointsGUI((MinimapSession)waypointsManager);
   }

   public boolean waypointsGUI(MinimapSession waypointSession) {
      return class_310.method_1551().field_1724 != null
         && waypointSession.getWorldState().getAutoWorldPath() != null
         && (minimapItem == null || class_310.method_1551().field_1724 == null || MinimapProcessor.hasMinimapItem(class_310.method_1551().field_1724));
   }

   public boolean getLockNorth(int mapSize, int shape) {
      if (mapSize > 180 && shape == 0) {
         return true;
      } else {
         XaeroMinimapSession minimapSession = XaeroMinimapSession.getCurrentSession();
         return minimapSession == null
            ? this.lockNorth
            : this.lockNorth || !this.keepUnlockedWhenEnlarged && minimapSession.getMinimapProcessor().isEnlargedMap();
      }
   }

   public boolean getAntiAliasing() {
      return this.antiAliasing && this.assumeUsingFBO();
   }

   public boolean getDisplayRedstone() {
      return this.modMain.getSupportMods().shouldUseWorldMapChunks() ? this.displayRedstone : this.displayRedstone;
   }

   public int getBlockColours() {
      return this.modMain.getSupportMods().shouldUseWorldMapChunks() ? this.modMain.getSupportMods().worldmapSupport.getWorldMapColours() : this.blockColours;
   }

   public boolean getLighting() {
      return this.lighting;
   }

   public int getMinimapSize() {
      if (this.minimapSize > 0) {
         return this.minimapSize;
      } else {
         int height = class_310.method_1551().method_22683().method_4506();
         int width = class_310.method_1551().method_22683().method_4489();
         int size = (int)((float)(height <= width ? height : width) / this.getMinimapScale());
         return Math.min(
            Math.max((int)(ModOptions.SIZE.getValueMin() + ModOptions.SIZE.getValueStep()), 2 * size * 130 / 1080), (int)ModOptions.SIZE.getValueMax()
         );
      }
   }

   public float getMinimapScale() {
      return this.getUIScale(this.uiScale, 1, 11);
   }

   public float getUIScale(int optionValue, int min, int max) {
      if (optionValue <= min) {
         return (float)this.getAutoUIScale();
      } else {
         return optionValue == max ? (float)class_310.method_1551().method_22683().method_4495() : (float)optionValue;
      }
   }

   public int getAutoUIScale() {
      int height = class_310.method_1551().method_22683().method_4506();
      int width = class_310.method_1551().method_22683().method_4489();
      int size = height <= width ? height : width;
      return size >= 1500 ? size / 500 : 2;
   }

   public float getWaypointsIngameIconScale() {
      return this.getUIScale(this.waypointsIngameIconScale, 0, 17);
   }

   public float getWaypointsIngameDistanceScale() {
      return this.getUIScale(this.waypointsIngameDistanceScale, 0, 17);
   }

   public int getWaypointsIngameNameScale() {
      int scale = (int)this.getUIScale(this.waypointsIngameNameScale, 0, 17);
      return this.waypointsIngameNameScale <= 0 ? (int)Math.ceil((double)(scale / 2)) : scale;
   }

   public float getTrackedPlayerWorldIconScale() {
      return this.getUIScale(this.trackedPlayerWorldIconScale, 0, 17);
   }

   public float getTrackedPlayerWorldNameScale() {
      return this.getUIScale(this.trackedPlayerWorldNameScale, 0, 17);
   }

   public float getTrackedPlayerMinimapIconScale() {
      return this.getUIScale(this.trackedPlayerMinimapIconScale, 0, 17);
   }

   public double getWaypointsClampDepth(double fov, int height) {
      int baseIconScale = (int)this.getWaypointsIngameIconScale();
      double frameSizeAtClampDepth = this.waypointsIngameCloseScale * 0.021333335F * (double)height / (double)baseIconScale;
      double fovMultiplier = 2.0 * Math.tan(Math.toRadians(fov / 2.0));
      return frameSizeAtClampDepth / fovMultiplier;
   }

   public double getDotNameScale() {
      return this.dotNameScale * (class_310.method_1551().method_1573() ? 2.0 : 1.0);
   }

   public boolean getShowFlowers() {
      return this.modMain.getSupportMods().shouldUseWorldMapChunks() ? this.modMain.getSupportMods().worldmapSupport.getWorldMapFlowers() : this.showFlowers;
   }

   public double getMaxWaypointsDistance() {
      return this.waypointsDistanceExp <= 0 ? 0.0 : Math.pow(2.0, (double)(2 + this.waypointsDistanceExp));
   }

   public boolean getSmoothDots() {
      return this.smoothDots && this.assumeUsingFBO();
   }

   public boolean getUseWorldMap() {
      return this.worldMap && this.assumeUsingFBO();
   }

   private boolean assumeUsingFBO() {
      return !this.modMain.getInterfaces().getMinimapInterface().getMinimapFBORenderer().isTriedFBO() && !this.mapSafeMode
         || this.modMain.getInterfaces().getMinimapInterface().usingFBO();
   }

   public boolean getTerrainDepth() {
      return this.modMain.getSupportMods().shouldUseWorldMapChunks()
         ? this.modMain.getSupportMods().worldmapSupport.getWorldMapTerrainDepth()
         : this.terrainDepth;
   }

   public int getTerrainSlopes() {
      return this.modMain.getSupportMods().shouldUseWorldMapChunks()
         ? this.modMain.getSupportMods().worldmapSupport.getWorldMapTerrainSlopes()
         : this.terrainSlopes;
   }

   public boolean getBiomeColorsVanillaMode() {
      return this.modMain.getSupportMods().shouldUseWorldMapChunks()
         ? this.modMain.getSupportMods().worldmapSupport.getWorldMapBiomeColorsVanillaMode()
         : this.biomeColorsVanillaMode;
   }

   public int getCaveMapsDepth() {
      return this.modMain.getSupportMods().shouldUseWorldMapChunks() ? this.modMain.getSupportMods().worldmapSupport.getCaveModeDepth() : this.caveMapsDepth;
   }

   public int getCompassScale() {
      return this.compassDirectionScale;
   }

   public int getNorthCompassColor() {
      return this.northCompassColor < 0 ? this.compassColor : this.northCompassColor;
   }

   public boolean getPartialYTeleportation() {
      return !this.modMain.getSupportMods().worldmap() ? this.partialYTeleportation : this.modMain.getSupportMods().worldmapSupport.getPartialYTeleport();
   }

   public boolean getDisplayClaims() {
      return this.modMain.getSupportMods().shouldUseWorldMapChunks() ? this.modMain.getSupportMods().worldmapSupport.getDisplayClaims() : this.displayClaims;
   }

   public int getClaimsFillOpacity() {
      return this.modMain.getSupportMods().shouldUseWorldMapChunks()
         ? this.modMain.getSupportMods().worldmapSupport.getClaimsFillOpacity()
         : this.claimsFillOpacity;
   }

   public int getClaimsBorderOpacity() {
      return this.modMain.getSupportMods().shouldUseWorldMapChunks()
         ? this.modMain.getSupportMods().worldmapSupport.getClaimsBorderOpacity()
         : this.claimsBorderOpacity;
   }

   public boolean getBiomeBlending() {
      return this.modMain.getSupportMods().shouldUseWorldMapChunks() ? this.modMain.getSupportMods().worldmapSupport.getBiomeBlending() : this.biomeBlending;
   }

   public boolean isLegibleCaveMaps() {
      return this.modMain.getSupportMods().shouldUseWorldMapChunks() ? this.modMain.getSupportMods().worldmapSupport.isLegibleCaveMaps() : this.legibleCaveMaps;
   }

   public boolean isStainedGlassDisplayed() {
      return this.modMain.getSupportMods().shouldUseWorldMapChunks()
         ? this.modMain.getSupportMods().worldmapSupport.isStainedGlassDisplayed()
         : this.displayStainedGlass;
   }

   public boolean getAdjustHeightForCarpetLikeBlocks() {
      return this.modMain.getSupportMods().shouldUseWorldMapChunks()
         ? this.modMain.getSupportMods().worldmapSupport.getAdjustHeightForCarpetLikeBlocks()
         : this.adjustHeightForCarpetLikeBlocks;
   }

   public boolean getEntityRadar() {
      return this.entityRadar
         && !this.modMain.isFairPlay()
         && (class_310.method_1551().field_1687 == null || MinimapClientWorldDataHelper.getCurrentWorldData().getSyncedRules().allowRadarOnServer);
   }

   public int getDotsStyle() {
      return this.assumeUsingFBO() ? this.dotsStyle : 1;
   }

   public boolean isIgnoreHeightmaps() {
      if (this.modMain.getSupportMods().shouldUseWorldMapChunks()) {
         return this.modMain.getSupportMods().worldmapSupport.getWorldMapIgnoreHeightmaps();
      } else {
         MinimapWorldRootContainer currentRootContainer = BuiltInHudModules.MINIMAP.getCurrentSession().getWorldManager().getAutoRootContainer();
         return currentRootContainer.getConfig().isIgnoreHeightmaps();
      }
   }

   @Deprecated
   public void saveAllWaypoints(WaypointsManager waypointsManager) throws IOException {
      waypointsManager.getWorldManagerIO().saveAllWorlds(waypointsManager);
   }

   @Deprecated
   public void saveWaypoints(WaypointWorld wpw) throws IOException {
      this.saveWaypoints(wpw, true);
   }

   @Deprecated
   public void saveWaypoints(WaypointWorld wpw, boolean overwrite) throws IOException {
      wpw.getContainer().getSession().getWorldManagerIO().saveWorld(wpw, overwrite);
   }

   public void writeSettings(PrintWriter writer) {
      writer.println("#CONFIG ONLY OPTIONS");
      writer.println("ignoreUpdate:" + ignoreUpdate);
      writer.println("settingsButton:" + settingsButton);
      if (minimapItemId != null) {
         writer.println("minimapItemId:" + minimapItemId);
      }

      writer.println("allowWrongWorldTeleportation:" + this.allowWrongWorldTeleportation);
      writer.println("differentiateByServerAddress:" + this.differentiateByServerAddress);
      writer.println("debugEntityIcons:" + this.debugEntityIcons);
      writer.println("debugEntityVariantIds:" + this.debugEntityVariantIds);
      writer.println("radarHideInvisibleEntities:" + this.radarHideInvisibleEntities);
      writer.println("allowInternetAccess:" + this.allowInternetAccess);
      writer.println("#INGAME SETTINGS (DO NOT EDIT!)");
      writer.println("updateNotification:" + updateNotification);
      writer.println("minimap:" + BuiltInHudModules.MINIMAP.isActive());
      writer.println("caveMaps:" + this.caveMaps);
      writer.println("caveZoom:" + this.caveZoom);
      writer.println("showWaypoints:" + this.showWaypoints);
      writer.println("showIngameWaypoints:" + this.showIngameWaypoints);
      writer.println("displayRedstone:" + this.displayRedstone);
      writer.println("deathpoints:" + this.deathpoints);
      writer.println("oldDeathpoints:" + this.oldDeathpoints);
      writer.println("distance:" + this.distance);
      writer.println("lockNorth:" + this.lockNorth);
      writer.println("zoom:" + this.zoom);
      writer.println("minimapSize:" + this.minimapSize);
      writer.println("chunkGrid:" + this.chunkGrid);
      writer.println("slimeChunks:" + this.slimeChunks);
      writer.println("mapSafeMode:" + this.mapSafeMode);
      writer.println("minimapOpacity:" + this.minimapOpacity);
      writer.println("waypointsIngameIconScale:" + this.waypointsIngameIconScale);
      writer.println("waypointsIngameDistanceScale:" + this.waypointsIngameDistanceScale);
      writer.println("waypointsIngameNameScale:" + this.waypointsIngameNameScale);
      writer.println("waypointsIngameCloseScale:" + this.waypointsIngameCloseScale);
      writer.println("antiAliasing:" + this.antiAliasing);
      writer.println("blockColours:" + this.blockColours);
      writer.println("lighting:" + this.lighting);
      writer.println("dotsStyle:" + this.dotsStyle);
      writer.println("dotNameScale:" + this.dotNameScale);
      writer.println("compassOverEverything:" + this.compassOverEverything);
      writer.println("showFlowers:" + this.showFlowers);
      writer.println("keepWaypointNames:" + this.keepWaypointNames);
      writer.println("waypointsDistanceExp:" + this.waypointsDistanceExp);
      writer.println("waypointsDistanceMin:" + this.waypointsDistanceMin);
      writer.println("defaultWaypointTPCommandFormat:" + this.defaultWaypointTPCommandFormat.replace(":", "^col^"));
      writer.println("defaultWaypointTPCommandRotationFormat:" + this.defaultWaypointTPCommandRotationFormat.replace(":", "^col^"));
      writer.println("arrowScale:" + this.arrowScale);
      writer.println("arrowColour:" + this.arrowColour);
      writer.println("smoothDots:" + this.smoothDots);
      writer.println("worldMap:" + this.worldMap);
      writer.println("terrainDepth:" + this.terrainDepth);
      writer.println("terrainSlopes:" + this.terrainSlopes);
      writer.println("mainEntityAs:" + this.mainEntityAs);
      writer.println("blockTransparency:" + this.blockTransparency);
      writer.println("waypointOpacityIngame:" + this.waypointOpacityIngame);
      writer.println("waypointOpacityMap:" + this.waypointOpacityMap);
      writer.println("hideWorldNames:" + this.hideWorldNames);
      writer.println("openSlimeSettings:" + this.openSlimeSettings);
      writer.println("alwaysShowDistance:" + this.alwaysShowDistance);
      writer.println("renderLayerIndex:" + this.renderLayerIndex);
      writer.println("crossDimensionalTp:" + this.crossDimensionalTp);
      writer.println("biomeColorsVanillaMode:" + this.biomeColorsVanillaMode);
      writer.println("lookingAtAngle:" + this.lookingAtAngle);
      writer.println("lookingAtAngleVertical:" + this.lookingAtAngleVertical);
      writer.println("centeredEnlarged:" + this.centeredEnlarged);
      writer.println("zoomOnEnlarged:" + this.zoomOnEnlarged);
      writer.println("minimapTextAlign:" + this.minimapTextAlign);
      writer.println("waypointsMutualEdit:" + this.waypointsMutualEdit);
      writer.println("compassLocation:" + this.compassLocation);
      writer.println("compassDirectionScale:" + this.compassDirectionScale);
      writer.println("caveMapsDepth:" + this.caveMapsDepth);
      writer.println("hideWaypointCoordinates:" + this.hideWaypointCoordinates);
      writer.println("renderAllSets:" + this.renderAllSets);
      writer.println("playerArrowOpacity:" + this.playerArrowOpacity);
      writer.println("waypointsBottom:" + this.waypointsBottom);
      writer.println("minimapShape:" + this.minimapShape);
      writer.println("lightOverlayType:" + this.lightOverlayType);
      writer.println("lightOverlayMaxLight:" + this.lightOverlayMaxLight);
      writer.println("lightOverlayMinLight:" + this.lightOverlayMinLight);
      writer.println("lightOverlayColor:" + this.lightOverlayColor);
      writer.println("uiScale:" + this.uiScale);
      writer.println("bossHealthPushBox:" + this.bossHealthPushBox);
      writer.println("potionEffectPushBox:" + this.potionEffectPushBox);
      writer.println("minimapFrame:" + this.minimapFrame);
      writer.println("minimapFrameColor:" + this.minimapFrameColor);
      writer.println("compassColor:" + this.compassColor);
      writer.println("northCompassColor:" + this.northCompassColor);
      writer.println("displayMultipleWaypointInfo:" + this.displayMultipleWaypointInfo);
      writer.println("entityRadar:" + this.entityRadar);
      writer.println("adjustHeightForCarpetLikeBlocks:" + this.adjustHeightForCarpetLikeBlocks);
      writer.println("autoConvertWaypointDistanceToKmThreshold:" + this.autoConvertWaypointDistanceToKmThreshold);
      writer.println("waypointDistancePrecision:" + this.waypointDistancePrecision);
      writer.println("mainDotSize:" + this.mainDotSize);
      writer.println("partialYTeleportation:" + this.partialYTeleportation);
      writer.println("deleteReachedDeathpoints:" + this.deleteReachedDeathpoints);
      writer.println("hideMinimapUnderScreen:" + this.hideMinimapUnderScreen);
      writer.println("hideMinimapUnderF3:" + this.hideMinimapUnderF3);
      writer.println("manualCaveModeStartAuto:" + this.manualCaveModeStartAuto);
      writer.println("manualCaveModeStart:" + this.manualCaveModeStart);
      writer.println("chunkGridLineWidth:" + this.chunkGridLineWidth);
      writer.println("temporaryWaypointsGlobal:" + this.temporaryWaypointsGlobal);
      writer.println("keepUnlockedWhenEnlarged:" + this.keepUnlockedWhenEnlarged);
      writer.println("enlargedMinimapAToggle:" + this.enlargedMinimapAToggle);
      writer.println("displayStainedGlass:" + this.displayStainedGlass);
      writer.println("waypointOnMapScale:" + this.waypointOnMapScale);
      writer.println("switchToAutoOnDeath:" + this.switchToAutoOnDeath);
      writer.println("infoDisplayBackgroundOpacity:" + this.infoDisplayBackgroundOpacity);
      writer.println("caveModeToggleTimer:" + this.caveModeToggleTimer);
      writer.println("legibleCaveMaps:" + this.legibleCaveMaps);
      writer.println("biomeBlending:" + this.biomeBlending);
      writer.println("displayTrackedPlayersOnMap:" + this.displayTrackedPlayersOnMap);
      writer.println("displayTrackedPlayersInWorld:" + this.displayTrackedPlayersInWorld);
      writer.println("dimensionScaledMaxWaypointDistance:" + this.dimensionScaledMaxWaypointDistance);
      writer.println("trackedPlayerWorldIconScale:" + this.trackedPlayerWorldIconScale);
      writer.println("trackedPlayerWorldNameScale:" + this.trackedPlayerWorldNameScale);
      writer.println("trackedPlayerMinimapIconScale:" + this.trackedPlayerMinimapIconScale);
      writer.println("displayClaims:" + this.displayClaims);
      writer.println("displayCurrentClaim:" + this.displayCurrentClaim);
      writer.println("claimsFillOpacity:" + this.claimsFillOpacity);
      writer.println("claimsBorderOpacity:" + this.claimsBorderOpacity);
   }

   public void saveSettings() throws IOException {
      PrintWriter writer = null;

      try {
         writer = new PrintWriter(new FileWriter(this.modMain.getConfigFile().toFile()));
         this.writeSettings(writer);
         this.modMain.getMinimap().getInfoDisplays().getIo().save(writer);
         Object[] keys = serverSlimeSeeds.keySet().toArray();
         Object[] values = serverSlimeSeeds.values().toArray();

         for (int i = 0; i < keys.length; i++) {
            writer.println("seed:" + keys[i] + ":" + values[i]);
         }

         this.modMain.getHudIO().save(writer);
      } finally {
         if (writer != null) {
            writer.close();
         }
      }
   }

   public void readSetting(String[] args) {
      String valueString = args.length < 2 ? "" : args[1];
      if (args[0].equalsIgnoreCase("ignoreUpdate")) {
         ignoreUpdate = Integer.parseInt(valueString);
      } else if (args[0].equalsIgnoreCase("updateNotification")) {
         updateNotification = valueString.equals("true");
      } else if (args[0].equalsIgnoreCase("settingsButton")) {
         settingsButton = valueString.equals("true");
      } else if (args[0].equalsIgnoreCase("minimapItemId")) {
         minimapItemId = valueString + ":" + args[2];
         minimapItem = (class_1792)class_7923.field_41178.method_10223(new class_2960(valueString, args[2]));
         MinimapLogs.LOGGER.info("Minimap item set: " + minimapItem.method_7848().getString());
      } else if (args[0].equalsIgnoreCase("allowWrongWorldTeleportation")) {
         this.allowWrongWorldTeleportation = valueString.equals("true");
      } else if (args[0].equalsIgnoreCase("differentiateByServerAddress")) {
         this.differentiateByServerAddress = valueString.equals("true");
      } else if (args[0].equalsIgnoreCase("debugEntityIcons")) {
         this.debugEntityIcons = valueString.equals("true");
      } else if (args[0].equalsIgnoreCase("debugEntityVariantIds")) {
         this.debugEntityVariantIds = valueString.equals("true");
      } else if (args[0].equalsIgnoreCase("radarHideInvisibleEntities")) {
         this.radarHideInvisibleEntities = valueString.equals("true");
      } else if (args[0].equalsIgnoreCase("allowInternetAccess")) {
         this.allowInternetAccess = valueString.equals("true");
      } else if (args[0].equalsIgnoreCase("minimap")) {
         BuiltInHudModules.MINIMAP.setActive(valueString.equals("true"));
      } else if (args[0].equalsIgnoreCase("caveMaps")) {
         this.caveMaps = valueString.equals("true") ? 1 : (valueString.equals("false") ? 0 : Integer.parseInt(valueString));
      } else if (args[0].equalsIgnoreCase("caveZoom")) {
         this.caveZoom = valueString.equals("true") ? 2 : (valueString.equals("false") ? 0 : Integer.parseInt(valueString));
      } else if (args[0].equalsIgnoreCase("showWaypoints")) {
         this.showWaypoints = valueString.equals("true");
      } else if (args[0].equalsIgnoreCase("deathpoints")) {
         this.deathpoints = valueString.equals("true");
      } else if (args[0].equalsIgnoreCase("oldDeathpoints")) {
         this.oldDeathpoints = valueString.equals("true");
      } else if (args[0].equalsIgnoreCase("showIngameWaypoints")) {
         this.showIngameWaypoints = valueString.equals("true");
      } else if (args[0].equalsIgnoreCase("displayRedstone")) {
         this.displayRedstone = valueString.equals("true");
      } else if (args[0].equalsIgnoreCase("distance")) {
         this.distance = Integer.parseInt(valueString);
      } else if (args[0].equalsIgnoreCase("showCoords")) {
         BuiltInInfoDisplays.COORDINATES.setState(valueString.equals("true"));
      } else if (args[0].equalsIgnoreCase("lockNorth")) {
         this.lockNorth = valueString.equals("true");
      } else if (args[0].equalsIgnoreCase("zoom")) {
         this.zoom = Integer.parseInt(valueString);
         if (this.zoom >= zooms.length) {
            this.zoom = zooms.length - 1;
         }
      } else if (args[0].equalsIgnoreCase("mapSize")) {
         int oldSize = Integer.parseInt(valueString);
         if (oldSize == -1) {
            this.minimapSize = 0;
         } else {
            this.minimapSize = OLD_MINIMAP_SIZES[oldSize];
         }
      } else if (args[0].equalsIgnoreCase("minimapSize")) {
         this.minimapSize = Integer.parseInt(valueString);
      } else if (args[0].equalsIgnoreCase("chunkGrid")) {
         this.chunkGrid = valueString.equals("true") ? 0 : (valueString.equals("false") ? -1 : Integer.parseInt(valueString));
      } else if (args[0].equalsIgnoreCase("slimeChunks")) {
         this.slimeChunks = valueString.equals("true");
      } else if (args[0].equalsIgnoreCase("mapSafeMode")) {
         this.mapSafeMode = valueString.equals("true");
      } else if (args[0].equalsIgnoreCase("minimapOpacity")) {
         this.minimapOpacity = Double.valueOf(valueString);
      } else if (args[0].equalsIgnoreCase("waypointsIngameIconScale")) {
         this.waypointsIngameIconScale = Integer.parseInt(valueString);
      } else if (args[0].equalsIgnoreCase("waypointsIngameDistanceScale")) {
         this.waypointsIngameDistanceScale = Integer.parseInt(valueString);
      } else if (args[0].equalsIgnoreCase("waypointsIngameNameScale")) {
         this.waypointsIngameNameScale = Integer.parseInt(valueString);
      } else if (args[0].equalsIgnoreCase("waypointsIngameCloseScale")) {
         this.waypointsIngameCloseScale = Double.valueOf(valueString);
      } else if (args[0].equalsIgnoreCase("antiAliasing")) {
         this.antiAliasing = valueString.equals("true");
      } else if (args[0].equalsIgnoreCase("blockColours")) {
         this.blockColours = Integer.parseInt(valueString);
      } else if (args[0].equalsIgnoreCase("lighting")) {
         this.lighting = valueString.equals("true");
      } else if (args[0].equalsIgnoreCase("dotsStyle")) {
         this.dotsStyle = Integer.parseInt(valueString);
      } else if (args[0].equalsIgnoreCase("dotNameScale")) {
         this.dotNameScale = Double.valueOf(valueString);
      } else if (args[0].equalsIgnoreCase("compassOverEverything")) {
         this.compassOverEverything = valueString.equals("true");
      } else if (args[0].equalsIgnoreCase("showBiome")) {
         BuiltInInfoDisplays.BIOME.setState(valueString.equals("true"));
      } else if (args[0].equalsIgnoreCase("showFlowers")) {
         this.showFlowers = valueString.equals("true");
      } else if (args[0].equalsIgnoreCase("keepWaypointNames")) {
         this.keepWaypointNames = valueString.equals("true");
      } else if (args[0].equalsIgnoreCase("waypointsDistance")) {
         double oldValue = Double.valueOf(valueString);
         this.waypointsDistanceExp = oldValue <= 0.0 ? 0 : (int)Math.max(3.0, Math.ceil(Math.log(oldValue) / Math.log(2.0))) - 2;
      } else if (args[0].equalsIgnoreCase("waypointsDistanceExp")) {
         this.waypointsDistanceExp = Integer.parseInt(valueString);
      } else if (args[0].equalsIgnoreCase("waypointsDistanceMin")) {
         this.waypointsDistanceMin = Double.valueOf(valueString);
      } else if (args[0].equalsIgnoreCase("waypointTp")) {
         this.defaultWaypointTPCommandFormat = "/" + valueString + " {x} {y} {z}";
         this.defaultWaypointTPCommandRotationFormat = "/" + valueString + " {x} {y} {z} {yaw} ~";
      } else if (args[0].equalsIgnoreCase("waypointTPCommand")) {
         this.defaultWaypointTPCommandFormat = valueString.replace("^col^", ":") + " {x} {y} {z}";
         this.defaultWaypointTPCommandRotationFormat = valueString.replace("^col^", ":") + " {x} {y} {z} {yaw} ~";
      } else if (args[0].equalsIgnoreCase("defaultWaypointTPCommandFormat")) {
         this.defaultWaypointTPCommandFormat = valueString.replace("^col^", ":");
      } else if (args[0].equalsIgnoreCase("defaultWaypointTPCommandRotationFormat")) {
         this.defaultWaypointTPCommandRotationFormat = valueString.replace("^col^", ":");
      } else if (args[0].equalsIgnoreCase("arrowScale")) {
         this.arrowScale = Double.valueOf(valueString);
      } else if (args[0].equalsIgnoreCase("arrowColour")) {
         this.arrowColour = Integer.parseInt(valueString);
      } else if (args[0].equalsIgnoreCase("seed")) {
         serverSlimeSeeds.put(new XaeroPathReader().read(valueString), Long.parseLong(args[2]));
      } else if (args[0].equalsIgnoreCase("smoothDots")) {
         this.smoothDots = valueString.equals("true");
      } else if (args[0].equalsIgnoreCase("worldMap")) {
         this.worldMap = valueString.equals("true");
      } else if (args[0].equalsIgnoreCase("terrainDepth")) {
         this.terrainDepth = valueString.equals("true");
      } else if (args[0].equalsIgnoreCase("terrainSlopes")) {
         this.terrainSlopes = valueString.equals("true") ? 2 : (valueString.equals("false") ? 0 : Integer.parseInt(valueString));
      } else if (args[0].equalsIgnoreCase("alwaysArrow") && valueString.equals("true")) {
         this.mainEntityAs = 2;
      } else if (args[0].equalsIgnoreCase("mainEntityAs")) {
         this.mainEntityAs = Integer.parseInt(valueString);
      } else if (args[0].equalsIgnoreCase("blockTransparency")) {
         this.blockTransparency = valueString.equals("true");
      } else if (args[0].equalsIgnoreCase("waypointOpacityIngame")) {
         this.waypointOpacityIngame = Integer.parseInt(valueString);
      } else if (args[0].equalsIgnoreCase("waypointOpacityMap")) {
         this.waypointOpacityMap = Integer.parseInt(valueString);
      } else if (args[0].equalsIgnoreCase("hideWorldNames")) {
         this.hideWorldNames = valueString.equals("true") ? 2 : (valueString.equals("false") ? 1 : Integer.parseInt(valueString));
      } else if (args[0].equalsIgnoreCase("openSlimeSettings")) {
         this.openSlimeSettings = valueString.equals("true");
      } else if (args[0].equalsIgnoreCase("alwaysShowDistance")) {
         this.alwaysShowDistance = valueString.equals("true");
      } else if (args[0].equalsIgnoreCase("showLightLevel")) {
         BuiltInInfoDisplays.LIGHT_LEVEL.setState(valueString.equals("true") ? 1 : (valueString.equals("false") ? 0 : Integer.parseInt(valueString)));
      } else if (args[0].equalsIgnoreCase("renderLayerIndex")) {
         this.renderLayerIndex = Integer.parseInt(valueString);
      } else if (args[0].equalsIgnoreCase("crossDimensionalTp")) {
         this.crossDimensionalTp = valueString.equals("true");
      } else if (args[0].equalsIgnoreCase("showTime")) {
         BuiltInInfoDisplays.TIME.setState(Integer.parseInt(valueString));
      } else if (args[0].equalsIgnoreCase("biomeColorsVanillaMode")) {
         this.biomeColorsVanillaMode = valueString.equals("true");
      } else if (args[0].equalsIgnoreCase("lookingAtAngle")) {
         this.lookingAtAngle = Integer.parseInt(valueString);
      } else if (args[0].equalsIgnoreCase("lookingAtAngleVertical")) {
         this.lookingAtAngleVertical = Integer.parseInt(valueString);
      } else if (args[0].equalsIgnoreCase("centeredEnlarged")) {
         this.centeredEnlarged = valueString.equals("true");
      } else if (args[0].equalsIgnoreCase("zoomedOutEnlarged")) {
         this.zoomOnEnlarged = valueString.equals("true") ? 1 : 0;
      } else if (args[0].equalsIgnoreCase("zoomOnEnlarged")) {
         this.zoomOnEnlarged = Integer.parseInt(valueString);
      } else if (args[0].equalsIgnoreCase("minimapTextAlign")) {
         this.minimapTextAlign = Integer.parseInt(valueString);
      } else if (args[0].equalsIgnoreCase("showAngles")) {
         BuiltInInfoDisplays.ANGLES.setState(valueString.equals("true"));
      } else if (args[0].equalsIgnoreCase("waypointsMutualEdit")) {
         this.waypointsMutualEdit = valueString.equals("true");
      } else if (args[0].equalsIgnoreCase("compass")) {
         this.compassLocation = valueString.equals("true") ? 1 : 0;
      } else if (args[0].equalsIgnoreCase("compassLocation")) {
         this.compassLocation = Integer.parseInt(valueString);
      } else if (args[0].equalsIgnoreCase("compassDirectionScale")) {
         this.compassDirectionScale = Integer.parseInt(valueString);
      } else if (args[0].equalsIgnoreCase("caveMapsDepth")) {
         this.caveMapsDepth = Integer.parseInt(valueString);
      } else if (args[0].equalsIgnoreCase("hideWaypointCoordinates")) {
         this.hideWaypointCoordinates = valueString.equals("true");
      } else if (args[0].equalsIgnoreCase("renderAllSets")) {
         this.renderAllSets = valueString.equals("true");
      } else if (args[0].equalsIgnoreCase("playerArrowOpacity")) {
         this.playerArrowOpacity = Integer.parseInt(valueString);
      } else if (args[0].equalsIgnoreCase("waypointsBottom")) {
         this.waypointsBottom = valueString.equals("true");
      } else if (args[0].equalsIgnoreCase("minimapShape")) {
         this.minimapShape = Integer.parseInt(valueString);
      } else if (args[0].equalsIgnoreCase("lightOverlayType")) {
         this.lightOverlayType = Integer.parseInt(valueString);
      } else if (args[0].equalsIgnoreCase("lightOverlayMaxLight")) {
         this.lightOverlayMaxLight = Integer.parseInt(valueString);
      } else if (args[0].equalsIgnoreCase("lightOverlayMinLight")) {
         this.lightOverlayMinLight = Integer.parseInt(valueString);
      } else if (args[0].equalsIgnoreCase("lightOverlayColor")) {
         this.lightOverlayColor = Integer.parseInt(valueString);
      } else if (args[0].equalsIgnoreCase("uiScale")) {
         this.uiScale = Integer.parseInt(valueString);
      } else if (args[0].equalsIgnoreCase("bossHealthPushBox")) {
         this.bossHealthPushBox = Integer.parseInt(valueString);
      } else if (args[0].equalsIgnoreCase("potionEffectPushBox")) {
         this.potionEffectPushBox = Integer.parseInt(valueString);
      } else if (args[0].equalsIgnoreCase("minimapFrame")) {
         this.minimapFrame = Integer.parseInt(valueString);
      } else if (args[0].equalsIgnoreCase("minimapFrameColor")) {
         this.minimapFrameColor = Integer.parseInt(valueString);
      } else if (args[0].equalsIgnoreCase("compassColor")) {
         this.compassColor = Integer.parseInt(valueString);
      } else if (args[0].equalsIgnoreCase("northCompassColor")) {
         this.northCompassColor = Integer.parseInt(valueString);
      } else if (args[0].equalsIgnoreCase("showDimensionName")) {
         BuiltInInfoDisplays.DIMENSION.setState(valueString.equals("true"));
      } else if (args[0].equalsIgnoreCase("displayMultipleWaypointInfo")) {
         this.displayMultipleWaypointInfo = Integer.parseInt(valueString);
      } else if (args[0].equalsIgnoreCase("entityRadar")) {
         this.entityRadar = valueString.equals("true");
      } else {
         if (this.entityRadarBackwardsCompatibilityConfig.readSetting(args)) {
            this.foundOldRadarSettings = true;
            return;
         }

         if (args[0].equalsIgnoreCase("adjustHeightForCarpetLikeBlocks")) {
            this.adjustHeightForCarpetLikeBlocks = valueString.equals("true");
         } else if (args[0].equalsIgnoreCase("autoConvertWaypointDistanceToKmThreshold")) {
            this.autoConvertWaypointDistanceToKmThreshold = Integer.parseInt(valueString);
         } else if (args[0].equalsIgnoreCase("waypointDistancePrecision")) {
            this.waypointDistancePrecision = Integer.parseInt(valueString);
         } else if (args[0].equalsIgnoreCase("mainDotSize")) {
            this.mainDotSize = Integer.parseInt(valueString);
         } else if (args[0].equalsIgnoreCase("partialYTeleportation")) {
            this.partialYTeleportation = valueString.equals("true");
         } else if (args[0].equalsIgnoreCase("deleteReachedDeathpoints")) {
            this.deleteReachedDeathpoints = valueString.equals("true");
         } else if (args[0].equalsIgnoreCase("hideMinimapUnderScreen")) {
            this.hideMinimapUnderScreen = valueString.equals("true");
         } else if (args[0].equalsIgnoreCase("manualCaveModeStart")) {
            this.manualCaveModeStart = Integer.parseInt(valueString);
         } else if (args[0].equalsIgnoreCase("manualCaveModeStartAuto")) {
            this.manualCaveModeStartAuto = valueString.equals("true");
         } else if (args[0].equalsIgnoreCase("chunkGridLineWidth")) {
            this.chunkGridLineWidth = Integer.parseInt(valueString);
         } else if (args[0].equalsIgnoreCase("hideMinimapUnderF3")) {
            this.hideMinimapUnderF3 = valueString.equals("true");
         } else if (args[0].equalsIgnoreCase("temporaryWaypointsGlobal")) {
            this.temporaryWaypointsGlobal = valueString.equals("true");
         } else if (args[0].equalsIgnoreCase("keepUnlockedWhenEnlarged")) {
            this.keepUnlockedWhenEnlarged = valueString.equals("true");
         } else if (args[0].equalsIgnoreCase("enlargedMinimapAToggle")) {
            this.enlargedMinimapAToggle = valueString.equals("true");
         } else if (args[0].equalsIgnoreCase("displayStainedGlass")) {
            this.displayStainedGlass = valueString.equals("true");
         } else if (args[0].equalsIgnoreCase("waypointOnMapScale")) {
            this.waypointOnMapScale = Integer.parseInt(valueString);
         } else if (args[0].equalsIgnoreCase("switchToAutoOnDeath")) {
            this.switchToAutoOnDeath = valueString.equals("true");
         } else if (args[0].equalsIgnoreCase("displayWeatherInfo")) {
            BuiltInInfoDisplays.WEATHER.setState(valueString.equals("true"));
         } else if (args[0].equalsIgnoreCase("infoDisplayBackgroundOpacity")) {
            this.infoDisplayBackgroundOpacity = Integer.parseInt(valueString);
         } else if (args[0].equalsIgnoreCase("caveModeToggleTimer")) {
            this.caveModeToggleTimer = Integer.parseInt(valueString);
         } else if (args[0].equalsIgnoreCase("legibleCaveMaps")) {
            this.legibleCaveMaps = valueString.equals("true");
         } else if (args[0].equalsIgnoreCase("biomeBlending")) {
            this.biomeBlending = args[1].equals("true");
         } else if (args[0].equalsIgnoreCase("displayPacPlayers") || args[0].equalsIgnoreCase("displayTrackedPlayers")) {
            this.displayTrackedPlayersOnMap = this.displayTrackedPlayersInWorld = valueString.equals("true");
         } else if (args[0].equalsIgnoreCase("displayTrackedPlayersOnMap")) {
            this.displayTrackedPlayersOnMap = valueString.equals("true");
         } else if (args[0].equalsIgnoreCase("displayTrackedPlayersInWorld")) {
            this.displayTrackedPlayersInWorld = valueString.equals("true");
         } else if (args[0].equalsIgnoreCase("dimensionScaledMaxWaypointDistance")) {
            this.dimensionScaledMaxWaypointDistance = args[1].equals("true");
         } else if (args[0].equalsIgnoreCase("trackedPlayerWorldIconScale")) {
            this.trackedPlayerWorldIconScale = Integer.parseInt(args[1]);
         } else if (args[0].equalsIgnoreCase("trackedPlayerWorldNameScale")) {
            this.trackedPlayerWorldNameScale = Integer.parseInt(args[1]);
         } else if (args[0].equalsIgnoreCase("trackedPlayerMinimapIconScale")) {
            this.trackedPlayerMinimapIconScale = Integer.parseInt(args[1]);
         } else if (args[0].equalsIgnoreCase("displayClaims")) {
            this.displayClaims = valueString.equals("true");
         } else if (args[0].equalsIgnoreCase("displayCurrentClaim")) {
            this.displayCurrentClaim = valueString.equals("true");
         } else if (args[0].equalsIgnoreCase("claimsOpacity")) {
            this.claimsBorderOpacity = Integer.parseInt(valueString);
            this.claimsFillOpacity = this.claimsBorderOpacity * 58 / 100;
         } else if (args[0].equalsIgnoreCase("claimsBorderOpacity")) {
            this.claimsBorderOpacity = Integer.parseInt(valueString);
         } else if (args[0].equalsIgnoreCase("claimsFillOpacity")) {
            this.claimsFillOpacity = Integer.parseInt(valueString);
         }
      }
   }

   public void loadDefaultSettings() throws IOException {
      Path mainConfigFile = this.modMain.getConfigFile();
      File defaultConfigFile = mainConfigFile.getParent().resolveSibling("defaultconfigs").resolve(mainConfigFile.getFileName()).toFile();
      if (defaultConfigFile.exists()) {
         this.loadSettingsFile(defaultConfigFile);
      }
   }

   public void loadSettings() throws IOException {
      this.loadDefaultSettings();
      Path mainConfigFile = this.modMain.getConfigFile();
      Path configFolderPath = mainConfigFile.getParent();
      if (!Files.exists(configFolderPath)) {
         Files.createDirectories(configFolderPath);
      }

      if (Files.exists(mainConfigFile)) {
         this.loadSettingsFile(mainConfigFile.toFile());
      }

      this.saveSettings();
   }

   private void loadSettingsFile(File file) throws IOException {
      BufferedReader reader = null;

      try {
         reader = new BufferedReader(new FileReader(file));
         InfoDisplayIO infoDisplayIO = this.modMain.getMinimap().getInfoDisplays().getIo();

         String s;
         while ((s = reader.readLine()) != null) {
            if (!this.modMain.getHudIO().load(s)) {
               String[] args = s.split(":");

               try {
                  if (args[0].equalsIgnoreCase("interface") && args[1].equals("gui.xaero_minimap")) {
                     BuiltInHudModules.MINIMAP.setTransform(this.modMain.getHud().getOldSystemCompatibility().loadOldTransform(args));
                  } else if (args[0].equals("infoDisplayOrder")) {
                     infoDisplayIO.loadInfoDisplayOrderLine(args);
                  } else if (args[0].equals("infoDisplay")) {
                     infoDisplayIO.loadInfoDisplayLine(args);
                  } else {
                     this.readSetting(args);
                  }
               } catch (Exception var10) {
                  MinimapLogs.LOGGER.info("Skipping setting:" + args[0]);
               }
            }
         }
      } finally {
         if (reader != null) {
            reader.close();
         }
      }
   }

   public String getMoreOptionValueNames(ModOptions par1EnumOptions) {
      return "undefined";
   }

   private String getBooleanName(ModOptions par1EnumOptions) {
      boolean clientSetting = this.getClientBooleanValue(par1EnumOptions);
      boolean serverSetting = this.getBooleanValue(par1EnumOptions);
      return getTranslation(clientSetting) + (serverSetting != clientSetting ? "§e (" + getTranslation(serverSetting) + ")" : "");
   }

   public Object getOptionValue(ModOptions par1EnumOptions) {
      if (par1EnumOptions.enumBoolean) {
         return this.getClientBooleanValue(par1EnumOptions);
      } else if (par1EnumOptions.isIngameOnly() && !canEditIngameSettings()) {
         return 0;
      } else if (par1EnumOptions == ModOptions.ZOOM) {
         return this.zoom;
      } else if (par1EnumOptions == ModOptions.DISTANCE) {
         return this.distance;
      } else if (par1EnumOptions == ModOptions.SLIME_CHUNKS && this.customSlimeSeedNeeded(XaeroMinimapSession.getCurrentSession())) {
         return -1;
      } else if (par1EnumOptions == ModOptions.CAVE_MAPS) {
         return this.caveMaps;
      } else if (par1EnumOptions == ModOptions.CAVE_ZOOM) {
         return this.caveZoom;
      } else if (par1EnumOptions == ModOptions.HIDE_WORLD_NAMES) {
         return this.hideWorldNames;
      } else if (par1EnumOptions == ModOptions.MINIMAP_TEXT_ALIGN) {
         return this.minimapTextAlign;
      } else if (par1EnumOptions == ModOptions.ARROW_COLOUR) {
         return this.arrowColour;
      } else if (par1EnumOptions == ModOptions.COLOURS) {
         return this.blockColours;
      } else if (par1EnumOptions == ModOptions.TERRAIN_SLOPES) {
         return this.terrainSlopes;
      } else if (par1EnumOptions == ModOptions.MAIN_ENTITY_AS) {
         return this.mainEntityAs;
      } else if (par1EnumOptions == ModOptions.MINIMAP_SHAPE) {
         return this.minimapShape;
      } else if (par1EnumOptions == ModOptions.LIGHT_OVERLAY_TYPE) {
         return this.lightOverlayType < 0 ? 0 : this.lightOverlayType;
      } else if (par1EnumOptions == ModOptions.DOTS_STYLE) {
         return this.dotsStyle;
      } else if (par1EnumOptions == ModOptions.BOSS_HEALTH_PUSHBOX) {
         return this.bossHealthPushBox;
      } else if (par1EnumOptions == ModOptions.POTION_EFFECTS_PUSHBOX) {
         return this.potionEffectPushBox;
      } else if (par1EnumOptions == ModOptions.MINIMAP_FRAME) {
         return this.minimapFrame;
      } else if (par1EnumOptions == ModOptions.COMPASS_LOCATION) {
         return this.compassLocation;
      } else if (par1EnumOptions == ModOptions.MULTIPLE_WAYPOINT_INFO) {
         return this.displayMultipleWaypointInfo;
      } else if (par1EnumOptions == ModOptions.ZOOM_ON_ENLARGE) {
         return this.zoomOnEnlarged;
      } else if (par1EnumOptions == ModOptions.EAMOUNT) {
         return this.getOptionIndexForRadarSetting(EntityRadarCategorySettings.ENTITY_NUMBER);
      } else if (par1EnumOptions == ModOptions.RADAR_ICONS_DISPLAYED) {
         return this.getOptionIndexForRadarSetting(EntityRadarCategorySettings.ICONS);
      } else if (par1EnumOptions == ModOptions.RADAR_NAMES_DISPLAYED) {
         return this.getOptionIndexForRadarSetting(EntityRadarCategorySettings.NAMES);
      } else if (par1EnumOptions == ModOptions.RADAR_OVER_FRAME) {
         return this.getOptionIndexForRadarSetting(EntityRadarCategorySettings.RENDER_OVER_MINIMAP);
      } else {
         return par1EnumOptions == ModOptions.RADAR_Y_DISPLAYED
            ? this.getOptionIndexForRadarSetting(EntityRadarCategorySettings.DISPLAY_Y)
            : this.getMoreOptionValues(par1EnumOptions);
      }
   }

   protected Object getMoreOptionValues(ModOptions par1EnumOptions) {
      return false;
   }

   public String getOptionValueName(ModOptions par1EnumOptions) {
      if (par1EnumOptions.isIngameOnly() && !canEditIngameSettings()) {
         return getTranslation(false);
      } else if (this.usesWorldMapScreenValue(par1EnumOptions) || this.usesWorldMapOptionValue(par1EnumOptions) || this.usesWorldMapHardValue(par1EnumOptions)) {
         return "§e" + class_1074.method_4662("gui.xaero_world_map", new Object[0]);
      } else if (par1EnumOptions.enumBoolean) {
         return this.getBooleanName(par1EnumOptions);
      } else {
         String s = "";
         if (par1EnumOptions == ModOptions.ZOOM) {
            s = s + zooms[this.zoom] + "x";
         } else if (par1EnumOptions == ModOptions.DISTANCE) {
            s = s + class_1074.method_4662(distanceTypes[this.distance], new Object[0]);
         } else if (par1EnumOptions == ModOptions.SLIME_CHUNKS && this.customSlimeSeedNeeded(XaeroMinimapSession.getCurrentSession())) {
            s = par1EnumOptions.getEnumString();
         } else if (par1EnumOptions == ModOptions.CAVE_MAPS) {
            if (this.caveMaps == 0) {
               s = s + class_1074.method_4662("gui.xaero_off", new Object[0]);
            } else {
               int roofSideSize = this.caveMaps * 2 - 1;
               s = s + roofSideSize + "x" + roofSideSize + " " + class_1074.method_4662("gui.xaero_roof", new Object[0]);
               if (!this.getCaveMaps(false)) {
                  s = s + "§e (" + getTranslation(false) + ")";
               }
            }
         } else if (par1EnumOptions == ModOptions.CAVE_ZOOM) {
            if (this.caveZoom == 0) {
               s = s + class_1074.method_4662("gui.xaero_off", new Object[0]);
            } else {
               s = s + (1 + this.caveZoom) + "x";
            }
         } else if (par1EnumOptions == ModOptions.HIDE_WORLD_NAMES) {
            s = s
               + (
                  this.hideWorldNames == 0
                     ? class_1074.method_4662("gui.xaero_off", new Object[0])
                     : (
                        this.hideWorldNames == 1
                           ? class_1074.method_4662("gui.xaero_partial", new Object[0])
                           : class_1074.method_4662("gui.xaero_full", new Object[0])
                     )
               );
         } else if (par1EnumOptions == ModOptions.MINIMAP_TEXT_ALIGN) {
            s = s
               + (
                  this.minimapTextAlign == 0
                     ? class_1074.method_4662("gui.xaero_center", new Object[0])
                     : (
                        this.minimapTextAlign == 1
                           ? class_1074.method_4662("gui.xaero_left", new Object[0])
                           : class_1074.method_4662("gui.xaero_right", new Object[0])
                     )
               );
         } else if (par1EnumOptions == ModOptions.ARROW_COLOUR) {
            String colourName = "gui.xaero_team";
            if (this.arrowColour != -1) {
               colourName = this.arrowColourNames[this.arrowColour];
            }

            s = s + class_1074.method_4662(colourName, new Object[0]);
         } else if (par1EnumOptions == ModOptions.COLOURS) {
            s = s + class_1074.method_4662(blockColourTypes[this.getBlockColours()], new Object[0]);
         } else if (par1EnumOptions == ModOptions.TERRAIN_SLOPES) {
            s = s + class_1074.method_4662(SLOPES_MODES[this.terrainSlopes], new Object[0]);
         } else if (par1EnumOptions == ModOptions.MAIN_ENTITY_AS) {
            s = s
               + (
                  this.mainEntityAs == 0
                     ? class_1074.method_4662("gui.xaero_crosshair", new Object[0])
                     : (
                        this.mainEntityAs == 1
                           ? class_1074.method_4662("gui.xaero_dot", new Object[0])
                           : class_1074.method_4662("gui.xaero_arrow", new Object[0])
                     )
               );
         } else if (par1EnumOptions == ModOptions.MINIMAP_SHAPE) {
            s = s + class_1074.method_4662(MINIMAP_SHAPES[this.minimapShape], new Object[0]);
         } else if (par1EnumOptions == ModOptions.LIGHT_OVERLAY_TYPE) {
            s = s + class_1074.method_4662(SHOW_LIGHT_LEVEL_NAMES[this.lightOverlayType < 0 ? 0 : this.lightOverlayType], new Object[0]);
         } else if (par1EnumOptions == ModOptions.DOTS_STYLE) {
            s = s + class_1074.method_4662(DOTS_STYLES[this.dotsStyle], new Object[0]);
         } else if (par1EnumOptions == ModOptions.BOSS_HEALTH_PUSHBOX) {
            s = s + class_1074.method_4662(PUSHBOX_OPTIONS[this.bossHealthPushBox], new Object[0]);
         } else if (par1EnumOptions == ModOptions.POTION_EFFECTS_PUSHBOX) {
            s = s + class_1074.method_4662(PUSHBOX_OPTIONS[this.potionEffectPushBox], new Object[0]);
         } else if (par1EnumOptions == ModOptions.MINIMAP_FRAME) {
            s = s + class_1074.method_4662(FRAME_OPTIONS[this.minimapFrame], new Object[0]);
         } else if (par1EnumOptions == ModOptions.COMPASS_LOCATION) {
            s = s + class_1074.method_4662(COMPASS_OPTIONS[this.compassLocation], new Object[0]);
         } else if (par1EnumOptions == ModOptions.MULTIPLE_WAYPOINT_INFO) {
            s = s + class_1074.method_4662(MULTIPLE_WAYPOINT_INFO[this.displayMultipleWaypointInfo], new Object[0]);
         } else if (par1EnumOptions == ModOptions.ZOOM_ON_ENLARGE) {
            s = s + (this.zoomOnEnlarged <= 0 ? class_1074.method_4662("gui.xaero_zoom_on_enlarge_auto", new Object[0]) : zooms[this.zoomOnEnlarged - 1] + "x");
         } else if (par1EnumOptions == ModOptions.EAMOUNT) {
            s = s + this.getRadarSettingOptionName(EntityRadarCategorySettings.ENTITY_NUMBER);
         } else if (par1EnumOptions == ModOptions.RADAR_ICONS_DISPLAYED) {
            s = s + this.getRadarSettingOptionName(EntityRadarCategorySettings.ICONS);
         } else if (par1EnumOptions == ModOptions.RADAR_NAMES_DISPLAYED) {
            s = s + this.getRadarSettingOptionName(EntityRadarCategorySettings.NAMES);
         } else if (par1EnumOptions == ModOptions.RADAR_OVER_FRAME) {
            s = s + this.getRadarSettingOptionName(EntityRadarCategorySettings.RENDER_OVER_MINIMAP);
         } else if (par1EnumOptions == ModOptions.RADAR_Y_DISPLAYED) {
            s = s + this.getRadarSettingOptionName(EntityRadarCategorySettings.DISPLAY_Y);
         } else {
            s = this.getMoreOptionValueNames(par1EnumOptions);
         }

         return s;
      }
   }

   public boolean usesWorldMapOptionValue(ModOptions par1EnumOptions) {
      return this.modMain.getSupportMods().shouldUseWorldMapChunks()
            && (
               par1EnumOptions == ModOptions.ADJUST_HEIGHT_FOR_SHORT_BLOCKS
                  || par1EnumOptions == ModOptions.COLOURS
                  || par1EnumOptions == ModOptions.IGNORE_HEIGHTMAPS
                  || par1EnumOptions == ModOptions.FLOWERS
                  || par1EnumOptions == ModOptions.BIOMES_VANILLA
                  || par1EnumOptions == ModOptions.TERRAIN_DEPTH
                  || par1EnumOptions == ModOptions.TERRAIN_SLOPES
                  || par1EnumOptions == ModOptions.DISPLAY_STAINED_GLASS
                  || par1EnumOptions == ModOptions.PAC_CLAIMS
                  || par1EnumOptions == ModOptions.PAC_CLAIMS_FILL_OPACITY
                  || par1EnumOptions == ModOptions.PAC_CLAIMS_BORDER_OPACITY
                  || par1EnumOptions == ModOptions.CAVE_MAPS_DEPTH
                  || par1EnumOptions == ModOptions.LEGIBLE_CAVE_MAPS
                  || par1EnumOptions == ModOptions.BIOME_BLENDING
            )
         || this.modMain.getSupportMods().worldmap() && par1EnumOptions == ModOptions.PARTIAL_Y_TELEPORTATION;
   }

   public boolean usesWorldMapHardValue(ModOptions par1EnumOptions) {
      return this.modMain.getSupportMods().shouldUseWorldMapChunks() && par1EnumOptions == ModOptions.REDSTONE;
   }

   public String getSliderOptionText(ModOptions par1EnumOptions) {
      String s = par1EnumOptions.getEnumString() + ": ";
      boolean usingSafeMode = this.modMain.getInterfaces().getMinimapInterface().getMinimapFBORenderer().isTriedFBO()
            && !this.modMain.getInterfaces().getMinimapInterface().usingFBO()
         || this.mapSafeMode;
      if (this.usesWorldMapScreenValue(par1EnumOptions) || this.usesWorldMapOptionValue(par1EnumOptions) || this.usesWorldMapHardValue(par1EnumOptions)) {
         s = s + "§e" + class_1074.method_4662("gui.xaero_world_map", new Object[0]);
      } else if (par1EnumOptions == ModOptions.CHUNK_GRID) {
         s = s
            + (
               this.chunkGrid > -1
                  ? "§" + ENCHANT_COLORS[this.chunkGrid] + class_1074.method_4662(ENCHANT_COLOR_NAMES[this.chunkGrid], new Object[0])
                  : class_1074.method_4662("gui.xaero_off", new Object[0])
            );
      } else if (par1EnumOptions == ModOptions.LIGHT_OVERLAY_COLOR) {
         s = s + "§" + ENCHANT_COLORS[this.lightOverlayColor] + class_1074.method_4662(ENCHANT_COLOR_NAMES[this.lightOverlayColor], new Object[0]);
      } else if (par1EnumOptions == ModOptions.UI_SCALE) {
         s = s
            + (
               this.uiScale <= 1
                  ? class_1074.method_4662("gui.xaero_ui_scale_auto", new Object[0]) + " (" + this.getAutoUIScale() + ")"
                  : (
                     this.uiScale == 11
                        ? class_1074.method_4662("gui.xaero_ui_scale_mc", new Object[0])
                           + " ("
                           + (int)class_310.method_1551().method_22683().method_4495()
                           + ")"
                        : this.uiScale
                  )
            );
      } else if (par1EnumOptions == ModOptions.LIGHT_OVERLAY_MAX_LIGHT) {
         s = s + this.lightOverlayMaxLight;
      } else if (par1EnumOptions == ModOptions.LIGHT_OVERLAY_MIN_LIGHT) {
         s = s + this.lightOverlayMinLight;
      } else if (par1EnumOptions == ModOptions.MINIMAP_FRAME_COLOR) {
         s = s + "§" + ENCHANT_COLORS[this.minimapFrameColor] + class_1074.method_4662(ENCHANT_COLOR_NAMES[this.minimapFrameColor], new Object[0]);
      } else if (par1EnumOptions == ModOptions.COMPASS_COLOR) {
         s = s + "§" + ENCHANT_COLORS[this.compassColor] + class_1074.method_4662(ENCHANT_COLOR_NAMES[this.compassColor], new Object[0]);
      } else if (par1EnumOptions == ModOptions.NORTH_COMPASS_COLOR) {
         int effectiveColor = this.getNorthCompassColor();
         s = s
            + (
               effectiveColor != this.northCompassColor
                  ? class_1074.method_4662("gui.xaero_north_compass_color_default", new Object[0])
                  : "§" + ENCHANT_COLORS[effectiveColor] + class_1074.method_4662(ENCHANT_COLOR_NAMES[effectiveColor], new Object[0])
            );
      } else if (par1EnumOptions == ModOptions.DOTS_SIZE) {
         s = s + this.modMain.getEntityRadarCategoryManager().getRootCategory().getSettingValue(EntityRadarCategorySettings.DOT_SIZE);
         if (usingSafeMode) {
            s = s + "§e (" + getTranslation(false) + ")";
         }
      } else if (par1EnumOptions == ModOptions.SIZE) {
         s = s
            + (
               this.minimapSize > 0
                  ? this.minimapSize + ""
                  : class_1074.method_4662("gui.xaero_auto_map_size", new Object[0]) + " (" + this.getMinimapSize() + ")"
            );
      } else if (par1EnumOptions != ModOptions.WAYPOINTS_ICON_SCALE && par1EnumOptions != ModOptions.WAYPOINTS_DISTANCE_SCALE) {
         if (par1EnumOptions == ModOptions.WAYPOINTS_NAME_SCALE) {
            int settingValue = this.waypointsIngameNameScale;
            s = s
               + (
                  settingValue <= 0
                     ? class_1074.method_4662("gui.xaero_ui_scale_auto", new Object[0]) + " (" + this.getWaypointsIngameNameScale() + ")"
                     : (
                        settingValue == 17
                           ? class_1074.method_4662("gui.xaero_ui_scale_mc", new Object[0])
                              + " ("
                              + (int)class_310.method_1551().method_22683().method_4495()
                              + ")"
                           : settingValue
                     )
               );
         } else if (par1EnumOptions != ModOptions.TRACKED_PLAYER_WORLD_ICON_SCALE
            && par1EnumOptions != ModOptions.TRACKED_PLAYER_WORLD_NAME_SCALE
            && par1EnumOptions != ModOptions.TRACKED_PLAYER_MINIMAP_ICON_SCALE) {
            if (par1EnumOptions == ModOptions.HEADS_SCALE) {
               return s + this.getRadarSettingOptionName(EntityRadarCategorySettings.ICON_SCALE);
            }

            if (par1EnumOptions == ModOptions.HEIGHT_LIMIT) {
               return s + this.getRadarSettingOptionName(EntityRadarCategorySettings.HEIGHT_LIMIT);
            }

            if (par1EnumOptions == ModOptions.START_FADING_AT) {
               return s + this.getRadarSettingOptionName(EntityRadarCategorySettings.START_FADING_AT);
            }

            if (par1EnumOptions == ModOptions.WAYPOINTS_CLOSE_SCALE) {
               return this.getEnumFloatSliderText(s, "%.3f", par1EnumOptions);
            }

            if (par1EnumOptions == ModOptions.AUTO_CONVERT_TO_KM) {
               s = s
                  + (
                     this.autoConvertWaypointDistanceToKmThreshold == -1
                        ? class_1074.method_4662("gui.xaero_auto_convert_wp_distance_km_never", new Object[0])
                        : this.autoConvertWaypointDistanceToKmThreshold
                  );
            } else if (par1EnumOptions == ModOptions.WP_DISTANCE_PRECISION) {
               s = s + this.waypointDistancePrecision;
            } else if (par1EnumOptions == ModOptions.MANUAL_CAVE_MODE_START) {
               s = s
                  + (this.manualCaveModeStartAuto ? class_1074.method_4662("gui.xaero_manual_cave_mode_start_auto", new Object[0]) : this.manualCaveModeStart);
            } else if (par1EnumOptions == ModOptions.CHUNK_GRID_LINE_WIDTH) {
               s = s + this.chunkGridLineWidth;
            } else if (par1EnumOptions == ModOptions.WAYPOINT_ONMAP_SCALE) {
               s = s + (this.waypointOnMapScale <= 0 ? class_1074.method_4662("gui.xaero_waypoint_onmap_scale_auto", new Object[0]) : this.waypointOnMapScale);
            } else if (par1EnumOptions == ModOptions.INFO_DISPLAY_BG_OPACITY) {
               s = s + this.infoDisplayBackgroundOpacity;
            } else if (par1EnumOptions == ModOptions.CAVE_MODE_TOGGLE_TIMER) {
               s = s + this.caveModeToggleTimer + " ms";
            } else if (par1EnumOptions == ModOptions.MAIN_DOT_SIZE) {
               s = s + this.mainDotSize;
               if (usingSafeMode) {
                  s = s + "§e (" + getTranslation(false) + ")";
               }
            } else {
               if (par1EnumOptions != ModOptions.COMPASS_SCALE) {
                  return this.getEnumFloatSliderText(s, "%.1f", par1EnumOptions);
               }

               s = s + (this.compassDirectionScale <= 0 ? class_1074.method_4662("gui.xaero_compass_scale_auto", new Object[0]) : this.compassDirectionScale);
            }
         } else {
            int settingValue = par1EnumOptions == ModOptions.TRACKED_PLAYER_WORLD_ICON_SCALE
               ? this.trackedPlayerWorldIconScale
               : (par1EnumOptions == ModOptions.TRACKED_PLAYER_WORLD_NAME_SCALE ? this.trackedPlayerWorldNameScale : this.trackedPlayerMinimapIconScale);
            s = s
               + (
                  settingValue <= 0
                     ? class_1074.method_4662("gui.xaero_ui_scale_auto", new Object[0]) + " (" + this.getAutoUIScale() + ")"
                     : (
                        (double)settingValue == par1EnumOptions.getValueMax()
                           ? class_1074.method_4662("gui.xaero_ui_scale_mc", new Object[0])
                              + " ("
                              + (int)class_310.method_1551().method_22683().method_4495()
                              + ")"
                           : settingValue
                     )
               );
         }
      } else {
         int settingValue = par1EnumOptions == ModOptions.WAYPOINTS_ICON_SCALE ? this.waypointsIngameIconScale : this.waypointsIngameDistanceScale;
         s = s
            + (
               settingValue <= 0
                  ? class_1074.method_4662("gui.xaero_ui_scale_auto", new Object[0]) + " (" + this.getAutoUIScale() + ")"
                  : (
                     settingValue == 17
                        ? class_1074.method_4662("gui.xaero_ui_scale_mc", new Object[0])
                           + " ("
                           + (int)class_310.method_1551().method_22683().method_4495()
                           + ")"
                        : settingValue
                  )
            );
      }

      return s;
   }

   public boolean usesWorldMapScreenValue(ModOptions par1EnumOptions) {
      return this.modMain.getSupportMods().shouldUseWorldMapChunks()
         && par1EnumOptions == ModOptions.MANUAL_CAVE_MODE_START
         && this.modMain.getSupportMods().worldmapSupport.caveLayersAreUsable();
   }

   protected String getEnumFloatSliderText(String s, String f, ModOptions par1EnumOptions) {
      String f1 = String.format(f, this.getOptionDoubleValue(par1EnumOptions));
      if (par1EnumOptions == ModOptions.WAYPOINTS_DISTANCE) {
         double waypointsDistance = this.getMaxWaypointsDistance();
         if (waypointsDistance == 0.0) {
            f1 = class_1074.method_4662("gui.xaero_unlimited", new Object[0]);
         } else {
            f1 = (int)waypointsDistance + "m";
         }
      } else if (par1EnumOptions == ModOptions.WAYPOINTS_DISTANCE_MIN) {
         if (this.waypointsDistanceMin == 0.0) {
            f1 = class_1074.method_4662("gui.xaero_off", new Object[0]);
         } else {
            f1 = (int)this.waypointsDistanceMin + "m";
         }
      } else if (par1EnumOptions == ModOptions.ARROW_SCALE) {
         f1 = f1 + "x";
      }

      return s + f1;
   }

   public boolean getBooleanValue(ModOptions o) {
      if (o == ModOptions.MINIMAP) {
         return this.getMinimap();
      } else if (o == ModOptions.CAVE_MAPS) {
         return this.getCaveMaps(false);
      } else if (o == ModOptions.WAYPOINTS) {
         return this.getShowWaypoints();
      } else if (o == ModOptions.DEATHPOINTS) {
         return this.getDeathpoints();
      } else if (o == ModOptions.OLD_DEATHPOINTS) {
         return this.getOldDeathpoints();
      } else if (o == ModOptions.INGAME_WAYPOINTS) {
         return this.getShowIngameWaypoints();
      } else if (o == ModOptions.NORTH) {
         return this.getLockNorth(this.getMinimapSize(), this.minimapShape);
      } else if (o != ModOptions.SAFE_MAP) {
         if (o == ModOptions.AA) {
            return this.getAntiAliasing();
         } else if (o == ModOptions.SMOOTH_DOTS) {
            return this.getSmoothDots();
         } else if (o == ModOptions.WORLD_MAP) {
            return this.getUseWorldMap();
         } else if (o == ModOptions.TERRAIN_DEPTH) {
            return this.getTerrainDepth();
         } else if (o == ModOptions.RADAR_DISPLAYED) {
            return this.getEntityRadar();
         } else if (o == ModOptions.ADJUST_HEIGHT_FOR_SHORT_BLOCKS) {
            return this.getAdjustHeightForCarpetLikeBlocks();
         } else {
            return o == ModOptions.BIOME_BLENDING ? this.getBiomeBlending() : this.getClientBooleanValue(o);
         }
      } else {
         return this.mapSafeMode
            || this.modMain.getInterfaces().getMinimapInterface().getMinimapFBORenderer().isTriedFBO()
               && !this.modMain.getInterfaces().getMinimapInterface().getMinimapFBORenderer().isLoadedFBO();
      }
   }

   public boolean getClientBooleanValue(ModOptions o) {
      if (o.isIngameOnly() && !canEditIngameSettings()) {
         return false;
      } else if (o == ModOptions.IGNORE_HEIGHTMAPS) {
         return this.isIgnoreHeightmaps();
      } else if (o == ModOptions.MINIMAP) {
         return BuiltInHudModules.MINIMAP.isActive();
      } else if (o == ModOptions.WAYPOINTS) {
         return this.showWaypoints;
      } else if (o == ModOptions.DEATHPOINTS) {
         return this.deathpoints;
      } else if (o == ModOptions.OLD_DEATHPOINTS) {
         return this.oldDeathpoints;
      } else if (o == ModOptions.INGAME_WAYPOINTS) {
         return this.showIngameWaypoints;
      } else if (o == ModOptions.REDSTONE) {
         return this.displayRedstone;
      } else if (o == ModOptions.NORTH) {
         return this.lockNorth;
      } else if (o == ModOptions.SLIME_CHUNKS) {
         return this.slimeChunks;
      } else if (o == ModOptions.SAFE_MAP) {
         return this.mapSafeMode;
      } else if (o == ModOptions.AA) {
         return this.antiAliasing;
      } else if (o == ModOptions.LIGHT) {
         return this.lighting;
      } else if (o == ModOptions.COMPASS) {
         return this.compassOverEverything;
      } else if (o == ModOptions.FLOWERS) {
         return this.showFlowers;
      } else if (o == ModOptions.KEEP_WP_NAMES) {
         return this.keepWaypointNames;
      } else if (o == ModOptions.SMOOTH_DOTS) {
         return this.smoothDots;
      } else if (o == ModOptions.WORLD_MAP) {
         return this.worldMap;
      } else if (o == ModOptions.TERRAIN_DEPTH) {
         return this.terrainDepth;
      } else if (o == ModOptions.BLOCK_TRANSPARENCY) {
         return this.blockTransparency;
      } else if (o == ModOptions.OPEN_SLIME_SETTINGS) {
         return this.openSlimeSettings;
      } else if (o == ModOptions.ALWAYS_SHOW_DISTANCE) {
         return this.alwaysShowDistance;
      } else if (o == ModOptions.CROSS_DIMENSIONAL_TP) {
         return this.crossDimensionalTp;
      } else if (o == ModOptions.BIOMES_VANILLA) {
         return this.biomeColorsVanillaMode;
      } else if (o == ModOptions.CENTERED_ENLARGED) {
         return this.centeredEnlarged;
      } else if (o == ModOptions.HIDE_WP_COORDS) {
         return this.hideWaypointCoordinates;
      } else if (o == ModOptions.WAYPOINTS_ALL_SETS) {
         return this.renderAllSets;
      } else if (o == ModOptions.WAYPOINTS_BOTTOM) {
         return this.waypointsBottom;
      } else if (o == ModOptions.RADAR_DISPLAYED) {
         return this.entityRadar;
      } else if (o == ModOptions.ENTITY_HEIGHT) {
         return this.modMain.getEntityRadarCategoryManager().getRootCategory().getSettingValue(EntityRadarCategorySettings.HEIGHT_FADE);
      } else if (o == ModOptions.ENTITY_NAMETAGS) {
         return this.modMain.getEntityRadarCategoryManager().getRootCategory().getSettingValue(EntityRadarCategorySettings.ALWAYS_NAMETAGS);
      } else if (o == ModOptions.ICON_NAME_FALLBACK) {
         return this.modMain.getEntityRadarCategoryManager().getRootCategory().getSettingValue(EntityRadarCategorySettings.ICON_NAME_FALLBACK);
      } else if (o == ModOptions.UPDATE_NOTIFICATION) {
         return updateNotification;
      } else if (o == ModOptions.ADJUST_HEIGHT_FOR_SHORT_BLOCKS) {
         return this.adjustHeightForCarpetLikeBlocks;
      } else if (o == ModOptions.PARTIAL_Y_TELEPORTATION) {
         return this.partialYTeleportation;
      } else if (o == ModOptions.DELETE_REACHED_DEATHPOINTS) {
         return this.deleteReachedDeathpoints;
      } else if (o == ModOptions.HIDE_MINIMAP_UNDER_SCREEN) {
         return this.hideMinimapUnderScreen;
      } else if (o == ModOptions.HIDE_MINIMAP_UNDER_F3) {
         return this.hideMinimapUnderF3;
      } else if (o == ModOptions.TEMPORARY_WAYPOINTS_GLOBAL) {
         return this.temporaryWaypointsGlobal;
      } else if (o == ModOptions.KEEP_ENLARGED_UNLOCKED) {
         return this.keepUnlockedWhenEnlarged;
      } else if (o == ModOptions.TOGGLED_ENLARGED) {
         return this.enlargedMinimapAToggle;
      } else if (o == ModOptions.DISPLAY_STAINED_GLASS) {
         return this.displayStainedGlass;
      } else if (o == ModOptions.SWITCH_TO_AUTO_ON_DEATH) {
         return this.switchToAutoOnDeath;
      } else if (o == ModOptions.LEGIBLE_CAVE_MAPS) {
         return this.legibleCaveMaps;
      } else if (o == ModOptions.BIOME_BLENDING) {
         return this.biomeBlending;
      } else if (o == ModOptions.TRACKED_PLAYERS_ON_MAP) {
         return this.displayTrackedPlayersOnMap;
      } else if (o == ModOptions.TRACKED_PLAYERS_IN_WORLD) {
         return this.displayTrackedPlayersInWorld;
      } else if (o == ModOptions.SCALED_MAX_WAYPOINT_DISTANCE) {
         return this.dimensionScaledMaxWaypointDistance;
      } else if (o == ModOptions.PAC_CLAIMS) {
         return this.displayClaims;
      } else {
         return o == ModOptions.PAC_CURRENT_CLAIM ? this.displayCurrentClaim : false;
      }
   }

   public static String getTranslation(boolean o) {
      return class_1074.method_4662("gui.xaero_" + (o ? "on" : "off"), new Object[0]);
   }

   private void changeZoomUnchecked(int direction) {
      this.zoom = class_3532.method_15387(this.zoom + direction, zooms.length);
   }

   public void changeZoom(int direction) {
      XaeroMinimapSession minimapSession = XaeroMinimapSession.getCurrentSession();
      if (minimapSession != null) {
         double targetBefore = minimapSession.getMinimapProcessor().getTargetZoom();
         int attempts = 0;

         do {
            this.changeZoomUnchecked(direction);
         } while (++attempts < zooms.length && targetBefore == minimapSession.getMinimapProcessor().getTargetZoom());

         if (attempts == zooms.length) {
            this.changeZoomUnchecked(direction);
         }
      } else {
         this.changeZoomUnchecked(direction);
      }
   }

   public void toggleBooleanOptionValue(ModOptions par1EnumOptions) {
      if (!par1EnumOptions.isIngameOnly() || canEditIngameSettings()) {
         if (par1EnumOptions.enumBoolean) {
            this.setOptionValue(par1EnumOptions, !(Boolean)this.getOptionValue(par1EnumOptions));
         }
      }
   }

   public void setOptionValue(ModOptions par1EnumOptions, Object value) {
      if (!par1EnumOptions.isIngameOnly() || canEditIngameSettings()) {
         if (this.usesWorldMapOptionValue(par1EnumOptions)) {
            this.modMain.getSupportMods().worldmapSupport.openSettings();
         } else if (this.usesWorldMapScreenValue(par1EnumOptions)) {
            this.modMain.getSupportMods().worldmapSupport.openScreenForOption(par1EnumOptions);
         } else {
            if (par1EnumOptions == ModOptions.ZOOM) {
               this.changeZoomUnchecked((Integer)value - this.zoom);
               this.refreshScreen();
            } else if (par1EnumOptions == ModOptions.MINIMAP) {
               BuiltInHudModules.MINIMAP.setActive((Boolean)value);
            } else if (par1EnumOptions == ModOptions.CAVE_MAPS) {
               this.caveMaps = (Integer)value;
            } else if (par1EnumOptions == ModOptions.CAVE_ZOOM) {
               this.caveZoom = (Integer)value;
            } else if (par1EnumOptions == ModOptions.WAYPOINTS) {
               this.showWaypoints = (Boolean)value;
            } else if (par1EnumOptions == ModOptions.DEATHPOINTS) {
               this.deathpoints = (Boolean)value;
            } else if (par1EnumOptions == ModOptions.OLD_DEATHPOINTS) {
               this.oldDeathpoints = (Boolean)value;
            } else if (par1EnumOptions == ModOptions.INGAME_WAYPOINTS) {
               this.showIngameWaypoints = (Boolean)value;
            } else if (par1EnumOptions == ModOptions.REDSTONE) {
               this.displayRedstone = (Boolean)value;
            } else if (par1EnumOptions == ModOptions.DISTANCE) {
               this.distance = (Integer)value;
            } else if (par1EnumOptions == ModOptions.NORTH) {
               this.lockNorth = (Boolean)value;
            } else if (par1EnumOptions == ModOptions.SLIME_CHUNKS) {
               this.slimeChunks = (Boolean)value;
            } else if (par1EnumOptions == ModOptions.SAFE_MAP) {
               this.mapSafeMode = (Boolean)value;
               XaeroMinimapSession minimapSession = XaeroMinimapSession.getCurrentSession();
               if (minimapSession != null) {
                  minimapSession.getMinimapProcessor().setToResetImage(true);
               }

               this.refreshScreen();
            } else if (par1EnumOptions == ModOptions.AA) {
               this.antiAliasing = (Boolean)value;
            } else if (par1EnumOptions == ModOptions.COLOURS) {
               this.blockColours = (Integer)value;
            } else if (par1EnumOptions == ModOptions.LIGHT) {
               this.lighting = (Boolean)value;
            } else if (par1EnumOptions == ModOptions.COMPASS) {
               this.compassOverEverything = (Boolean)value;
            } else if (par1EnumOptions == ModOptions.FLOWERS) {
               this.showFlowers = (Boolean)value;
            } else if (par1EnumOptions == ModOptions.KEEP_WP_NAMES) {
               this.keepWaypointNames = (Boolean)value;
            } else if (par1EnumOptions == ModOptions.ARROW_COLOUR) {
               this.arrowColour = (Integer)value;
            } else if (par1EnumOptions == ModOptions.SMOOTH_DOTS) {
               this.smoothDots = (Boolean)value;
            } else if (par1EnumOptions == ModOptions.DOTS_STYLE) {
               this.dotsStyle = (Integer)value;
            } else if (par1EnumOptions == ModOptions.WORLD_MAP) {
               this.worldMap = (Boolean)value;
               this.refreshScreen();
            } else if (par1EnumOptions == ModOptions.TERRAIN_DEPTH) {
               this.terrainDepth = (Boolean)value;
            } else if (par1EnumOptions == ModOptions.TERRAIN_SLOPES) {
               this.terrainSlopes = (Integer)value;
            } else if (par1EnumOptions == ModOptions.MAIN_ENTITY_AS) {
               this.mainEntityAs = (Integer)value;
            } else if (par1EnumOptions == ModOptions.BLOCK_TRANSPARENCY) {
               this.blockTransparency = (Boolean)value;
               XaeroMinimapSession minimapSession = XaeroMinimapSession.getCurrentSession();
               if (minimapSession != null) {
                  minimapSession.getMinimapProcessor().setToResetImage(true);
               }
            } else if (par1EnumOptions == ModOptions.HIDE_WORLD_NAMES) {
               this.hideWorldNames = (Integer)value;
            } else if (par1EnumOptions == ModOptions.OPEN_SLIME_SETTINGS) {
               this.openSlimeSettings = (Boolean)value;
            } else if (par1EnumOptions == ModOptions.ALWAYS_SHOW_DISTANCE) {
               this.alwaysShowDistance = (Boolean)value;
            } else if (par1EnumOptions == ModOptions.CROSS_DIMENSIONAL_TP) {
               this.crossDimensionalTp = (Boolean)value;
            } else if (par1EnumOptions == ModOptions.BIOMES_VANILLA) {
               this.biomeColorsVanillaMode = (Boolean)value;
            } else if (par1EnumOptions == ModOptions.CENTERED_ENLARGED) {
               this.centeredEnlarged = (Boolean)value;
            } else if (par1EnumOptions == ModOptions.ZOOM_ON_ENLARGE) {
               this.zoomOnEnlarged = (Integer)value;
            } else if (par1EnumOptions == ModOptions.MINIMAP_TEXT_ALIGN) {
               this.minimapTextAlign = (Integer)value;
            } else if (par1EnumOptions == ModOptions.COMPASS_LOCATION) {
               this.compassLocation = (Integer)value;
            } else if (par1EnumOptions == ModOptions.HIDE_WP_COORDS) {
               this.hideWaypointCoordinates = (Boolean)value;
            } else if (par1EnumOptions == ModOptions.WAYPOINTS_ALL_SETS) {
               this.renderAllSets = (Boolean)value;
            } else if (par1EnumOptions == ModOptions.IGNORE_HEIGHTMAPS) {
               MinimapWorldRootContainer currentRootContainer = BuiltInHudModules.MINIMAP.getCurrentSession().getWorldManager().getAutoRootContainer();
               currentRootContainer.getConfig().setIgnoreHeightmaps((Boolean)value);
               currentRootContainer.getSession().getWorldManagerIO().getRootConfigIO().save(currentRootContainer);
            } else if (par1EnumOptions == ModOptions.WAYPOINTS_BOTTOM) {
               this.waypointsBottom = (Boolean)value;
            } else if (par1EnumOptions == ModOptions.MINIMAP_SHAPE) {
               this.minimapShape = (Integer)value;
            } else if (par1EnumOptions == ModOptions.LIGHT_OVERLAY_TYPE) {
               this.lightOverlayType = (Integer)value;
            } else if (par1EnumOptions == ModOptions.BOSS_HEALTH_PUSHBOX) {
               this.bossHealthPushBox = (Integer)value;
            } else if (par1EnumOptions == ModOptions.POTION_EFFECTS_PUSHBOX) {
               this.potionEffectPushBox = (Integer)value;
            } else if (par1EnumOptions == ModOptions.MINIMAP_FRAME) {
               this.minimapFrame = (Integer)value;
            } else if (par1EnumOptions == ModOptions.MULTIPLE_WAYPOINT_INFO) {
               this.displayMultipleWaypointInfo = (Integer)value;
            } else if (par1EnumOptions == ModOptions.RADAR_DISPLAYED) {
               this.entityRadar = (Boolean)value;
            } else {
               if (par1EnumOptions == ModOptions.EAMOUNT) {
                  this.setOptionIndexForRadarSetting(EntityRadarCategorySettings.ENTITY_NUMBER, (Integer)value);
                  this.modMain.getEntityRadarCategoryManager().save();
                  return;
               }

               if (par1EnumOptions == ModOptions.ENTITY_HEIGHT) {
                  this.setOptionIndexForRadarSetting(EntityRadarCategorySettings.HEIGHT_FADE, (Boolean)value ? 1 : 0);
                  this.modMain.getEntityRadarCategoryManager().save();
                  return;
               }

               if (par1EnumOptions == ModOptions.ENTITY_NAMETAGS) {
                  this.setOptionIndexForRadarSetting(EntityRadarCategorySettings.ALWAYS_NAMETAGS, (Boolean)value ? 1 : 0);
                  this.modMain.getEntityRadarCategoryManager().save();
                  return;
               }

               if (par1EnumOptions == ModOptions.RADAR_Y_DISPLAYED) {
                  this.setOptionIndexForRadarSetting(EntityRadarCategorySettings.DISPLAY_Y, (Integer)value);
                  return;
               }

               if (par1EnumOptions == ModOptions.ICON_NAME_FALLBACK) {
                  this.setOptionIndexForRadarSetting(EntityRadarCategorySettings.ICON_NAME_FALLBACK, (Boolean)value ? 1 : 0);
                  this.modMain.getEntityRadarCategoryManager().save();
                  return;
               }

               if (par1EnumOptions == ModOptions.RADAR_ICONS_DISPLAYED) {
                  this.setOptionIndexForRadarSetting(EntityRadarCategorySettings.ICONS, (Integer)value);
                  this.modMain.getEntityRadarCategoryManager().save();
                  return;
               }

               if (par1EnumOptions == ModOptions.RADAR_NAMES_DISPLAYED) {
                  this.setOptionIndexForRadarSetting(EntityRadarCategorySettings.NAMES, (Integer)value);
                  this.modMain.getEntityRadarCategoryManager().save();
                  return;
               }

               if (par1EnumOptions == ModOptions.UPDATE_NOTIFICATION) {
                  updateNotification = (Boolean)value;
               } else if (par1EnumOptions == ModOptions.ADJUST_HEIGHT_FOR_SHORT_BLOCKS) {
                  this.adjustHeightForCarpetLikeBlocks = (Boolean)value;
               } else if (par1EnumOptions == ModOptions.PARTIAL_Y_TELEPORTATION) {
                  this.partialYTeleportation = (Boolean)value;
               } else if (par1EnumOptions == ModOptions.DELETE_REACHED_DEATHPOINTS) {
                  this.deleteReachedDeathpoints = (Boolean)value;
               } else if (par1EnumOptions == ModOptions.HIDE_MINIMAP_UNDER_SCREEN) {
                  this.hideMinimapUnderScreen = (Boolean)value;
               } else if (par1EnumOptions == ModOptions.HIDE_MINIMAP_UNDER_F3) {
                  this.hideMinimapUnderF3 = (Boolean)value;
               } else {
                  if (par1EnumOptions == ModOptions.RADAR_OVER_FRAME) {
                     this.setOptionIndexForRadarSetting(EntityRadarCategorySettings.RENDER_OVER_MINIMAP, (Integer)value);
                     return;
                  }

                  if (par1EnumOptions == ModOptions.TEMPORARY_WAYPOINTS_GLOBAL) {
                     this.temporaryWaypointsGlobal = (Boolean)value;
                  } else if (par1EnumOptions == ModOptions.KEEP_ENLARGED_UNLOCKED) {
                     this.keepUnlockedWhenEnlarged = (Boolean)value;
                  } else if (par1EnumOptions == ModOptions.TOGGLED_ENLARGED) {
                     this.enlargedMinimapAToggle = (Boolean)value;
                  } else if (par1EnumOptions == ModOptions.DISPLAY_STAINED_GLASS) {
                     this.displayStainedGlass = (Boolean)value;
                     XaeroMinimapSession minimapSession = XaeroMinimapSession.getCurrentSession();
                     if (minimapSession != null) {
                        minimapSession.getMinimapProcessor().setToResetImage(true);
                     }
                  } else if (par1EnumOptions == ModOptions.SWITCH_TO_AUTO_ON_DEATH) {
                     this.switchToAutoOnDeath = (Boolean)value;
                  } else if (par1EnumOptions == ModOptions.LEGIBLE_CAVE_MAPS) {
                     this.legibleCaveMaps = (Boolean)value;
                     XaeroMinimapSession minimapSession = XaeroMinimapSession.getCurrentSession();
                     if (minimapSession != null) {
                        minimapSession.getMinimapProcessor().setToResetImage(true);
                     }
                  } else if (par1EnumOptions == ModOptions.BIOME_BLENDING) {
                     this.biomeBlending = (Boolean)value;
                  } else if (par1EnumOptions == ModOptions.TRACKED_PLAYERS_ON_MAP) {
                     this.displayTrackedPlayersOnMap = (Boolean)value;
                  } else if (par1EnumOptions == ModOptions.TRACKED_PLAYERS_IN_WORLD) {
                     this.displayTrackedPlayersInWorld = (Boolean)value;
                  } else if (par1EnumOptions == ModOptions.SCALED_MAX_WAYPOINT_DISTANCE) {
                     this.dimensionScaledMaxWaypointDistance = !this.dimensionScaledMaxWaypointDistance;
                  } else if (par1EnumOptions == ModOptions.PAC_CLAIMS) {
                     this.displayClaims = !this.displayClaims;
                     XaeroMinimapSession minimapSession = XaeroMinimapSession.getCurrentSession();
                     if (minimapSession != null) {
                        DimensionHighlighterHandler hh = minimapSession.getMinimapProcessor().getMinimapWriter().getDimensionHighlightHandler();
                        if (hh != null) {
                           hh.requestRefresh();
                        }
                     }
                  } else if (par1EnumOptions == ModOptions.PAC_CURRENT_CLAIM) {
                     this.displayCurrentClaim = !this.displayCurrentClaim;
                  }
               }
            }

            try {
               this.saveSettings();
            } catch (IOException var5) {
               MinimapLogs.LOGGER.error("suppressed exception", var5);
            }
         }
      }
   }

   private void refreshScreen() {
      class_437 currentScreen = class_310.method_1551().field_1755;
      GuiSettings settingsScreen = currentScreen instanceof GuiSettings ? (GuiSettings)currentScreen : null;
      int focusedIndex = -1;
      if (settingsScreen != null) {
         focusedIndex = settingsScreen.getIndex(settingsScreen.method_25399());
      }

      class_310.method_1551().method_1507(currentScreen);
      settingsScreen.restoreFocus(focusedIndex);
   }

   public void setOptionDoubleValue(ModOptions options, double d) {
      if (!options.isIngameOnly() || canEditIngameSettings()) {
         if (this.usesWorldMapOptionValue(options)) {
            this.modMain.getSupportMods().worldmapSupport.openSettings();
         } else if (this.usesWorldMapScreenValue(options)) {
            this.modMain.getSupportMods().worldmapSupport.openScreenForOption(options);
         } else {
            if (options == ModOptions.OPACITY) {
               this.minimapOpacity = d;
            }

            if (options == ModOptions.WAYPOINTS_ICON_SCALE) {
               this.waypointsIngameIconScale = (int)d;
            }

            if (options == ModOptions.WAYPOINTS_DISTANCE_SCALE) {
               this.waypointsIngameDistanceScale = (int)d;
            }

            if (options == ModOptions.WAYPOINTS_NAME_SCALE) {
               this.waypointsIngameNameScale = (int)d;
            }

            if (options == ModOptions.WAYPOINTS_CLOSE_SCALE) {
               this.waypointsIngameCloseScale = d;
            }

            if (options == ModOptions.DOT_NAME_SCALE) {
               this.dotNameScale = d;
            }

            if (options == ModOptions.WAYPOINTS_DISTANCE) {
               this.waypointsDistanceExp = (int)d;
            }

            if (options == ModOptions.WAYPOINTS_DISTANCE_MIN) {
               this.waypointsDistanceMin = (double)((int)d);
            }

            if (options == ModOptions.ARROW_SCALE) {
               this.arrowScale = d;
            }

            if (options == ModOptions.WAYPOINT_OPACITY_INGAME) {
               this.waypointOpacityIngame = (int)d;
            }

            if (options == ModOptions.WAYPOINT_OPACITY_MAP) {
               this.waypointOpacityMap = (int)d;
            }

            if (options == ModOptions.WAYPOINT_LOOKING_ANGLE) {
               this.lookingAtAngle = (int)d;
            }

            if (options == ModOptions.WAYPOINT_VERTICAL_LOOKING_ANGLE) {
               this.lookingAtAngleVertical = (int)d;
            }

            if (options == ModOptions.CAVE_MAPS_DEPTH) {
               this.caveMapsDepth = (int)d;
            }

            if (options == ModOptions.CHUNK_GRID) {
               this.chunkGrid = (int)d;
            }

            if (options == ModOptions.PLAYER_ARROW_OPACITY) {
               this.playerArrowOpacity = (int)d;
            }

            if (options == ModOptions.LIGHT_OVERLAY_COLOR) {
               this.lightOverlayColor = (int)d;
            }

            if (options == ModOptions.LIGHT_OVERLAY_MAX_LIGHT) {
               this.lightOverlayMaxLight = (int)d;
            }

            if (options == ModOptions.LIGHT_OVERLAY_MIN_LIGHT) {
               this.lightOverlayMinLight = (int)d;
            }

            if (options == ModOptions.SIZE) {
               if (d == 54.0) {
                  this.minimapSize = 0;
               } else {
                  this.minimapSize = (int)d;
               }
            }

            if (options == ModOptions.UI_SCALE) {
               this.uiScale = (int)d;
            }

            if (options == ModOptions.MINIMAP_FRAME_COLOR) {
               this.minimapFrameColor = (int)d;
            }

            if (options == ModOptions.COMPASS_SCALE) {
               this.compassDirectionScale = (int)d;
            }

            if (options == ModOptions.COMPASS_COLOR) {
               this.compassColor = (int)d;
            }

            if (options == ModOptions.NORTH_COMPASS_COLOR) {
               this.northCompassColor = (int)d;
            }

            if (options == ModOptions.DOTS_SIZE) {
               this.setOptionIndexForRadarSetting(EntityRadarCategorySettings.DOT_SIZE, (int)d);
            } else if (options != ModOptions.HEADS_SCALE) {
               if (options == ModOptions.HEIGHT_LIMIT) {
                  this.setOptionIndexForRadarSetting(EntityRadarCategorySettings.HEIGHT_LIMIT, (int)d);
               } else if (options == ModOptions.START_FADING_AT) {
                  this.setOptionIndexForRadarSetting(EntityRadarCategorySettings.START_FADING_AT, (int)d);
               } else {
                  if (options == ModOptions.AUTO_CONVERT_TO_KM) {
                     this.autoConvertWaypointDistanceToKmThreshold = d <= 0.0 ? (int)d : (int)Math.pow(10.0, d - 1.0);
                  }

                  if (options == ModOptions.WP_DISTANCE_PRECISION) {
                     this.waypointDistancePrecision = (int)d;
                  }

                  if (options == ModOptions.MAIN_DOT_SIZE) {
                     this.mainDotSize = (int)d;
                  }

                  if (options == ModOptions.MANUAL_CAVE_MODE_START) {
                     this.manualCaveModeStart = (int)d * 8 - 65;
                     this.manualCaveModeStartAuto = (int)d == 0;
                  }

                  if (options == ModOptions.CHUNK_GRID_LINE_WIDTH) {
                     this.chunkGridLineWidth = (int)d;
                  }

                  if (options == ModOptions.WAYPOINT_ONMAP_SCALE) {
                     this.waypointOnMapScale = (int)d;
                  }

                  if (options == ModOptions.INFO_DISPLAY_BG_OPACITY) {
                     this.infoDisplayBackgroundOpacity = (int)d;
                  }

                  if (options == ModOptions.CAVE_MODE_TOGGLE_TIMER) {
                     this.caveModeToggleTimer = (int)d;
                  }

                  if (options == ModOptions.TRACKED_PLAYER_WORLD_ICON_SCALE) {
                     this.trackedPlayerWorldIconScale = (int)d;
                  }

                  if (options == ModOptions.TRACKED_PLAYER_WORLD_NAME_SCALE) {
                     this.trackedPlayerWorldNameScale = (int)d;
                  }

                  if (options == ModOptions.TRACKED_PLAYER_MINIMAP_ICON_SCALE) {
                     this.trackedPlayerMinimapIconScale = (int)d;
                  }

                  if (options == ModOptions.PAC_CLAIMS_BORDER_OPACITY) {
                     this.claimsBorderOpacity = (int)d;
                     if (this.displayClaims) {
                        XaeroMinimapSession minimapSession = XaeroMinimapSession.getCurrentSession();
                        if (minimapSession != null) {
                           DimensionHighlighterHandler hh = minimapSession.getMinimapProcessor().getMinimapWriter().getDimensionHighlightHandler();
                           if (hh != null) {
                              hh.requestRefresh();
                           }
                        }
                     }
                  }

                  if (options == ModOptions.PAC_CLAIMS_FILL_OPACITY) {
                     this.claimsFillOpacity = (int)d;
                     if (this.displayClaims) {
                        XaeroMinimapSession minimapSession = XaeroMinimapSession.getCurrentSession();
                        if (minimapSession != null) {
                           DimensionHighlighterHandler hh = minimapSession.getMinimapProcessor().getMinimapWriter().getDimensionHighlightHandler();
                           if (hh != null) {
                              hh.requestRefresh();
                           }
                        }
                     }
                  }

                  try {
                     this.saveSettings();
                  } catch (IOException var8) {
                     MinimapLogs.LOGGER.error("suppressed exception", var8);
                  }
               }
            } else {
               double currentScale = this.modMain.getEntityRadarCategoryManager().getRootCategory().getSettingValue(EntityRadarCategorySettings.ICON_SCALE);
               this.setOptionIndexForRadarSetting(EntityRadarCategorySettings.ICON_SCALE, (int)d);
               double newScale = this.modMain.getEntityRadarCategoryManager().getRootCategory().getSettingValue(EntityRadarCategorySettings.ICON_SCALE);
               if (newScale < 1.0 || newScale < 1.0 != currentScale < 1.0) {
                  this.modMain.getInterfaces().getMinimapInterface().getMinimapFBORenderer().resetEntityIcons();
               }
            }
         }
      }
   }

   public double getOptionDoubleValue(ModOptions options) {
      if (options.isIngameOnly() && !canEditIngameSettings()) {
         return 0.0;
      } else if (options == ModOptions.OPACITY) {
         return this.minimapOpacity;
      } else if (options == ModOptions.WAYPOINTS_ICON_SCALE) {
         return (double)this.waypointsIngameIconScale;
      } else if (options == ModOptions.WAYPOINTS_DISTANCE_SCALE) {
         return (double)this.waypointsIngameDistanceScale;
      } else if (options == ModOptions.WAYPOINTS_NAME_SCALE) {
         return (double)this.waypointsIngameNameScale;
      } else if (options == ModOptions.WAYPOINTS_CLOSE_SCALE) {
         return this.waypointsIngameCloseScale;
      } else if (options == ModOptions.DOT_NAME_SCALE) {
         return this.dotNameScale;
      } else if (options == ModOptions.WAYPOINTS_DISTANCE) {
         return (double)this.waypointsDistanceExp;
      } else if (options == ModOptions.WAYPOINTS_DISTANCE_MIN) {
         return this.waypointsDistanceMin;
      } else if (options == ModOptions.ARROW_SCALE) {
         return this.arrowScale;
      } else if (options == ModOptions.WAYPOINT_OPACITY_INGAME) {
         return (double)this.waypointOpacityIngame;
      } else if (options == ModOptions.WAYPOINT_OPACITY_MAP) {
         return (double)this.waypointOpacityMap;
      } else if (options == ModOptions.WAYPOINT_LOOKING_ANGLE) {
         return (double)this.lookingAtAngle;
      } else if (options == ModOptions.WAYPOINT_VERTICAL_LOOKING_ANGLE) {
         return (double)this.lookingAtAngleVertical;
      } else if (options == ModOptions.CAVE_MAPS_DEPTH) {
         return (double)this.caveMapsDepth;
      } else if (options == ModOptions.CHUNK_GRID) {
         return (double)this.chunkGrid;
      } else if (options == ModOptions.PLAYER_ARROW_OPACITY) {
         return (double)this.playerArrowOpacity;
      } else if (options == ModOptions.LIGHT_OVERLAY_COLOR) {
         return (double)this.lightOverlayColor;
      } else if (options == ModOptions.LIGHT_OVERLAY_MAX_LIGHT) {
         return (double)this.lightOverlayMaxLight;
      } else if (options == ModOptions.LIGHT_OVERLAY_MIN_LIGHT) {
         return (double)this.lightOverlayMinLight;
      } else if (options == ModOptions.SIZE) {
         return (double)this.minimapSize;
      } else if (options == ModOptions.UI_SCALE) {
         return (double)this.uiScale;
      } else if (options == ModOptions.MINIMAP_FRAME_COLOR) {
         return (double)this.minimapFrameColor;
      } else if (options == ModOptions.COMPASS_SCALE) {
         return (double)this.compassDirectionScale;
      } else if (options == ModOptions.COMPASS_COLOR) {
         return (double)this.compassColor;
      } else if (options == ModOptions.NORTH_COMPASS_COLOR) {
         return (double)this.northCompassColor;
      } else if (options == ModOptions.DOTS_SIZE) {
         return (double)this.getOptionIndexForRadarSetting(EntityRadarCategorySettings.DOT_SIZE);
      } else if (options == ModOptions.HEADS_SCALE) {
         return (double)this.getOptionIndexForRadarSetting(EntityRadarCategorySettings.ICON_SCALE);
      } else if (options == ModOptions.HEIGHT_LIMIT) {
         return (double)this.getOptionIndexForRadarSetting(EntityRadarCategorySettings.HEIGHT_LIMIT);
      } else if (options == ModOptions.START_FADING_AT) {
         return (double)this.getOptionIndexForRadarSetting(EntityRadarCategorySettings.START_FADING_AT);
      } else if (options == ModOptions.AUTO_CONVERT_TO_KM) {
         return this.autoConvertWaypointDistanceToKmThreshold <= 0
            ? (double)this.autoConvertWaypointDistanceToKmThreshold
            : 1.0 + Math.log10((double)this.autoConvertWaypointDistanceToKmThreshold);
      } else if (options == ModOptions.WP_DISTANCE_PRECISION) {
         return (double)this.waypointDistancePrecision;
      } else if (options == ModOptions.MAIN_DOT_SIZE) {
         return (double)this.mainDotSize;
      } else if (options == ModOptions.MANUAL_CAVE_MODE_START) {
         return (double)((this.manualCaveModeStart + 65) / 8);
      } else if (options == ModOptions.CHUNK_GRID_LINE_WIDTH) {
         return (double)this.chunkGridLineWidth;
      } else if (options == ModOptions.WAYPOINT_ONMAP_SCALE) {
         return (double)this.waypointOnMapScale;
      } else if (options == ModOptions.INFO_DISPLAY_BG_OPACITY) {
         return (double)this.infoDisplayBackgroundOpacity;
      } else if (options == ModOptions.CAVE_MODE_TOGGLE_TIMER) {
         return (double)this.caveModeToggleTimer;
      } else if (options == ModOptions.TRACKED_PLAYER_WORLD_ICON_SCALE) {
         return (double)this.trackedPlayerWorldIconScale;
      } else if (options == ModOptions.TRACKED_PLAYER_WORLD_NAME_SCALE) {
         return (double)this.trackedPlayerWorldNameScale;
      } else if (options == ModOptions.TRACKED_PLAYER_MINIMAP_ICON_SCALE) {
         return (double)this.trackedPlayerMinimapIconScale;
      } else if (options == ModOptions.PAC_CLAIMS_BORDER_OPACITY) {
         return (double)this.claimsBorderOpacity;
      } else {
         return options == ModOptions.PAC_CLAIMS_FILL_OPACITY ? (double)this.claimsFillOpacity : 1.0;
      }
   }

   private <T> int getOptionIndexForRadarSetting(ObjectCategorySetting<T> setting) {
      EntityRadarCategory rootCategory = this.modMain.getEntityRadarCategoryManager().getRootCategory();
      return setting.getIndexWriter().apply(rootCategory.getSettingValue(setting));
   }

   public boolean minimapDisabled() {
      return (serverSettings & 1) != 1;
   }

   public boolean caveMapsDisabled() {
      return (serverSettings & 16384) != 16384
         || this.modMain.isFairPlay()
         || class_310.method_1551().field_1687 != null
            && (
               !MinimapClientWorldDataHelper.getCurrentWorldData().getSyncedRules().allowCaveModeOnServer
                     && class_310.method_1551().field_1687.method_27983() != class_1937.field_25180
                  || !MinimapClientWorldDataHelper.getCurrentWorldData().getSyncedRules().allowNetherCaveModeOnServer
                     && class_310.method_1551().field_1687.method_27983() == class_1937.field_25180
            );
   }

   public boolean showWaypointsDisabled() {
      return (serverSettings & 65536) != 65536;
   }

   public boolean deathpointsDisabled() {
      return (serverSettings & 2097152) == 0;
   }

   public void resetServerSettings() {
      serverSettings = defaultSettings;
   }

   public static void setServerSettings() {
   }

   public static boolean canEditIngameSettings() {
      MinimapSession minimapSession = BuiltInHudModules.MINIMAP.getCurrentSession();
      return minimapSession != null && minimapSession.getWorldState().getAutoWorldPath() != null;
   }

   private <T> String getRadarSettingOptionName(ObjectCategorySetting<T> setting) {
      EntityRadarCategory rootCategory = this.modMain.getEntityRadarCategoryManager().getRootCategory();
      return setting.getWidgetValueNameProvider().apply(rootCategory.getSettingValue(setting));
   }

   private <T> void setOptionIndexForRadarSetting(ObjectCategorySetting<T> setting, int index) {
      EntityRadarCategory rootCategory = this.modMain.getEntityRadarCategoryManager().getRootCategory();
      rootCategory.setSettingValue(setting, setting.getIndexReader().apply(index));
      if (class_310.method_1551().field_1755 instanceof GuiSettings) {
         ((GuiSettings)class_310.method_1551().field_1755).setShouldSaveRadar();
      }
   }

   public EntityRadarBackwardsCompatibilityConfig getEntityRadarBackwardsCompatibilityConfig() {
      return this.entityRadarBackwardsCompatibilityConfig;
   }

   public void resetEntityRadarBackwardsCompatibilityConfig() {
      this.entityRadarBackwardsCompatibilityConfig = new EntityRadarBackwardsCompatibilityConfig();
      this.foundOldRadarSettings = false;
   }

   public boolean foundOldRadarSettings() {
      return this.foundOldRadarSettings;
   }

   public int getManualCaveModeStart() {
      return this.usesWorldMapScreenValue(ModOptions.MANUAL_CAVE_MODE_START)
         ? this.modMain.getSupportMods().worldmapSupport.getManualCaveStart()
         : (this.manualCaveModeStartAuto ? Integer.MAX_VALUE : this.manualCaveModeStart);
   }
}
