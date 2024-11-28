package xaero.common.category.ui.entry;

import java.util.function.Supplier;
import xaero.common.category.ui.GuiCategorySettings;
import xaero.common.category.ui.data.GuiCategoryUIEditorExpandableData;
import xaero.common.graphics.CursorBox;

public class CategorySettingsListEntryWrapper<D extends GuiCategoryUIEditorExpandableData<?>> extends CategorySettingsListMainEntry<D> {
   public CategorySettingsListEntryWrapper(
      CategorySettingsListMainEntry.CenteredEntryFactory wrappedFactory,
      int screenWidth,
      int index,
      GuiCategorySettings<?, ?, ?, ?, ?, ?>.SettingRowList rowList,
      ConnectionLineType lineType,
      GuiCategoryUIEditorExpandableData<D> data
   ) {
      this(wrappedFactory, screenWidth, index, rowList, lineType, data, null);
   }

   public CategorySettingsListEntryWrapper(
      CategorySettingsListMainEntry.CenteredEntryFactory wrappedFactory,
      int screenWidth,
      int index,
      GuiCategorySettings<?, ?, ?, ?, ?, ?>.SettingRowList rowList,
      ConnectionLineType lineType,
      GuiCategoryUIEditorExpandableData<D> data,
      Supplier<CursorBox> helpTooltipSupplier
   ) {
      super(screenWidth, index, rowList, lineType, data);
      this.withSubEntry(wrappedFactory);
      this.addHelpElement(helpTooltipSupplier);
   }

   @Override
   public String getMessage() {
      return "";
   }
}
