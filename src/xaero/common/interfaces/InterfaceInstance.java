package xaero.common.interfaces;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.class_332;
import xaero.common.graphics.CustomVertexConsumers;

public class InterfaceInstance {
   protected Interface inter;

   public InterfaceInstance(Interface inter) {
      this.inter = inter;
   }

   public void prePotentialRender() {
   }

   public void render(class_332 guiGraphics, int width, int height, double scale, float partial, CustomVertexConsumers cvc) {
      RenderSystem.enableBlend();
      RenderSystem.blendFunc(770, 771);
   }

   public int getW(double scale) {
      return this.inter.getW(scale);
   }

   public int getH(double scale) {
      return this.inter.getH(scale);
   }

   public int getWC(double scale) {
      return this.inter.getWC(scale);
   }

   public int getHC(double scale) {
      return this.inter.getHC(scale);
   }

   public int getW0(double scale) {
      return this.inter.getW0(scale);
   }

   public int getH0(double scale) {
      return this.inter.getH0(scale);
   }

   public int getSize() {
      return this.inter.getSize();
   }

   public void cleanup() {
   }
}
