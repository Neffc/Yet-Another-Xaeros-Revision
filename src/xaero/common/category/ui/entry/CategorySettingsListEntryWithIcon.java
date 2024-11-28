package xaero.common.category.ui.entry;

import java.util.function.Supplier;
import net.minecraft.class_327;
import net.minecraft.class_332;
import xaero.common.category.ui.GuiCategorySettings;
import xaero.common.category.ui.data.GuiCategoryUIEditorExpandableData;
import xaero.common.graphics.CursorBox;
import xaero.hud.render.TextureLocations;

public class CategorySettingsListEntryWithIcon<D extends GuiCategoryUIEditorExpandableData<?>> extends CategorySettingsListEntryWithRootReference<D> {
   private final int iconU;
   private final int iconV;
   private final int iconW;
   private final int iconH;

   public CategorySettingsListEntryWithIcon(
      int entryX,
      int entryY,
      int entryW,
      int entryH,
      int index,
      GuiCategorySettings<?, ?, ?, ?, ?, ?>.SettingRowList rowList,
      int iconU,
      int iconV,
      int iconW,
      int iconH,
      CategorySettingsListMainEntry<D> root,
      Supplier<CursorBox> tooltipSupplier
   ) {
      super(entryX, entryY, entryW, entryH, index, rowList, root, tooltipSupplier);
      this.iconU = iconU;
      this.iconV = iconV;
      this.iconW = iconW;
      this.iconH = iconH;
   }

   public int getIconX() {
      return this.iconU;
   }

   public int getIconY() {
      return this.iconV;
   }

   public int getIconW() {
      return this.iconW;
   }

   public int getIconH() {
      return this.iconH;
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
      guiGraphics.method_25302(TextureLocations.GUI_TEXTURES, 0, 0, this.iconU, this.iconV, this.iconW, this.iconH);
      return result;
   }

   @Override
   protected boolean selectAction() {
      return false;
   }

   @Override
   public String getMessage() {
      return "unnamed";
   }
}
