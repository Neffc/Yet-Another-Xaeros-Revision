package xaero.common.minimap.info.render;

import java.util.Iterator;
import java.util.List;
import net.minecraft.class_2338;
import net.minecraft.class_2561;
import net.minecraft.class_310;
import net.minecraft.class_332;
import net.minecraft.class_4587;
import net.minecraft.class_4588;
import net.minecraft.class_4597.class_4598;
import xaero.common.XaeroMinimapSession;
import xaero.common.graphics.CustomRenderTypes;
import xaero.common.minimap.MinimapInterface;
import xaero.common.minimap.MinimapProcessor;
import xaero.common.minimap.info.InfoDisplay;
import xaero.common.minimap.info.render.compile.InfoDisplayCompiler;
import xaero.common.minimap.render.MinimapRendererHelper;
import xaero.common.misc.Misc;
import xaero.common.settings.ModSettings;

public final class InfoDisplayRenderer {
   private final InfoDisplayCompiler compiler = new InfoDisplayCompiler();

   public void render(
      class_332 guiGraphics,
      XaeroMinimapSession session,
      MinimapProcessor processor,
      MinimapInterface minimapInterface,
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
      class_4587 matrixStack = guiGraphics.method_51448();
      Iterator<InfoDisplay<?>> iterator = minimapInterface.getInfoDisplayManager().getStream().iterator();
      int interfaceSize = size;
      int scaledHeight = (int)((float)height * mapScale);
      int align = settings.minimapTextAlign;
      boolean under = scaledY + size / 2 < scaledHeight / 2;
      int stringY = scaledY + (under ? size : -9);
      int bgOpacityMask = settings.infoDisplayBackgroundOpacity * 255 / 100 << 24;
      matrixStack.method_46416(0.0F, 0.0F, -1.0F);

      while (iterator.hasNext()) {
         InfoDisplay<?> infoDisplay = iterator.next();
         List<class_2561> compiledLines = this.compiler
            .compile(infoDisplay, session, processor, x, y, width, height, scale, size, playerBlockX, playerBlockY, playerBlockZ, playerPos);
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
               matrixStack.method_46416(0.0F, 0.0F, -1.0F);
               helper.addColoredRectToExistingBuffer(
                  matrixStack.method_23760().method_23761(),
                  backgroundVertexBuffer,
                  (float)(stringX - 1),
                  (float)(stringY - 1),
                  stringWidth + 2,
                  10,
                  backgroundColor
               );
               matrixStack.method_46416(0.0F, 0.0F, 1.0F);
            }

            Misc.drawNormalText(matrixStack, s, (float)stringX, (float)stringY, textColor, true, renderTypeBuffer);
            stringY += 10 * (under ? 1 : -1);
         }

         compiledLines.clear();
      }

      matrixStack.method_46416(0.0F, 0.0F, 1.0F);
      renderTypeBuffer.method_22993();
   }
}
