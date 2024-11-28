package xaero.common.mixin;

import net.minecraft.class_2637;
import net.minecraft.class_4076;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({class_2637.class})
public interface MixinChunkDeltaUpdateS2CPacketAccessor {
   @Accessor
   class_4076 getSectionPos();
}
