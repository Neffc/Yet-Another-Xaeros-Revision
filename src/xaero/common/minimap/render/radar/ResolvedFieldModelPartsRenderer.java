package xaero.common.minimap.render.radar;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import net.minecraft.class_4587;
import net.minecraft.class_4588;
import net.minecraft.class_630;

public class ResolvedFieldModelPartsRenderer implements EntityIconModelFieldResolver.Listener {
   private class_4587 matrixStack;
   private class_4588 vertexBuilder;
   private boolean justOne;
   private ArrayList<class_630> renderedModels;
   private class_630 mainPart;
   private boolean zeroRotation;
   private ModelRenderDetectionElement mrde;
   private EntityIconModelPartsRenderer entityModelPartsRenderer;
   private boolean stop;

   public void prepare(
      class_4587 matrixStack,
      class_4588 vertexBuilder,
      boolean justOne,
      ArrayList<class_630> renderedModels,
      class_630 mainPart,
      boolean zeroRotation,
      ModelRenderDetectionElement mrde,
      EntityIconModelPartsRenderer entityModelPartsRenderer
   ) {
      this.matrixStack = matrixStack;
      this.vertexBuilder = vertexBuilder;
      this.justOne = justOne;
      this.renderedModels = renderedModels;
      this.mainPart = mainPart;
      this.zeroRotation = zeroRotation;
      this.mrde = mrde;
      this.entityModelPartsRenderer = entityModelPartsRenderer;
      this.stop = false;
   }

   @Override
   public boolean isFieldAllowed(Field f) {
      try {
         f.getType().asSubclass(class_630.class);
      } catch (ClassCastException var9) {
         try {
            f.getType().asSubclass(class_630[].class);
         } catch (ClassCastException var8) {
            try {
               f.getType().asSubclass(Collection.class);
            } catch (ClassCastException var7) {
               try {
                  f.getType().asSubclass(Map.class);
               } catch (ClassCastException var6) {
                  return false;
               }
            }
         }
      }

      return true;
   }

   @Override
   public boolean shouldStop() {
      return this.stop;
   }

   @Override
   public void onFieldResolved(Object[] resolved, String matchedFilterElement) {
      class_4587 matrixStack = this.matrixStack;
      class_4588 vertexBuilder = this.vertexBuilder;
      boolean justOne = this.justOne;
      ArrayList<class_630> renderedModels = this.renderedModels;
      boolean zeroRotation = this.zeroRotation;
      ModelRenderDetectionElement mrde = this.mrde;
      EntityIconModelPartsRenderer entityModelPartsRenderer = this.entityModelPartsRenderer;

      for (Object o : resolved) {
         if (o instanceof class_630) {
            class_630 mr = (class_630)o;
            if (this.mainPart == null) {
               this.mainPart = mr;
            }

            entityModelPartsRenderer.renderPart(matrixStack, vertexBuilder, mr, renderedModels, this.mainPart, zeroRotation, mrde);
            if (justOne) {
               this.stop = true;
               break;
            }
         }
      }
   }

   public class_630 getMainPart() {
      return this.mainPart;
   }
}
