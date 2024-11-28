package xaero.common.settings;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;
import net.minecraft.class_2561;
import net.minecraft.class_315;
import net.minecraft.class_339;
import net.minecraft.client.ProgressOption;
import xaero.common.gui.ModOptionSlider;

public class ModSliderPercentageOption extends ProgressOption {
   public final ModOptions modOption;

   public ModSliderPercentageOption(
      ModOptions modOption,
      String translationKey,
      double minValueIn,
      double maxValueIn,
      float stepSizeIn,
      Function<class_315, Double> getter,
      BiConsumer<class_315, Double> setter,
      BiFunction<class_315, ProgressOption, class_2561> getDisplayString
   ) {
      super(translationKey, minValueIn, maxValueIn, stepSizeIn, getter, setter, getDisplayString);
      this.modOption = modOption;
   }

   public class_339 createButton(class_315 options, int p_216586_2_, int p_216586_3_, int p_216586_4_) {
      return new ModOptionSlider(this.modOption, options, p_216586_2_, p_216586_3_, p_216586_4_, 20, this);
   }
}
