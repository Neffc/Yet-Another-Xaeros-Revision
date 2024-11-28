package xaero.common.mixin;

import com.mojang.authlib.GameProfile;
import java.time.Instant;
import net.minecraft.class_2561;
import net.minecraft.class_7471;
import net.minecraft.class_7594;
import net.minecraft.class_2556.class_7602;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xaero.common.HudMod;
import xaero.common.core.XaeroMinimapCore;

@Mixin({class_7594.class})
public class MixinChatListener {
   @Inject(
      method = {"showMessageToPlayer"},
      cancellable = true,
      at = {@At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/gui/components/ChatComponent;addMessage(Lnet/minecraft/network/chat/Component;Lnet/minecraft/network/chat/MessageSignature;Lnet/minecraft/client/GuiMessageTag;)V"
      )}
   )
   public void onShowMessageToPlayer(
      class_7602 bound,
      class_7471 playerChatMessage,
      class_2561 component,
      GameProfile gameProfile,
      boolean bl,
      Instant instant,
      CallbackInfoReturnable<Boolean> info
   ) {
      if (XaeroMinimapCore.isModLoaded()) {
         if (HudMod.INSTANCE.getEvents().handleClientPlayerChatReceivedEvent(bound, component, gameProfile)) {
            info.setReturnValue(false);
         }
      }
   }

   @Inject(
      method = {"handleDisguisedChatMessage"},
      cancellable = true,
      at = {@At("HEAD")}
   )
   public void onHandleDisguisedChatMessag(class_2561 component, class_7602 bound, CallbackInfo info) {
      if (!XaeroMinimapCore.onHandleDisguisedChatMessage(bound, component)) {
         info.cancel();
      }
   }

   @Inject(
      method = {"handleSystemMessage"},
      cancellable = true,
      at = {@At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/gui/components/ChatComponent;addMessage(Lnet/minecraft/network/chat/Component;)V"
      )}
   )
   public void onHandleSystemChat(class_2561 component, boolean bl, CallbackInfo info) {
      if (XaeroMinimapCore.onSystemChat(component)) {
         info.cancel();
      }
   }
}
