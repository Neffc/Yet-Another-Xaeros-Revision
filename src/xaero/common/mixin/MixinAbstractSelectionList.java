package xaero.common.mixin;

import net.minecraft.class_350;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import xaero.common.core.IAbstractSelectionList;

@Mixin({class_350.class})
public class MixinAbstractSelectionList implements IAbstractSelectionList {
   @Shadow
   private int field_22742;

   @Override
   public int getWidth() {
      return this.field_22742;
   }
}
