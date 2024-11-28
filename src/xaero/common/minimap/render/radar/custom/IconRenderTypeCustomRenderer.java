package xaero.common.minimap.render.radar.custom;

import java.util.ArrayList;
import net.minecraft.class_1297;
import net.minecraft.class_1921;
import net.minecraft.class_4587;
import net.minecraft.class_4588;
import net.minecraft.class_583;
import net.minecraft.class_630;
import net.minecraft.class_897;
import net.minecraft.class_4597.class_4598;
import xaero.common.minimap.render.radar.EntityIconModelPartsRenderer;
import xaero.common.minimap.render.radar.ModelRenderDetectionElement;
import xaero.common.minimap.render.radar.resource.EntityIconModelConfig;

public abstract class IconRenderTypeCustomRenderer extends EntityIconCustomRenderer {
   @Override
   public class_630 render(
      class_4587 matrixStack,
      class_4598 renderTypeBuffer,
      class_897 entityRenderer,
      class_1297 entity,
      class_583 defaultModel,
      EntityIconModelPartsRenderer modelPartsRenderer,
      ArrayList<class_630> renderedModels,
      class_630 mainPart,
      EntityIconModelConfig modelConfig,
      ModelRenderDetectionElement mrde
   ) {
      class_1921 renderType = this.getRenderType(entityRenderer, entity);
      if (renderType != null) {
         class_4588 vertexBuilder = renderTypeBuffer.getBuffer(renderType);
         mainPart = modelPartsRenderer.renderPartsIterable(
            this.getModelRenderers(modelPartsRenderer, renderedModels, entity, defaultModel),
            matrixStack,
            vertexBuilder,
            this.getRenderedModelsDest(renderedModels),
            mainPart,
            modelConfig.modelPartsRotationReset,
            mrde
         );
         renderTypeBuffer.method_22993();
      }

      return mainPart;
   }

   protected Iterable<class_630> getModelRenderers(
      EntityIconModelPartsRenderer modelPartsRenderer, ArrayList<class_630> renderedModels, class_1297 entity, class_583 defaultModel
   ) {
      return renderedModels;
   }

   protected ArrayList<class_630> getRenderedModelsDest(ArrayList<class_630> renderedModels) {
      return new ArrayList<>();
   }

   protected abstract class_1921 getRenderType(class_897 var1, class_1297 var2);
}
