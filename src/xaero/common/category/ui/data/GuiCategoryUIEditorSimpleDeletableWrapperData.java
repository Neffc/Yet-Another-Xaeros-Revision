package xaero.common.category.ui.data;

import java.util.function.BiFunction;
import java.util.function.Supplier;
import javax.annotation.Nonnull;
import xaero.common.category.ui.GuiCategorySettings;
import xaero.common.category.ui.entry.CategorySettingsListEntryDeletableListElement;
import xaero.common.category.ui.entry.CategorySettingsListMainEntryFactory;
import xaero.common.graphics.CursorBox;

public class GuiCategoryUIEditorSimpleDeletableWrapperData<S extends Comparable<S>> extends GuiCategoryUIEditorSimpleWrapperData<S> {
   private final GuiCategoryUIEditorSimpleDeletableWrapperData.DeletionCallback deletionCallback;

   protected GuiCategoryUIEditorSimpleDeletableWrapperData(
      @Nonnull S element,
      boolean movable,
      @Nonnull CategorySettingsListMainEntryFactory listEntryFactory,
      BiFunction<GuiCategoryUIEditorExpandableData<?>, GuiCategoryUIEditorExpandableData<GuiCategoryUIEditorExpandableData<?>>, Supplier<CursorBox>> tooltipSupplier,
      @Nonnull GuiCategoryUIEditorSimpleDeletableWrapperData.DeletionCallback deletionCallback
   ) {
      super(element, movable, listEntryFactory, tooltipSupplier);
      this.deletionCallback = deletionCallback;
   }

   public GuiCategoryUIEditorSimpleDeletableWrapperData.DeletionCallback getDeletionCallback() {
      return this.deletionCallback;
   }

   public static final class Builder<S extends Comparable<S>>
      extends GuiCategoryUIEditorSimpleWrapperData.Builder<S, GuiCategoryUIEditorSimpleDeletableWrapperData.Builder<S>> {
      private GuiCategoryUIEditorSimpleDeletableWrapperData.DeletionCallback deletionCallback;

      public GuiCategoryUIEditorSimpleDeletableWrapperData.Builder<S> setDefault() {
         super.setDefault();
         this.setListEntryFactory(
            (data, parent, index, lineType, rowList, screenWidth, isFinalExpanded) -> new CategorySettingsListEntryDeletableListElement(
                  screenWidth,
                  index,
                  rowList,
                  lineType,
                  (GuiCategoryUIEditorSimpleDeletableWrapperData<?>)data,
                  parent,
                  ((GuiCategoryUIEditorSimpleDeletableWrapperData)data).getDeletionCallback(),
                  data.getTooltipSupplier(parent)
               )
         );
         this.setDeletionCallback(null);
         return this.self;
      }

      public GuiCategoryUIEditorSimpleDeletableWrapperData.Builder<S> setDeletionCallback(
         GuiCategoryUIEditorSimpleDeletableWrapperData.DeletionCallback deletionCallback
      ) {
         this.deletionCallback = deletionCallback;
         return this.self;
      }

      public GuiCategoryUIEditorSimpleDeletableWrapperData<S> build() {
         if (this.deletionCallback == null) {
            throw new IllegalStateException("required fields not set!");
         } else {
            return (GuiCategoryUIEditorSimpleDeletableWrapperData<S>)super.build();
         }
      }

      @Override
      protected GuiCategoryUIEditorExpandableData<GuiCategoryUIEditorExpandableData<?>> buildInternally() {
         return new GuiCategoryUIEditorSimpleDeletableWrapperData(
            this.element, this.movable, this.listEntryFactory, this.tooltipSupplier, this.deletionCallback
         );
      }

      public static <S extends Comparable<S>> GuiCategoryUIEditorSimpleDeletableWrapperData.Builder<S> getDefault() {
         return new GuiCategoryUIEditorSimpleDeletableWrapperData.Builder<S>().setDefault();
      }
   }

   public interface DeletionCallback {
      boolean delete(
         GuiCategoryUIEditorExpandableData<?> var1,
         GuiCategoryUIEditorSimpleDeletableWrapperData<?> var2,
         GuiCategorySettings<?, ?, ?, ?, ?, ?>.SettingRowList var3
      );
   }
}
