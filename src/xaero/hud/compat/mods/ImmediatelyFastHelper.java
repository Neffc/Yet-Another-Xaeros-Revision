package xaero.hud.compat.mods;

import net.minecraft.class_332;
import xaero.hud.render.util.DirectRender;

public class ImmediatelyFastHelper {
   public static void triggerBatchingBuffersFlush(class_332 guiGraphics) {
      DirectRender.coloredRectangle(guiGraphics.method_51448().method_23760().method_23761(), 0.0F, 0.0F, 0.0F, 0.0F, 0);
   }
}
