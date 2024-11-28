package xaero.common.minimap.info;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import net.minecraft.class_1074;
import net.minecraft.class_1944;
import net.minecraft.class_1959;
import net.minecraft.class_2561;
import net.minecraft.class_2960;
import net.minecraft.class_310;
import net.minecraft.class_3532;
import net.minecraft.class_6880;
import net.minecraft.class_7924;
import xaero.common.minimap.info.codec.InfoDisplayCommonStateCodecs;
import xaero.common.minimap.info.widget.InfoDisplayCommonWidgetFactories;
import xaero.common.minimap.info.widget.InfoDisplayCycleWidgetFactory;
import xaero.common.minimap.waypoints.WaypointsManager;

public class BuiltInInfoDisplays {
   private static List<InfoDisplay<?>> ALL = new ArrayList<>();
   public static final InfoDisplay<Boolean> COORDINATES = new InfoDisplay<>(
      "coords",
      class_2561.method_43471("gui.xaero_infodisplay_coords"),
      true,
      InfoDisplayCommonStateCodecs.BOOLEAN,
      InfoDisplayCommonWidgetFactories.OFF_ON,
      (displayInfo, compiler, session, processor, x, y, w, h, scale, size, playerBlockX, playerBlockY, playerBlockZ, playerPos) -> {
         if ((Boolean)displayInfo.getState()) {
            String coords = playerBlockX + ", " + playerBlockY + ", " + playerBlockZ;
            if (class_310.method_1551().field_1772.method_1727(coords) >= size) {
               String stringLevel = playerBlockY + "";
               coords = playerBlockX + ", " + playerBlockZ;
               compiler.addLine(coords);
               compiler.addLine(stringLevel);
            } else {
               compiler.addLine(coords);
            }
         }
      },
      ALL
   );
   public static final InfoDisplay<Boolean> OVERWORLD_COORDINATES = new InfoDisplay<>(
      "overworld_coords",
      class_2561.method_43471("gui.xaero_infodisplay_overworld_coords"),
      false,
      InfoDisplayCommonStateCodecs.BOOLEAN,
      InfoDisplayCommonWidgetFactories.OFF_ON,
      (displayInfo, compiler, session, processor, x, y, w, h, scale, size, playerBlockX, playerBlockY, playerBlockZ, playerPos) -> {
         if ((Boolean)displayInfo.getState()) {
            class_310 mc = class_310.method_1551();
            double coordinateScale = mc.field_1687.method_8597().comp_646();
            if (coordinateScale != 1.0) {
               int overworldPlayerX = class_3532.method_15357((double)playerBlockX * coordinateScale);
               int overworldPlayerZ = class_3532.method_15357((double)playerBlockZ * coordinateScale);
               String coords = "Xo: " + overworldPlayerX + ", Zo: " + overworldPlayerZ;
               compiler.addWords(size, coords);
            }
         }
      },
      ALL
   );
   public static final InfoDisplay<Boolean> CHUNK_COORDINATES = new InfoDisplay<>(
      "chunk_coords",
      class_2561.method_43471("gui.xaero_infodisplay_chunk_coords"),
      false,
      InfoDisplayCommonStateCodecs.BOOLEAN,
      InfoDisplayCommonWidgetFactories.OFF_ON,
      (displayInfo, compiler, session, processor, x, y, w, h, scale, size, playerBlockX, playerBlockY, playerBlockZ, playerPos) -> {
         if ((Boolean)displayInfo.getState()) {
            int chunkX = playerBlockX >> 4;
            int chunkZ = playerBlockZ >> 4;
            int insideX = playerBlockX & 15;
            int insideZ = playerBlockZ & 15;
            String coords = "C " + chunkX + ", " + chunkZ + " (" + insideX + "; " + insideZ + ")";
            compiler.addWords(size, coords);
         }
      },
      ALL
   );
   public static final InfoDisplay<Boolean> ANGLES = new InfoDisplay<>(
      "angles",
      class_2561.method_43471("gui.xaero_infodisplay_angles"),
      false,
      InfoDisplayCommonStateCodecs.BOOLEAN,
      InfoDisplayCommonWidgetFactories.OFF_ON,
      (displayInfo, compiler, session, processor, x, y, w, h, scale, size, playerBlockX, playerBlockY, playerBlockZ, playerPos) -> {
         if ((Boolean)displayInfo.getState()) {
            class_310 mc = class_310.method_1551();
            compiler.addLine(
               class_2561.method_43470(
                  String.format(
                     "%.1f / %.1f", class_3532.method_15393(mc.method_1560().method_36454()), class_3532.method_15393(mc.method_1560().method_36455())
                  )
               )
            );
         }
      },
      ALL
   );
   public static final InfoDisplay<Boolean> DIMENSION = new InfoDisplay<>(
      "dimension",
      class_2561.method_43471("gui.xaero_infodisplay_dimension"),
      false,
      InfoDisplayCommonStateCodecs.BOOLEAN,
      InfoDisplayCommonWidgetFactories.OFF_ON,
      (displayInfo, compiler, session, processor, x, y, w, h, scale, size, playerBlockX, playerBlockY, playerBlockZ, playerPos) -> {
         if ((Boolean)displayInfo.getState()) {
            class_2960 dimensionId = class_310.method_1551().field_1687.method_27983().method_29177();
            if (dimensionId != null) {
               String dimensionName = dimensionId.method_12836().equals("minecraft") ? dimensionId.method_12832() : dimensionId.toString();
               compiler.addLine(dimensionName);
            }
         }
      },
      ALL
   );
   public static final InfoDisplay<Boolean> BIOME = new InfoDisplay<>(
      "biome",
      class_2561.method_43471("gui.xaero_infodisplay_biome"),
      false,
      InfoDisplayCommonStateCodecs.BOOLEAN,
      InfoDisplayCommonWidgetFactories.OFF_ON,
      (displayInfo, compiler, session, processor, x, y, w, h, scale, size, playerBlockX, playerBlockY, playerBlockZ, playerPos) -> {
         if ((Boolean)displayInfo.getState()) {
            class_6880<class_1959> biomeHolder = class_310.method_1551().field_1687.method_23753(playerPos);
            class_1959 biome = biomeHolder == null ? null : (class_1959)biomeHolder.comp_349();
            class_2960 biomeRL = biome == null
               ? null
               : class_310.method_1551().field_1687.method_30349().method_30530(class_7924.field_41236).method_10221(biome);
            String biomeText = biomeRL == null
               ? class_1074.method_4662("gui.xaero_unknown_biome", new Object[0])
               : class_1074.method_4662("biome." + biomeRL.method_12836() + "." + biomeRL.method_12832(), new Object[0]);
            compiler.addWords(size, biomeText);
         }
      },
      ALL
   );
   public static final InfoDisplay<Boolean> WEATHER = new InfoDisplay<>(
      "weather",
      class_2561.method_43471("gui.xaero_infodisplay_weather"),
      false,
      InfoDisplayCommonStateCodecs.BOOLEAN,
      InfoDisplayCommonWidgetFactories.OFF_ON,
      (displayInfo, compiler, session, processor, x, y, w, h, scale, size, playerBlockX, playerBlockY, playerBlockZ, playerPos) -> {
         if ((Boolean)displayInfo.getState()) {
            if (class_310.method_1551().field_1687.method_8546()) {
               compiler.addLine(class_2561.method_43471("gui.xaero_weather_thundering"));
            } else if (class_310.method_1551().field_1687.method_8419()) {
               compiler.addLine(class_2561.method_43471("gui.xaero_weather_raining"));
            }
         }
      },
      ALL
   );
   public static final InfoDisplay<Integer> LIGHT_LEVEL = new InfoDisplay<>(
      "light_level",
      class_2561.method_43471("gui.xaero_infodisplay_light_level"),
      0,
      InfoDisplayCommonStateCodecs.INTEGER,
      new InfoDisplayCycleWidgetFactory<>(
         Lists.newArrayList(new Integer[]{0, 1, 2, 3, 4}),
         Lists.newArrayList(
            new class_2561[]{
               class_2561.method_43471("gui.xaero_off"),
               class_2561.method_43471("gui.xaero_light_block"),
               class_2561.method_43471("gui.xaero_light_sky"),
               class_2561.method_43471("gui.xaero_light_all"),
               class_2561.method_43471("gui.xaero_light_both2")
            }
         )
      ),
      (displayInfo, compiler, session, processor, x, y, w, h, scale, size, playerBlockX, playerBlockY, playerBlockZ, playerPos) -> {
         int showLightLevel = displayInfo.getState();
         if (showLightLevel != 0) {
            class_310 mc = class_310.method_1551();
            int blockLight = mc.field_1687.method_8314(class_1944.field_9282, playerPos);
            int skyLight = mc.field_1687.method_8314(class_1944.field_9284, playerPos);
            if (showLightLevel == 1) {
               compiler.addLine(class_2561.method_43469("gui.xaero_block_light_value", new Object[]{blockLight}));
            } else if (showLightLevel == 2) {
               compiler.addLine(class_2561.method_43469("gui.xaero_sky_light_value", new Object[]{skyLight}));
            } else if (showLightLevel == 3) {
               compiler.addLine(class_2561.method_43469("gui.xaero_all_light_value", new Object[]{Math.max(blockLight, skyLight)}));
            } else {
               compiler.addLine(class_2561.method_43469("gui.xaero_both_light_value", new Object[]{blockLight, skyLight}));
            }
         }
      },
      ALL
   );
   public static final InfoDisplay<Integer> TIME = new InfoDisplay<>(
      "time",
      class_2561.method_43471("gui.xaero_infodisplay_time"),
      0,
      InfoDisplayCommonStateCodecs.INTEGER,
      new InfoDisplayCycleWidgetFactory<>(
         Lists.newArrayList(new Integer[]{0, 1, 2, 3, 4}),
         Lists.newArrayList(
            new class_2561[]{
               class_2561.method_43471("gui.xaero_off"),
               class_2561.method_43469("%s+%s", new Object[]{class_2561.method_43471("gui.xaero_day"), class_2561.method_43471("gui.xaero_24h")}),
               class_2561.method_43469("%s+%s", new Object[]{class_2561.method_43471("gui.xaero_day"), class_2561.method_43471("gui.xaero_12h")}),
               class_2561.method_43471("gui.xaero_24h"),
               class_2561.method_43471("gui.xaero_12h")
            }
         )
      ),
      (displayInfo, compiler, session, processor, x, y, w, h, scale, size, playerBlockX, playerBlockY, playerBlockZ, playerPos) -> {
         int showTime = displayInfo.getState();
         if (showTime > 0) {
            class_310 mc = class_310.method_1551();
            long totalTime = 6000L + mc.field_1687.method_8532();
            int dayNumber = (int)(totalTime / 24000L) + 1;
            int dayTimeSinceMidnight = (int)(totalTime % 24000L);
            int timeHours = dayTimeSinceMidnight / 1000;
            int minutes = (int)((double)(dayTimeSinceMidnight % 1000) / 1000.0 * 60.0);
            if ((showTime - 1) % 2 == 0) {
               if (showTime < 3) {
                  compiler.addWords(
                     size, class_1074.method_4662("gui.xaero_day", new Object[0]) + String.format(" %d, %02d:%02d", dayNumber, timeHours, minutes)
                  );
               } else {
                  compiler.addWords(size, String.format("%02d:%02d", timeHours, minutes));
               }
            } else {
               String half = "AM";
               if (timeHours >= 12) {
                  timeHours -= 12;
                  half = "PM";
               }

               if (timeHours == 0) {
                  timeHours = 12;
               }

               if (showTime < 3) {
                  compiler.addWords(
                     size, class_1074.method_4662("gui.xaero_day", new Object[0]) + String.format(" %d, %02d:%02d %s", dayNumber, timeHours, minutes, half)
                  );
               } else {
                  compiler.addWords(size, String.format("%02d:%02d %s", timeHours, minutes, half));
               }
            }
         }
      },
      ALL
   );
   private static final Calendar CALENDAR = Calendar.getInstance();
   public static final InfoDisplay<Integer> REAL_TIME = new InfoDisplay<>(
      "real_time",
      class_2561.method_43471("gui.xaero_infodisplay_real_time"),
      0,
      InfoDisplayCommonStateCodecs.INTEGER,
      new InfoDisplayCycleWidgetFactory<>(
         Lists.newArrayList(new Integer[]{0, 1, 2}),
         Lists.newArrayList(
            new class_2561[]{class_2561.method_43471("gui.xaero_off"), class_2561.method_43471("gui.xaero_24h"), class_2561.method_43471("gui.xaero_12h")}
         )
      ),
      (displayInfo, compiler, session, processor, x, y, w, h, scale, size, playerBlockX, playerBlockY, playerBlockZ, playerPos) -> {
         int showTime = displayInfo.getState();
         if (showTime > 0) {
            CALENDAR.setTimeInMillis(System.currentTimeMillis());
            int timeHours = CALENDAR.get(11);
            int minutes = CALENDAR.get(12);
            if (showTime == 1) {
               compiler.addWords(size, String.format("%02d:%02d IRL", timeHours, minutes));
            } else {
               String half = "AM";
               if (timeHours >= 12) {
                  timeHours -= 12;
                  half = "PM";
               }

               if (timeHours == 0) {
                  timeHours = 12;
               }

               compiler.addWords(size, String.format("%02d:%02d %s IRL", timeHours, minutes, half));
            }
         }
      },
      ALL
   );
   public static final InfoDisplay<Boolean> HIGHLIGHTS = new InfoDisplay<>(
      "highlights",
      class_2561.method_43471("gui.xaero_infodisplay_highlights"),
      true,
      InfoDisplayCommonStateCodecs.BOOLEAN,
      InfoDisplayCommonWidgetFactories.ALWAYS_ON,
      (displayInfo, compiler, session, processor, x, y, w, h, scale, size, playerBlockX, playerBlockY, playerBlockZ, playerPos) -> {
         if ((Boolean)displayInfo.getState()) {
            if (processor.getMinimapWriter().getDimensionHighlightHandler() != null) {
               processor.getMinimapWriter().getDimensionHighlightHandler().addBlockHighlightTooltips(compiler, playerBlockX, playerBlockZ, size, true);
            }
         }
      },
      ALL
   );
   public static final InfoDisplay<Boolean> LIGHT_OVERLAY_INDICATOR = new InfoDisplay<>(
      "light_overlay_indicator",
      class_2561.method_43471("gui.xaero_infodisplay_light_overlay_indicator"),
      true,
      InfoDisplayCommonStateCodecs.BOOLEAN,
      InfoDisplayCommonWidgetFactories.ALWAYS_ON,
      (displayInfo, compiler, session, processor, x, y, w, h, scale, size, playerBlockX, playerBlockY, playerBlockZ, playerPos) -> {
         if ((Boolean)displayInfo.getState()) {
            if (session.getModMain().getSettings().lightOverlayType > 0) {
               compiler.addWords(size, class_1074.method_4662("gui.xaero_light_overlay_status", new Object[0]));
            }
         }
      },
      ALL
   );
   public static final InfoDisplay<Boolean> MANUAL_CAVE_MODE_INDICATOR = new InfoDisplay<>(
      "manual_cave_mode_indicator",
      class_2561.method_43471("gui.xaero_infodisplay_manual_cave_mode_indicator"),
      true,
      InfoDisplayCommonStateCodecs.BOOLEAN,
      InfoDisplayCommonWidgetFactories.ALWAYS_ON,
      (displayInfo, compiler, session, processor, x, y, w, h, scale, size, playerBlockX, playerBlockY, playerBlockZ, playerPos) -> {
         if ((Boolean)displayInfo.getState()) {
            if (processor.isManualCaveMode()) {
               compiler.addWords(size, class_1074.method_4662("gui.xaero_manual_cave_mode", new Object[0]));
            }
         }
      },
      ALL
   );
   public static final InfoDisplay<Boolean> CUSTOM_SUB_WORLD = new InfoDisplay<>(
      "custom_sub_world",
      class_2561.method_43471("gui.xaero_infodisplay_custom_sub_world"),
      true,
      InfoDisplayCommonStateCodecs.BOOLEAN,
      InfoDisplayCommonWidgetFactories.ALWAYS_ON,
      (displayInfo, compiler, session, processor, x, y, w, h, scale, size, playerBlockX, playerBlockY, playerBlockZ, playerPos) -> {
         if ((Boolean)displayInfo.getState()) {
            WaypointsManager waypointsManager = session.getWaypointsManager();
            if (waypointsManager.getCurrentWorld() != null && waypointsManager.getAutoWorld() != waypointsManager.getCurrentWorld()) {
               compiler.addWords(
                  size, class_1074.method_4662("gui.xaero_using_custom_subworld", new Object[]{waypointsManager.getCurrentWorld().getContainer().getSubName()})
               );
            }
         }
      },
      ALL
   );

   public static void addToManager(InfoDisplayManager manager) {
      ALL.forEach(manager::add);
   }
}
