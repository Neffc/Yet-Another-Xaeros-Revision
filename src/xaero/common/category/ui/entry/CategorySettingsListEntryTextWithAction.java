package xaero.common.category.ui.entry;

import java.util.function.Supplier;
import net.minecraft.class_1109;
import net.minecraft.class_310;
import net.minecraft.class_3417;
import xaero.common.category.ui.GuiCategorySettings;
import xaero.common.category.ui.data.GuiCategoryUIEditorExpandableData;
import xaero.common.graphics.CursorBox;

public class CategorySettingsListEntryTextWithAction<D extends GuiCategoryUIEditorExpandableData<?>> extends CategorySettingsListEntryWithIconAndText<D> {
   private final Runnable action;

   public CategorySettingsListEntryTextWithAction(
      int entryX,
      int entryY,
      int entryW,
      int entryH,
      int index,
      GuiCategorySettings<?, ?, ?, ?, ?, ?>.SettingRowList rowList,
      CategorySettingsListMainEntry<D> root,
      Runnable action,
      Supplier<CursorBox> tooltipSupplier
   ) {
      super(entryX, entryY, entryW, entryH, index, rowList, root.data.getDisplayName(), root, tooltipSupplier);
      this.action = action;
   }

   @Override
   public boolean selectAction() {
      this.action.run();
      class_310.method_1551().method_1483().method_4873(class_1109.method_47978(class_3417.field_15015, 1.0F));
      return false;
   }
}
