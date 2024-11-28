package xaero.common.category.ui.entry;

import java.util.function.Supplier;
import net.minecraft.class_2583;
import xaero.common.category.ui.GuiCategorySettings;
import xaero.common.category.ui.data.GuiCategoryUIEditorExpandableData;
import xaero.common.category.ui.data.GuiCategoryUIEditorSimpleDeletableWrapperData;
import xaero.common.graphics.CursorBox;

public class CategorySettingsListEntryDeletableListElement extends CategorySettingsListMainEntry<GuiCategoryUIEditorExpandableData<?>> {
   private final GuiCategoryUIEditorSimpleDeletableWrapperData.DeletionCallback deletionCallback;
   private final GuiCategoryUIEditorExpandableData<?> parent;
   private static final CursorBox DELETE_TOOLTIP = new CursorBox("gui.xaero_category_delete_list_element", class_2583.field_24360, true);

   public CategorySettingsListEntryDeletableListElement(
      int screenWidth,
      int index,
      GuiCategorySettings<?, ?, ?, ?, ?, ?>.SettingRowList rowList,
      ConnectionLineType lineType,
      GuiCategoryUIEditorSimpleDeletableWrapperData<?> data,
      GuiCategoryUIEditorExpandableData<?> parent,
      GuiCategoryUIEditorSimpleDeletableWrapperData.DeletionCallback deletionCallback,
      Supplier<CursorBox> tooltipSupplier
   ) {
      super(screenWidth, index, rowList, lineType, (GuiCategoryUIEditorExpandableData<GuiCategoryUIEditorExpandableData<?>>)data);
      this.deletionCallback = deletionCallback;
      this.parent = parent;
      this.withSubEntry(
         (x, y, w, h, root) -> new CategorySettingsListEntryTextWithAction<>(x, y, w, h, index, rowList, this, data.getExpandAction(rowList), tooltipSupplier)
      );
      this.withSubEntry(
         (x, y, w, h, root) -> new CategorySettingsListTextButtonEntry<>(
               x - 24, y + 2, index, rowList, "x", -5636096, -43691, 5, () -> deletionCallback.delete(parent, data, rowList), this, () -> DELETE_TOOLTIP
            )
      );
   }

   @Override
   public boolean keyPressed(int i, int j, int k, boolean isRoot) {
      if (i == 261) {
         if (this.deletionCallback.delete(this.parent, (GuiCategoryUIEditorSimpleDeletableWrapperData<?>)this.data, this.rowList)) {
            this.rowList.restoreScrollAfterUpdate();
            this.rowList.updateEntries();
         }

         return false;
      } else {
         return super.keyPressed(i, j, k, isRoot);
      }
   }

   @Override
   public String getMessage() {
      return this.data.getDisplayName();
   }
}
