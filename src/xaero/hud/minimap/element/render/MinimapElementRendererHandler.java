package xaero.hud.minimap.element.render;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import net.minecraft.class_1297;
import net.minecraft.class_1657;
import net.minecraft.class_1937;
import net.minecraft.class_243;
import net.minecraft.class_276;
import net.minecraft.class_310;
import net.minecraft.class_332;
import net.minecraft.class_4587;
import net.minecraft.class_5321;
import net.minecraft.class_4597.class_4598;
import xaero.common.HudMod;
import xaero.common.graphics.renderer.multitexture.MultiTextureRenderTypeRendererProvider;
import xaero.common.misc.OptimizedMath;
import xaero.hud.minimap.BuiltInHudModules;
import xaero.hud.minimap.module.MinimapSession;

public abstract class MinimapElementRendererHandler {
   private final HudMod modMain;
   private final List<MinimapElementRenderer<?, ?>> renderers;
   protected final MinimapElementRenderLocation location;
   private final int indexLimit;

   protected MinimapElementRendererHandler(HudMod modMain, List<MinimapElementRenderer<?, ?>> renderers, MinimapElementRenderLocation location, int indexLimit) {
      this.modMain = modMain;
      this.renderers = renderers;
      this.location = location;
      this.indexLimit = indexLimit;
   }

   public void add(MinimapElementRenderer<?, ?> renderer) {
      this.renderers.add(renderer);
      Collections.sort(this.renderers);
   }

   public void render(
      class_332 guiGraphics,
      class_243 renderPos,
      float partialTicks,
      class_276 framebuffer,
      double backgroundCoordinateScale,
      class_5321<class_1937> mapDimension
   ) {
      class_310 mc = class_310.method_1551();
      class_1297 renderEntity = mc.method_1560();
      class_1657 player = mc.field_1724;
      MinimapSession session = BuiltInHudModules.MINIMAP.getCurrentSession();
      MultiTextureRenderTypeRendererProvider multiTextureRenderTypeRenderers = session.getMultiTextureRenderTypeRenderers();
      class_4598 vanillaBufferSource = guiGraphics.method_51450();
      boolean cave = session.getProcessor().isCaveModeDisplayed();
      MinimapElementRenderInfo renderInfo = new MinimapElementRenderInfo(
         this.location, renderEntity, player, renderPos, cave, partialTicks, framebuffer, backgroundCoordinateScale, mapDimension
      );
      class_4587 matrixStack = guiGraphics.method_51448();
      this.beforeRender(guiGraphics, renderInfo, vanillaBufferSource);
      int indexLimit = this.getIndexLimit();

      for (int i = 0; i < this.renderers.size(); i++) {
         MinimapElementRenderer<?, ?> renderer = this.renderers.get(i);
         int elementIndex = 0;
         elementIndex = this.renderForRenderer(renderer, guiGraphics, elementIndex, multiTextureRenderTypeRenderers, indexLimit, renderInfo);
         matrixStack.method_22904(0.0, 0.0, this.getElementIndexDepth(elementIndex, indexLimit));
         indexLimit -= elementIndex;
         if (indexLimit < 0) {
            indexLimit = 0;
         }
      }

      this.afterRender(guiGraphics, renderInfo, vanillaBufferSource);
   }

   protected <E, RRC, RR extends MinimapElementRenderer<E, RRC>> int renderForRenderer(
      RR renderer,
      class_332 guiGraphics,
      int elementIndex,
      MultiTextureRenderTypeRendererProvider multiTextureRenderTypeRenderers,
      int indexLimit,
      MinimapElementRenderInfo renderInfo
   ) {
      MinimapElementRenderLocation location = this.location;
      if (!renderer.shouldRender(location)) {
         return elementIndex;
      } else {
         class_4598 vanillaBufferSource = guiGraphics.method_51450();
         MinimapElementReader<E, RRC> elementReader = renderer.elementReader;
         MinimapElementRenderProvider<E, RRC> provider = renderer.provider;
         RRC context = (RRC)renderer.context;
         renderer.preRender(renderInfo, vanillaBufferSource, multiTextureRenderTypeRenderers);
         provider.begin(location, context);

         while (provider.hasNext(location, context)) {
            E element = provider.setupContextAndGetNext(location, context);
            if (element != null && !elementReader.isHidden(element, context)) {
               double optionalDepth = this.getElementIndexDepth(elementIndex, indexLimit);
               if (this.transformAndRenderForRenderer(element, renderer, context, elementIndex, optionalDepth, renderInfo, guiGraphics, vanillaBufferSource)) {
                  elementIndex++;
               }
            }
         }

         provider.end(location, context);
         renderer.postRender(renderInfo, vanillaBufferSource, multiTextureRenderTypeRenderers);
         return elementIndex;
      }
   }

   protected <E, RRC, RR extends MinimapElementRenderer<E, RRC>> boolean transformAndRenderForRenderer(
      E element,
      RR renderer,
      RRC context,
      int elementIndex,
      double optionalDepth,
      MinimapElementRenderInfo renderInfo,
      class_332 guiGraphics,
      class_4598 vanillaBufferSource
   ) {
      MinimapElementReader<E, RRC> elementReader = renderer.elementReader;
      double elementX = elementReader.getRenderX(element, context, renderInfo.partialTicks);
      double elementY = elementReader.getRenderY(element, context, renderInfo.partialTicks);
      double elementZ = elementReader.getRenderZ(element, context, renderInfo.partialTicks);
      double elementCoordinateScale = elementReader.getCoordinateScale(element, context, renderInfo);
      double coordinateMultiplier = elementCoordinateScale / renderInfo.backgroundCoordinateScale;
      if (coordinateMultiplier == 1.0) {
         return this.transformAndRenderForRenderer(
            element, elementX, elementY, elementZ, renderer, context, elementIndex, optionalDepth, renderInfo, guiGraphics, vanillaBufferSource
         );
      } else {
         if (elementReader.shouldScalePartialCoordinates(element, context, renderInfo)) {
            elementX *= coordinateMultiplier;
            elementZ *= coordinateMultiplier;
         } else {
            int flooredRenderX = OptimizedMath.myFloor(elementX);
            int flooredRenderZ = OptimizedMath.myFloor(elementZ);
            elementX = (double)OptimizedMath.myFloor((double)flooredRenderX * coordinateMultiplier) + (elementX - (double)flooredRenderX);
            elementZ = (double)OptimizedMath.myFloor((double)flooredRenderZ * coordinateMultiplier) + (elementZ - (double)flooredRenderZ);
         }

         return this.transformAndRenderForRenderer(
            element, elementX, elementY, elementZ, renderer, context, elementIndex, optionalDepth, renderInfo, guiGraphics, vanillaBufferSource
         );
      }
   }

   protected double getElementIndexDepth(int elementIndex, int indexLimit) {
      return (double)(elementIndex >= indexLimit ? indexLimit : elementIndex) * 0.1;
   }

   protected int getIndexLimit() {
      return this.indexLimit;
   }

   protected abstract <E, RRC, RR extends MinimapElementRenderer<E, RRC>> boolean transformAndRenderForRenderer(
      E var1,
      double var2,
      double var4,
      double var6,
      RR var8,
      RRC var9,
      int var10,
      double var11,
      MinimapElementRenderInfo var13,
      class_332 var14,
      class_4598 var15
   );

   protected abstract void beforeRender(class_332 var1, MinimapElementRenderInfo var2, class_4598 var3);

   protected abstract void afterRender(class_332 var1, MinimapElementRenderInfo var2, class_4598 var3);

   public abstract static class Builder {
      protected Builder() {
      }

      protected MinimapElementRendererHandler.Builder setDefault() {
         return this;
      }

      public MinimapElementRendererHandler build() {
         return this.buildInternally(new ArrayList<>());
      }

      protected abstract MinimapElementRendererHandler buildInternally(List<MinimapElementRenderer<?, ?>> var1);
   }
}
