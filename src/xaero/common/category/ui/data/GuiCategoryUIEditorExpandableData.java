package xaero.common.category.ui.data;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Supplier;
import javax.annotation.Nonnull;
import xaero.common.category.ui.GuiCategorySettings;
import xaero.common.category.ui.entry.CategorySettingsListEntryTextWithAction;
import xaero.common.category.ui.entry.CategorySettingsListEntryWrapper;
import xaero.common.category.ui.entry.CategorySettingsListMainEntryFactory;
import xaero.common.graphics.CursorBox;

public abstract class GuiCategoryUIEditorExpandableData<SE extends GuiCategoryUIEditorExpandableData<?>> {
   private final boolean movable;
   private boolean expanded;
   protected final CategorySettingsListMainEntryFactory listEntryFactory;
   protected final BiFunction<GuiCategoryUIEditorExpandableData<?>, GuiCategoryUIEditorExpandableData<SE>, Supplier<CursorBox>> tooltipSupplier;

   public GuiCategoryUIEditorExpandableData(
      boolean movable,
      @Nonnull CategorySettingsListMainEntryFactory listEntryFactory,
      BiFunction<GuiCategoryUIEditorExpandableData<?>, GuiCategoryUIEditorExpandableData<SE>, Supplier<CursorBox>> tooltipSupplier
   ) {
      this.movable = movable;
      this.listEntryFactory = listEntryFactory;
      this.tooltipSupplier = tooltipSupplier;
   }

   public boolean isMovable() {
      return this.movable;
   }

   public boolean isExpanded() {
      return this.expanded;
   }

   public void setExpanded(boolean expanded) {
      this.expanded = expanded;
      if (!expanded) {
         List<SE> subExpandables = this.getSubExpandables();
         if (subExpandables != null) {
            for (SE sub : subExpandables) {
               if (sub.isExpanded()) {
                  sub.setExpanded(false);
                  break;
               }
            }
         }
      }
   }

   public final CategorySettingsListMainEntryFactory getListEntryFactory() {
      return this.listEntryFactory;
   }

   public Supplier<CursorBox> getTooltipSupplier(GuiCategoryUIEditorExpandableData<?> parent) {
      return this.tooltipSupplier == null ? null : this.tooltipSupplier.apply(parent, this);
   }

   public Runnable getExpandAction(GuiCategorySettings<?, ?, ?, ?, ?, ?>.SettingRowList rowList) {
      return () -> {
         List<?> subExpandables = this.getSubExpandables();
         if (subExpandables != null && !subExpandables.isEmpty()) {
            this.setExpanded(true);
            rowList.setLastExpandedData(this);

            for (Object o : subExpandables) {
               GuiCategoryUIEditorExpandableData<?> sed = (GuiCategoryUIEditorExpandableData<?>)o;
               if (sed.isExpanded()) {
                  sed.setExpanded(false);
                  break;
               }
            }

            rowList.updateEntries();
         }
      };
   }

   public abstract String getDisplayName();

   public abstract List<SE> getSubExpandables();

   public abstract static class Builder<SE extends GuiCategoryUIEditorExpandableData<?>, B extends GuiCategoryUIEditorExpandableData.Builder<SE, B>> {
      protected B self = (B)this;
      protected CategorySettingsListMainEntryFactory listEntryFactory;
      protected BiFunction<GuiCategoryUIEditorExpandableData<?>, GuiCategoryUIEditorExpandableData<SE>, Supplier<CursorBox>> tooltipSupplier;
      protected boolean movable;

      protected Builder() {
      }

      public B setDefault() {
         this.setMovable(false);
         this.setListEntryFactory(
            (data, parent, index, lineType, rowList, screenWidth, isFinalExpanded) -> new CategorySettingsListEntryWrapper<>(
                  (x, y, width, height, root) -> new CategorySettingsListEntryTextWithAction<>(
                        x, y, width, height, index, rowList, root, data.getExpandAction(rowList), data.getTooltipSupplier(parent)
                     ),
                  screenWidth,
                  index,
                  rowList,
                  lineType,
                  data
               )
         );
         this.setTooltipSupplier(null);
         return this.self;
      }

      public B setMovable(boolean movable) {
         this.movable = movable;
         return this.self;
      }

      public B setListEntryFactory(CategorySettingsListMainEntryFactory listEntryFactory) {
         this.listEntryFactory = listEntryFactory;
         return this.self;
      }

      public B setTooltipSupplier(BiFunction<GuiCategoryUIEditorExpandableData<?>, GuiCategoryUIEditorExpandableData<SE>, Supplier<CursorBox>> tooltipSupplier) {
         this.tooltipSupplier = tooltipSupplier;
         return this.self;
      }

      public GuiCategoryUIEditorExpandableData<SE> build() {
         if (this.listEntryFactory == null) {
            throw new IllegalStateException("required fields not set!");
         } else {
            return this.buildInternally();
         }
      }

      protected abstract GuiCategoryUIEditorExpandableData<SE> buildInternally();
   }
}
