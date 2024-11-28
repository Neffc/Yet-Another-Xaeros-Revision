package xaero.common.graphics.shader;

import java.io.IOException;
import net.minecraft.class_290;
import net.minecraft.class_310;
import net.minecraft.class_3300;
import net.minecraft.class_5944;
import xaero.common.MinimapLogs;

public class MinimapShaders {
   public static FramebufferLinesShader FRAMEBUFFER_LINES = null;
   public static class_5944 POSITION_COLOR_TEX = null;
   public static class_5944 POSITION_COLOR_TEX_PRE = null;
   public static class_5944 POSITION_COLOR = null;
   public static class_5944 POSITION_COLOR_NO_ALPHA_TEST = null;
   public static class_5944 POSITION_TEX_NO_ALPHA_TEST = null;
   public static class_5944 POSITION_TEX_NO_ALPHA_TEST_NO_BLEND = null;
   public static PositionTexAlphaTestShader POSITION_TEX_ALPHA_TEST = null;
   public static PositionTexAlphaTestShader POSITION_TEX_ALPHA_TEST_NO_BLEND = null;
   public static PositionTexAlphaTestShader POSITION_TEX_ICON_OUTLINE = null;
   private static boolean firstTime = true;

   public static void onResourceReload(class_3300 resourceManager) {
      String errorMessage = "Couldn't reload the minimap shaders!";

      try {
         FRAMEBUFFER_LINES = reloadShader(FRAMEBUFFER_LINES, new FramebufferLinesShader(resourceManager));
         POSITION_COLOR_TEX = reloadShader(POSITION_COLOR_TEX, new class_5944(resourceManager, "xaerominimap/position_color_tex", class_290.field_20887));
         POSITION_COLOR_TEX_PRE = reloadShader(
            POSITION_COLOR_TEX_PRE, new class_5944(resourceManager, "xaerominimap/position_color_tex_pre", class_290.field_20887)
         );
         POSITION_COLOR = reloadShader(POSITION_COLOR, new class_5944(resourceManager, "xaerominimap/position_color", class_290.field_20887));
         POSITION_COLOR_NO_ALPHA_TEST = reloadShader(
            POSITION_COLOR_NO_ALPHA_TEST, new class_5944(resourceManager, "xaerominimap/position_color_no_alpha_test", class_290.field_20887)
         );
         POSITION_TEX_NO_ALPHA_TEST = reloadShader(
            POSITION_TEX_NO_ALPHA_TEST, new class_5944(resourceManager, "xaerominimap/pos_tex_no_alpha_test", class_290.field_1585)
         );
         POSITION_TEX_NO_ALPHA_TEST_NO_BLEND = reloadShader(
            POSITION_TEX_NO_ALPHA_TEST_NO_BLEND, new class_5944(resourceManager, "xaerominimap/pos_tex_no_alpha_test_no_blend", class_290.field_1585)
         );
         POSITION_TEX_ALPHA_TEST = reloadShader(POSITION_TEX_ALPHA_TEST, new PositionTexAlphaTestShader(resourceManager, "xaerominimap/pos_tex_alpha_test"));
         POSITION_TEX_ALPHA_TEST_NO_BLEND = reloadShader(
            POSITION_TEX_ALPHA_TEST_NO_BLEND, new PositionTexAlphaTestShader(resourceManager, "xaerominimap/pos_tex_alpha_test_no_blend")
         );
         POSITION_TEX_ICON_OUTLINE = reloadShader(
            POSITION_TEX_ICON_OUTLINE, new PositionTexAlphaTestShader(resourceManager, "xaerominimap/pos_tex_icon_outline")
         );
         MinimapLogs.LOGGER.info("Successfully reloaded the minimap shaders!");
      } catch (IOException var3) {
         if (firstTime) {
            throw new RuntimeException("Couldn't reload the minimap shaders!", var3);
         }

         MinimapLogs.LOGGER.error("Couldn't reload the minimap shaders!", var3);
      }

      firstTime = false;
   }

   private static <S extends class_5944> S reloadShader(S current, S newOne) throws IOException {
      if (current != null) {
         current.close();
      }

      return newOne;
   }

   public static void ensureShaders() {
      if (FRAMEBUFFER_LINES == null && firstTime) {
         onResourceReload(class_310.method_1551().method_1478());
      }
   }
}
