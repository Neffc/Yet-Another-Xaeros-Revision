package xaero.common.minimap.render.radar;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import net.minecraft.class_4587;
import net.minecraft.class_4588;
import net.minecraft.class_4592;
import net.minecraft.class_4595;
import net.minecraft.class_4608;
import net.minecraft.class_5603;
import net.minecraft.class_583;
import net.minecraft.class_630;
import net.minecraft.class_630.class_628;
import org.lwjgl.opengl.GL11;
import xaero.common.IXaeroMinimap;
import xaero.common.graphics.CustomRenderTypes;
import xaero.common.misc.Misc;
import xaero.hud.minimap.MinimapLogs;

public class EntityIconModelPartsRenderer {
   public Method ageableModelHeadPartsMethod;
   public Method ageableModelBodyPartsMethod;
   public Method segmentedModelPartsMethod;
   private Field cubeListField;
   private Field childModelsField;
   private EntityIconModelPartsRenderer.VertexConsumerWrapper vertexConsumerWrapper;
   private boolean testedRenderEngineWrapperCompatibility;
   private boolean renderEngineIsWrapperCompatible;
   private IXaeroMinimap modMain;

   public EntityIconModelPartsRenderer(IXaeroMinimap modMain) {
      this.modMain = modMain;
      this.ageableModelHeadPartsMethod = Misc.getMethodReflection(class_4592.class, "headParts", "method_22946", "()Ljava/lang/Iterable;", "m_5607_");
      this.ageableModelBodyPartsMethod = Misc.getMethodReflection(class_4592.class, "bodyParts", "method_22948", "()Ljava/lang/Iterable;", "m_5608_");
      this.segmentedModelPartsMethod = Misc.getMethodReflection(class_4595.class, "parts", "method_22960", "()Ljava/lang/Iterable;", "m_6195_");
      this.cubeListField = Misc.getFieldReflection(class_630.class, "cubes", "field_3663", "Ljava/util/List;", "f_104212_");
      this.childModelsField = Misc.getFieldReflection(class_630.class, "children", "field_3661", "Ljava/util/Map;", "f_104213_");
      this.vertexConsumerWrapper = new EntityIconModelPartsRenderer.VertexConsumerWrapper();
   }

   private List<class_628> getCubeList(class_630 modelRenderer) {
      return Misc.getReflectFieldValue(modelRenderer, this.cubeListField);
   }

   public Map<String, class_630> getChildModels(class_630 modelRenderer) {
      return Misc.getReflectFieldValue(modelRenderer, this.childModelsField);
   }

   public boolean hasDirectCubes(class_630 mr) {
      List<class_628> mrCubeList = this.getCubeList(mr);
      return mrCubeList != null && !mrCubeList.isEmpty();
   }

   public boolean hasCubes(class_630 mr) {
      if (this.hasDirectCubes(mr)) {
         return true;
      } else {
         Map<String, class_630> mrChildren = this.getChildModels(mr);

         for (class_630 child : mrChildren.values()) {
            if (this.hasCubes(child)) {
               return true;
            }
         }

         return false;
      }
   }

   public void renderPart(
      class_4587 matrixStack,
      class_4588 vertexBuilder,
      class_630 mr,
      ArrayList<class_630> renderedModels,
      class_630 mainModelPart,
      boolean zeroRotation,
      ModelRenderDetectionElement mrde
   ) {
      if (mr != null) {
         if (!renderedModels.contains(mr)) {
            ModelPartRenderDetectionInfo renderInfo = mrde.getModelPartRenderInfo(mr);
            if (renderInfo != null) {
               if (this.hasCubes(mr)) {
                  boolean showModelBU = mr.field_3665;
                  boolean skipDrawBU = mr.field_38456;
                  if (!this.testedRenderEngineWrapperCompatibility) {
                     boolean normalWorks = false;

                     try {
                        class_4587 testMatrix = new class_4587();
                        testMatrix.method_46416(0.0F, 0.0F, -2500.0F);
                        class_4588 actualVertexConsumer = this.modMain
                           .getHudRenderer()
                           .getCustomVertexConsumers()
                           .getBetterPVPRenderTypeBuffers()
                           .getBuffer(CustomRenderTypes.entityIconRenderType(mrde.renderTexture, mrde.layerPhases));
                        mr.method_22699(
                           testMatrix,
                           actualVertexConsumer,
                           15728880,
                           class_4608.field_21444,
                           renderInfo.red,
                           renderInfo.green,
                           renderInfo.blue,
                           renderInfo.alpha
                        );
                        normalWorks = true;
                        testMatrix = new class_4587();
                        testMatrix.method_46416(0.0F, 0.0F, -2500.0F);
                        this.vertexConsumerWrapper
                           .prepareDetection(
                              actualVertexConsumer,
                              Double.NEGATIVE_INFINITY,
                              Double.POSITIVE_INFINITY,
                              Double.NEGATIVE_INFINITY,
                              Double.POSITIVE_INFINITY,
                              Double.NEGATIVE_INFINITY,
                              Double.POSITIVE_INFINITY
                           );
                        mr.field_3665 = true;
                        mr.field_38456 = false;
                        mr.method_22699(
                           testMatrix,
                           this.vertexConsumerWrapper,
                           15728880,
                           class_4608.field_21444,
                           renderInfo.red,
                           renderInfo.green,
                           renderInfo.blue,
                           renderInfo.alpha
                        );
                        this.renderEngineIsWrapperCompatible = this.vertexConsumerWrapper.detectedVertex;
                        if (!this.renderEngineIsWrapperCompatible) {
                           throw new Exception("can't detect vertices");
                        }
                     } catch (Throwable var23) {
                        if (normalWorks) {
                           MinimapLogs.LOGGER
                              .warn(
                                 "Render engine used for entities is not fully compatible with the minimap entity icons. Using fallback. " + var23.getMessage()
                              );
                        }
                     }

                     if (normalWorks) {
                        this.testedRenderEngineWrapperCompatibility = true;
                     }
                  }

                  float centerPointX = mainModelPart.field_3657;
                  float centerPointY = mainModelPart.field_3656;
                  float centerPointZ = mainModelPart.field_3655;
                  List<class_628> mainCubeList = this.getCubeList(mainModelPart);
                  if (mainCubeList != null && !mainCubeList.isEmpty()) {
                     float biggestSize = 0.0F;
                     class_628 biggestCuboid = null;

                     for (class_628 cuboid : mainCubeList) {
                        float size = Math.abs(
                           (cuboid.field_3648 - cuboid.field_3645) * (cuboid.field_3647 - cuboid.field_3644) * (cuboid.field_3646 - cuboid.field_3643)
                        );
                        if (size >= biggestSize) {
                           biggestCuboid = cuboid;
                           biggestSize = size;
                        }
                     }

                     centerPointY += (biggestCuboid.field_3647 + biggestCuboid.field_3644) / 2.0F;
                     centerPointZ += (biggestCuboid.field_3646 + biggestCuboid.field_3643) / 2.0F;
                  }

                  float xRotBU = 0.0F;
                  float yRotBU = 0.0F;
                  float zRotBU = 0.0F;
                  if (zeroRotation) {
                     xRotBU = mr.field_3654;
                     yRotBU = mr.field_3675;
                     zRotBU = mr.field_3674;
                     class_5603 initPose = mr.method_41921();
                     mr.method_33425(initPose.field_27705, initPose.field_27706, initPose.field_27707);
                  }

                  mr.field_3665 = true;
                  mr.field_38456 = false;
                  float xBU = mr.field_3657;
                  float yBU = mr.field_3656;
                  float zBU = mr.field_3655;
                  mr.method_2851(mr.field_3657 - centerPointX, mr.field_3656 - centerPointY, mr.field_3655 - centerPointZ);

                  try {
                     class_4588 vertexConsumer = (class_4588)(!this.renderEngineIsWrapperCompatible
                        ? vertexBuilder
                        : this.vertexConsumerWrapper.prepareDetection(vertexBuilder, 3.0, 61.0, 3.0, 61.0, -497.0, -2.0));
                     mr.method_22699(
                        matrixStack, vertexConsumer, 15728880, class_4608.field_21444, renderInfo.red, renderInfo.green, renderInfo.blue, renderInfo.alpha
                     );
                     if ((!this.renderEngineIsWrapperCompatible || this.vertexConsumerWrapper.hasDetectedVertex()) && renderInfo.alpha > 0.0F) {
                        renderedModels.add(mr);
                     }
                  } catch (Throwable var22) {
                     MinimapLogs.LOGGER.info("Exception when rendering entity part. " + mr + " " + var22.getMessage());
                  }

                  mr.method_2851(xBU, yBU, zBU);

                  while (GL11.glGetError() != 0) {
                  }

                  if (zeroRotation) {
                     mr.method_33425(xRotBU, yRotBU, zRotBU);
                  }

                  mr.field_3665 = showModelBU;
                  mr.field_38456 = skipDrawBU;
               }
            }
         }
      }
   }

   public class_630 renderDeclaredMethod(
      class_4587 matrixStack,
      class_4588 vertexBuilder,
      Method m,
      class_583 entityModel,
      ArrayList<class_630> renderedModels,
      class_630 mainPart,
      boolean zeroRotation,
      ModelRenderDetectionElement mrde
   ) {
      Iterable<class_630> renderers = this.handleDeclaredMethod(m, entityModel);
      return this.renderPartsIterable(renderers, matrixStack, vertexBuilder, renderedModels, mainPart, zeroRotation, mrde);
   }

   public Iterable<class_630> handleDeclaredMethod(Method m, class_583 entityModel) {
      return m != null ? Misc.getReflectMethodValue(entityModel, m) : null;
   }

   public class_630 renderPartsIterable(
      Iterable<class_630> parts,
      class_4587 matrixStack,
      class_4588 vertexBuilder,
      ArrayList<class_630> renderedModels,
      class_630 mainPart,
      boolean zeroRotation,
      ModelRenderDetectionElement mrde
   ) {
      if (parts == null) {
         return mainPart;
      } else {
         Iterator<class_630> partsIterator = parts.iterator();
         if (partsIterator.hasNext()) {
            if (mainPart == null) {
               mainPart = partsIterator.next();
               this.renderPart(matrixStack, vertexBuilder, mainPart, renderedModels, mainPart, zeroRotation, mrde);
            }

            while (partsIterator.hasNext()) {
               this.renderPart(matrixStack, vertexBuilder, partsIterator.next(), renderedModels, mainPart, zeroRotation, mrde);
            }
         }

         return mainPart;
      }
   }

   public static class VertexConsumerWrapper implements class_4588 {
      private class_4588 consumer;
      private boolean detectedVertex;
      private double detectionMinX;
      private double detectionMaxX;
      private double detectionMinY;
      private double detectionMaxY;
      private double detectionMinZ;
      private double detectionMaxZ;

      public EntityIconModelPartsRenderer.VertexConsumerWrapper prepareDetection(
         class_4588 consumer,
         double detectionMinX,
         double detectionMaxX,
         double detectionMinY,
         double detectionMaxY,
         double detectionMinZ,
         double detectionMaxZ
      ) {
         this.consumer = consumer;
         this.detectionMinX = detectionMinX;
         this.detectionMaxX = detectionMaxX;
         this.detectionMinY = detectionMinY;
         this.detectionMaxY = detectionMaxY;
         this.detectionMinZ = detectionMinZ;
         this.detectionMaxZ = detectionMaxZ;
         this.detectedVertex = false;
         return this;
      }

      public class_4588 method_22912(double d, double e, double f) {
         if (d >= this.detectionMinX
            && d <= this.detectionMaxX
            && e >= this.detectionMinY
            && e <= this.detectionMaxY
            && f >= this.detectionMinZ
            && f <= this.detectionMaxZ) {
            this.detectedVertex = true;
         }

         return this.consumer.method_22912(d, e, f);
      }

      public class_4588 method_1336(int i, int j, int k, int l) {
         return this.consumer.method_1336(i, j, k, l);
      }

      public class_4588 method_22913(float f, float g) {
         return this.consumer.method_22913(f, g);
      }

      public class_4588 method_22917(int i, int j) {
         return this.consumer.method_22917(i, j);
      }

      public class_4588 method_22921(int i, int j) {
         return this.consumer.method_22921(i, j);
      }

      public class_4588 method_22914(float f, float g, float h) {
         return this.consumer.method_22914(f, g, h);
      }

      public void method_1344() {
         this.consumer.method_1344();
      }

      public boolean hasDetectedVertex() {
         return this.detectedVertex;
      }

      public void method_22901(int i, int j, int k, int l) {
         this.consumer.method_22901(i, j, k, l);
      }

      public void method_35666() {
         this.consumer.method_35666();
      }
   }
}
