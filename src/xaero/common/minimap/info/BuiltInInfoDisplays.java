package xaero.common.minimap.info;

import java.util.ArrayList;
import java.util.List;

@Deprecated
public class BuiltInInfoDisplays {
   private static List<InfoDisplay<?>> ALL = new ArrayList<>();
   @Deprecated
   public static final InfoDisplay<Boolean> COORDINATES = (InfoDisplay<Boolean>)xaero.hud.minimap.info.BuiltInInfoDisplays.COORDINATES;
   @Deprecated
   public static final InfoDisplay<Boolean> OVERWORLD_COORDINATES = (InfoDisplay<Boolean>)xaero.hud.minimap.info.BuiltInInfoDisplays.OVERWORLD_COORDINATES;
   @Deprecated
   public static final InfoDisplay<Boolean> CHUNK_COORDINATES = (InfoDisplay<Boolean>)xaero.hud.minimap.info.BuiltInInfoDisplays.CHUNK_COORDINATES;
   @Deprecated
   public static final InfoDisplay<Boolean> ANGLES = (InfoDisplay<Boolean>)xaero.hud.minimap.info.BuiltInInfoDisplays.ANGLES;
   @Deprecated
   public static final InfoDisplay<Boolean> DIMENSION = (InfoDisplay<Boolean>)xaero.hud.minimap.info.BuiltInInfoDisplays.DIMENSION;
   @Deprecated
   public static final InfoDisplay<Boolean> BIOME = (InfoDisplay<Boolean>)xaero.hud.minimap.info.BuiltInInfoDisplays.BIOME;
   @Deprecated
   public static final InfoDisplay<Boolean> WEATHER = (InfoDisplay<Boolean>)xaero.hud.minimap.info.BuiltInInfoDisplays.WEATHER;
   @Deprecated
   public static final InfoDisplay<Integer> LIGHT_LEVEL = (InfoDisplay<Integer>)xaero.hud.minimap.info.BuiltInInfoDisplays.LIGHT_LEVEL;
   @Deprecated
   public static final InfoDisplay<Integer> TIME = (InfoDisplay<Integer>)xaero.hud.minimap.info.BuiltInInfoDisplays.TIME;
   @Deprecated
   public static final InfoDisplay<Integer> REAL_TIME = (InfoDisplay<Integer>)xaero.hud.minimap.info.BuiltInInfoDisplays.REAL_TIME;
   @Deprecated
   public static final InfoDisplay<Boolean> HIGHLIGHTS = (InfoDisplay<Boolean>)xaero.hud.minimap.info.BuiltInInfoDisplays.HIGHLIGHTS;
   @Deprecated
   public static final InfoDisplay<Boolean> LIGHT_OVERLAY_INDICATOR = (InfoDisplay<Boolean>)xaero.hud.minimap.info.BuiltInInfoDisplays.LIGHT_OVERLAY_INDICATOR;
   @Deprecated
   public static final InfoDisplay<Boolean> MANUAL_CAVE_MODE_INDICATOR = (InfoDisplay<Boolean>)xaero.hud.minimap.info.BuiltInInfoDisplays.MANUAL_CAVE_MODE_INDICATOR;
   @Deprecated
   public static final InfoDisplay<Boolean> CUSTOM_SUB_WORLD = (InfoDisplay<Boolean>)xaero.hud.minimap.info.BuiltInInfoDisplays.CUSTOM_SUB_WORLD;

   @Deprecated
   public static void addToManager(InfoDisplayManager manager) {
      ALL.forEach(manager::add);
   }
}
