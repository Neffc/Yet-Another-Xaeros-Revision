package xaero.common.gui.widget.render;

import net.minecraft.class_332;
import xaero.common.gui.widget.Widget;

public interface WidgetRenderer<T extends Widget> {
   void render(class_332 var1, int var2, int var3, int var4, int var5, double var6, T var8);
}
