package xaero.common.mixin;

import net.minecraft.class_329;
import net.minecraft.class_332;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xaero.common.core.XaeroMinimapCore;

@Mixin({class_329.class})
public class MixinInGameHud {
   @Inject(
      at = {@At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/multiplayer/MultiPlayerGameMode;getPlayerMode()Lnet/minecraft/world/level/GameType;",
         ordinal = 0
      )},
      method = {"render"}
   )
   public void onRender(class_332 guiGraphics, float float_1, CallbackInfo info) {
      XaeroMinimapCore.handleRenderModOverlay(guiGraphics, float_1);
   }

   @Inject(
      at = {@At("RETURN")},
      method = {"render"}
   )
   public void onRenderEnd(class_332 guiGraphics, float float_1, CallbackInfo info) {
      XaeroMinimapCore.afterIngameGuiRender(guiGraphics, float_1);
   }

   @Inject(
      at = {@At("HEAD")},
      method = {"renderCrosshair"},
      cancellable = true
   )
   public void onRenderCrosshair(class_332 guiGraphics, CallbackInfo info) {
      if (XaeroMinimapCore.onRenderCrosshair(guiGraphics)) {
         info.cancel();
      }
   }

   @Inject(
      at = {@At("RETURN")},
      method = {"renderEffects"}
   )
   public void postRenderStatusEffectOverlay(class_332 guiGraphics, CallbackInfo info) {
      XaeroMinimapCore.onRenderStatusEffectOverlayPost(guiGraphics);
   }

   @Inject(
      at = {@At("HEAD")},
      method = {"render"}
   )
   public void onRenderStart(class_332 guiGraphics, float float_1, CallbackInfo info) {
      XaeroMinimapCore.beforeIngameGuiRender(guiGraphics, float_1);
   }

   @Inject(
      at = {@At("HEAD")},
      method = {"renderEffects"},
      cancellable = true
   )
   public void onRenderStatusEffectOverlay(class_332 guiGraphics, CallbackInfo info) {
      if (XaeroMinimapCore.onRenderStatusEffectOverlay(guiGraphics)) {
         info.cancel();
      }
   }
}
