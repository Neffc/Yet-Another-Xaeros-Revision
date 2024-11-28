package xaero.common.settings;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import net.minecraft.class_2561;
import net.minecraft.class_315;
import net.minecraft.class_339;
import net.minecraft.client.CycleOption;
import xaero.common.gui.ModOptionButton;

public class ModIteratableOption extends CycleOption {
   public final ModOptions modOption;

   public ModIteratableOption(
      ModOptions modOption, String p_i51164_1_, BiConsumer<class_315, Integer> getter, BiFunction<class_315, CycleOption, class_2561> p_i51164_3_
   ) {
      super(p_i51164_1_, getter, p_i51164_3_);
      this.modOption = modOption;
   }

   public class_339 createButton(class_315 options, int p_216586_2_, int p_216586_3_, int p_216586_4_) {
      return new ModOptionButton(this.modOption, p_216586_2_, p_216586_3_, p_216586_4_, 20, this, this.getMessage(options), p_216721_2_ -> {
         this.toggle(options, 1);
         p_216721_2_.method_25355(this.getMessage(options));
      });
   }
}
