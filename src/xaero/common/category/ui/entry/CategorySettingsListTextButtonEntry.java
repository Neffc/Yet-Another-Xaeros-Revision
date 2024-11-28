package xaero.common.category.ui.entry;

import java.util.function.Supplier;
import net.minecraft.class_310;
import net.minecraft.class_327;
import net.minecraft.class_332;
import xaero.common.category.ui.GuiCategorySettings;
import xaero.common.category.ui.data.GuiCategoryUIEditorExpandableData;
import xaero.common.graphics.CursorBox;

public class CategorySettingsListTextButtonEntry<D extends GuiCategoryUIEditorExpandableData<?>> extends CategorySettingsListEntryWithRootReference<D> {
   private final String text;
   private final int color;
   private final int hoverColor;
   private final int frameSize;
   private final Supplier<Boolean> action;

   public CategorySettingsListTextButtonEntry(
      int entryX,
      int entryY,
      int index,
      GuiCategorySettings<?, ?, ?, ?, ?, ?>.SettingRowList rowList,
      String text,
      int color,
      int hoverColor,
      int frameSize,
      Supplier<Boolean> action,
      CategorySettingsListMainEntry<D> root,
      Supplier<CursorBox> tooltipSupplier
   ) {
      super(
         entryX - class_310.method_1551().field_1772.method_1727(text) / 2 - frameSize,
         entryY,
         class_310.method_1551().field_1772.method_1727(text) + frameSize * 2,
         9 + frameSize * 2,
         index,
         rowList,
         root,
         tooltipSupplier
      );
      this.text = text;
      this.color = color;
      this.hoverColor = hoverColor;
      this.frameSize = frameSize;
      this.action = action;
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
      guiGraphics.method_25300(
         font,
         this.text,
         this.frameSize + class_310.method_1551().field_1772.method_1727(this.text) / 2,
         this.frameSize + 1,
         isMouseOver ? this.hoverColor : this.color
      );
      return result;
   }

   @Override
   protected boolean selectAction() {
      return this.action.get();
   }

   @Override
   public String getMessage() {
      return this.text;
   }
}
