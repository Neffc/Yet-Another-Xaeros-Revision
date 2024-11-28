package xaero.common.category.ui.entry;

import xaero.common.category.ui.GuiCategorySettings;
import xaero.common.category.ui.data.GuiCategoryUIEditorExpandableData;

public interface CategorySettingsListMainEntryFactory {
   CategorySettingsListMainEntry<?> get(
      GuiCategoryUIEditorExpandableData<?> var1,
      GuiCategoryUIEditorExpandableData<?> var2,
      int var3,
      ConnectionLineType var4,
      GuiCategorySettings<?, ?, ?, ?, ?, ?>.SettingRowList var5,
      int var6,
      boolean var7
   );
}
