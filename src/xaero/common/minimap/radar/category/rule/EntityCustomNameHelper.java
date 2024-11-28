package xaero.common.minimap.radar.category.rule;

import net.minecraft.class_1297;
import net.minecraft.class_2561;
import net.minecraft.class_2585;

public class EntityCustomNameHelper {
   public static String getCustomName(class_1297 e, boolean nullable) {
      class_2561 c = e.method_5797();
      return c != null && c.method_10851() instanceof class_2585 ? ((class_2585)c.method_10851()).comp_737() : (nullable ? null : "{non-plain}");
   }
}
