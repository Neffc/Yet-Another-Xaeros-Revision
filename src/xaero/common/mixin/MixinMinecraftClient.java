package xaero.common.mixin;

import net.minecraft.class_310;
import net.minecraft.class_437;
import net.minecraft.class_638;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xaero.common.AXaeroMinimap;

@Mixin({class_310.class})
public class MixinMinecraftClient {
   @Shadow
   public class_437 field_1755;
   @Shadow
   public class_638 field_1687;

   @Inject(
      at = {@At("HEAD")},
      method = {"tick"}
   )
   public void onTickStart(CallbackInfo info) {
      AXaeroMinimap.INSTANCE.tryLoadLater();
      AXaeroMinimap.INSTANCE.getFMLEvents().handleClientTickStart();
   }

   @Inject(
      at = {@At("HEAD")},
      method = {"setScreen"},
      cancellable = true
   )
   public void onOpenScreen(class_437 screen_1, CallbackInfo info) {
      if (AXaeroMinimap.INSTANCE != null && AXaeroMinimap.INSTANCE.isLoaded()) {
         class_437 resultScreen = AXaeroMinimap.INSTANCE.getEvents().handleGuiOpen(screen_1);
         if (screen_1 != resultScreen) {
            ((class_310)this).method_1507(resultScreen);
            info.cancel();
         }
      }
   }

   @Inject(
      at = {@At("HEAD")},
      method = {"clearLevel(Lnet/minecraft/client/gui/screens/Screen;)V"}
   )
   public void onDisconnect(class_437 screen_1, CallbackInfo info) {
      if (this.field_1687 != null) {
         AXaeroMinimap.INSTANCE.getEvents().worldUnload(this.field_1687);
      }
   }

   @Inject(
      at = {@At("HEAD")},
      method = {"setLevel"}
   )
   public void onJoinWorld(class_638 newWorld, CallbackInfo info) {
      if (this.field_1687 != null) {
         AXaeroMinimap.INSTANCE.getEvents().worldUnload(this.field_1687);
      }
   }
}
