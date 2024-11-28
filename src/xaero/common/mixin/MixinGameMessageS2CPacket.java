package xaero.common.mixin;

import net.minecraft.class_2561;
import net.minecraft.network.protocol.game.ClientboundChatPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xaero.common.core.IXaeroMinimapGameMessageS2CPacket;

@Mixin({ClientboundChatPacket.class})
public class MixinGameMessageS2CPacket implements IXaeroMinimapGameMessageS2CPacket {
   private class_2561 xaero_replaceableMessage;

   @Override
   public void setMessage(class_2561 message) {
      this.xaero_replaceableMessage = message;
   }

   @Inject(
      at = {@At("HEAD")},
      method = {"getMessage"},
      cancellable = true
   )
   public void onGetMessage(CallbackInfoReturnable<class_2561> info) {
      if (this.xaero_replaceableMessage != null) {
         info.setReturnValue(this.xaero_replaceableMessage);
      }
   }
}
