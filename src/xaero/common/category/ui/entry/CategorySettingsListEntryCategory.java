package xaero.common.category.ui.entry;

import java.util.function.Supplier;
import net.minecraft.class_1074;
import net.minecraft.class_2583;
import xaero.common.category.ObjectCategory;
import xaero.common.category.ui.GuiCategorySettings;
import xaero.common.category.ui.data.GuiCategoryUIEditorCategoryData;
import xaero.common.category.ui.data.GuiCategoryUIEditorExpandableData;
import xaero.common.graphics.CursorBox;

public class CategorySettingsListEntryCategory<C extends ObjectCategory<?, C>, ED extends GuiCategoryUIEditorCategoryData<C, ?, ED>>
   extends CategorySettingsListMainEntry<GuiCategoryUIEditorExpandableData<?>> {
   private static final CursorBox HELP_TOOLTIP = new CursorBox("gui.xaero_category_help2", class_2583.field_24360, true);
   private static final CursorBox PROTECTED_TOOLTIP = new CursorBox("gui.xaero_category_protected_category", class_2583.field_24360, true);
   private static final CursorBox UP_TOOLTIP = new CursorBox("gui.xaero_category_category_move_up", class_2583.field_24360, true);
   private static final CursorBox DOWN_TOOLTIP = new CursorBox("gui.xaero_category_category_move_down", class_2583.field_24360, true);

   public CategorySettingsListEntryCategory(
      int screenWidth,
      int index,
      GuiCategorySettings<?, ?, ?, ?, ?, ?>.SettingRowList rowList,
      ConnectionLineType lineType,
      GuiCategoryUIEditorCategoryData<?, ?, ?> data,
      GuiCategoryUIEditorCategoryData<?, ?, ?> parent,
      Supplier<CursorBox> tooltipSupplier,
      boolean isFinalExpanded
   ) {
      super(screenWidth, index, rowList, lineType, data);
      int subIndex = parent == null ? -1 : parent.getSubCategories().indexOf(data);
      boolean isCut = rowList.isCut(data);
      ED currentCut = (ED)rowList.getCut();
      CursorBox cutTooltip = new CursorBox(
         class_1074.method_4662("gui.xaero_category_cut", new Object[]{class_1074.method_4662(data.getDisplayName(), new Object[0])}),
         class_2583.field_24360,
         true
      );
      CursorBox pasteTooltip = currentCut == null
         ? null
         : (
            isCut
               ? new CursorBox("gui.xaero_category_paste_cancel", class_2583.field_24360, true)
               : new CursorBox(
                  class_1074.method_4662(
                     "gui.xaero_category_paste",
                     new Object[]{
                        class_1074.method_4662(currentCut.getDisplayName(), new Object[0]), class_1074.method_4662(data.getDisplayName(), new Object[0])
                     }
                  ),
                  class_2583.field_24360,
                  true
               )
         );
      CursorBox duplicateTooltip = new CursorBox(
         class_1074.method_4662("gui.xaero_category_duplicate", new Object[]{class_1074.method_4662(data.getDisplayName(), new Object[0])}),
         class_2583.field_24360,
         true
      );
      cutTooltip.setAutoLinebreak(false);
      if (pasteTooltip != null) {
         pasteTooltip.setAutoLinebreak(false);
      }

      duplicateTooltip.setAutoLinebreak(false);
      this.withSubEntry(
         (x, y, w, h, root) -> {
            CategorySettingsListEntryTextWithAction<?> result = new CategorySettingsListEntryTextWithAction<>(
               x, y, w, h, index, rowList, this, isCut ? () -> rowList.pasteTo(data) : data.getExpandAction(rowList), tooltipSupplier
            );
            if (isCut) {
               result.setColor(-5636096);
               result.setHoverColor(-43691);
            }

            return result;
         }
      );
      CategorySettingsListMainEntry.CenteredEntryFactory pasteEntryFactory = (x, y, w, h, root) -> new CategorySettingsListTextButtonEntry<>(
            x + 248, y + 2, index, rowList, "←", -5592406, -1, 5, data.getPasteAction(rowList), this, () -> pasteTooltip
         );
      if (!data.isExpanded() && data.isMovable()) {
         if (!data.getSettingsData().getProtection()) {
            this.withSubEntry(
               (x, y, w, h, root) -> new CategorySettingsListTextButtonEntry<>(
                     x + 230, y + 2, index, rowList, "+", -5592406, -1, 5, parent.getDuplicateAction(subIndex, rowList), this, () -> duplicateTooltip
                  )
            );
         }

         if (rowList.hasCut()) {
            this.withSubEntry(pasteEntryFactory);
         }

         if (data.getSettingsData().getProtection()) {
            this.withSubEntry(
               (x, y, w, h, root) -> new CategorySettingsListTextButtonEntry<>(
                     x - 24, y + 2, index, rowList, "!", -1644980, -171, 5, () -> false, this, () -> PROTECTED_TOOLTIP
                  )
            );
         } else {
            if (!rowList.hasCut()) {
               this.withSubEntry(
                  (x, y, w, h, root) -> new CategorySettingsListTextButtonEntry<>(
                        x + 248,
                        y + 2,
                        index,
                        rowList,
                        "↔",
                        -5592406,
                        -1,
                        5,
                        ((GuiCategoryUIEditorCategoryData<?, ?, GuiCategoryUIEditorCategoryData<?, ?, ?>>)data).getCutAction(parent, rowList),
                        this,
                        () -> cutTooltip
                     )
               );
            }

            if (parent.getSubCategories().size() > 1) {
               this.withSubEntry(
                  (x, y, w, h, root) -> new CategorySettingsListTextButtonEntry<>(
                        x - 40, y + 2, index, rowList, "↑", -5592406, -1, 5, parent.getMoveAction(subIndex, -1, rowList), this, () -> UP_TOOLTIP
                     )
               );
               this.withSubEntry(
                  (x, y, w, h, root) -> new CategorySettingsListTextButtonEntry<>(
                        x - 24, y + 2, index, rowList, "↓", -5592406, -1, 5, parent.getMoveAction(subIndex, 1, rowList), this, () -> DOWN_TOOLTIP
                     )
               );
            }
         }
      } else {
         if (rowList.hasCut()) {
            this.withSubEntry(pasteEntryFactory);
         }

         if (isFinalExpanded) {
            this.addHelpElement(() -> HELP_TOOLTIP);
         }
      }
   }

   @Override
   public String getMessage() {
      return "";
   }
}
