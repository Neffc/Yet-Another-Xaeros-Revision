package xaero.common.mixin;

import net.minecraft.class_2561;
import net.minecraft.network.protocol.game.ClientboundChatPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({ClientboundChatPacket.class})
public interface MixinGameMessageS2CPacketAccessor {
   @Accessor
   void setMessage(class_2561 var1);
}
