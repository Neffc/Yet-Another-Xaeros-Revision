package xaero.common.category.ui.entry.widget;

import java.util.function.IntConsumer;
import java.util.function.Supplier;
import net.minecraft.class_437;
import xaero.common.category.ui.GuiCategorySettings;
import xaero.common.category.ui.data.GuiCategoryUIEditorExpandableData;

public class CategorySettingsButtonIteration extends CategorySettingsButton {
   protected int currentOptionIndex;
   protected int optionCount;
   protected IntConsumer updatedIndexConsumer;

   public CategorySettingsButtonIteration(
      GuiCategoryUIEditorExpandableData<?> parent,
      IntConsumer updatedIndexConsumer,
      Supplier<String> messageSupplier,
      boolean active,
      int currentOptionIndex,
      int optionCount,
      int w,
      int h,
      GuiCategorySettings<?, ?, ?, ?, ?, ?>.SettingRowList rowList
   ) {
      super(parent, messageSupplier, active, w, h, b -> ((CategorySettingsButtonIteration)b).toggle(), rowList);
      this.currentOptionIndex = currentOptionIndex;
      this.optionCount = optionCount;
      this.updatedIndexConsumer = updatedIndexConsumer;
      this.updateMessage();
   }

   public final void toggle() {
      this.iterate(class_437.method_25442() ? -1 : 1);
   }

   private final void iterate(int direction) {
      this.currentOptionIndex += direction;
      if (this.currentOptionIndex >= this.optionCount) {
         this.currentOptionIndex = this.currentOptionIndex % this.optionCount;
      } else if (this.currentOptionIndex < 0) {
         this.currentOptionIndex = this.optionCount + this.currentOptionIndex % this.optionCount;
      }

      this.updatedIndexConsumer.accept(this.currentOptionIndex);
      this.updateMessage();
   }

   public boolean method_25401(double d, double e, double f) {
      return super.method_25401(d, e, f);
   }
}
