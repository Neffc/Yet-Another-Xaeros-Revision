package xaero.common.category.ui.entry.widget;

import java.util.function.IntConsumer;
import java.util.function.Supplier;
import net.minecraft.class_2561;
import net.minecraft.class_3532;
import net.minecraft.class_4892;
import net.minecraft.class_5250;
import xaero.common.category.ui.GuiCategorySettings;
import xaero.common.gui.IXaeroNarratableWidget;

public class CategorySettingsSlider extends class_4892 implements IXaeroNarratableWidget {
   protected int currentOptionIndex;
   protected int prevNarrationIndex;
   protected int optionCount;
   protected IntConsumer updatedIndexConsumer;
   protected Supplier<String> messageSupplier;
   protected final GuiCategorySettings<?, ?, ?, ?, ?, ?>.SettingRowList rowList;

   public <T> CategorySettingsSlider(
      IntConsumer updatedIndexConsumer,
      Supplier<String> messageSupplier,
      int currentIndex,
      int optionCount,
      int widthIn,
      int heightIn,
      GuiCategorySettings<?, ?, ?, ?, ?, ?>.SettingRowList rowList
   ) {
      super(null, 2, 2, widthIn, heightIn, 0.0);
      this.updatedIndexConsumer = updatedIndexConsumer;
      this.messageSupplier = messageSupplier;
      this.optionCount = optionCount;
      this.currentOptionIndex = this.prevNarrationIndex = currentIndex;
      this.field_22753 = this.toSliderValue(currentIndex);
      this.rowList = rowList;
      this.method_25346();
   }

   public boolean method_25404(int i, int j, int k) {
      if (i == 263) {
         this.manualOptionChange(this.currentOptionIndex - 1);
         return false;
      } else if (i == 262) {
         this.manualOptionChange(this.currentOptionIndex + 1);
         return false;
      } else {
         return super.method_25404(i, j, k);
      }
   }

   private void manualOptionChange(int index) {
      if (index < 0) {
         index = 0;
      } else if (index >= this.optionCount) {
         index = this.optionCount - 1;
      }

      this.field_22753 = this.toSliderValue(index);
      this.method_25344();
      this.method_25346();
   }

   @Override
   public class_5250 method_25360() {
      return class_2561.method_43470("");
   }

   protected void method_25344() {
      this.currentOptionIndex = this.toValue(this.field_22753);
      this.updatedIndexConsumer.accept(this.currentOptionIndex);
   }

   protected void method_25346() {
      this.method_25355(class_2561.method_43470(this.messageSupplier.get()));
      if (this.currentOptionIndex != this.prevNarrationIndex) {
         this.rowList.narrateSelection();
      }

      this.prevNarrationIndex = this.currentOptionIndex;
   }

   public double toSliderValue(int i) {
      return (double)i / (double)(this.optionCount - 1);
   }

   public int toValue(double d) {
      return (int)this.clamp(class_3532.method_16436(class_3532.method_15350(d, 0.0, 1.0), 0.0, (double)(this.optionCount - 1)));
   }

   private double clamp(double d) {
      d = (double)Math.round(d);
      return class_3532.method_15350(d, 0.0, (double)(this.optionCount - 1));
   }
}
