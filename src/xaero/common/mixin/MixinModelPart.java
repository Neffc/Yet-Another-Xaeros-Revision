package xaero.common.mixin;

import net.minecraft.class_4588;
import net.minecraft.class_630;
import net.minecraft.class_4587.class_4665;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xaero.common.core.XaeroMinimapCore;

@Mixin({class_630.class})
public class MixinModelPart {
   @Inject(
      at = {@At("HEAD")},
      method = {"compile(Lcom/mojang/blaze3d/vertex/PoseStack$Pose;Lcom/mojang/blaze3d/vertex/VertexConsumer;IIFFFF)V"}
   )
   public void onRender(
      class_4665 matrices, class_4588 vertexConsumer, int light, int overlay, float red, float green, float blue, float alpha, CallbackInfo info
   ) {
      XaeroMinimapCore.onEntityIconsModelPartRenderDetection((class_630)this, red, green, blue, alpha);
   }
}
