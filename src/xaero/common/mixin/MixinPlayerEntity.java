package xaero.common.mixin;

import net.minecraft.class_1657;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xaero.common.HudMod;
import xaero.common.core.XaeroMinimapCore;

@Mixin({class_1657.class})
public class MixinPlayerEntity {
   @Inject(
      at = {@At("HEAD")},
      method = {"tick"}
   )
   public void onTickStart(CallbackInfo info) {
      if (XaeroMinimapCore.isModLoaded()) {
         HudMod.INSTANCE.getCommonEvents().handlePlayerTickStart((class_1657)this);
      }
   }
}
