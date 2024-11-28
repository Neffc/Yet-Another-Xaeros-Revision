package xaero.common.category.ui.entry;

import java.util.function.Supplier;
import net.minecraft.class_1074;
import net.minecraft.class_2561;
import net.minecraft.class_339;
import xaero.common.category.ui.GuiCategorySettings;
import xaero.common.category.ui.data.GuiCategoryUIEditorExpandableData;
import xaero.common.graphics.CursorBox;

public class CategorySettingsListEntryExpandingOptions<D extends GuiCategoryUIEditorExpandableData<?>> extends CategorySettingsListEntryWidget<D> {
   public CategorySettingsListEntryExpandingOptions(
      int entryX,
      int entryY,
      int entryW,
      int entryH,
      int index,
      GuiCategorySettings<?, ?, ?, ?, ?, ?>.SettingRowList rowList,
      CategorySettingsListMainEntry<D> root,
      class_339 widget,
      Supplier<String> messageSupplier,
      Supplier<CursorBox> tooltipSupplier
   ) {
      super(entryX, entryY, entryW, entryH, index, rowList, root, widget, tooltipSupplier);
      if (messageSupplier != null) {
         String optionTypeName = messageSupplier.get();
         if (root.data.isExpanded()) {
            widget.method_25355(class_2561.method_43470(class_1074.method_4662("gui.xaero_category_expanded_options", new Object[]{optionTypeName})));
         } else {
            widget.method_25355(class_2561.method_43470(optionTypeName));
         }
      }
   }
}
