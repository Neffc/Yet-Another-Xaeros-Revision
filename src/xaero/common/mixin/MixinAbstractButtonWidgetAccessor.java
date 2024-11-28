package xaero.common.mixin;

import net.minecraft.class_339;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({class_339.class})
public interface MixinAbstractButtonWidgetAccessor {
   @Accessor
   int getHeight();
}
