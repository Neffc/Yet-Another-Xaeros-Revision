package xaero.hud.minimap.element.render.world;

import com.mojang.blaze3d.systems.RenderSystem;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.class_1937;
import net.minecraft.class_243;
import net.minecraft.class_276;
import net.minecraft.class_310;
import net.minecraft.class_332;
import net.minecraft.class_4587;
import net.minecraft.class_5321;
import net.minecraft.class_4597.class_4598;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import xaero.common.HudMod;
import xaero.common.graphics.CustomRenderTypes;
import xaero.common.graphics.renderer.multitexture.MultiTextureRenderTypeRendererProvider;
import xaero.hud.minimap.BuiltInHudModules;
import xaero.hud.minimap.element.render.MinimapElementReader;
import xaero.hud.minimap.element.render.MinimapElementRenderInfo;
import xaero.hud.minimap.element.render.MinimapElementRenderLocation;
import xaero.hud.minimap.element.render.MinimapElementRenderer;
import xaero.hud.minimap.element.render.MinimapElementRendererHandler;
import xaero.hud.minimap.module.MinimapSession;

public class MinimapElementWorldRendererHandler extends MinimapElementRendererHandler {
   private static final float DEFAULT_SCALE = 0.8F;
   private static final float MINECRAFT_SCALE = 0.02666667F;
   private static final double ELEMENT_WORLD_SCALE = 0.021333335F;
   private final class_4587 matrixStackWorld;
   private final Vector4f origin4f;
   private Matrix4f waypointsProjection;
   private Matrix4f worldModelView;
   private int screenWidth;
   private int screenHeight;
   private Object workingClosestHoveredElement;
   private float workingClosestHoveredElementDistance;
   private MinimapElementRenderer<?, ?> workingClosestHoveredElementRenderer;
   private float previousClosestHoveredElementDistance;
   private Object previousClosestHoveredElement;
   private MinimapElementRenderer<?, ?> previousClosestHoveredElementRenderer;
   private boolean previousClosestHoveredElementPresent;
   private boolean renderingMainHighlightedElement;

   protected MinimapElementWorldRendererHandler(HudMod modMain, List<MinimapElementRenderer<?, ?>> renderers, class_4587 matrixStackWorld, Vector4f origin4f) {
      super(modMain, renderers, MinimapElementRenderLocation.IN_WORLD, 19499);
      this.matrixStackWorld = matrixStackWorld;
      this.origin4f = origin4f;
   }

   public void prepareRender(Matrix4f waypointsProjection, Matrix4f worldModelView) {
      this.waypointsProjection = waypointsProjection;
      this.worldModelView = worldModelView;
   }

   @Override
   public void render(
      class_332 guiGraphics,
      class_243 renderPos,
      float partialTicks,
      class_276 framebuffer,
      double backgroundCoordinateScale,
      class_5321<class_1937> mapDimension
   ) {
      if (!HudMod.INSTANCE.getSupportMods().vivecraft) {
         this.renderingMainHighlightedElement = false;
         super.render(guiGraphics, renderPos, partialTicks, framebuffer, backgroundCoordinateScale, mapDimension);
      }
   }

   @Override
   protected <E, RRC, RR extends MinimapElementRenderer<E, RRC>> boolean transformAndRenderForRenderer(
      E element,
      double elementX,
      double elementY,
      double elementZ,
      RR renderer,
      RRC context,
      int elementIndex,
      double optionalDepth,
      MinimapElementRenderInfo renderInfo,
      class_332 guiGraphics,
      class_4598 vanillaBufferSource
   ) {
      class_4587 matrixStackOverlay = guiGraphics.method_51448();
      float partialTicks = renderInfo.partialTicks;
      class_243 renderPos = renderInfo.renderPos;
      MinimapElementReader<E, RRC> elementReader = renderer.getElementReader();
      double offX = elementX - renderPos.field_1352;
      double offY = elementReader.getRenderY(element, context, partialTicks) - renderPos.field_1351;
      double offZ = elementZ - renderPos.field_1350;
      Vector3f lookVector = class_310.method_1551().field_1773.method_19418().method_19335().get(new Vector3f());
      double depth = offX * (double)lookVector.x() + offY * (double)lookVector.y() + offZ * (double)lookVector.z();
      if (depth < 0.05) {
         return false;
      } else if (!this.renderingMainHighlightedElement && element == this.previousClosestHoveredElement) {
         this.previousClosestHoveredElementPresent = true;
         return false;
      } else {
         double distance = Math.sqrt(offX * offX + offY * offY + offZ * offZ);
         if (distance > 250000.0) {
            double offScaler = 250000.0 / distance;
            offX *= offScaler;
            offY *= offScaler;
            offZ *= offScaler;
         }

         matrixStackOverlay.method_22903();
         this.matrixStackWorld.method_22903();
         this.matrixStackWorld.method_22904(offX, offY, offZ);
         this.origin4f.mul(this.matrixStackWorld.method_23760().method_23761());
         this.matrixStackWorld.method_22909();
         this.origin4f.mul(this.waypointsProjection);
         float translateX = (1.0F + this.origin4f.x() / this.origin4f.w()) / 2.0F * (float)this.screenWidth;
         float translateY = (1.0F - this.origin4f.y() / this.origin4f.w()) / 2.0F * (float)this.screenHeight;
         this.origin4f.set(0.0F, 0.0F, 0.0F, 1.0F);
         int roundedX = Math.round(translateX);
         int roundedY = Math.round(translateY);
         boolean outOfBounds = roundedX < 0 || roundedY < 0 || roundedX >= this.screenWidth || roundedY >= this.screenHeight;
         boolean renderingHoveredElement = this.isElementHovered(element, roundedX, roundedY, elementReader, context, renderInfo);
         double partialX = (double)(translateX - (float)roundedX);
         double partialY = (double)(translateY - (float)roundedY);
         matrixStackOverlay.method_46416((float)roundedX, (float)roundedY, 0.0F);
         boolean highlighted = this.renderingMainHighlightedElement;
         highlighted = highlighted || renderingHoveredElement && elementReader.isAlwaysHighlightedWhenHovered(element, context);
         boolean result = renderer.renderElement(
            element, highlighted, outOfBounds, optionalDepth, 1.0F, partialX, partialY, renderInfo, guiGraphics, vanillaBufferSource
         );
         matrixStackOverlay.method_22909();
         if (result && renderingHoveredElement) {
            this.handleClosestHovered(element, renderer, roundedX, roundedY);
         }

         return result;
      }
   }

   private <E, RRC> boolean isElementHovered(
      E element, int roundedX, int roundedY, MinimapElementReader<E, RRC> elementReader, RRC context, MinimapElementRenderInfo renderInfo
   ) {
      if (!elementReader.isInteractable(this.location, element)) {
         return false;
      } else {
         float partialTicks = renderInfo.partialTicks;
         int interactionLeft = elementReader.getInteractionBoxLeft(element, context, partialTicks);
         int interactionRight = elementReader.getInteractionBoxRight(element, context, partialTicks);
         int interactionTop = elementReader.getInteractionBoxTop(element, context, partialTicks);
         int interactionBottom = elementReader.getInteractionBoxBottom(element, context, partialTicks);
         double boxScale = (double)elementReader.getBoxScale(this.location, element, context);
         if (boxScale != 1.0) {
            interactionLeft = (int)((double)interactionLeft * boxScale);
            interactionRight = (int)((double)interactionRight * boxScale);
            interactionTop = (int)((double)interactionTop * boxScale);
            interactionBottom = (int)((double)interactionBottom * boxScale);
         }

         int centerX = this.screenWidth / 2;
         if (centerX - roundedX >= interactionLeft && centerX - roundedX < interactionRight) {
            int centerY = this.screenHeight / 2;
            return centerY - roundedY >= interactionTop && centerY - roundedY < interactionBottom;
         } else {
            return false;
         }
      }
   }

   private <E, RRC, RR extends MinimapElementRenderer<E, RRC>> void handleClosestHovered(E element, RR renderer, int roundedX, int roundedY) {
      int centerX = this.screenWidth / 2;
      int centerY = this.screenHeight / 2;
      int screenOffX = roundedX - centerX;
      int screenOffY = roundedY - centerY;
      float squaredScreenDistance = (float)(screenOffX * screenOffX + screenOffY * screenOffY);
      if (element == this.previousClosestHoveredElement) {
         this.previousClosestHoveredElementDistance = squaredScreenDistance;
      } else {
         if (this.workingClosestHoveredElement == null || squaredScreenDistance < this.workingClosestHoveredElementDistance) {
            this.workingClosestHoveredElement = element;
            this.workingClosestHoveredElementDistance = squaredScreenDistance;
            this.workingClosestHoveredElementRenderer = renderer;
         }
      }
   }

   private <E, RR extends MinimapElementRenderer<E, RRC>, RRC> void renderMainHighlightedElement(
      MinimapElementRenderInfo renderInfo, class_332 guiGraphics, class_4598 vanillaBufferSource
   ) {
      if (this.previousClosestHoveredElementPresent) {
         class_4587 matrixStack = guiGraphics.method_51448();
         MinimapSession session = BuiltInHudModules.MINIMAP.getCurrentSession();
         MultiTextureRenderTypeRendererProvider multiTextureRenderTypeRenderers = session.getMultiTextureRenderTypeRenderers();
         E element = (E)this.previousClosestHoveredElement;
         RR renderer = (RR)this.previousClosestHoveredElementRenderer;
         this.renderingMainHighlightedElement = true;
         renderer.preRender(renderInfo, vanillaBufferSource, multiTextureRenderTypeRenderers);
         boolean result = this.transformAndRenderForRenderer(element, renderer, renderer.getContext(), 0, 0.0, renderInfo, guiGraphics, vanillaBufferSource);
         renderer.postRender(renderInfo, vanillaBufferSource, multiTextureRenderTypeRenderers);
         this.renderingMainHighlightedElement = false;
         this.previousClosestHoveredElementPresent = false;
         if (result) {
            matrixStack.method_22904(0.0, 0.0, this.getElementIndexDepth(1, 1));
            if (this.previousClosestHoveredElementDistance != -1.0F) {
               if (this.workingClosestHoveredElement == null || this.previousClosestHoveredElementDistance <= this.workingClosestHoveredElementDistance) {
                  this.workingClosestHoveredElement = this.previousClosestHoveredElement;
                  this.workingClosestHoveredElementRenderer = this.previousClosestHoveredElementRenderer;
                  this.workingClosestHoveredElementDistance = this.previousClosestHoveredElementDistance;
               }
            }
         }
      }
   }

   @Override
   protected void beforeRender(class_332 guiGraphics, MinimapElementRenderInfo renderInfo, class_4598 vanillaBufferSource) {
      this.previousClosestHoveredElementDistance = -1.0F;
      this.screenWidth = class_310.method_1551().method_22683().method_4489();
      this.screenHeight = class_310.method_1551().method_22683().method_4506();
      this.matrixStackWorld.method_22903();
      this.matrixStackWorld.method_23760().method_23761().mul(this.worldModelView);
      class_4587 matrixStackOverlay = guiGraphics.method_51448();
      matrixStackOverlay.method_22903();
      matrixStackOverlay.method_46416(0.0F, 0.0F, -2980.0F);
   }

   @Override
   protected void afterRender(class_332 guiGraphics, MinimapElementRenderInfo renderInfo, class_4598 vanillaBufferSource) {
      this.renderMainHighlightedElement(renderInfo, guiGraphics, vanillaBufferSource);
      this.previousClosestHoveredElement = this.workingClosestHoveredElement;
      this.previousClosestHoveredElementRenderer = this.workingClosestHoveredElementRenderer;
      this.workingClosestHoveredElement = null;
      this.workingClosestHoveredElementRenderer = null;
      class_4587 matrixStackOverlay = guiGraphics.method_51448();
      matrixStackOverlay.method_22909();
      this.matrixStackWorld.method_22909();
      CustomRenderTypes.DEPTH_CLEAR.method_23516();
      RenderSystem.clear(256, class_310.field_1703);
      CustomRenderTypes.DEPTH_CLEAR.method_23518();
   }

   public static final class Builder {
      public MinimapElementWorldRendererHandler build() {
         List<MinimapElementRenderer<?, ?>> renderers = new ArrayList<>();
         return new MinimapElementWorldRendererHandler(HudMod.INSTANCE, renderers, new class_4587(), new Vector4f(0.0F, 0.0F, 0.0F, 1.0F));
      }

      protected MinimapElementWorldRendererHandler.Builder setDefault() {
         return this;
      }

      public static MinimapElementWorldRendererHandler.Builder begin() {
         return new MinimapElementWorldRendererHandler.Builder().setDefault();
      }
   }
}
