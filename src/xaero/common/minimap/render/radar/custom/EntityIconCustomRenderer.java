package xaero.common.minimap.render.radar.custom;

import java.util.ArrayList;
import net.minecraft.class_1297;
import net.minecraft.class_4587;
import net.minecraft.class_583;
import net.minecraft.class_630;
import net.minecraft.class_897;
import net.minecraft.class_4597.class_4598;
import xaero.common.minimap.render.radar.EntityIconModelPartsRenderer;
import xaero.common.minimap.render.radar.ModelRenderDetectionElement;
import xaero.common.minimap.render.radar.resource.EntityIconModelConfig;

public abstract class EntityIconCustomRenderer {
   public abstract class_630 render(
      class_4587 var1,
      class_4598 var2,
      class_897 var3,
      class_1297 var4,
      class_583 var5,
      EntityIconModelPartsRenderer var6,
      ArrayList<class_630> var7,
      class_630 var8,
      EntityIconModelConfig var9,
      ModelRenderDetectionElement var10
   );
}
