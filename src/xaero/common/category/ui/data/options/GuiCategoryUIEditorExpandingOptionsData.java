package xaero.common.category.ui.data.options;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Supplier;
import javax.annotation.Nonnull;
import xaero.common.category.ui.data.GuiCategoryUIEditorExpandableData;
import xaero.common.category.ui.entry.CategorySettingsListEntryExpandingOptions;
import xaero.common.category.ui.entry.CategorySettingsListEntryWrapper;
import xaero.common.category.ui.entry.CategorySettingsListMainEntryFactory;
import xaero.common.category.ui.entry.widget.CategorySettingsButton;
import xaero.common.graphics.CursorBox;
import xaero.common.misc.ListFactory;

public class GuiCategoryUIEditorExpandingOptionsData<V> extends GuiCategoryUIEditorOptionsData<V> {
   protected final List<GuiCategoryUIEditorOptionData<V>> options;

   protected GuiCategoryUIEditorExpandingOptionsData(
      @Nonnull String displayName,
      @Nonnull GuiCategoryUIEditorOptionData<V> currentValue,
      @Nonnull List<GuiCategoryUIEditorOptionData<V>> options,
      boolean movable,
      @Nonnull CategorySettingsListMainEntryFactory listEntryFactory,
      BiFunction<GuiCategoryUIEditorExpandableData<?>, GuiCategoryUIEditorExpandableData<GuiCategoryUIEditorOptionData<V>>, Supplier<CursorBox>> tooltipSupplier,
      GuiCategoryUIEditorOptionsData.IOptionsDataIsActiveSupplier isActiveSupplier
   ) {
      super(displayName, movable, listEntryFactory, tooltipSupplier, isActiveSupplier);
      this.options = options;
      this.currentValue = currentValue;
   }

   public boolean onSelected(GuiCategoryUIEditorOptionData<V> option) {
      this.setCurrentValue(option);
      this.setExpanded(false);
      return true;
   }

   @Override
   public List<GuiCategoryUIEditorOptionData<V>> getSubExpandables() {
      return this.options;
   }

   @Override
   public String getDisplayName() {
      return "";
   }

   public abstract static class Builder<V, B extends GuiCategoryUIEditorExpandingOptionsData.Builder<V, B>>
      extends GuiCategoryUIEditorOptionsData.Builder<V, B> {
      protected final List<GuiCategoryUIEditorOptionData.Builder<V>> optionBuilders;
      protected final ListFactory listFactory;

      protected Builder(ListFactory listFactory) {
         this.optionBuilders = listFactory.get();
         this.listFactory = listFactory;
      }

      public B setDefault() {
         super.setDefault();
         this.optionBuilders.clear();
         this.listEntryFactory = (data, parent, index, lineType, rowList, screenWidth, isFinalExpanded) -> new CategorySettingsListEntryWrapper<>(
               (x, y, width, height, root) -> new CategorySettingsListEntryExpandingOptions<>(
                     x,
                     y,
                     width,
                     height,
                     index,
                     rowList,
                     root,
                     new CategorySettingsButton(
                        parent,
                        () -> "",
                        ((GuiCategoryUIEditorExpandingOptionsData)data).getIsActiveSupplier().get(parent, (GuiCategoryUIEditorExpandingOptionsData)data),
                        216,
                        20,
                        b -> data.getExpandAction(rowList).run(),
                        rowList
                     ),
                     ((GuiCategoryUIEditorExpandingOptionsData)data).getMessageSupplier(),
                     data.getTooltipSupplier(parent)
                  ),
               screenWidth,
               index,
               rowList,
               lineType,
               data
            );
         return this.self;
      }

      public B addOptionBuilderFor(V option) {
         this.optionBuilders.add(GuiCategoryUIEditorOptionData.Builder.<V>getDefault().setValue(option));
         return this.self;
      }

      public B addOptionBuilder(GuiCategoryUIEditorOptionData.Builder<V> optionBuilder) {
         this.optionBuilders.add(optionBuilder);
         return this.self;
      }

      public GuiCategoryUIEditorExpandingOptionsData<V> build() {
         if (this.listFactory == null) {
            throw new IllegalStateException("required fields not set!");
         } else {
            return (GuiCategoryUIEditorExpandingOptionsData<V>)super.build();
         }
      }

      @Override
      protected GuiCategoryUIEditorOptionsData<V> buildInternally() {
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
            return this.buildInternally(currentValueData, options);
         }
      }

      protected abstract GuiCategoryUIEditorOptionsData<V> buildInternally(GuiCategoryUIEditorOptionData<V> var1, List<GuiCategoryUIEditorOptionData<V>> var2);
   }

   public static class FinalBuilder<V> extends GuiCategoryUIEditorExpandingOptionsData.Builder<V, GuiCategoryUIEditorExpandingOptionsData.FinalBuilder<V>> {
      protected FinalBuilder(ListFactory listFactory) {
         super(listFactory);
      }

      public static <V> GuiCategoryUIEditorExpandingOptionsData.FinalBuilder<V> getDefault(ListFactory listFactory) {
         return new GuiCategoryUIEditorExpandingOptionsData.FinalBuilder(listFactory).setDefault();
      }

      @Override
      protected GuiCategoryUIEditorOptionsData<V> buildInternally(
         GuiCategoryUIEditorOptionData<V> currentValueData, List<GuiCategoryUIEditorOptionData<V>> options
      ) {
         return new GuiCategoryUIEditorExpandingOptionsData<>(
            this.displayName, currentValueData, options, this.movable, this.listEntryFactory, this.tooltipSupplier, this.isActiveSupplier
         );
      }
   }
}
