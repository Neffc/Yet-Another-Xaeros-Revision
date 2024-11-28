package xaero.hud.minimap.info.widget;

import com.google.common.collect.Lists;
import net.minecraft.class_2561;

public class InfoDisplayCommonWidgetFactories {
   public static final InfoDisplayCycleWidgetFactory<Boolean> OFF_ON = new InfoDisplayCycleWidgetFactory<>(
      Lists.newArrayList(new Boolean[]{false, true}),
      Lists.newArrayList(new class_2561[]{class_2561.method_43471("gui.xaero_off"), class_2561.method_43471("gui.xaero_on")})
   );
   public static final InfoDisplayWidgetFactory<Boolean> ALWAYS_ON = (x, y, w, h, infoDisplay) -> null;
}
