package xaero.common.minimap.render.radar.custom;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.class_1297;
import net.minecraft.class_4587;
import net.minecraft.class_583;
import net.minecraft.class_630;
import net.minecraft.class_897;
import net.minecraft.class_4597.class_4598;
import xaero.common.minimap.render.radar.EntityIconModelPartsRenderer;
import xaero.common.minimap.render.radar.ModelRenderDetectionElement;
import xaero.common.minimap.render.radar.resource.EntityIconModelConfig;

public class MultiLayerIconCustomRenderer extends EntityIconCustomRenderer {
   private List<EntityIconCustomRenderer> layers;

   public MultiLayerIconCustomRenderer(List<EntityIconCustomRenderer> layers) {
      this.layers = layers;
   }

   @Override
   public class_630 render(
      class_4587 matrixStack,
      class_4598 renderTypeBuffer,
      class_897 entityRenderer,
      class_1297 e,
      class_583 defaultModel,
      EntityIconModelPartsRenderer modelPartsRenderer,
      ArrayList<class_630> renderedModels,
      class_630 mainPart,
      EntityIconModelConfig modelConfig,
      ModelRenderDetectionElement mrde
   ) {
      for (EntityIconCustomRenderer layer : this.layers) {
         mainPart = layer.render(
            matrixStack, renderTypeBuffer, entityRenderer, e, defaultModel, modelPartsRenderer, renderedModels, mainPart, modelConfig, mrde
         );
      }

      return mainPart;
   }
}
