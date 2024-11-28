package xaero.common.mixin;

import net.minecraft.class_1259;
import net.minecraft.class_332;
import net.minecraft.class_337;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xaero.common.core.XaeroMinimapCore;

@Mixin({class_337.class})
public class MixinBossBarHud {
   @Inject(
      at = {@At("HEAD")},
      method = {"drawBar"}
   )
   public void onRenderBossBar(class_332 guiGraphics, int x, int y, class_1259 bossBar, CallbackInfo info) {
      XaeroMinimapCore.onBossHealthRender(y + 19);
   }
}
