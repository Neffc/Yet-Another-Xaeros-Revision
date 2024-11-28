package xaero.common.mixin;

import net.minecraft.class_329;
import net.minecraft.class_332;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xaero.common.HudMod;
import xaero.common.core.XaeroMinimapCore;
import xaero.common.events.ClientEventsFabric;

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
      if (XaeroMinimapCore.isModLoaded()) {
         HudMod.INSTANCE.getEvents().handleRenderGameOverlayEventPre(guiGraphics, float_1);
         HudMod.INSTANCE.getModEvents().handleRenderModOverlay(guiGraphics, float_1);
      }
   }

   @Inject(
      at = {@At("RETURN")},
      method = {"render"}
   )
   public void onRenderEnd(class_332 guiGraphics, float float_1, CallbackInfo info) {
      if (XaeroMinimapCore.isModLoaded()) {
         HudMod.INSTANCE.getEvents().handleRenderGameOverlayEventPost();
      }
   }

   @Inject(
      at = {@At("HEAD")},
      method = {"renderCrosshair"},
      cancellable = true
   )
   public void onRenderCrosshair(class_332 guiGraphics, CallbackInfo info) {
      if (!ClientEventsFabric.renderCrosshairs) {
         info.cancel();
      }
   }

   @Inject(
      at = {@At("RETURN")},
      method = {"renderEffects"}
   )
   public void postRenderStatusEffectOverlay(class_332 guiGraphics, CallbackInfo info) {
      XaeroMinimapCore.onPotionEffectsRender();
   }

   @Inject(
      at = {@At("HEAD")},
      method = {"render"}
   )
   public void onRenderStart(class_332 guiGraphics, float float_1, CallbackInfo info) {
      XaeroMinimapCore.beforeIngameGuiRender(float_1);
   }

   @Inject(
      at = {@At("HEAD")},
      method = {"renderEffects"},
      cancellable = true
   )
   public void onRenderStatusEffectOverlay(class_332 guiGraphics, CallbackInfo info) {
      if (XaeroMinimapCore.isModLoaded()) {
         if (HudMod.INSTANCE.getEvents().handleRenderStatusEffectOverlay(guiGraphics)) {
            info.cancel();
         }
      }
   }
}
