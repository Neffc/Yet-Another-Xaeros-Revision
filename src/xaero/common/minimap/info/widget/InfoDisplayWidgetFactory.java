package xaero.common.minimap.info.widget;

import net.minecraft.class_339;
import xaero.common.minimap.info.InfoDisplay;
import xaero.common.settings.ModSettings;

public interface InfoDisplayWidgetFactory<T> {
   class_339 create(int var1, int var2, int var3, int var4, InfoDisplay<T> var5, ModSettings var6);
}
