package xaero.common.category.ui.data.options;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Supplier;
import javax.annotation.Nonnull;
import xaero.common.category.ui.data.GuiCategoryUIEditorExpandableData;
import xaero.common.category.ui.entry.CategorySettingsListEntryExpandingOption;
import xaero.common.category.ui.entry.CategorySettingsListEntryWrapper;
import xaero.common.category.ui.entry.CategorySettingsListMainEntry;
import xaero.common.category.ui.entry.CategorySettingsListMainEntryFactory;
import xaero.common.graphics.CursorBox;

public final class GuiCategoryUIEditorOptionData<V> extends GuiCategoryUIEditorExpandableData<GuiCategoryUIEditorExpandableData<?>> {
   private final V value;
   private final String displayName;

   public GuiCategoryUIEditorOptionData(
      V index,
      String displayName,
      boolean movable,
      @Nonnull CategorySettingsListMainEntryFactory listEntryFactory,
      BiFunction<GuiCategoryUIEditorExpandableData<?>, GuiCategoryUIEditorExpandableData<GuiCategoryUIEditorExpandableData<?>>, Supplier<CursorBox>> tooltipSupplier
   ) {
      super(movable, listEntryFactory, tooltipSupplier);
      this.value = index;
      this.displayName = displayName;
   }

   public V getValue() {
      return this.value;
   }

   @Override
   public String getDisplayName() {
      return this.displayName;
   }

   @Override
   public List<GuiCategoryUIEditorExpandableData<?>> getSubExpandables() {
      return null;
   }

   public static final class Builder<V>
      extends GuiCategoryUIEditorExpandableData.Builder<GuiCategoryUIEditorExpandableData<?>, GuiCategoryUIEditorOptionData.Builder<V>> {
      private V value;
      private String displayName;

      public GuiCategoryUIEditorOptionData.Builder<V> setDefault() {
         super.setDefault();
         this.listEntryFactory = (data, parent, index, lineType, rowList, screenWidth, isFinalExpanded) -> new CategorySettingsListEntryWrapper<>(
               (x, y, width, height, root) -> new CategorySettingsListEntryExpandingOption(
                     x,
                     y,
                     width,
                     height,
                     index,
                     rowList,
                     (GuiCategoryUIEditorExpandingOptionsData<V>)parent,
                     (CategorySettingsListMainEntry<GuiCategoryUIEditorExpandableData<?>>)root,
                     data.getTooltipSupplier(parent)
                  ),
               screenWidth,
               index,
               rowList,
               lineType,
               data
            );
         this.setValue(null);
         this.setDisplayName(null);
         return this;
      }

      public GuiCategoryUIEditorOptionData.Builder<V> setValue(V value) {
         this.value = value;
         return this;
      }

      public GuiCategoryUIEditorOptionData.Builder<V> setDisplayName(String displayName) {
         this.displayName = displayName;
         return this;
      }

      public GuiCategoryUIEditorOptionData<V> build() {
         if (this.displayName == null) {
            this.displayName = this.value == null ? "N/A" : this.value.toString();
         }

         return (GuiCategoryUIEditorOptionData<V>)super.build();
      }

      public static <V> GuiCategoryUIEditorOptionData.Builder<V> getDefault() {
         return new GuiCategoryUIEditorOptionData.Builder<V>().setDefault();
      }

      protected GuiCategoryUIEditorOptionData<V> buildInternally() {
         return new GuiCategoryUIEditorOptionData<>(this.value, this.displayName, this.movable, this.listEntryFactory, this.tooltipSupplier);
      }
   }
}
