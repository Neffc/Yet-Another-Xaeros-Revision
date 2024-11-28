package xaero.common.category.ui.entry;

import java.util.function.Supplier;
import xaero.common.category.ui.GuiCategorySettings;
import xaero.common.category.ui.data.GuiCategoryUIEditorExpandableData;
import xaero.common.graphics.CursorBox;

public abstract class CategorySettingsListEntryWithRootReference<D extends GuiCategoryUIEditorExpandableData<?>> extends CategorySettingsListEntry {
   protected final CategorySettingsListMainEntry<D> root;

   public CategorySettingsListEntryWithRootReference(
      int entryX,
      int entryY,
      int entryW,
      int entryH,
      int index,
      GuiCategorySettings<?, ?, ?, ?, ?, ?>.SettingRowList rowList,
      CategorySettingsListMainEntry<D> root,
      Supplier<CursorBox> tooltipSupplier
   ) {
      super(entryX, entryY, entryW, entryH, index, rowList, tooltipSupplier);
      this.root = root;
   }
}
