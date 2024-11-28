package xaero.common.category.ui.entry;

import java.util.function.Supplier;
import net.minecraft.class_1074;
import net.minecraft.class_327;
import net.minecraft.class_332;
import xaero.common.category.ui.GuiCategorySettings;
import xaero.common.category.ui.data.GuiCategoryUIEditorExpandableData;
import xaero.common.graphics.CursorBox;

public class CategorySettingsListEntryWithIconAndText<D extends GuiCategoryUIEditorExpandableData<?>> extends CategorySettingsListEntryWithIcon<D> {
   protected String text;
   protected int color;
   protected int hoverColor;

   public CategorySettingsListEntryWithIconAndText(
      int entryX,
      int entryY,
      int entryW,
      int entryH,
      int index,
      GuiCategorySettings<?, ?, ?, ?, ?, ?>.SettingRowList rowList,
      String text,
      CategorySettingsListMainEntry<D> root,
      Supplier<CursorBox> tooltipSupplier
   ) {
      super(entryX, entryY, entryW, entryH, index, rowList, 0, 0, 0, 0, root, tooltipSupplier);
      this.text = class_1074.method_4662(text, new Object[0]);
      this.color = -5592406;
      this.hoverColor = -1;
   }

   public void setColor(int color) {
      this.color = color;
   }

   public void setHoverColor(int hoverColor) {
      this.hoverColor = hoverColor;
   }

   public int getColor() {
      return this.color;
   }

   public int getHoverColor() {
      return this.hoverColor;
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
      guiGraphics.method_25303(font, this.text, 4, 8, isMouseOver ? this.getHoverColor() : this.getColor());
      return result;
   }

   @Override
   protected boolean selectAction() {
      return false;
   }

   @Override
   public String getMessage() {
      return this.text;
   }
}
