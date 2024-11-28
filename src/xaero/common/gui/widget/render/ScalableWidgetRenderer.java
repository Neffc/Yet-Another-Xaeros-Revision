package xaero.common.gui.widget.render;

import net.minecraft.class_332;
import net.minecraft.class_4587;
import xaero.common.gui.widget.ScalableWidget;

public abstract class ScalableWidgetRenderer<T extends ScalableWidget> implements WidgetRenderer<T> {
   public void render(class_332 guiGraphics, int width, int height, int mouseX, int mouseY, double guiScale, T widget) {
      class_4587 matrixStack = guiGraphics.method_51448();
      matrixStack.method_22903();
      matrixStack.method_46416((float)widget.getX(width), (float)widget.getY(height), 0.0F);
      if (widget.isNoGuiScale()) {
         matrixStack.method_22905((float)(1.0 / guiScale), (float)(1.0 / guiScale), 1.0F);
      }

      matrixStack.method_22905((float)widget.getScale(), (float)widget.getScale(), 1.0F);
      matrixStack.method_46416((float)widget.getScaledOffsetX(), (float)widget.getScaledOffsetY(), 0.0F);
      this.renderScaled(guiGraphics, width, height, mouseX, mouseY, guiScale, widget);
      matrixStack.method_22909();
   }

   protected abstract void renderScaled(class_332 var1, int var2, int var3, int var4, int var5, double var6, T var8);
}
