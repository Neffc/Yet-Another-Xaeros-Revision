package xaero.common.mixin;

import net.minecraft.class_1058;
import net.minecraft.class_777;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({class_777.class})
public interface MixinBakedQuad {
   @Accessor
   class_1058 getSprite();
}
