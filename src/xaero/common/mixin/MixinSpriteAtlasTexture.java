package xaero.common.mixin;

import net.minecraft.class_1059;
import net.minecraft.class_7766.class_7767;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xaero.common.AXaeroMinimap;

@Mixin({class_1059.class})
public class MixinSpriteAtlasTexture {
   @Inject(
      at = {@At("RETURN")},
      method = {"upload"}
   )
   public void onUpload(class_7767 spriteAtlasTexture$Data_1, CallbackInfo info) {
      AXaeroMinimap.INSTANCE.getModEvents().handleTextureStitchEventPost((class_1059)this);
   }
}
