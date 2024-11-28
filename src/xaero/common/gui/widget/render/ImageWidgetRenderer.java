package xaero.common.gui.widget.render;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.class_332;
import xaero.common.graphics.GuiHelper;
import xaero.common.gui.widget.ImageWidget;

public class ImageWidgetRenderer extends ScalableWidgetRenderer<ImageWidget> {
   protected void renderScaled(class_332 guiGraphics, int width, int height, int mouseX, int mouseY, double guiScale, ImageWidget widget) {
      RenderSystem.setShaderTexture(0, widget.getGlTexture());
      RenderSystem.enableBlend();
      GuiHelper.blit(guiGraphics.method_51448(), 0, 0, 0, 0.0F, 0.0F, widget.getW(), widget.getH(), widget.getW(), widget.getH());
   }
}
