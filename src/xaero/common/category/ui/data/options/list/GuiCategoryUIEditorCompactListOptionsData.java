package xaero.common.category.ui.data.options.list;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.IntFunction;
import java.util.function.Supplier;
import javax.annotation.Nonnull;
import xaero.common.category.ui.data.GuiCategoryUIEditorExpandableData;
import xaero.common.category.ui.data.options.GuiCategoryUIEditorCompactOptionsData;
import xaero.common.category.ui.data.options.GuiCategoryUIEditorOptionData;
import xaero.common.category.ui.data.options.GuiCategoryUIEditorOptionsData;
import xaero.common.category.ui.entry.CategorySettingsListMainEntryFactory;
import xaero.common.graphics.CursorBox;
import xaero.common.misc.ListFactory;

public final class GuiCategoryUIEditorCompactListOptionsData<V> extends GuiCategoryUIEditorCompactOptionsData<V> {
   private IntFunction<GuiCategoryUIEditorOptionData<V>> indexReader;
   private List<GuiCategoryUIEditorOptionData<V>> options;

   protected GuiCategoryUIEditorCompactListOptionsData(
      String displayName,
      @Nonnull GuiCategoryUIEditorOptionData<V> currentValue,
      @Nonnull List<GuiCategoryUIEditorOptionData<V>> options,
      boolean movable,
      CategorySettingsListMainEntryFactory listEntryFactory,
      BiFunction<GuiCategoryUIEditorExpandableData<?>, GuiCategoryUIEditorExpandableData<GuiCategoryUIEditorOptionData<V>>, Supplier<CursorBox>> tooltipSupplier,
      GuiCategoryUIEditorOptionsData.IOptionsDataIsActiveSupplier isActiveSupplier
   ) {
      super(displayName, options.indexOf(currentValue), options.size(), movable, listEntryFactory, tooltipSupplier, isActiveSupplier);
      this.currentValue = currentValue;
      this.options = options;
   }

   @Override
   protected final IntFunction<GuiCategoryUIEditorOptionData<V>> getIndexReader() {
      if (this.indexReader == null) {
         this.indexReader = this.options::get;
      }

      return this.indexReader;
   }

   public static final class Builder<V> extends GuiCategoryUIEditorCompactOptionsData.Builder<V, GuiCategoryUIEditorCompactListOptionsData.Builder<V>> {
      protected final List<GuiCategoryUIEditorOptionData.Builder<V>> optionBuilders;
      protected final ListFactory listFactory;

      protected Builder(ListFactory listFactory) {
         this.optionBuilders = listFactory.get();
         this.listFactory = listFactory;
      }

      public GuiCategoryUIEditorCompactListOptionsData.Builder<V> setDefault() {
         this.optionBuilders.clear();
         return (GuiCategoryUIEditorCompactListOptionsData.Builder<V>)super.setDefault();
      }

      public GuiCategoryUIEditorCompactListOptionsData.Builder<V> addOptionBuilder(GuiCategoryUIEditorOptionData.Builder<V> optionBuilder) {
         this.optionBuilders.add(optionBuilder);
         return this;
      }

      public GuiCategoryUIEditorCompactListOptionsData<V> build() {
         if (this.listFactory == null) {
            throw new IllegalStateException("required fields not set!");
         } else {
            return (GuiCategoryUIEditorCompactListOptionsData<V>)super.build();
         }
      }

      protected GuiCategoryUIEditorCompactListOptionsData<V> buildInternally(CategorySettingsListMainEntryFactory listEntryFactory) {
         List<GuiCategoryUIEditorOptionData<V>> options = this.optionBuilders
            .stream()
            .map(GuiCategoryUIEditorOptionData.Builder::build)
            .collect(this.listFactory::get, List::add, List::addAll);
         GuiCategoryUIEditorOptionData<V> currentValueData = null;

         for (GuiCategoryUIEditorOptionData<V> optionData : options) {
            if (optionData.getValue() == this.currentValue) {
               currentValueData = optionData;
               break;
            }
         }

         if (currentValueData == null) {
            throw new IllegalStateException("current value is not one of the options! " + this.currentValue);
         } else {
            return new GuiCategoryUIEditorCompactListOptionsData<>(
               this.displayName, currentValueData, options, this.movable, listEntryFactory, this.tooltipSupplier, this.isActiveSupplier
            );
         }
      }

      public static <V> GuiCategoryUIEditorCompactListOptionsData.Builder<V> getDefault(ListFactory listFactory) {
         return new GuiCategoryUIEditorCompactListOptionsData.Builder<V>(listFactory).setDefault();
      }
   }
}
