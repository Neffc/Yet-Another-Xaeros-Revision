package xaero.common.minimap.element.render;

import net.minecraft.class_1297;
import net.minecraft.class_1657;
import net.minecraft.class_276;
import net.minecraft.class_327;
import net.minecraft.class_332;
import net.minecraft.class_4597.class_4598;
import xaero.common.AXaeroMinimap;
import xaero.common.graphics.renderer.multitexture.MultiTextureRenderTypeRendererProvider;
import xaero.common.minimap.render.MinimapRendererHelper;

public abstract class MinimapElementRenderer<E, RC> implements Comparable<MinimapElementRenderer<?, ?>> {
   protected final MinimapElementReader<E, RC> elementReader;
   protected final RC context;
   protected final MinimapElementRenderProvider<E, RC> provider;

   public MinimapElementRenderer(MinimapElementReader<E, RC> elementReader, MinimapElementRenderProvider<E, RC> provider, RC context) {
      this.elementReader = elementReader;
      this.context = context;
      this.provider = provider;
   }

   public int getOrder() {
      return 0;
   }

   public int compareTo(MinimapElementRenderer<?, ?> o) {
      return Integer.compare(this.getOrder(), o.getOrder());
   }

   public RC getContext() {
      return this.context;
   }

   public MinimapElementRenderProvider<E, RC> getProvider() {
      return this.provider;
   }

   public MinimapElementReader<E, RC> getElementReader() {
      return this.elementReader;
   }

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

   public abstract void preRender(
      int var1,
      class_1297 var2,
      class_1657 var3,
      double var4,
      double var6,
      double var8,
      AXaeroMinimap var10,
      class_4598 var11,
      MultiTextureRenderTypeRendererProvider var12
   );

   public abstract void postRender(
      int var1,
      class_1297 var2,
      class_1657 var3,
      double var4,
      double var6,
      double var8,
      AXaeroMinimap var10,
      class_4598 var11,
      MultiTextureRenderTypeRendererProvider var12
   );

   public abstract boolean shouldRender(int var1);
}
