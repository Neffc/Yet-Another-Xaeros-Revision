package xaero.hud.minimap.element.render;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import net.minecraft.class_1297;
import net.minecraft.class_1657;
import net.minecraft.class_243;
import net.minecraft.class_276;
import net.minecraft.class_310;
import net.minecraft.class_327;
import net.minecraft.class_332;
import net.minecraft.class_4587;
import net.minecraft.class_4597.class_4598;
import xaero.common.HudMod;
import xaero.common.graphics.renderer.multitexture.MultiTextureRenderTypeRendererProvider;

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

   public final void render(
      class_332 guiGraphics,
      class_1297 renderEntity,
      class_1657 player,
      class_243 renderPos,
      double playerDimDiv,
      double ps,
      double pc,
      double zoom,
      boolean cave,
      float partialTicks,
      class_276 framebuffer,
      MultiTextureRenderTypeRendererProvider multiTextureRenderTypeRenderers
   ) {
      MinimapElementRenderInfo renderInfo = new MinimapElementRenderInfo(this.location, renderEntity, player, renderPos, cave, partialTicks, framebuffer);
      class_4587 matrixStack = guiGraphics.method_51448();
      this.beforeRender(matrixStack);
      int indexLimit = this.getIndexLimit();

      for (int i = 0; i < this.renderers.size(); i++) {
         MinimapElementRenderer<?, ?> renderer = this.renderers.get(i);
         int elementIndex = 0;
         elementIndex = this.renderForRenderer(
            renderer, guiGraphics, playerDimDiv, ps, pc, zoom, elementIndex, multiTextureRenderTypeRenderers, indexLimit, renderInfo
         );
         matrixStack.method_22904(0.0, 0.0, this.getElementIndexDepth(elementIndex, indexLimit));
         indexLimit -= elementIndex;
         if (indexLimit < 0) {
            indexLimit = 0;
         }
      }

      this.afterRender(matrixStack);
   }

   protected <E, RRC, RR extends MinimapElementRenderer<E, RRC>> int renderForRenderer(
      RR renderer,
      class_332 guiGraphics,
      double playerDimDiv,
      double ps,
      double pc,
      double zoom,
      int elementIndex,
      MultiTextureRenderTypeRendererProvider multiTextureRenderTypeRenderers,
      int indexLimit,
      MinimapElementRenderInfo renderInfo
   ) {
      MinimapElementRenderLocation location = this.location;
      if (!renderer.shouldRender(location)) {
         return elementIndex;
      } else {
         class_4598 renderTypeBuffers = this.modMain.getHudRenderer().getCustomVertexConsumers().getBetterPVPRenderTypeBuffers();
         class_327 font = class_310.method_1551().field_1772;
         MinimapElementReader<E, RRC> elementReader = renderer.elementReader;
         MinimapElementRenderProvider<E, RRC> provider = renderer.provider;
         RRC context = (RRC)renderer.context;
         renderer.preRender(renderInfo, renderTypeBuffers, multiTextureRenderTypeRenderers);
         provider.begin(location, context);

         while (provider.hasNext(location, context)) {
            E element = provider.setupContextAndGetNext(location, context);
            if (element != null && !elementReader.isHidden(element, context)) {
               double optionalDepth = this.getElementIndexDepth(elementIndex, indexLimit);
               if (this.transformAndRenderForRenderer(
                  element, renderer, context, playerDimDiv, ps, pc, zoom, elementIndex, optionalDepth, renderInfo, guiGraphics, renderTypeBuffers
               )) {
                  elementIndex++;
               }
            }
         }

         provider.end(location, context);
         renderer.postRender(renderInfo, renderTypeBuffers, multiTextureRenderTypeRenderers);
         return elementIndex;
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
      RR var2,
      RRC var3,
      double var4,
      double var6,
      double var8,
      double var10,
      int var12,
      double var13,
      MinimapElementRenderInfo var15,
      class_332 var16,
      class_4598 var17
   );

   protected abstract void beforeRender(class_4587 var1);

   protected abstract void afterRender(class_4587 var1);

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
