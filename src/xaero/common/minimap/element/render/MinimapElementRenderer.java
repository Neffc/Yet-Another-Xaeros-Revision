package xaero.common.minimap.element.render;

import net.minecraft.class_1297;
import net.minecraft.class_1657;
import net.minecraft.class_276;
import net.minecraft.class_310;
import net.minecraft.class_327;
import net.minecraft.class_332;
import net.minecraft.class_4597.class_4598;
import xaero.common.HudMod;
import xaero.common.IXaeroMinimap;
import xaero.common.graphics.renderer.multitexture.MultiTextureRenderTypeRendererProvider;
import xaero.common.minimap.render.MinimapRendererHelper;
import xaero.hud.minimap.element.render.MinimapElementRenderInfo;

@Deprecated
public abstract class MinimapElementRenderer<E, RC> extends xaero.hud.minimap.element.render.MinimapElementRenderer<E, RC> {
   @Deprecated
   protected final MinimapElementReader<E, RC> elementReader = this.getElementReader();
   @Deprecated
   protected final MinimapElementRenderProvider<E, RC> provider = this.getProvider();

   @Deprecated
   public MinimapElementRenderer(MinimapElementReader<E, RC> elementReader, MinimapElementRenderProvider<E, RC> provider, RC context) {
      super(elementReader, provider, context);
   }

   @Deprecated
   public MinimapElementRenderProvider<E, RC> getProvider() {
      return (MinimapElementRenderProvider<E, RC>)super.getProvider();
   }

   @Deprecated
   public MinimapElementReader<E, RC> getElementReader() {
      return (MinimapElementReader<E, RC>)super.getElementReader();
   }

   @Deprecated
   public abstract boolean renderElement(
      int var1,
      boolean var2,
      boolean var3,
      class_332 var4,
      class_4598 var5,
      class_327 var6,
      class_276 var7,
      MinimapRendererHelper var8,
      class_1297 var9,
      class_1657 var10,
      double var11,
      double var13,
      double var15,
      int var17,
      double var18,
      float var20,
      E var21,
      double var22,
      double var24,
      boolean var26,
      float var27
   );

   @Deprecated
   public abstract void preRender(
      int var1,
      class_1297 var2,
      class_1657 var3,
      double var4,
      double var6,
      double var8,
      IXaeroMinimap var10,
      class_4598 var11,
      MultiTextureRenderTypeRendererProvider var12
   );

   @Deprecated
   public abstract void postRender(
      int var1,
      class_1297 var2,
      class_1657 var3,
      double var4,
      double var6,
      double var8,
      IXaeroMinimap var10,
      class_4598 var11,
      MultiTextureRenderTypeRendererProvider var12
   );

   @Deprecated
   public abstract boolean shouldRender(int var1);

   @Override
   public boolean renderElement(
      E element,
      boolean highlit,
      boolean outOfBounds,
      double optionalDepth,
      float optionalScale,
      double partialX,
      double partialY,
      MinimapElementRenderInfo renderInfo,
      class_332 guiGraphics,
      class_4598 renderTypeBuffers
   ) {
      return this.renderElement(
         renderInfo.location.getIndex(),
         highlit,
         outOfBounds,
         guiGraphics,
         renderTypeBuffers,
         class_310.method_1551().field_1772,
         renderInfo.framebuffer,
         HudMod.INSTANCE.getMinimap().getMinimapFBORenderer().getHelper(),
         renderInfo.renderEntity,
         renderInfo.player,
         renderInfo.renderPos.field_1352,
         renderInfo.renderPos.field_1351,
         renderInfo.renderPos.field_1350,
         0,
         optionalDepth,
         optionalScale,
         element,
         partialX,
         partialY,
         renderInfo.cave,
         renderInfo.partialTicks
      );
   }

   @Override
   public void preRender(
      MinimapElementRenderInfo renderInfo, class_4598 renderTypeBuffers, MultiTextureRenderTypeRendererProvider multiTextureRenderTypeRenderers
   ) {
      this.preRender(
         renderInfo.location.getIndex(),
         renderInfo.renderEntity,
         renderInfo.player,
         renderInfo.renderPos.field_1352,
         renderInfo.renderPos.field_1351,
         renderInfo.renderPos.field_1350,
         HudMod.INSTANCE,
         renderTypeBuffers,
         multiTextureRenderTypeRenderers
      );
   }

   @Override
   public void postRender(
      MinimapElementRenderInfo renderInfo, class_4598 renderTypeBuffers, MultiTextureRenderTypeRendererProvider multiTextureRenderTypeRenderers
   ) {
      this.postRender(
         renderInfo.location.getIndex(),
         renderInfo.renderEntity,
         renderInfo.player,
         renderInfo.renderPos.field_1352,
         renderInfo.renderPos.field_1351,
         renderInfo.renderPos.field_1350,
         HudMod.INSTANCE,
         renderTypeBuffers,
         multiTextureRenderTypeRenderers
      );
   }

   @Override
   public boolean shouldRender(xaero.hud.minimap.element.render.MinimapElementRenderLocation location) {
      return this.shouldRender(location.getIndex());
   }
}
