package xaero.common.mixin;

import net.minecraft.class_437;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xaero.common.AXaeroMinimap;

@Mixin({class_437.class})
public class MixinScreen {
   @Inject(
      at = {@At("HEAD")},
      method = {"sendMessage(Ljava/lang/String;Z)V"},
      cancellable = true
   )
   public void onSendMessage(String string_1, boolean boolean_1, CallbackInfo info) {
      String result = AXaeroMinimap.INSTANCE.getEvents().handleClientSendChatEvent(string_1);
      if (result == null || result.isEmpty()) {
         info.cancel();
      }
   }
}
