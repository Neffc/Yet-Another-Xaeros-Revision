package xaero.common.category.ui.data.options.range;

import com.google.common.base.Objects;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.Supplier;
import javax.annotation.Nonnull;
import xaero.common.category.ui.data.GuiCategoryUIEditorExpandableData;
import xaero.common.category.ui.data.options.GuiCategoryUIEditorExpandingOptionsData;
import xaero.common.category.ui.data.options.GuiCategoryUIEditorOptionData;
import xaero.common.category.ui.data.options.GuiCategoryUIEditorOptionsData;
import xaero.common.category.ui.entry.CategorySettingsListMainEntryFactory;
import xaero.common.graphics.CursorBox;
import xaero.common.misc.ListFactory;

public class GuiCategoryUIEditorExpandingRangeData<V> extends GuiCategoryUIEditorExpandingOptionsData<Integer> {
   private V currentRangeValue;
   private final IntFunction<V> numberReader;

   protected GuiCategoryUIEditorExpandingRangeData(
      @Nonnull String displayName,
      V currentRangeValue,
      @Nonnull IntFunction<V> numberReader,
      @Nonnull GuiCategoryUIEditorOptionData<Integer> currentValue,
      @Nonnull List<GuiCategoryUIEditorOptionData<Integer>> options,
      boolean movable,
      @Nonnull CategorySettingsListMainEntryFactory listEntryFactory,
      BiFunction<GuiCategoryUIEditorExpandableData<?>, GuiCategoryUIEditorExpandableData<GuiCategoryUIEditorOptionData<Integer>>, Supplier<CursorBox>> tooltipSupplier,
      GuiCategoryUIEditorOptionsData.IOptionsDataIsActiveSupplier isActiveSupplier
   ) {
      super(displayName, currentValue, options, movable, listEntryFactory, tooltipSupplier, isActiveSupplier);
      this.currentRangeValue = currentRangeValue;
      this.numberReader = numberReader;
   }

   @Override
   public boolean onSelected(GuiCategoryUIEditorOptionData<Integer> option) {
      V selectedValue = option.getValue() == null ? null : this.numberReader.apply(option.getValue());
      if (this.currentRangeValue != selectedValue && !Objects.equal(this.currentRangeValue, selectedValue)) {
         this.currentRangeValue = selectedValue;
      }

      return super.onSelected((GuiCategoryUIEditorOptionData<V>)option);
   }

   public V getCurrentRangeValue() {
      return this.currentRangeValue;
   }

   public abstract static class Builder<V, B extends GuiCategoryUIEditorExpandingRangeData.Builder<V, B>>
      extends GuiCategoryUIEditorExpandingOptionsData.Builder<Integer, B> {
      protected V currentRangeValue;
      protected int minNumber;
      protected int maxNumber;
      protected IntFunction<V> numberReader;
      protected Function<V, Integer> numberWriter;
      protected Function<V, String> valueNamer;
      protected boolean hasNullOption;

      protected Builder(ListFactory listFactory) {
         super(listFactory);
      }

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

      public GuiCategoryUIEditorExpandingRangeData<V> build() {
         if (this.numberReader != null && this.valueNamer != null && this.numberWriter != null) {
            this.optionBuilders.clear();
            if (this.currentRangeValue != null) {
               this.setCurrentValue(this.numberWriter.apply(this.currentRangeValue));
            }

            if (this.hasNullOption) {
               GuiCategoryUIEditorOptionData.Builder<Integer> optionBuilder = GuiCategoryUIEditorOptionData.Builder.getDefault();
               optionBuilder.setValue(null);
               optionBuilder.setDisplayName(this.valueNamer.apply(null));
               this.addOptionBuilder(optionBuilder);
            }

            for (int index = this.minNumber; index <= this.maxNumber; index++) {
               GuiCategoryUIEditorOptionData.Builder<Integer> optionBuilder = GuiCategoryUIEditorOptionData.Builder.getDefault();
               optionBuilder.setValue(index);
               optionBuilder.setDisplayName(this.valueNamer.apply(this.numberReader.apply(index)));
               this.addOptionBuilder(optionBuilder);
            }

            return (GuiCategoryUIEditorExpandingRangeData<V>)super.build();
         } else {
            throw new IllegalStateException("required fields not set!");
         }
      }

      protected abstract GuiCategoryUIEditorExpandingRangeData<V> buildInternally(
         GuiCategoryUIEditorOptionData<Integer> var1, List<GuiCategoryUIEditorOptionData<Integer>> var2
      );
   }

   public static final class FinalBuilder<V> extends GuiCategoryUIEditorExpandingRangeData.Builder<V, GuiCategoryUIEditorExpandingRangeData.FinalBuilder<V>> {
      protected FinalBuilder(ListFactory listFactory) {
         super(listFactory);
      }

      @Override
      protected GuiCategoryUIEditorExpandingRangeData<V> buildInternally(
         GuiCategoryUIEditorOptionData<Integer> currentValueData, List<GuiCategoryUIEditorOptionData<Integer>> options
      ) {
         return new GuiCategoryUIEditorExpandingRangeData<>(
            this.displayName,
            this.currentRangeValue,
            this.numberReader,
            currentValueData,
            options,
            this.movable,
            this.listEntryFactory,
            this.tooltipSupplier,
            this.isActiveSupplier
         );
      }
   }
}
