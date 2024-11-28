package xaero.common.category.ui.data.options;

import java.util.function.BiFunction;
import java.util.function.IntFunction;
import java.util.function.Supplier;
import net.minecraft.class_1074;
import xaero.common.category.ui.data.GuiCategoryUIEditorExpandableData;
import xaero.common.category.ui.entry.CategorySettingsListMainEntryFactory;
import xaero.common.graphics.CursorBox;

public class GuiCategoryUIEditorCompactBooleanOptionsData extends GuiCategoryUIEditorCompactOptionsData<Boolean> {
   private final GuiCategoryUIEditorOptionData<Boolean> trueOption;
   private final GuiCategoryUIEditorOptionData<Boolean> falseOption;
   private IntFunction<GuiCategoryUIEditorOptionData<Boolean>> indexReader;

   protected GuiCategoryUIEditorCompactBooleanOptionsData(
      String displayName,
      int currentIndex,
      int optionCount,
      boolean movable,
      CategorySettingsListMainEntryFactory listEntryFactory,
      BiFunction<GuiCategoryUIEditorExpandableData<?>, GuiCategoryUIEditorExpandableData<GuiCategoryUIEditorOptionData<Boolean>>, Supplier<CursorBox>> tooltipSupplier,
      GuiCategoryUIEditorOptionsData.IOptionsDataIsActiveSupplier isActiveSupplier,
      GuiCategoryUIEditorOptionData<Boolean> trueOption,
      GuiCategoryUIEditorOptionData<Boolean> falseOption
   ) {
      super(displayName, currentIndex, optionCount, movable, listEntryFactory, tooltipSupplier, isActiveSupplier);
      this.trueOption = trueOption;
      this.falseOption = falseOption;
      this.currentValue = this.getIndexReader().apply(currentIndex);
   }

   @Override
   protected IntFunction<GuiCategoryUIEditorOptionData<Boolean>> getIndexReader() {
      if (this.indexReader == null) {
         this.indexReader = i -> i != 0 ? this.trueOption : this.falseOption;
      }

      return this.indexReader;
   }

   public static final class Builder extends GuiCategoryUIEditorCompactOptionsData.Builder<Boolean, GuiCategoryUIEditorCompactBooleanOptionsData.Builder> {
      protected final GuiCategoryUIEditorOptionData.Builder<Boolean> trueOptionBuilder = GuiCategoryUIEditorOptionData.Builder.getDefault();
      protected final GuiCategoryUIEditorOptionData.Builder<Boolean> falseOptionBuilder = GuiCategoryUIEditorOptionData.Builder.getDefault();

      protected Builder() {
      }

      public GuiCategoryUIEditorCompactBooleanOptionsData.Builder setDefault() {
         super.setDefault();
         this.trueOptionBuilder.setDefault().setDisplayName(class_1074.method_4662("gui.xaero_on", new Object[0])).setValue(true);
         this.falseOptionBuilder.setDefault().setDisplayName(class_1074.method_4662("gui.xaero_off", new Object[0])).setValue(false);
         this.setCurrentValue(Boolean.valueOf(false));
         return this.self;
      }

      public GuiCategoryUIEditorOptionData.Builder<Boolean> getTrueOptionBuilder() {
         return this.trueOptionBuilder;
      }

      public GuiCategoryUIEditorOptionData.Builder<Boolean> getFalseOptionBuilder() {
         return this.falseOptionBuilder;
      }

      public GuiCategoryUIEditorCompactBooleanOptionsData build() {
         if (this.currentValue == null) {
            throw new IllegalStateException();
         } else if (this.movable) {
            throw new IllegalStateException("toggles can't be movable!");
         } else {
            return (GuiCategoryUIEditorCompactBooleanOptionsData)super.build();
         }
      }

      protected GuiCategoryUIEditorCompactBooleanOptionsData buildInternally(CategorySettingsListMainEntryFactory listEntryFactory) {
         GuiCategoryUIEditorOptionData<Boolean> trueOption = this.trueOptionBuilder.build();
         GuiCategoryUIEditorOptionData<Boolean> falseOption = this.falseOptionBuilder.build();
         return new GuiCategoryUIEditorCompactBooleanOptionsData(
            this.displayName,
            this.currentValue ? 1 : 0,
            2,
            this.movable,
            listEntryFactory,
            this.tooltipSupplier,
            this.isActiveSupplier,
            trueOption,
            falseOption
         );
      }

      public static GuiCategoryUIEditorCompactBooleanOptionsData.Builder getDefault() {
         return new GuiCategoryUIEditorCompactBooleanOptionsData.Builder().setDefault();
      }
   }
}
