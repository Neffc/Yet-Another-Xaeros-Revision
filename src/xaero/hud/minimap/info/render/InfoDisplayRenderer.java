package xaero.hud.minimap.info.render;

import java.util.Iterator;
import java.util.List;
import net.minecraft.class_2338;
import net.minecraft.class_2561;
import net.minecraft.class_310;
import net.minecraft.class_332;
import net.minecraft.class_4587;
import net.minecraft.class_4588;
import net.minecraft.class_4597.class_4598;
import xaero.common.graphics.CustomRenderTypes;
import xaero.common.minimap.render.MinimapRendererHelper;
import xaero.common.misc.Misc;
import xaero.common.settings.ModSettings;
import xaero.hud.minimap.Minimap;
import xaero.hud.minimap.info.InfoDisplay;
import xaero.hud.minimap.info.render.compile.InfoDisplayCompiler;
import xaero.hud.minimap.module.MinimapSession;

public abstract class InfoDisplayRenderer {
   public static final int DEPTH_OFFSET = 2;
   private final InfoDisplayCompiler compiler = InfoDisplayCompiler.Builder.begin().build();

   public void render(
      class_332 guiGraphics,
      MinimapSession session,
      Minimap minimap,
      int height,
      int size,
      class_2338 playerPos,
      int scaledX,
      int scaledY,
      float mapScale,
      class_4598 renderTypeBuffer
   ) {
      ModSettings settings = minimap.getModMain().getSettings();
      MinimapRendererHelper helper = minimap.getMinimapFBORenderer().getHelper();
      class_4587 matrixStack = guiGraphics.method_51448();
      Iterator<InfoDisplay<?>> iterator = minimap.getInfoDisplays().getManager().getOrderedStream().iterator();
      int interfaceSize = size;
      int scaledHeight = (int)((float)height * mapScale);
      int align = settings.minimapTextAlign;
      boolean under = scaledY + size / 2 < scaledHeight / 2;
      int stringY = scaledY + (under ? size : -9);
      int bgOpacityMask = settings.infoDisplayBackgroundOpacity * 255 / 100 << 24;
      matrixStack.method_22904(0.0, 0.0, 0.5);

      while (iterator.hasNext()) {
         InfoDisplay<?> infoDisplay = iterator.next();
         List<class_2561> compiledLines = this.compiler.compile(infoDisplay, session, size, playerPos);
         int textColorIndex = infoDisplay.getTextColor();
         int backgroundColorIndex = infoDisplay.getBackgroundColor();
         int textColor = ModSettings.COLORS[textColorIndex < 0 ? 15 : textColorIndex % ModSettings.COLORS.length];
         int backgroundColor = backgroundColorIndex < 0 ? 0 : bgOpacityMask | ModSettings.COLORS[backgroundColorIndex % ModSettings.COLORS.length] & 16777215;
         class_4588 backgroundVertexBuffer = renderTypeBuffer.getBuffer(CustomRenderTypes.COLORED_WAYPOINTS_BGS);

         for (int i = 0; i < compiledLines.size(); i++) {
            class_2561 s = compiledLines.get(i);
            int stringWidth = class_310.method_1551().field_1772.method_27525(s);
            int stringX = scaledX + (align == 0 ? interfaceSize / 2 - stringWidth / 2 : (align == 1 ? 6 : interfaceSize - 6 - stringWidth));
            if (backgroundColor != 0) {
               helper.addColoredRectToExistingBuffer(
                  matrixStack.method_23760().method_23761(),
                  backgroundVertexBuffer,
                  (float)(stringX - 1),
                  (float)(stringY - 1),
                  stringWidth + 2,
                  10,
                  backgroundColor
               );
            }

            Misc.drawNormalText(matrixStack, s, (float)stringX, (float)stringY, textColor, true, renderTypeBuffer);
            stringY += 10 * (under ? 1 : -1);
         }

         compiledLines.clear();
      }

      matrixStack.method_22904(0.0, 0.0, -0.5);
      renderTypeBuffer.method_22993();
   }

   public static final class Builder {
      private Builder() {
      }

      private InfoDisplayRenderer.Builder setDefault() {
         return this;
      }

      public InfoDisplayRenderer build() {
         return new xaero.common.minimap.info.render.InfoDisplayRenderer();
      }

      public static InfoDisplayRenderer.Builder begin() {
         return new InfoDisplayRenderer.Builder().setDefault();
      }
   }
}
