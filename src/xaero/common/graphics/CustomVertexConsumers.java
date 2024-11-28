package xaero.common.graphics;

import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import java.util.SortedMap;
import net.minecraft.class_156;
import net.minecraft.class_1921;
import net.minecraft.class_287;
import net.minecraft.class_4597;
import net.minecraft.class_4597.class_4598;

public class CustomVertexConsumers {
   private final SortedMap<class_1921, class_287> builders = (SortedMap<class_1921, class_287>)class_156.method_654(
      new Object2ObjectLinkedOpenHashMap(), map -> {
         checkedAddToMap(map, CustomRenderTypes.GUI_NEAREST, new class_287(256));
         checkedAddToMap(map, CustomRenderTypes.GUI_BILINEAR, new class_287(256));
         checkedAddToMap(map, CustomRenderTypes.COLORED_WAYPOINTS_BGS, new class_287(256));
         checkedAddToMap(map, CustomRenderTypes.MAP_CHUNK_OVERLAY, new class_287(256));
         checkedAddToMap(map, CustomRenderTypes.MAP_LINES, new class_287(256));
         checkedAddToMap(map, CustomRenderTypes.RADAR_NAME_BGS, new class_287(256));
      }
   );
   private class_4598 betterPVPRenderTypeBuffers = class_4597.method_22992(this.builders, new class_287(256));

   public class_4598 getBetterPVPRenderTypeBuffers() {
      return this.betterPVPRenderTypeBuffers;
   }

   private static void checkedAddToMap(Object2ObjectLinkedOpenHashMap<class_1921, class_287> map, class_1921 layer, class_287 bb) {
      if (map.containsKey(layer)) {
         throw new RuntimeException("Duplicate render layers!");
      } else {
         map.put(layer, bb);
      }
   }
}
