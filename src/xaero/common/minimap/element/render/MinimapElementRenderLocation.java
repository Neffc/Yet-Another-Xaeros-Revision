package xaero.common.minimap.element.render;

public class MinimapElementRenderLocation {
   public static final int UNKNOWN = -1;
   public static final int IN_MINIMAP = 0;
   public static final int OVER_MINIMAP = 1;
   public static final int IN_GAME = 2;
   public static final int WORLD_MAP = 3;
   public static final int WORLD_MAP_MENU = 4;

   public static int fromWorldMap(int location) {
      return location <= 4 && location >= -1 ? location : -1;
   }
}
