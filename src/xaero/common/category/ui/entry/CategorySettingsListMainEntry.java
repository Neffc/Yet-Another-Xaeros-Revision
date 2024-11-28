package xaero.common.category.ui.entry;

import java.util.function.Supplier;
import net.minecraft.class_327;
import net.minecraft.class_332;
import xaero.common.category.ui.GuiCategorySettings;
import xaero.common.category.ui.data.GuiCategoryUIEditorExpandableData;
import xaero.common.graphics.CursorBox;

public abstract class CategorySettingsListMainEntry<D extends GuiCategoryUIEditorExpandableData<?>> extends CategorySettingsListEntry {
   private final ConnectionLineType lineType;
   protected final GuiCategoryUIEditorExpandableData<D> data;

   public CategorySettingsListMainEntry(
      int screenWidth,
      int index,
      GuiCategorySettings<?, ?, ?, ?, ?, ?>.SettingRowList rowList,
      ConnectionLineType lineType,
      GuiCategoryUIEditorExpandableData<D> data
   ) {
      super(0, 0, screenWidth, 24, index, rowList, () -> null);
      this.lineType = lineType;
      this.data = data;
   }

   protected void addHelpElement(Supplier<CursorBox> helpTooltipSupplier) {
      if (helpTooltipSupplier != null) {
         this.withSubEntry(
            (x, y, w, h, root) -> new CategorySettingsListTextButtonEntry<>(
                  x - 24, y + 2, this.index, this.rowList, "?", -5592406, -1, 5, () -> false, this, helpTooltipSupplier
               )
         );
      }
   }

   @Override
   public CategorySettingsListEntry render(
      class_332 guiGraphics,
      int index,
      int rowWidth,
      int rowHeight,
      int relativeMouseX,
      int relativeMouseY,
      boolean isMouseOver,
      float partialTicks,
      class_327 font,
      int globalMouseX,
      int globalMouseY,
      boolean includesSelected,
      boolean isRoot
   ) {
      CategorySettingsListEntry result = super.render(
         guiGraphics,
         index,
         rowWidth,
         rowHeight,
         relativeMouseX,
         relativeMouseY,
         isMouseOver,
         partialTicks,
         font,
         globalMouseX,
         globalMouseY,
         includesSelected,
         isRoot
      );
      int xOffset = rowWidth / 2 - 110;
      int yOffset = 8;
      if (this.lineType == ConnectionLineType.TAIL_LEAF || this.lineType == ConnectionLineType.HEAD_LEAF) {
         int leftX = xOffset - 14;
         int rightX = xOffset - 2;
         int bottomY = yOffset + 4;
         int topY = yOffset - 24 + 4;
         guiGraphics.method_25292(leftX, rightX, bottomY, -5592406);
         guiGraphics.method_25301(leftX, topY, bottomY, -5592406);
         guiGraphics.method_25301(rightX - 1, bottomY - 2, bottomY + 2, -5592406);
         guiGraphics.method_25301(rightX - 2, bottomY - 3, bottomY + 3, -5592406);
         if (this.lineType == ConnectionLineType.HEAD_LEAF) {
            guiGraphics.method_25292(leftX, rightX, topY, -5592406);
         }
      } else if (this.lineType == ConnectionLineType.PATH) {
         int topY = yOffset - 24 + 9;
         int bottomY = yOffset - 2;
         int lineX = xOffset + 12;
         guiGraphics.method_25292(lineX - 2, lineX + 2, bottomY - 3, -5592406);
         guiGraphics.method_25292(lineX - 1, lineX + 1, bottomY - 2, -5592406);
         guiGraphics.method_25301(lineX, topY, bottomY, -5592406);
      }

      return result;
   }

   public CategorySettingsListMainEntry<D> withSubEntry(CategorySettingsListMainEntry.CenteredEntryFactory entryFactory) {
      super.withSubEntry(entryFactory.get(this.rowList.method_25322() / 2 - 110 - 1, 0, 220, 24, this));
      return this;
   }

   @Override
   protected boolean selectAction() {
      return false;
   }

   @FunctionalInterface
   public interface CenteredEntryFactory {
      CategorySettingsListEntry get(int var1, int var2, int var3, int var4, CategorySettingsListMainEntry<?> var5);
   }
}
