package xaero.common.category.ui.entry;

import java.util.function.Supplier;
import xaero.common.category.ui.GuiCategorySettings;
import xaero.common.category.ui.data.GuiCategoryUIEditorExpandableData;
import xaero.common.category.ui.data.options.GuiCategoryUIEditorExpandingOptionsData;
import xaero.common.category.ui.data.options.GuiCategoryUIEditorOptionData;
import xaero.common.graphics.CursorBox;

public class CategorySettingsListEntryExpandingOption<V> extends CategorySettingsListEntryWithIconAndText<GuiCategoryUIEditorExpandableData<?>> {
   private GuiCategoryUIEditorExpandingOptionsData<V> dataParent;

   public CategorySettingsListEntryExpandingOption(
      int entryX,
      int entryY,
      int entryW,
      int entryH,
      int index,
      GuiCategorySettings<?, ?, ?, ?, ?, ?>.SettingRowList rowList,
      GuiCategoryUIEditorExpandingOptionsData<V> dataParent,
      CategorySettingsListMainEntry<GuiCategoryUIEditorExpandableData<?>> root,
      Supplier<CursorBox> tooltipSupplier
   ) {
      super(entryX, entryY, entryW, entryH, index, rowList, root.data.getDisplayName(), root, tooltipSupplier);
      this.dataParent = dataParent;
   }

   @Override
   public boolean selectAction() {
      return this.dataParent.onSelected((GuiCategoryUIEditorOptionData<V>)this.root.data);
   }
}
