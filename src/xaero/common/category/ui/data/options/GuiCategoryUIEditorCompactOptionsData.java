package xaero.common.category.ui.data.options;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.IntConsumer;
import java.util.function.IntFunction;
import java.util.function.Supplier;
import xaero.common.category.ui.data.GuiCategoryUIEditorExpandableData;
import xaero.common.category.ui.entry.CategorySettingsListEntryWidget;
import xaero.common.category.ui.entry.CategorySettingsListEntryWrapper;
import xaero.common.category.ui.entry.CategorySettingsListMainEntryFactory;
import xaero.common.category.ui.entry.widget.CategorySettingsButtonIteration;
import xaero.common.category.ui.entry.widget.CategorySettingsSlider;
import xaero.common.graphics.CursorBox;

public abstract class GuiCategoryUIEditorCompactOptionsData<V> extends GuiCategoryUIEditorOptionsData<V> {
   private IntConsumer updatedIndexConsumer;
   protected int currentIndex;
   protected final int optionCount;

   protected GuiCategoryUIEditorCompactOptionsData(
      String displayName,
      int currentIndex,
      int optionCount,
      boolean movable,
      CategorySettingsListMainEntryFactory listEntryFactory,
      BiFunction<GuiCategoryUIEditorExpandableData<?>, GuiCategoryUIEditorExpandableData<GuiCategoryUIEditorOptionData<V>>, Supplier<CursorBox>> tooltipSupplier,
      GuiCategoryUIEditorOptionsData.IOptionsDataIsActiveSupplier isActiveSupplier
   ) {
      super(displayName, movable, listEntryFactory, tooltipSupplier, isActiveSupplier);
      this.currentIndex = currentIndex;
      this.optionCount = optionCount;
   }

   public int getCurrentIndex() {
      return this.currentIndex;
   }

   public int getOptionCount() {
      return this.optionCount;
   }

   public final IntConsumer getUpdatedIndexConsumer() {
      if (this.updatedIndexConsumer == null) {
         this.updatedIndexConsumer = i -> {
            this.currentIndex = i;
            this.setCurrentValue(this.getIndexReader().apply(i));
         };
      }

      return this.updatedIndexConsumer;
   }

   protected abstract IntFunction<GuiCategoryUIEditorOptionData<V>> getIndexReader();

   @Override
   public List<GuiCategoryUIEditorOptionData<V>> getSubExpandables() {
      return null;
   }

   public abstract static class Builder<V, B extends GuiCategoryUIEditorCompactOptionsData.Builder<V, B>> extends GuiCategoryUIEditorOptionsData.Builder<V, B> {
      private boolean slider;

      protected Builder() {
      }

      public B setDefault() {
         super.setDefault();
         this.setSlider(false);
         return this.self;
      }

      public B setSlider(boolean slider) {
         this.slider = slider;
         return this.self;
      }

      protected GuiCategoryUIEditorCompactOptionsData<V> buildInternally() {
         CategorySettingsListMainEntryFactory listEntryFactory;
         if (this.slider) {
            listEntryFactory = (data, parent, index, lineType, rowList, screenWidth, isFinalExpanded) -> new CategorySettingsListEntryWrapper<>(
                  (x, y, width, height, root) -> new CategorySettingsListEntryWidget<>(
                        x,
                        y,
                        width,
                        height,
                        index,
                        rowList,
                        root,
                        new CategorySettingsSlider(
                           ((GuiCategoryUIEditorCompactOptionsData)data).getUpdatedIndexConsumer(),
                           ((GuiCategoryUIEditorCompactOptionsData)data).getMessageSupplier(),
                           ((GuiCategoryUIEditorCompactOptionsData)data).getCurrentIndex(),
                           ((GuiCategoryUIEditorCompactOptionsData)data).getOptionCount(),
                           216,
                           20,
                           rowList
                        ),
                        data.getTooltipSupplier(parent)
                     ),
                  screenWidth,
                  index,
                  rowList,
                  lineType,
                  data
               );
         } else {
            listEntryFactory = (data, parent, index, lineType, rowList, screenWidth, isFinalExpanded) -> new CategorySettingsListEntryWrapper<>(
                  (x, y, width, height, root) -> new CategorySettingsListEntryWidget<>(
                        x,
                        y,
                        width,
                        height,
                        index,
                        rowList,
                        root,
                        new CategorySettingsButtonIteration(
                           parent,
                           ((GuiCategoryUIEditorCompactOptionsData)data).getUpdatedIndexConsumer(),
                           ((GuiCategoryUIEditorCompactOptionsData)data).getMessageSupplier(),
                           true,
                           ((GuiCategoryUIEditorCompactOptionsData)data).getCurrentIndex(),
                           ((GuiCategoryUIEditorCompactOptionsData)data).getOptionCount(),
                           216,
                           20,
                           rowList
                        ),
                        data.getTooltipSupplier(parent)
                     ),
                  screenWidth,
                  index,
                  rowList,
                  lineType,
                  data
               );
         }

         return this.buildInternally(listEntryFactory);
      }

      protected abstract GuiCategoryUIEditorCompactOptionsData<V> buildInternally(CategorySettingsListMainEntryFactory var1);
   }
}
