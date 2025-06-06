package xaero.common.mixin;

import net.minecraft.class_4587;
import net.minecraft.class_4588;
import net.minecraft.class_583;
import net.minecraft.class_895.class_625;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xaero.common.core.XaeroMinimapCore;

@Mixin({class_625.class})
public class MixinDragonEntityModel {
   @Inject(
      at = {@At("HEAD")},
      method = {"renderToBuffer"}
   )
   public void onRender(class_4587 matrices, class_4588 vertices, int light, int overlay, float red, float green, float blue, float alpha, CallbackInfo info) {
      XaeroMinimapCore.onEntityIconsModelRenderDetection((class_583<?>)this, vertices, red, green, blue, alpha);
   }
}
