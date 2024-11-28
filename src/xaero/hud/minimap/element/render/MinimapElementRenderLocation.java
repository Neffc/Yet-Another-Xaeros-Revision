package xaero.hud.minimap.element.render;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;

public class MinimapElementRenderLocation {
   private static final Int2ObjectMap<MinimapElementRenderLocation> ALL = new Int2ObjectOpenHashMap();
   public static final MinimapElementRenderLocation UNKNOWN = new MinimapElementRenderLocation(-1);
   public static final MinimapElementRenderLocation IN_MINIMAP = new MinimapElementRenderLocation(0);
   public static final MinimapElementRenderLocation OVER_MINIMAP = new MinimapElementRenderLocation(1);
   public static final MinimapElementRenderLocation IN_GAME = new MinimapElementRenderLocation(2);
   public static final MinimapElementRenderLocation WORLD_MAP = new MinimapElementRenderLocation(3);
   public static final MinimapElementRenderLocation WORLD_MAP_MENU = new MinimapElementRenderLocation(4);
   private final int index;

   private MinimapElementRenderLocation(int index) {
      this.index = index;
      ALL.put(index, this);
   }

   public MinimapElementRenderLocation() {
      this.index = -1;
   }

   public int getIndex() {
      return this.index;
   }

   public static MinimapElementRenderLocation fromIndex(int location) {
      MinimapElementRenderLocation result = (MinimapElementRenderLocation)ALL.get(location);
      return result == null ? UNKNOWN : result;
   }
}
