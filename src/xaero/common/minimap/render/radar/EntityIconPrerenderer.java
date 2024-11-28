package xaero.common.minimap.render.radar;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.UnmodifiableIterator;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import net.minecraft.class_1058;
import net.minecraft.class_1297;
import net.minecraft.class_1299;
import net.minecraft.class_1309;
import net.minecraft.class_1921;
import net.minecraft.class_276;
import net.minecraft.class_287;
import net.minecraft.class_2960;
import net.minecraft.class_308;
import net.minecraft.class_310;
import net.minecraft.class_332;
import net.minecraft.class_3882;
import net.minecraft.class_4587;
import net.minecraft.class_4588;
import net.minecraft.class_4592;
import net.minecraft.class_4595;
import net.minecraft.class_4597;
import net.minecraft.class_4599;
import net.minecraft.class_4668;
import net.minecraft.class_4723;
import net.minecraft.class_5597;
import net.minecraft.class_572;
import net.minecraft.class_583;
import net.minecraft.class_630;
import net.minecraft.class_8251;
import net.minecraft.class_895;
import net.minecraft.class_897;
import net.minecraft.class_922;
import net.minecraft.class_4587.class_4665;
import net.minecraft.class_4597.class_4598;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;
import xaero.common.IXaeroMinimap;
import xaero.common.MinimapLogs;
import xaero.common.exception.OpenGLException;
import xaero.common.graphics.CustomRenderTypes;
import xaero.common.graphics.ImprovedFramebuffer;
import xaero.common.icon.XaeroIcon;
import xaero.common.icon.XaeroIconAtlas;
import xaero.common.icon.XaeroIconAtlasManager;
import xaero.common.minimap.render.MinimapRendererHelper;
import xaero.common.minimap.render.radar.custom.EntityIconCustomRenderer;
import xaero.common.minimap.render.radar.resource.EntityIconModelConfig;
import xaero.common.misc.Misc;
import xaero.common.misc.OptimizedMath;

public class EntityIconPrerenderer {
   private static final int PREFERRED_ATLAS_WIDTH = 1024;
   public static final int ICON_WIDTH = 64;
   public static final int FAR_PLANE = 500;
   private static final Object[] ONE_RENDERER_ARRAY = new Object[1];
   private static final Object[] ONE_OBJECT_ARRAY = new Object[1];
   private static final boolean TEST_ALL_FIELDS = false;
   private static ArrayList<String> failedFields = new ArrayList<>();
   private final IXaeroMinimap modMain;
   private ImprovedFramebuffer modelRenderFramebuffer;
   private ImprovedFramebuffer iconRenderFramebuffer;
   private ImprovedFramebuffer atlasRenderFramebuffer;
   private class_4665 shaderMatrixBackup;
   private EntityIconModelPartsRenderer entityModelPartsRenderer;
   private ResolvedFieldModelPartsRenderer modelPartsFieldResolverListener;
   private ResolvedFieldModelRootPathListener resolvedFieldModelRootPathListener;
   private LivingEntityRotationResetter livingEntityRotationResetter;
   private final XaeroIconAtlasManager iconAtlasManager;
   private class_4598 entityIconRenderTypeBuffer = class_4597.method_22991(new class_287(256));
   public static boolean DETECTING_MODEL_RENDERS;
   private class_1297 modelRenderDetectionEntity;
   private class_897 modelRenderDetectionEntityRenderer;
   private Class<?> modelRenderDetectionEntityModelClass;
   private List<ModelRenderDetectionElement> modelRenderDetectionList;
   private ModelRenderDetectionElement lastModelRenderDetected;
   private class_4598 modelRenderDetectionRenderTypeBuffer;
   private Field lastRenderTypeField;
   private Class<?> renderTypeTypeClass;
   private Field enderDragonModelField;
   private Field renderStateField;
   private Class<?> renderPhaseTextureClass;
   private Field renderStateTextureStateField;
   private Field renderStateTextureStateTextureField;
   private Field renderStateDepthTestStateField;
   private Field renderStateWriteMaskStateField;
   private Field renderStateCullStateField;
   private Field renderStateTransparencyStateField;
   private Field renderStateShaderStateField;
   private Field vanillaEntityVertexConsumersField;
   private Field spriteCoordinateExpanderSpriteField;
   private Class<?> irisRenderLayerWrapperClass;
   private Method irisRenderLayerWrapperUnwrapMethod;

   public EntityIconPrerenderer(IXaeroMinimap modMain) {
      this.modMain = modMain;
      this.modelRenderFramebuffer = new ImprovedFramebuffer(512, 512, true);
      OpenGLException.checkGLError();
      this.iconRenderFramebuffer = new ImprovedFramebuffer(512, 512, false);
      OpenGLException.checkGLError();
      this.entityModelPartsRenderer = new EntityIconModelPartsRenderer(modMain);
      this.livingEntityRotationResetter = new LivingEntityRotationResetter();
      this.modelRenderFramebuffer.method_35610();
      OpenGLException.checkGLError();
      GL11.glTexParameteri(3553, 33085, 0);
      GL11.glTexParameterf(3553, 33082, 0.0F);
      GL11.glTexParameterf(3553, 33083, 0.0F);
      GL11.glTexParameterf(3553, 34049, 0.0F);
      GL11.glTexParameteri(3553, 10241, 9728);
      GL11.glTexParameteri(3553, 10240, 9728);
      GL11.glTexParameteri(3553, 10242, 33071);
      GL11.glTexParameteri(3553, 10243, 33071);
      GlStateManager._texImage2D(
         3553, 0, 32856, this.modelRenderFramebuffer.field_1480, this.modelRenderFramebuffer.field_1477, 0, 32993, 32821, (IntBuffer)null
      );
      OpenGLException.checkGLError();
      GlStateManager._bindTexture(0);
      this.iconRenderFramebuffer.method_35610();
      GL11.glTexParameteri(3553, 33085, 3);
      GL11.glTexParameterf(3553, 33082, 0.0F);
      GL11.glTexParameterf(3553, 33083, 3.0F);
      GL11.glTexParameterf(3553, 34049, 0.0F);
      GL11.glTexParameteri(3553, 10241, 9984);
      GL11.glTexParameteri(3553, 10240, 9728);
      GL11.glTexParameteri(3553, 10242, 33071);
      GL11.glTexParameteri(3553, 10243, 33071);
      GlStateManager._texImage2D(3553, 0, 32856, this.iconRenderFramebuffer.field_1480, this.iconRenderFramebuffer.field_1477, 0, 32993, 32821, (IntBuffer)null);
      OpenGLException.checkGLError();
      GlStateManager._bindTexture(0);
      this.modelRenderDetectionList = new ArrayList<>();
      this.enderDragonModelField = Misc.getFieldReflection(class_895.class, "model", "field_21008", "Lnet/minecraft/class_895$class_625;", "f_114183_");

      try {
         this.lastRenderTypeField = Misc.getFieldReflection(class_4598.class, "lastState", "field_20954", "Ljava/util/Optional;", "f_109906_");
      } catch (Exception var8) {
         this.lastRenderTypeField = Misc.getFieldReflection(class_4598.class, "lastState", "c", "Ljava/util/Optional;", "f_109906_");
      }

      try {
         this.renderTypeTypeClass = Misc.getClassForName("net.minecraft.class_1921$class_4687", "net.minecraft.client.renderer.RenderType$CompositeRenderType");
         this.renderStateField = Misc.getFieldReflection(this.renderTypeTypeClass, "state", "field_21403", "Lnet/minecraft/class_1921$class_4688;", "f_110511_");
      } catch (ClassNotFoundException var7) {
         throw new RuntimeException(var7);
      }

      try {
         Class<?> multiPhaseParametersClass = Misc.getClassForName(
            "net.minecraft.class_1921$class_4688", "net.minecraft.client.renderer.RenderType$CompositeState"
         );
         this.renderPhaseTextureClass = Misc.getClassForName(
            "net.minecraft.class_4668$class_4683", "net.minecraft.client.renderer.RenderStateShard$TextureStateShard"
         );
         this.renderStateTextureStateField = Misc.getFieldReflection(
            multiPhaseParametersClass, "textureState", "field_21406", "Lnet/minecraft/class_4668$class_5939;", "f_110576_"
         );
         this.renderStateTextureStateTextureField = Misc.getFieldReflection(
            this.renderPhaseTextureClass, "texture", "field_21397", "Ljava/util/Optional;", "f_110328_"
         );
         this.renderStateTransparencyStateField = Misc.getFieldReflection(
            multiPhaseParametersClass, "transparencyState", "field_21407", "Lnet/minecraft/class_4668$class_4685;", "f_110577_"
         );
         this.renderStateDepthTestStateField = Misc.getFieldReflection(
            multiPhaseParametersClass, "depthTestState", "field_21411", "Lnet/minecraft/class_4668$class_4672;", "f_110581_"
         );
         this.renderStateWriteMaskStateField = Misc.getFieldReflection(
            multiPhaseParametersClass, "writeMaskState", "field_21419", "Lnet/minecraft/class_4668$class_4686;", "f_110589_"
         );
         this.renderStateCullStateField = Misc.getFieldReflection(
            multiPhaseParametersClass, "cullState", "field_21412", "Lnet/minecraft/class_4668$class_4671;", "f_110582_"
         );
         this.renderStateShaderStateField = Misc.getFieldReflection(
            multiPhaseParametersClass, "shaderState", "field_29461", "Lnet/minecraft/class_4668$class_5942;", "f_173274_"
         );
      } catch (ClassNotFoundException var6) {
         throw new RuntimeException(var6);
      }

      this.vanillaEntityVertexConsumersField = Misc.getFieldReflection(
         class_4599.class, "bufferSource", "field_20958", "Lnet/minecraft/class_4597$class_4598;", "f_110094_"
      );
      this.spriteCoordinateExpanderSpriteField = Misc.getFieldReflection(class_4723.class, "sprite", "field_21731", "Lnet/minecraft/class_1058;", "f_110796_");

      try {
         try {
            this.irisRenderLayerWrapperClass = Class.forName("net.coderbot.iris.layer.IrisRenderTypeWrapper");
         } catch (ClassNotFoundException var4) {
            this.irisRenderLayerWrapperClass = Class.forName("net.coderbot.iris.layer.IrisRenderLayerWrapper");
         }

         this.irisRenderLayerWrapperUnwrapMethod = Misc.getMethodReflection(
            this.irisRenderLayerWrapperClass, "unwrap", "unwrap", "()Lnet/minecraft/class_1921;", "unwrap"
         );
         MinimapLogs.LOGGER.info("Old Iris!");
      } catch (Exception var5) {
      }

      this.modelPartsFieldResolverListener = new ResolvedFieldModelPartsRenderer();
      this.resolvedFieldModelRootPathListener = new ResolvedFieldModelRootPathListener();
      int maxTextureSize = GlStateManager._getInteger(3379);
      int atlasTextureSize = Math.min(maxTextureSize, 1024) / 64 * 64;
      this.iconAtlasManager = new XaeroIconAtlasManager(64, atlasTextureSize, new ArrayList<>());
      this.atlasRenderFramebuffer = new ImprovedFramebuffer(atlasTextureSize, atlasTextureSize, false);
      OpenGLException.checkGLError();
      GlStateManager._deleteTexture(this.atlasRenderFramebuffer.getFramebufferTexture());
      OpenGLException.checkGLError();
   }

   void clearAtlases() {
      this.iconAtlasManager.clearAtlases();
      this.atlasRenderFramebuffer.setFramebufferTexture(0);
   }

   private XaeroIconAtlas getCurrentAtlas() throws Exception {
      return this.iconAtlasManager.getCurrentAtlas();
   }

   private static void testField(ImmutableList<String> fields) throws NoSuchFieldException, SecurityException {
      boolean odd = true;
      boolean pairFirstFailed = false;
      String prevS = null;
      UnmodifiableIterator var4 = fields.iterator();

      while (var4.hasNext()) {
         String s = (String)var4.next();

         try {
            odd = !odd;
            if (!odd) {
               pairFirstFailed = false;
            }

            boolean failed = false;
            if (!s.isEmpty()) {
               String[] fieldArgs = s.split(";");
               Class<?> c = null;

               try {
                  c = Class.forName(fieldArgs[0]);
               } catch (ClassNotFoundException var15) {
                  MinimapLogs.LOGGER.info("Skipping testing a class: " + fieldArgs[0]);
                  continue;
               }

               try {
                  c.getDeclaredField(fieldArgs[1]);
               } catch (Exception var14) {
                  failed = true;
               }
            } else {
               failed = true;
            }

            if (failed) {
               if (!odd) {
                  pairFirstFailed = true;
               } else if (pairFirstFailed) {
                  failedFields.add(prevS);
                  failedFields.add(s);
               }
            }
         } finally {
            ;
         }
      }
   }

   public XaeroIcon prerender(
      class_332 guiGraphics,
      String cacheKey,
      class_897 entityRenderer,
      class_1297 entity,
      class_276 defaultFramebuffer,
      MinimapRendererHelper helper,
      float scale,
      EntityIconModelConfig modelConfig,
      EntityIconModelConfig defaultModelConfig,
      class_2960 readySprite,
      boolean outlined,
      boolean flipped,
      boolean debug
   ) {
      ImprovedFramebuffer modelRenderFramebuffer = this.modelRenderFramebuffer;
      ImprovedFramebuffer iconRenderFramebuffer = this.iconRenderFramebuffer;
      OpenGLException.checkGLError();
      class_4587 matrixStack = guiGraphics.method_51448();
      modelRenderFramebuffer.method_1235(true);
      this.setupMatrices(matrixStack, 64, 500);
      OpenGLException.checkGLError();
      boolean renderedSomething;
      if (readySprite == null) {
         GlStateManager._enableCull();
         if (class_310.method_1551().method_1561().field_4686 == null) {
            MinimapLogs.LOGGER.info("Render info was null for entity " + entity.method_5820());
         } else {
            DETECTING_MODEL_RENDERS = true;
            this.modelRenderDetectionEntity = entity;
            this.modelRenderDetectionEntityRenderer = entityRenderer;
            this.modelRenderDetectionEntityModelClass = null;
            this.modelRenderDetectionList.clear();
            this.lastModelRenderDetected = null;
            class_4665 matrixEntryToRestore = matrixStack.method_23760();
            matrixStack.method_22903();

            try {
               class_4598 renderTypeBuffer = this.modelRenderDetectionRenderTypeBuffer = Misc.getReflectFieldValue(
                  class_310.method_1551().method_22940(), this.vanillaEntityVertexConsumersField
               );
               entityRenderer.method_3936(entity, 0.0F, 1.0F, matrixStack, renderTypeBuffer, 15728880);
               renderTypeBuffer.method_22993();
               OpenGLException.checkGLError();
            } catch (Throwable var24) {
               this.modelRenderDetectionList.clear();
               MinimapLogs.LOGGER.error("Exception when calling the full entity renderer before rendering the icon. " + entity.method_5820(), var24);
            }

            modelRenderFramebuffer.method_1235(true);
            DETECTING_MODEL_RENDERS = false;
            this.modelRenderDetectionEntity = null;
            this.modelRenderDetectionEntityRenderer = null;

            while (matrixStack.method_23760() != matrixEntryToRestore) {
               matrixStack.method_22909();
            }

            while (GL11.glGetError() != 0) {
            }
         }

         class_4598 renderTypeBuffer = this.entityIconRenderTypeBuffer;
         class_583 entityModel = this.getEntityRendererModel(entityRenderer);
         if (entityModel == null) {
            this.endModelRendering(modelRenderFramebuffer);
            this.bindDefaultFramebuffer(defaultFramebuffer);
            this.restoreMatrices(matrixStack, helper, defaultFramebuffer);
            return EntityIconManager.FAILED;
         }

         GlStateManager._disableBlend();
         GlStateManager._clearColor(0.0F, 0.0F, 0.0F, 0.0F);
         GlStateManager._clear(16640, class_310.field_1703);
         GlStateManager._enableBlend();
         GlStateManager._disableDepthTest();
         GlStateManager._enableCull();
         GlStateManager._depthFunc(515);
         class_308.method_24210();
         if (debug) {
            matrixStack.method_22903();
            matrixStack.method_46416(0.0F, 10.0F, -10.0F);
            matrixStack.method_22905(1.0F, -1.0F, 1.0F);
            guiGraphics.method_25294(0, 0, 9, 9, -65536);
            matrixStack.method_22909();
            GlStateManager._enableBlend();
            RenderSystem.blendFuncSeparate(770, 771, 1, 771);
         }

         class_4665 matrixEntryToRestore = matrixStack.method_23760();
         matrixStack.method_22903();
         matrixStack.method_46416(32.0F, 32.0F, -450.0F);
         matrixStack.method_46416(modelConfig.offsetX, -modelConfig.offsetY, 0.0F);
         int mainScale = 32;
         matrixStack.method_22905((float)mainScale, (float)(-mainScale), (float)(-mainScale));
         if (scale < 1.0F) {
            matrixStack.method_22905(scale, scale, scale);
         }

         matrixStack.method_22905(modelConfig.baseScale, modelConfig.baseScale, modelConfig.baseScale);
         OptimizedMath.rotatePose(matrixStack, modelConfig.rotationY, OptimizedMath.YP);
         OptimizedMath.rotatePose(matrixStack, modelConfig.rotationX, OptimizedMath.XP);
         OptimizedMath.rotatePose(matrixStack, modelConfig.rotationZ, OptimizedMath.ZP);
         EntityIconDefinitions.customTransformation(matrixStack, entityModel, entity, this);
         class_1309 livingEntity = entity instanceof class_1309 ? (class_1309)entity : null;
         if (livingEntity != null) {
            this.livingEntityRotationResetter.rememberAndResetValues(livingEntity);
         }

         renderedSomething = this.renderIcon(
            matrixStack, renderTypeBuffer, entityRenderer, entityModel, this.modelRenderDetectionList, entity, modelConfig, defaultModelConfig
         );
         if (livingEntity != null) {
            this.livingEntityRotationResetter.restore(livingEntity);
         }

         EntityIconDefinitions.customPostRenderTransformation(matrixStack, entityModel, entity);

         while (matrixStack.method_23760() != matrixEntryToRestore) {
            matrixStack.method_22909();
         }

         if (debug) {
            matrixStack.method_22903();
            matrixStack.method_46416(9.0F, 10.0F, -10.0F);
            matrixStack.method_22905(1.0F, -1.0F, 1.0F);
            guiGraphics.method_25294(0, 0, 9, 9, -16711936);
            matrixStack.method_22909();
            RenderSystem.blendFuncSeparate(770, 771, 1, 771);
         }
      } else {
         matrixStack.method_22903();
         GlStateManager._disableBlend();
         GlStateManager._clearColor(0.0F, 0.0F, 0.0F, 0.0F);
         GlStateManager._clear(16640, class_310.field_1703);
         GlStateManager._enableBlend();
         GlStateManager._disableCull();
         class_308.method_24210();
         class_310.method_1551().method_1531().method_22813(readySprite);
         GL11.glTexParameteri(3553, 10241, 9729);
         GL11.glTexParameteri(3553, 10240, 9728);
         GL11.glTexParameteri(3553, 10242, 33071);
         GL11.glTexParameteri(3553, 10243, 33071);
         matrixStack.method_46416(32.0F, 32.0F, 1.0F);
         if (scale < 1.0F) {
            matrixStack.method_22905(scale, scale, 1.0F);
         }

         RenderSystem.setShaderTexture(0, readySprite);
         RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
         helper.drawMyTexturedModalRect(matrixStack, -32.0F, -32.0F, 0, 0, 64.0F, 64.0F, 64.0F, 64.0F);
         GlStateManager._enableCull();
         matrixStack.method_22909();
         renderedSomething = true;
      }

      this.endModelRendering(modelRenderFramebuffer);
      XaeroIcon icon = EntityIconManager.FAILED;
      if (renderedSomething) {
         iconRenderFramebuffer.method_1235(true);
         GlStateManager._clearColor(0.0F, 0.0F, 0.0F, 0.0F);
         GlStateManager._clear(16384, class_310.field_1703);
         GlStateManager._disableBlend();
         if (debug) {
            matrixStack.method_22903();
            matrixStack.method_46416(18.0F, 10.0F, -10.0F);
            matrixStack.method_22905(1.0F, -1.0F, 1.0F);
            guiGraphics.method_25294(0, 0, 9, 9, -16776961);
            matrixStack.method_22909();
            RenderSystem.blendFuncSeparate(770, 771, 1, 771);
         }

         modelRenderFramebuffer.method_35610();
         RenderSystem.setShaderColor(0.0F, 0.0F, 0.0F, 1.0F);
         GlStateManager._enableBlend();
         RenderSystem.blendFuncSeparate(770, 771, 1, 1);
         if (outlined) {
            for (int shadowOffsetX = -1; shadowOffsetX < 2; shadowOffsetX++) {
               for (int shadowOffsetY = -1; shadowOffsetY < 2; shadowOffsetY++) {
                  if (shadowOffsetX != 0 || shadowOffsetY != 0) {
                     helper.drawIconOutline(matrixStack, (float)shadowOffsetX, (float)(64 + shadowOffsetY), 0, 0, 64.0F, -64.0F, 64.0F, 64.0F, 0.05F);
                  }
               }
            }
         }

         RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
         GlStateManager._disableBlend();
         helper.drawMyTexturedModalRect(matrixStack, 0.0F, 64.0F, 0, 0, 64.0F, -64.0F, 64.0F, 64.0F, 0.05F, false);
         RenderSystem.blendFuncSeparate(770, 771, 1, 771);
         if (debug) {
            matrixStack.method_22903();
            matrixStack.method_46416(27.0F, 10.0F, -10.0F);
            matrixStack.method_22905(1.0F, -1.0F, 1.0F);
            guiGraphics.method_25294(0, 0, 9, 9, -16711681);
            matrixStack.method_22909();
            RenderSystem.blendFuncSeparate(770, 771, 1, 771);
         }

         GlStateManager._enableBlend();
         iconRenderFramebuffer.method_1240();
         iconRenderFramebuffer.method_35610();
         iconRenderFramebuffer.generateMipmaps();
         GlStateManager._bindTexture(0);

         try {
            XaeroIconAtlas atlas = this.getCurrentAtlas();
            icon = atlas.createIcon();
            this.atlasRenderFramebuffer.method_1235(false);
            GlStateManager._viewport(icon.getOffsetX(), icon.getOffsetY(), 64, 64);
            this.atlasRenderFramebuffer.setFramebufferTexture(atlas.getTextureId());
            this.atlasRenderFramebuffer.method_1239();
            iconRenderFramebuffer.method_35610();
            GlStateManager._disableBlend();
            if (flipped) {
               helper.drawMyTexturedModalRect(matrixStack, 0.0F, 64.0F, 0, 64, 64.0F, -64.0F, -64.0F, 64.0F, -1.0F, false);
            } else {
               helper.drawMyTexturedModalRect(matrixStack, 0.0F, 64.0F, 0, 0, 64.0F, -64.0F, 64.0F, 64.0F, -1.0F, false);
            }

            if (debug) {
               matrixStack.method_22903();
               matrixStack.method_46416(36.0F, 10.0F, -10.0F);
               matrixStack.method_22905(1.0F, -1.0F, 1.0F);
               guiGraphics.method_25294(0, 0, 9, 9, -256);
               matrixStack.method_22909();
               RenderSystem.blendFuncSeparate(770, 771, 1, 771);
            }
         } catch (Throwable var23) {
            MinimapLogs.LOGGER.error(String.format("Exception rendering to a entity icon atlas for %s!", cacheKey), var23);
         }

         GlStateManager._enableBlend();
         GlStateManager._bindTexture(0);
         MinimapRendererHelper.restoreDefaultShaderBlendState();
      }

      this.restoreMatrices(matrixStack, helper, defaultFramebuffer);
      this.atlasRenderFramebuffer.method_1240();
      this.bindDefaultFramebuffer(defaultFramebuffer);
      return icon;
   }

   private void bindDefaultFramebuffer(class_276 defaultFramebuffer) {
      if (defaultFramebuffer != null) {
         defaultFramebuffer.method_1235(true);
      } else {
         this.atlasRenderFramebuffer.bindDefaultFramebuffer(class_310.method_1551());
         GlStateManager._viewport(0, 0, class_310.method_1551().method_22683().method_4489(), class_310.method_1551().method_22683().method_4506());
      }
   }

   private void setupMatrices(class_4587 matrixStack, int finalIconSize, int farPlane) {
      this.shaderMatrixBackup = RenderSystem.getModelViewStack().method_23760();
      matrixStack.method_22903();
      matrixStack.method_34426();
      Matrix4f ortho = new Matrix4f().setOrtho(0.0F, (float)finalIconSize, 0.0F, (float)finalIconSize, -1.0F, (float)farPlane);
      RenderSystem.setProjectionMatrix(ortho, class_8251.field_43361);
      RenderSystem.getModelViewStack().method_22903();
      RenderSystem.getModelViewStack().method_34426();
      RenderSystem.applyModelViewMatrix();
   }

   private void restoreMatrices(class_4587 matrixStack, MinimapRendererHelper helper, class_276 framebuffer) {
      matrixStack.method_22909();
      helper.defaultOrtho(framebuffer, false);
      if (RenderSystem.getModelViewStack().method_23760() != this.shaderMatrixBackup) {
         RenderSystem.getModelViewStack().method_22909();
      }

      RenderSystem.applyModelViewMatrix();
   }

   private void endModelRendering(ImprovedFramebuffer modelRenderFramebuffer) {
      modelRenderFramebuffer.method_1240();
      GlStateManager._enableBlend();
      class_308.method_24211();
      class_308.method_24210();
   }

   private class_630 renderModel(
      class_4587 matrixStack,
      class_4598 renderTypeBuffer,
      class_897 entityRenderer,
      class_583 entityModel,
      class_1297 entity,
      EntityIconModelConfig modelConfig,
      EntityIconModelConfig defaultModelConfig,
      class_630 mainPart,
      ArrayList<class_630> renderedModels,
      class_2960 entityTexture,
      class_1058 entityAtlasSprite,
      boolean forceFieldCheck,
      boolean fullModelIcon,
      ModelRenderDetectionElement mrde,
      List<String> hardcodedMainPartAliases,
      List<String> hardcodedModelPartsFields
   ) {
      boolean isChildBU = entityModel.field_3448;
      entityModel.field_3448 = false;
      if (entityTexture != null) {
         Object modelRoot = null;
         if (modelConfig.modelRootPath != null) {
            modelRoot = this.resolveModelRoot(entityModel, modelConfig.modelRootPath, entity);
         }

         if (modelRoot == null) {
            modelRoot = EntityIconDefinitions.getModelRoot(entityRenderer, entityModel);
         }

         boolean treatAsHierarchicalRoot = false;
         if (modelConfig == defaultModelConfig && modelRoot instanceof class_4592 && !(modelRoot instanceof class_572)) {
            Iterable<class_630> headPartsTest = Misc.getReflectMethodValue(modelRoot, this.entityModelPartsRenderer.ageableModelHeadPartsMethod);
            if (headPartsTest != null) {
               Iterator<class_630> iterator = headPartsTest.iterator();
               class_630 headPartTest;
               if (iterator.hasNext()
                  && (headPartTest = iterator.next()) != null
                  && !iterator.hasNext()
                  && !this.entityModelPartsRenderer.hasDirectCubes(headPartTest)) {
                  modelRoot = headPartTest;
                  treatAsHierarchicalRoot = true;
                  fullModelIcon = true;
               }
            }
         }

         class_4588 vertexBuilder = this.setupModelRenderType(renderTypeBuffer, entityTexture, entityAtlasSprite, mrde);
         if (modelConfig.modelMainPartFieldAliases != null && !modelConfig.modelMainPartFieldAliases.isEmpty()) {
            mainPart = this.searchSuperclassFields(
               matrixStack,
               vertexBuilder,
               modelRoot,
               renderedModels,
               mainPart,
               modelConfig.modelMainPartFieldAliases,
               true,
               modelConfig.modelPartsRotationReset,
               mrde
            );
         }

         if (!forceFieldCheck && modelRoot instanceof class_4592) {
            if (modelRoot instanceof class_572) {
               class_630 headRenderer = ((class_572)modelRoot).field_3398;
               if (mainPart == null) {
                  mainPart = headRenderer;
               }

               class_630 headWearRenderer = ((class_572)modelRoot).field_3394;
               this.entityModelPartsRenderer
                  .renderPart(matrixStack, vertexBuilder, headRenderer, renderedModels, mainPart, modelConfig.modelPartsRotationReset, mrde);
               this.entityModelPartsRenderer
                  .renderPart(matrixStack, vertexBuilder, headWearRenderer, renderedModels, mainPart, modelConfig.modelPartsRotationReset, mrde);
            }

            mainPart = this.entityModelPartsRenderer
               .renderDeclaredMethod(
                  matrixStack,
                  vertexBuilder,
                  this.entityModelPartsRenderer.ageableModelHeadPartsMethod,
                  (class_4592)modelRoot,
                  renderedModels,
                  mainPart,
                  modelConfig.modelPartsRotationReset,
                  mrde
               );
            if (fullModelIcon) {
               mainPart = this.entityModelPartsRenderer
                  .renderDeclaredMethod(
                     matrixStack,
                     vertexBuilder,
                     this.entityModelPartsRenderer.ageableModelBodyPartsMethod,
                     (class_4592)modelRoot,
                     renderedModels,
                     mainPart,
                     modelConfig.modelPartsRotationReset,
                     mrde
                  );
            }
         } else {
            boolean singlePartSucceeded = false;
            if (!forceFieldCheck && (treatAsHierarchicalRoot || modelRoot instanceof class_5597)) {
               class_630 rootPart;
               if (treatAsHierarchicalRoot) {
                  rootPart = (class_630)modelRoot;
               } else {
                  class_5597 singlePartModel = (class_5597)modelRoot;
                  rootPart = singlePartModel.method_32008();
               }

               if (rootPart != null) {
                  class_630 headPart;
                  try {
                     headPart = rootPart.method_32086("head");
                  } catch (NoSuchElementException var25) {
                     headPart = null;
                  }

                  if (headPart != null) {
                     if (mainPart == null) {
                        mainPart = headPart;
                     }

                     this.entityModelPartsRenderer
                        .renderPart(matrixStack, vertexBuilder, headPart, renderedModels, mainPart, modelConfig.modelPartsRotationReset, mrde);
                     singlePartSucceeded = true;
                  }

                  if (fullModelIcon) {
                     Map<String, class_630> rootChildren = this.entityModelPartsRenderer.getChildModels(rootPart);
                     mainPart = this.entityModelPartsRenderer
                        .renderPartsIterable(
                           rootChildren.values(), matrixStack, vertexBuilder, renderedModels, mainPart, modelConfig.modelPartsRotationReset, mrde
                        );
                     singlePartSucceeded = true;
                  }
               }
            }

            if (!singlePartSucceeded) {
               if (!forceFieldCheck && modelRoot instanceof class_4595 && fullModelIcon) {
                  mainPart = this.entityModelPartsRenderer
                     .renderDeclaredMethod(
                        matrixStack,
                        vertexBuilder,
                        this.entityModelPartsRenderer.segmentedModelPartsMethod,
                        (class_4595)modelRoot,
                        renderedModels,
                        mainPart,
                        modelConfig.modelPartsRotationReset,
                        mrde
                     );
               } else {
                  if (!forceFieldCheck && modelRoot instanceof class_3882) {
                     class_630 headPartx = ((class_3882)modelRoot).method_2838();
                     if (mainPart == null) {
                        mainPart = headPartx;
                     }

                     this.entityModelPartsRenderer
                        .renderPart(matrixStack, vertexBuilder, headPartx, renderedModels, mainPart, modelConfig.modelPartsRotationReset, mrde);
                  }

                  if (modelConfig.modelPartsFields == null) {
                     mainPart = this.searchSuperclassFields(
                        matrixStack,
                        vertexBuilder,
                        modelRoot,
                        renderedModels,
                        mainPart,
                        hardcodedMainPartAliases,
                        true,
                        modelConfig.modelPartsRotationReset,
                        mrde
                     );
                  }

                  List<String> headPartsFields = hardcodedModelPartsFields;
                  if (fullModelIcon) {
                     headPartsFields = null;
                  } else if (modelConfig.modelPartsFields != null) {
                     headPartsFields = modelConfig.modelPartsFields;
                  }

                  mainPart = this.searchSuperclassFields(
                     matrixStack, vertexBuilder, modelRoot, renderedModels, mainPart, headPartsFields, false, modelConfig.modelPartsRotationReset, mrde
                  );
               }
            }
         }

         renderTypeBuffer.method_22993();
      }

      entityModel.field_3448 = isChildBU;
      return mainPart;
   }

   private class_4588 setupModelRenderType(
      class_4598 renderTypeBuffer, class_2960 entityTexture, class_1058 entityAtlasSprite, ModelRenderDetectionElement mrde
   ) {
      class_4588 regularConsumer = renderTypeBuffer.getBuffer(CustomRenderTypes.entityIconRenderType(entityTexture, mrde.layerPhases));
      return entityAtlasSprite != null ? entityAtlasSprite.method_24108(regularConsumer) : regularConsumer;
   }

   public class_630 searchSuperclassFields(
      class_4587 matrixStack,
      class_4588 vertexBuilder,
      Object modelRoot,
      ArrayList<class_630> renderedModels,
      class_630 mainPart,
      List<String> filter,
      boolean justOne,
      boolean zeroRotation,
      ModelRenderDetectionElement mrde
   ) {
      this.modelPartsFieldResolverListener
         .prepare(matrixStack, vertexBuilder, justOne, renderedModels, mainPart, zeroRotation, mrde, this.entityModelPartsRenderer);
      EntityIconModelFieldResolver.searchSuperclassFields(modelRoot, filter, this.modelPartsFieldResolverListener, ONE_RENDERER_ARRAY);
      return this.modelPartsFieldResolverListener.getMainPart();
   }

   public class_630 handleFields(
      class_4587 matrixStack,
      class_4588 vertexBuilder,
      Object modelRoot,
      Field[] declaredModelFields,
      ArrayList<class_630> renderedModels,
      class_630 mainPart,
      List<String> filter,
      boolean justOne,
      boolean zeroRotation,
      ModelRenderDetectionElement mrde
   ) {
      this.modelPartsFieldResolverListener
         .prepare(matrixStack, vertexBuilder, justOne, renderedModels, mainPart, zeroRotation, mrde, this.entityModelPartsRenderer);
      EntityIconModelFieldResolver.handleFields(modelRoot, declaredModelFields, filter, this.modelPartsFieldResolverListener, ONE_RENDERER_ARRAY);
      return this.modelPartsFieldResolverListener.getMainPart();
   }

   public void generateMipmaps() {
      this.modelRenderFramebuffer.generateMipmaps();
   }

   public void onModelRenderDetection(class_583<?> model, class_4588 vertexConsumer, float red, float green, float blue, float alpha) {
      if (this.modelRenderDetectionEntityModelClass == null) {
         class_583 currentMainModel = this.getEntityRendererModel(this.modelRenderDetectionEntityRenderer);
         this.modelRenderDetectionEntityModelClass = currentMainModel == null ? null : currentMainModel.getClass();
      }

      if (this.modelRenderDetectionEntityModelClass == model.getClass()) {
         class_1921 lastRenderType = this.getLastRenderType(this.modelRenderDetectionRenderTypeBuffer);
         if (lastRenderType == null && this.modelRenderDetectionList.isEmpty()) {
            class_2960 textureLocation = null;

            try {
               class_2960 textureLocationUnchecked = this.modelRenderDetectionEntityRenderer.method_3931(this.modelRenderDetectionEntity);
               textureLocation = textureLocationUnchecked;
            } catch (Throwable var19) {
               MinimapLogs.LOGGER.error("Couldn't fetch main entity texture when trying to use an alternative render type for an icon!", var19);
            }

            if (textureLocation != null) {
               lastRenderType = model.method_23500(textureLocation);
            }
         }

         if (lastRenderType != null) {
            Object renderState = this.getRenderState(lastRenderType);
            if (renderState != null) {
               Object renderTextureState = Misc.getReflectFieldValue(renderState, this.renderStateTextureStateField);
               class_2960 texture;
               if (this.renderPhaseTextureClass.isAssignableFrom(renderTextureState.getClass())) {
                  texture = this.getRenderStateTextureStateTexture(renderTextureState);
               } else {
                  texture = null;
               }

               Object renderTransparencyState = Misc.getReflectFieldValue(renderState, this.renderStateTransparencyStateField);
               ((class_4668)renderTransparencyState).method_23516();
               int blendDestFactor = GL11.glGetInteger(32968);
               if (blendDestFactor == 1) {
                  int blendSrcFactor = GL11.glGetInteger(32969);
                  if (blendSrcFactor != 0) {
                     renderTransparencyState = CustomRenderTypes.getTransparencyPhase(blendSrcFactor, blendDestFactor, 0, 1);
                  }
               }

               ((class_4668)renderTransparencyState).method_23518();
               Object renderDepthTestState = Misc.getReflectFieldValue(renderState, this.renderStateDepthTestStateField);
               Object renderWriteMaskState = Misc.getReflectFieldValue(renderState, this.renderStateWriteMaskStateField);
               Object renderCullState = Misc.getReflectFieldValue(renderState, this.renderStateCullStateField);
               Object renderShaderState = Misc.getReflectFieldValue(renderState, this.renderStateShaderStateField);
               CustomRenderTypes.EntityIconLayerPhases layerPhases = new CustomRenderTypes.EntityIconLayerPhases(
                  renderTextureState, renderTransparencyState, renderDepthTestState, renderWriteMaskState, renderCullState, renderShaderState
               );
               class_1058 renderAtlasSprite = null;
               if (vertexConsumer instanceof class_4723) {
                  renderAtlasSprite = Misc.getReflectFieldValue(vertexConsumer, this.spriteCoordinateExpanderSpriteField);
               }

               this.lastModelRenderDetected = new ModelRenderDetectionElement(model, texture, renderAtlasSprite, layerPhases, red, green, blue, alpha);
               this.modelRenderDetectionList.add(this.lastModelRenderDetected);
            }
         }
      }
   }

   public void onModelPartRenderDetection(class_630 modelRenderer, float red, float green, float blue, float alpha) {
      if (this.lastModelRenderDetected != null) {
         this.lastModelRenderDetected.addVisibleModelPart(modelRenderer, red, green, blue, alpha);
      }
   }

   private class_1921 getLastRenderType(class_4597 renderTypeBuffer) {
      if (renderTypeBuffer instanceof class_4598) {
         Object lastRenderTypeObject = Misc.getReflectFieldValue(renderTypeBuffer, this.lastRenderTypeField);
         class_1921 lastRenderType = null;
         if (lastRenderTypeObject instanceof Optional) {
            lastRenderType = (class_1921)((Optional)lastRenderTypeObject).orElse(null);
         } else {
            lastRenderType = (class_1921)lastRenderTypeObject;
         }

         return lastRenderType;
      } else {
         return null;
      }
   }

   private Object getRenderState(class_1921 renderType) {
      while (renderType.getClass() == this.irisRenderLayerWrapperClass && this.irisRenderLayerWrapperUnwrapMethod != null) {
         renderType = Misc.getReflectMethodValue(renderType, this.irisRenderLayerWrapperUnwrapMethod);
      }

      return renderType.getClass() == this.renderTypeTypeClass ? Misc.getReflectFieldValue(renderType, this.renderStateField) : null;
   }

   private class_2960 getRenderStateTextureStateTexture(Object renderTextureState) {
      Object renderStateTextureObject = Misc.getReflectFieldValue(renderTextureState, this.renderStateTextureStateTextureField);
      class_2960 renderStateTexture = null;
      if (renderStateTextureObject instanceof Optional optional) {
         if (!optional.isPresent()) {
            renderStateTexture = null;
         } else {
            renderStateTexture = (class_2960)optional.get();
         }
      } else {
         renderStateTexture = (class_2960)renderStateTextureObject;
      }

      return renderStateTexture;
   }

   private boolean renderIcon(
      class_4587 matrixStack,
      class_4598 renderTypeBuffer,
      class_897 entityRenderer,
      class_583 mainEntityModel,
      List<ModelRenderDetectionElement> detectedModels,
      class_1297 entity,
      EntityIconModelConfig modelConfig,
      EntityIconModelConfig defaultModelConfig
   ) {
      boolean forceFieldCheck = (modelConfig.renderingFullModel == null || !modelConfig.renderingFullModel)
         && (modelConfig.modelPartsFields != null || EntityIconDefinitions.forceFieldCheck(mainEntityModel));
      boolean fullModelIcon = modelConfig.renderingFullModel == null
         ? !forceFieldCheck && EntityIconDefinitions.fullModelIcon(mainEntityModel)
         : modelConfig.renderingFullModel;
      class_630 mainPart = null;
      boolean renderedSomething = false;
      ArrayList<class_630> mainRenderedModels = new ArrayList<>();
      if (detectedModels.isEmpty()) {
         class_2960 mainEntityTexture = null;

         try {
            class_2960 mainEntityTextureUnchecked = entityRenderer.method_3931(entity);
            mainEntityTexture = mainEntityTextureUnchecked;
         } catch (Throwable var26) {
            MinimapLogs.LOGGER.error("Couldn't fetch main entity texture when prerendering an icon with nothing detected!", var26);
         }

         if (mainEntityTexture != null) {
            detectedModels.add(
               new ModelRenderDetectionElement(
                  mainEntityModel, mainEntityTexture, null, CustomRenderTypes.getBasicEntityIconLayerPhases(mainEntityTexture), 1.0F, 1.0F, 1.0F, 1.0F
               )
            );
         }
      }

      boolean allEmpty = true;

      for (ModelRenderDetectionElement mrde : detectedModels) {
         if (!mrde.isEmpty()) {
            allEmpty = false;
            break;
         }
      }

      if (allEmpty) {
         for (ModelRenderDetectionElement mrdex : detectedModels) {
            mrdex.allVisible = true;
         }
      }

      List<String> hardcodedMainPartAliases = EntityIconDefinitions.getMainModelPartFields(entityRenderer, mainEntityModel, entity);
      List<String> hardcodedModelPartsFields = EntityIconDefinitions.getSecondaryModelPartsFields(entityRenderer, mainEntityModel, entity);
      ModelRenderDetectionElement mainMrde = null;

      for (ModelRenderDetectionElement mrdex : detectedModels) {
         if (!mrdex.isEmpty() && (!renderedSomething || modelConfig.layersAllowed)) {
            class_583<?> detectedModel = mrdex.model;
            class_2960 detectedTexture = mrdex.renderTexture;
            class_1058 detectedAtlasSprite = mrdex.renderAtlasSprite;
            boolean mainModel = detectedModel == mainEntityModel;
            boolean mainPartsVisibility = mainModel && mainMrde != null && mrdex.sameVisibility(mainMrde);
            if (mainModel && !mainPartsVisibility) {
               if (detectedTexture != null) {
                  if (!this.resetModelRotations(entity, detectedModel)) {
                     break;
                  }

                  mainRenderedModels.clear();
                  mainPart = this.renderModel(
                     matrixStack,
                     renderTypeBuffer,
                     entityRenderer,
                     detectedModel,
                     entity,
                     modelConfig,
                     defaultModelConfig,
                     mainPart,
                     mainRenderedModels,
                     detectedTexture,
                     detectedAtlasSprite,
                     forceFieldCheck,
                     fullModelIcon,
                     mrdex,
                     hardcodedMainPartAliases,
                     hardcodedModelPartsFields
                  );
                  mainMrde = mrdex;
                  renderedSomething = renderedSomething || !mainRenderedModels.isEmpty();
               }
            } else if (!mainModel) {
               if (!this.resetModelRotations(entity, detectedModel)) {
                  break;
               }

               ArrayList<class_630> renderedModels = new ArrayList<>();
               mainPart = this.renderModel(
                  matrixStack,
                  renderTypeBuffer,
                  entityRenderer,
                  detectedModel,
                  entity,
                  modelConfig,
                  defaultModelConfig,
                  mainPart,
                  renderedModels,
                  detectedTexture,
                  detectedAtlasSprite,
                  forceFieldCheck,
                  fullModelIcon,
                  mrdex,
                  hardcodedMainPartAliases,
                  hardcodedModelPartsFields
               );
               renderedSomething = renderedSomething || !renderedModels.isEmpty();
            } else if (!mainRenderedModels.isEmpty()) {
               class_4588 vertexBuilder = this.setupModelRenderType(renderTypeBuffer, detectedTexture, detectedAtlasSprite, mrdex);
               this.entityModelPartsRenderer
                  .renderPartsIterable(mainRenderedModels, matrixStack, vertexBuilder, new ArrayList<>(), mainPart, modelConfig.modelPartsRotationReset, mrdex);
               renderTypeBuffer.method_22993();
            }
         }
      }

      if (!mainRenderedModels.isEmpty() && modelConfig.layersAllowed) {
         EntityIconCustomRenderer extraLayer = EntityIconDefinitions.getCustomLayer(entityRenderer, entity);
         if (extraLayer != null) {
            mainPart = extraLayer.render(
               matrixStack,
               renderTypeBuffer,
               entityRenderer,
               entity,
               mainEntityModel,
               this.entityModelPartsRenderer,
               mainRenderedModels,
               mainPart,
               modelConfig,
               mainMrde
            );
         }
      }

      return renderedSomething;
   }

   private boolean resetModelRotations(class_1297 entity, class_583 model) {
      try {
         model.method_2816(entity, 0.0F, 0.0F, 1.0F);
         model.method_2819(entity, 0.0F, 0.0F, (float)entity.field_6012, 0.0F, 0.0F);
         OpenGLException.checkGLError();
         return true;
      } catch (Throwable var4) {
         MinimapLogs.LOGGER.error("suppressed exception", var4);
         return false;
      }
   }

   private class_583 getEntityRendererModel(class_897 entityRenderer) {
      if (entityRenderer instanceof class_922) {
         return ((class_922)entityRenderer).method_4038();
      } else {
         return entityRenderer instanceof class_895 ? Misc.getReflectFieldValue(entityRenderer, this.enderDragonModelField) : null;
      }
   }

   private Object resolveModelRoot(class_583<?> model, ArrayList<ArrayList<String>> rootPath, class_1297 entity) {
      ResolvedFieldModelRootPathListener resolvedFieldModelRootPathListener = this.resolvedFieldModelRootPathListener;
      Object currentChainNode = model;

      for (ArrayList<String> pathStep : rootPath) {
         resolvedFieldModelRootPathListener.prepare();
         EntityIconModelFieldResolver.searchSuperclassFields(currentChainNode, pathStep, resolvedFieldModelRootPathListener, ONE_OBJECT_ARRAY);
         currentChainNode = resolvedFieldModelRootPathListener.getCurrentNode();
         if (currentChainNode == null || resolvedFieldModelRootPathListener.failed()) {
            MinimapLogs.LOGGER
               .info(String.format("The following entity icon model root path step couldn't be resolved for %s:", class_1299.method_5890(entity.method_5864())));
            pathStep.forEach(MinimapLogs.LOGGER::info);
            return null;
         }
      }

      return currentChainNode;
   }
}
