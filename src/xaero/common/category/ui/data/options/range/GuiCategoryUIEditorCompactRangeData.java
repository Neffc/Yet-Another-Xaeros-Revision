package xaero.common.category.ui.data.options.range;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.Supplier;
import xaero.common.category.ui.data.GuiCategoryUIEditorExpandableData;
import xaero.common.category.ui.data.options.GuiCategoryUIEditorCompactOptionsData;
import xaero.common.category.ui.data.options.GuiCategoryUIEditorOptionData;
import xaero.common.category.ui.data.options.GuiCategoryUIEditorOptionsData;
import xaero.common.category.ui.entry.CategorySettingsListMainEntryFactory;
import xaero.common.graphics.CursorBox;

public class GuiCategoryUIEditorCompactRangeData<V> extends GuiCategoryUIEditorCompactOptionsData<Integer> {
   private V currentRangeValue;
   private final int minNumber;
   private final IntFunction<V> numberReader;
   private final Function<V, String> valueNamer;
   private IntFunction<GuiCategoryUIEditorOptionData<Integer>> zeroIndexReader;
   private final boolean hasNullOption;

   protected GuiCategoryUIEditorCompactRangeData(
      String displayName,
      V currentRangeValue,
      int currentIndex,
      int optionCount,
      int minNumber,
      boolean hasNullOption,
      IntFunction<V> numberReader,
      Function<V, String> valueNamer,
      boolean movable,
      CategorySettingsListMainEntryFactory listEntryFactory,
      BiFunction<GuiCategoryUIEditorExpandableData<?>, GuiCategoryUIEditorExpandableData<GuiCategoryUIEditorOptionData<Integer>>, Supplier<CursorBox>> tooltipSupplier,
      GuiCategoryUIEditorOptionsData.IOptionsDataIsActiveSupplier isActiveSupplier
   ) {
      super(displayName, currentIndex, optionCount, movable, listEntryFactory, tooltipSupplier, isActiveSupplier);
      this.numberReader = numberReader;
      this.valueNamer = valueNamer;
      this.currentRangeValue = currentRangeValue;
      this.hasNullOption = hasNullOption;
      this.minNumber = minNumber;
      this.currentValue = this.getIndexReader().apply(currentIndex);
   }

   @Override
   public void setCurrentValue(GuiCategoryUIEditorOptionData<Integer> currentValue) {
      super.setCurrentValue((GuiCategoryUIEditorOptionData<V>)currentValue);
      Integer currentInteger = currentValue.getValue();
      this.currentRangeValue = currentInteger == null ? null : this.numberReader.apply(currentInteger);
   }

   public V getCurrentRangeValue() {
      return this.currentRangeValue;
   }

   @Override
   protected IntFunction<GuiCategoryUIEditorOptionData<Integer>> getIndexReader() {
      if (this.zeroIndexReader == null) {
         this.zeroIndexReader = i -> {
            if (this.hasNullOption) {
               i--;
            }

            Integer actualOptionNumber = i < 0 ? null : this.minNumber + i;
            V correspondingSettingValue = actualOptionNumber == null ? null : this.numberReader.apply(actualOptionNumber);
            return GuiCategoryUIEditorOptionData.Builder.<Integer>getDefault()
               .setDisplayName(this.valueNamer.apply(correspondingSettingValue))
               .setValue(actualOptionNumber)
               .build();
         };
      }

      return this.zeroIndexReader;
   }

   public abstract static class Builder<V, B extends GuiCategoryUIEditorCompactRangeData.Builder<V, B>>
      extends GuiCategoryUIEditorCompactOptionsData.Builder<Integer, B> {
      protected V currentRangeValue;
      protected int minNumber;
      protected int maxNumber;
      protected IntFunction<V> numberReader;
      protected Function<V, Integer> numberWriter;
      protected Function<V, String> valueNamer;
      protected boolean hasNullOption;

      public B setDefault() {
         this.setCurrentRangeValue(null);
         this.setMinNumber(0);
         this.setMaxNumber(0);
         this.setNumberReader(null);
         this.setNumberWriter(null);
         this.setValueNamer(null);
         this.setHasNullOption(false);
         return super.setDefault();
      }

      public B setCurrentRangeValue(V currentRangeValue) {
         this.currentRangeValue = currentRangeValue;
         return this.self;
      }

      public B setMinNumber(int minNumber) {
         this.minNumber = minNumber;
         return this.self;
      }

      public B setMaxNumber(int maxNumber) {
         this.maxNumber = maxNumber;
         return this.self;
      }

      public B setNumberReader(IntFunction<V> numberReader) {
         this.numberReader = numberReader;
         return this.self;
      }

      public B setNumberWriter(Function<V, Integer> numberWriter) {
         this.numberWriter = numberWriter;
         return this.self;
      }

      public B setValueNamer(Function<V, String> valueNamer) {
         this.valueNamer = valueNamer;
         return this.self;
      }

      public B setHasNullOption(boolean hasNullOption) {
         this.hasNullOption = hasNullOption;
         return this.self;
      }

      public GuiCategoryUIEditorCompactRangeData<V> build() {
         if (this.numberReader != null && this.valueNamer != null && this.numberWriter != null) {
            return (GuiCategoryUIEditorCompactRangeData<V>)super.build();
         } else {
            throw new IllegalStateException("required fields not set!");
         }
      }

      protected GuiCategoryUIEditorCompactRangeData<V> buildInternally(CategorySettingsListMainEntryFactory listEntryFactory) {
         int currentIndex = this.currentRangeValue == null ? -1 : this.numberWriter.apply(this.currentRangeValue) - this.minNumber;
         if (this.currentRangeValue != null && currentIndex < 0) {
            currentIndex = 0;
         }

         int optionCount = this.maxNumber - this.minNumber + 1;
         if (this.hasNullOption) {
            optionCount++;
            currentIndex++;
         }

         return this.buildInternally(currentIndex, optionCount, listEntryFactory);
      }

      protected abstract GuiCategoryUIEditorCompactRangeData<V> buildInternally(int var1, int var2, CategorySettingsListMainEntryFactory var3);
   }

   public static final class FinalBuilder<V> extends GuiCategoryUIEditorCompactRangeData.Builder<V, GuiCategoryUIEditorCompactRangeData.FinalBuilder<V>> {
      @Override
      protected GuiCategoryUIEditorCompactRangeData<V> buildInternally(int currentIndex, int optionCount, CategorySettingsListMainEntryFactory listEntryFactory) {
         return new GuiCategoryUIEditorCompactRangeData<>(
            this.displayName,
            this.currentRangeValue,
            currentIndex,
            optionCount,
            this.minNumber,
            this.hasNullOption,
            this.numberReader,
            this.valueNamer,
            this.movable,
            listEntryFactory,
            this.tooltipSupplier,
            this.isActiveSupplier
         );
      }

      public static <V> GuiCategoryUIEditorCompactRangeData.FinalBuilder<V> getDefault() {
         return new GuiCategoryUIEditorCompactRangeData.FinalBuilder().setDefault();
      }
   }
}
