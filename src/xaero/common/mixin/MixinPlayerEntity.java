package xaero.common.mixin;

import net.minecraft.class_1657;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xaero.common.AXaeroMinimap;

@Mixin({class_1657.class})
public class MixinPlayerEntity {
   @Inject(
      at = {@At("HEAD")},
      method = {"tick"}
   )
   public void onTickStart(CallbackInfo info) {
      AXaeroMinimap.INSTANCE.getFMLCommonEvents().handlePlayerTickStart((class_1657)this);
   }
}
