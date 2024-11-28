package xaero.common.mixin;

import net.minecraft.class_350;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({class_350.class})
public interface MixinEntryListWidgetAccessor {
   @Accessor
   int getWidth();
}
