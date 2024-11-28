package xaero.common.gui.widget.render;

import net.minecraft.class_310;
import net.minecraft.class_332;
import xaero.common.gui.widget.TextWidget;

public class TextWidgetRenderer extends ScalableWidgetRenderer<TextWidget> {
   protected void renderScaled(class_332 guiGraphics, int width, int height, int mouseX, int mouseY, double guiScale, TextWidget widget) {
      guiGraphics.method_25303(class_310.method_1551().field_1772, widget.getText(), 0, 0, 16777215);
   }
}
