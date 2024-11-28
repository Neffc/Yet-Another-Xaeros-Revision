package xaero.common.minimap.info.render;

import net.minecraft.class_2338;
import net.minecraft.class_332;
import net.minecraft.class_4597.class_4598;
import xaero.common.XaeroMinimapSession;
import xaero.common.minimap.MinimapProcessor;
import xaero.common.minimap.info.render.compile.InfoDisplayCompiler;
import xaero.common.minimap.render.MinimapRendererHelper;
import xaero.common.settings.ModSettings;
import xaero.hud.minimap.BuiltInHudModules;
import xaero.hud.minimap.Minimap;

@Deprecated
public final class InfoDisplayRenderer extends xaero.hud.minimap.info.render.InfoDisplayRenderer {
   private final InfoDisplayCompiler compiler = new InfoDisplayCompiler();

   @Deprecated
   public void render(
      class_332 guiGraphics,
      XaeroMinimapSession session,
      MinimapProcessor processor,
      Minimap minimap,
      MinimapRendererHelper helper,
      int x,
      int y,
      int width,
      int height,
      double scale,
      int size,
      int playerBlockX,
      int playerBlockY,
      int playerBlockZ,
      class_2338 playerPos,
      int scaledX,
      int scaledY,
      float mapScale,
      ModSettings settings,
      class_4598 renderTypeBuffer
   ) {
      super.render(guiGraphics, session.getSession(BuiltInHudModules.MINIMAP), minimap, height, size, playerPos, scaledX, scaledY, mapScale, renderTypeBuffer);
   }
}
