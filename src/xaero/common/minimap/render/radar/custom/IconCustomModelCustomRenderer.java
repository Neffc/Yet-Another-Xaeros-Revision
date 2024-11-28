package xaero.common.minimap.render.radar.custom;

import java.util.ArrayList;
import net.minecraft.class_1297;
import net.minecraft.class_583;
import net.minecraft.class_630;
import xaero.common.minimap.render.radar.EntityIconModelPartsRenderer;

public abstract class IconCustomModelCustomRenderer extends IconRenderTypeCustomRenderer {
   @Override
   protected ArrayList<class_630> getRenderedModelsDest(ArrayList<class_630> renderedModels) {
      return renderedModels;
   }

   @Override
   protected abstract Iterable<class_630> getModelRenderers(EntityIconModelPartsRenderer var1, ArrayList<class_630> var2, class_1297 var3, class_583 var4);
}
