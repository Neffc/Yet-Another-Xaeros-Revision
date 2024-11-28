package xaero.common.minimap.element.render;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import net.minecraft.class_1297;
import net.minecraft.class_1657;
import net.minecraft.class_276;
import net.minecraft.class_327;
import net.minecraft.class_332;
import net.minecraft.class_4587;
import net.minecraft.class_4597.class_4598;
import xaero.common.IXaeroMinimap;
import xaero.common.graphics.renderer.multitexture.MultiTextureRenderTypeRendererProvider;
import xaero.common.minimap.render.MinimapRendererHelper;

public abstract class MinimapElementRendererHandler {
   private final List<MinimapElementRenderer<?, ?>> renderers;
   protected final int location;

   protected MinimapElementRendererHandler(List<MinimapElementRenderer<?, ?>> renderers, int location) {
      this.renderers = renderers;
      this.location = location;
   }

   public void add(MinimapElementRenderer<?, ?> renderer) {
      this.renderers.add(renderer);
   }

   protected void render(
      class_332 guiGraphics,
      class_1297 renderEntity,
      class_1657 player,
      double renderX,
      double renderY,
      double renderZ,
      double playerDimDiv,
      double ps,
      double pc,
      double zoom,
      boolean cave,
      float partialTicks,
      class_276 framebuffer,
      IXaeroMinimap modMain,
      MinimapRendererHelper helper,
      class_4598 renderTypeBuffers,
      class_327 font,
      MultiTextureRenderTypeRendererProvider multiTextureRenderTypeRenderers
   ) {
      class_4587 matrixStack = guiGraphics.method_51448();
      Collections.sort(this.renderers);
      this.beforeRender(matrixStack);
      int indexLimit = this.getIndexLimit();

      for (int i = 0; i < this.renderers.size(); i++) {
         MinimapElementRenderer<?, ?> renderer = this.renderers.get(i);
         int elementIndex = 0;
         elementIndex = this.renderForRenderer(
            renderer,
            guiGraphics,
            renderEntity,
            player,
            renderX,
            renderY,
            renderZ,
            playerDimDiv,
            ps,
            pc,
            zoom,
            cave,
            partialTicks,
            elementIndex,
            framebuffer,
            modMain,
            helper,
            renderTypeBuffers,
            font,
            multiTextureRenderTypeRenderers,
            indexLimit
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
      class_1297 renderEntity,
      class_1657 player,
      double renderX,
      double renderY,
      double renderZ,
      double playerDimDiv,
      double ps,
      double pc,
      double zoom,
      boolean cave,
      float partialTicks,
      int elementIndex,
      class_276 framebuffer,
      IXaeroMinimap modMain,
      MinimapRendererHelper helper,
      class_4598 renderTypeBuffers,
      class_327 font,
      MultiTextureRenderTypeRendererProvider multiTextureRenderTypeRenderers,
      int indexLimit
   ) {
      MinimapElementReader<E, RRC> elementReader = renderer.elementReader;
      MinimapElementRenderProvider<E, RRC> provider = renderer.provider;
      RRC context = (RRC)renderer.context;
      int location = this.location;
      if (!renderer.shouldRender(location)) {
         return elementIndex;
      } else {
         renderer.preRender(location, renderEntity, player, renderX, renderY, renderZ, modMain, renderTypeBuffers, multiTextureRenderTypeRenderers);
         provider.begin(location, context);

         while (provider.hasNext(location, context)) {
            E element = provider.setupContextAndGetNext(location, context);
            if (element != null && !elementReader.isHidden(element, context)) {
               double optionalDepth = this.getElementIndexDepth(elementIndex, indexLimit);
               if (this.transformAndRenderForRenderer(
                  renderer,
                  guiGraphics,
                  renderTypeBuffers,
                  font,
                  framebuffer,
                  helper,
                  renderEntity,
                  player,
                  renderX,
                  renderY,
                  renderZ,
                  playerDimDiv,
                  ps,
                  pc,
                  zoom,
                  cave,
                  partialTicks,
                  elementIndex,
                  optionalDepth,
                  element,
                  elementReader,
                  context
               )) {
                  elementIndex++;
               }
            }
         }

         provider.end(location, context);
         renderer.postRender(location, renderEntity, player, renderX, renderY, renderZ, modMain, renderTypeBuffers, multiTextureRenderTypeRenderers);
         return elementIndex;
      }
   }

   protected double getElementIndexDepth(int elementIndex, int indexLimit) {
      return (double)(elementIndex >= indexLimit ? indexLimit : elementIndex) * 0.1;
   }

   protected abstract int getIndexLimit();

   protected abstract <E, RRC, RR extends MinimapElementRenderer<E, RRC>> boolean transformAndRenderForRenderer(
      RR var1,
      class_332 var2,
      class_4598 var3,
      class_327 var4,
      class_276 var5,
      MinimapRendererHelper var6,
      class_1297 var7,
      class_1657 var8,
      double var9,
      double var11,
      double var13,
      double var15,
      double var17,
      double var19,
      double var21,
      boolean var23,
      float var24,
      int var25,
      double var26,
      E var28,
      MinimapElementReader<E, RRC> var29,
      RRC var30
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
