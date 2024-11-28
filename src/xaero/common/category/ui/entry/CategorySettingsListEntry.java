package xaero.common.category.ui.entry;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import net.minecraft.class_1109;
import net.minecraft.class_310;
import net.minecraft.class_327;
import net.minecraft.class_332;
import net.minecraft.class_3417;
import net.minecraft.class_4587;
import xaero.common.category.ui.GuiCategorySettings;
import xaero.common.graphics.CursorBox;

public abstract class CategorySettingsListEntry {
   protected final int entryRelativeX;
   protected final int entryRelativeY;
   protected final int entryW;
   protected final int entryH;
   protected final int index;
   protected final GuiCategorySettings<?, ?, ?, ?, ?, ?>.SettingRowList rowList;
   protected final List<CategorySettingsListEntry> subEntries;
   protected final Supplier<CursorBox> tooltipSupplier;
   protected int focusedSubEntryIndex;
   protected CategorySettingsListEntry hoveredSubEntry;

   public CategorySettingsListEntry(
      int entryX,
      int entryY,
      int entryW,
      int entryH,
      int index,
      GuiCategorySettings<?, ?, ?, ?, ?, ?>.SettingRowList rowList,
      Supplier<CursorBox> tooltipSupplier
   ) {
      this.entryRelativeX = entryX;
      this.entryRelativeY = entryY;
      this.entryW = entryW;
      this.entryH = entryH;
      this.index = index;
      this.rowList = rowList;
      this.subEntries = new ArrayList<>();
      this.focusedSubEntryIndex = -1;
      this.tooltipSupplier = tooltipSupplier;
   }

   public CategorySettingsListEntry onSelected() {
      if (!this.subEntries.isEmpty() && this.focusedSubEntryIndex >= 0) {
         CategorySettingsListEntry subEntry = this.subEntries.get(this.focusedSubEntryIndex);
         return subEntry.onSelected();
      } else {
         if (this.selectAction()) {
            if (!(this instanceof CategorySettingsListEntryWidget)) {
               class_310.method_1551().method_1483().method_4873(class_1109.method_47978(class_3417.field_15015, 1.0F));
            }

            this.rowList.updateEntries();
         }

         return this;
      }
   }

   public boolean mouseClicked(GuiCategorySettings<?, ?, ?, ?, ?, ?>.SettingRowList.Entry entry, double relativeMouseX, double relativeMouseY, int i) {
      for (int subIndex = 0; subIndex < this.subEntries.size(); subIndex++) {
         CategorySettingsListEntry subEntry = this.subEntries.get(subIndex);
         double subRelativeMouseX = relativeMouseX - (double)subEntry.entryRelativeX;
         double subRelativeMouseY = relativeMouseY - (double)subEntry.entryRelativeY;
         if (subEntry.isHoveredOver(relativeMouseX, relativeMouseY)) {
            if (this.focusedSubEntryIndex != subIndex) {
               this.unfocusRecursively();
               this.focusedSubEntryIndex = subIndex;
            }

            if (!subEntry.mouseClicked(entry, subRelativeMouseX, subRelativeMouseY, subIndex)) {
               subEntry.confirmSelection();
            }

            return true;
         }
      }

      return false;
   }

   public CategorySettingsListEntry confirmSelection() {
      return this.focusedSubEntryIndex >= 0 ? this.subEntries.get(this.focusedSubEntryIndex).confirmSelection() : this.onSelected();
   }

   public boolean mouseReleased(double relativeMouseX, double relativeMouseY, int i) {
      for (CategorySettingsListEntry subEntry : this.subEntries) {
         double subRelativeMouseX = relativeMouseX - (double)subEntry.entryRelativeX;
         double subRelativeMouseY = relativeMouseY - (double)subEntry.entryRelativeY;
         subEntry.mouseReleased(subRelativeMouseX, subRelativeMouseY, i);
      }

      return false;
   }

   public boolean mouseScrolled(double relativeMouseX, double relativeMouseY, double f) {
      for (CategorySettingsListEntry subEntry : this.subEntries) {
         double subRelativeMouseX = relativeMouseX - (double)subEntry.entryRelativeX;
         double subRelativeMouseY = relativeMouseY - (double)subEntry.entryRelativeY;
         if (subEntry.isHoveredOver(relativeMouseX, relativeMouseY)) {
            return subEntry.mouseScrolled(subRelativeMouseX, subRelativeMouseY, f);
         }
      }

      return false;
   }

   public boolean mouseDragged(double relativeMouseX, double relativeMouseY, int i, double f, double g) {
      for (CategorySettingsListEntry subEntry : this.subEntries) {
         double subRelativeMouseX = relativeMouseX - (double)subEntry.entryRelativeX;
         double subRelativeMouseY = relativeMouseY - (double)subEntry.entryRelativeY;
         subEntry.mouseDragged(subRelativeMouseX, subRelativeMouseY, i, f, g);
      }

      return false;
   }

   public void mouseMoved(double relativeMouseX, double relativeMouseY) {
   }

   public boolean keyPressed(int i, int j, int k, boolean isRoot) {
      if (!isRoot || (i != 263 || !this.moveFocus(-1)) && (i != 262 || !this.moveFocus(1))) {
         if (!this.subEntries.isEmpty() && this.focusedSubEntryIndex >= 0) {
            CategorySettingsListEntry subEntry = this.subEntries.get(this.focusedSubEntryIndex);
            return subEntry.keyPressed(i, j, k, false);
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   public boolean keyReleased(int i, int j, int k) {
      if (!this.subEntries.isEmpty()) {
         for (CategorySettingsListEntry subEntry : this.subEntries) {
            subEntry.keyReleased(i, j, k);
         }
      }

      return false;
   }

   public boolean charTyped(char c, int i) {
      if (!this.subEntries.isEmpty() && this.focusedSubEntryIndex >= 0) {
         CategorySettingsListEntry subEntry = this.subEntries.get(this.focusedSubEntryIndex);
         return subEntry.charTyped(c, i);
      } else {
         return false;
      }
   }

   public void tick() {
      if (!this.subEntries.isEmpty()) {
         for (CategorySettingsListEntry subEntry : this.subEntries) {
            subEntry.tick();
         }
      }
   }

   public String getSubNarration() {
      return this.hoveredSubEntry == null ? this.getSelectedNarration() : this.getHoveredNarration();
   }

   public String getHoveredNarration() {
      return this.hoveredSubEntry == null ? this.getHoverNarration() : this.hoveredSubEntry.getHoveredNarration();
   }

   public String getSelectedNarration() {
      if (this.focusedSubEntryIndex == -1) {
         return this.getNarration();
      } else {
         CategorySettingsListEntry subEntry = this.subEntries.get(this.focusedSubEntryIndex);
         return subEntry.getSelectedNarration();
      }
   }

   public Supplier<CursorBox> getTooltipSupplier() {
      return this.tooltipSupplier;
   }

   public abstract String getMessage();

   public String getNarrationMessage() {
      return this.getMessage();
   }

   public String getNarration() {
      StringBuilder narrationBuilder = new StringBuilder();
      narrationBuilder.append(this.getNarrationMessage());
      if (this.tooltipSupplier != null) {
         CursorBox tooltip = this.tooltipSupplier.get();
         if (tooltip != null) {
            narrationBuilder.append(" . ").append(this.tooltipSupplier.get().getPlainText());
         }
      }

      return narrationBuilder.toString();
   }

   public String getHoverNarration() {
      return this.getNarration();
   }

   public void preRender(class_332 guiGraphics, boolean includesSelected, boolean isRoot) {
      class_4587 poseStack = guiGraphics.method_51448();
      poseStack.method_22903();
      poseStack.method_46416((float)this.entryRelativeX, (float)this.entryRelativeY, 0.0F);
      if (includesSelected && this.focusedSubEntryIndex == -1) {
         guiGraphics.method_25294(0, 0, this.entryW, this.entryH, this.rowList.method_25370() ? -1 : -8355712);
         guiGraphics.method_25294(1, 1, this.entryW - 1, this.entryH - 1, -16777216);
      }
   }

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
      this.hoveredSubEntry = null;
      CategorySettingsListEntry result = isMouseOver ? this : null;

      for (int i = 0; i < this.subEntries.size(); i++) {
         CategorySettingsListEntry subEntry = this.subEntries.get(i);
         boolean subIsHovered = subEntry.isHoveredOver((double)relativeMouseX, (double)relativeMouseY);
         boolean subIncludesSelected = includesSelected && this.focusedSubEntryIndex == i;
         subEntry.preRender(guiGraphics, subIncludesSelected, false);
         CategorySettingsListEntry subResult = subEntry.render(
            guiGraphics,
            index,
            rowWidth,
            rowHeight,
            relativeMouseX - subEntry.entryRelativeX,
            relativeMouseY - subEntry.entryRelativeY,
            subIsHovered,
            partialTicks,
            font,
            globalMouseX,
            globalMouseY,
            subIncludesSelected,
            false
         );
         subEntry.postRender(guiGraphics);
         if (subIsHovered) {
            this.hoveredSubEntry = subEntry;
            result = subResult;
         }
      }

      return result;
   }

   public void postRender(class_332 guiGraphics) {
      class_4587 poseStack = guiGraphics.method_51448();
      poseStack.method_22909();
   }

   public boolean isHoveredOver(double relativeMouseX, double relativeMouseY) {
      return relativeMouseX >= (double)this.entryRelativeX
         && relativeMouseX < (double)(this.entryRelativeX + this.entryW)
         && relativeMouseY >= (double)this.entryRelativeY
         && relativeMouseY < (double)(this.entryRelativeY + this.entryH);
   }

   protected abstract boolean selectAction();

   public void setFocused(boolean bl) {
   }

   public void unhoverRecursively() {
      if (this.hoveredSubEntry != null) {
         this.hoveredSubEntry.unhoverRecursively();
         this.hoveredSubEntry = null;
      }
   }

   public boolean moveFocus(int direction) {
      this.unhoverRecursively();
      if (this.moveFocus(direction, true)) {
         this.rowList.narrateSelection();
         return true;
      } else {
         return false;
      }
   }

   public boolean moveFocus(int direction, boolean isRoot) {
      if (!this.subEntries.isEmpty()) {
         CategorySettingsListEntry focusedSub = null;
         boolean shouldSwitchLocal = true;
         if (this.focusedSubEntryIndex >= 0) {
            focusedSub = this.subEntries.get(this.focusedSubEntryIndex);
            shouldSwitchLocal = !focusedSub.moveFocus(direction, false);
         }

         if (shouldSwitchLocal) {
            int potentialValue = this.focusedSubEntryIndex + direction;
            if (potentialValue < 0 || potentialValue >= this.subEntries.size()) {
               if (!isRoot) {
                  return false;
               }

               if (potentialValue < 0) {
                  potentialValue = this.subEntries.size() - 1;
               } else {
                  potentialValue = 0;
               }
            }

            if (this.focusedSubEntryIndex == potentialValue) {
               return false;
            }

            this.focusedSubEntryIndex = potentialValue;
            focusedSub = this.subEntries.get(this.focusedSubEntryIndex);
            if (direction < 0) {
               focusedSub.focusLastRecursively();
            } else {
               focusedSub.focusFirstRecursively();
            }
         }

         return true;
      } else {
         return false;
      }
   }

   public void unfocusRecursively() {
      this.setFocused(false);
      if (!this.subEntries.isEmpty()) {
         if (this.focusedSubEntryIndex >= 0) {
            this.subEntries.get(this.focusedSubEntryIndex).unfocusRecursively();
         }

         this.focusedSubEntryIndex = -1;
      }
   }

   public void focusFirstRecursively() {
      this.setFocused(true);
      if (!this.subEntries.isEmpty()) {
         this.focusedSubEntryIndex = 0;
         this.subEntries.get(this.focusedSubEntryIndex).focusFirstRecursively();
      }
   }

   public void focusLastRecursively() {
      this.setFocused(true);
      if (!this.subEntries.isEmpty()) {
         this.focusedSubEntryIndex = this.subEntries.size() - 1;
         this.subEntries.get(this.focusedSubEntryIndex).focusLastRecursively();
      }
   }

   public CategorySettingsListEntry withSubEntry(CategorySettingsListEntry entry) {
      this.subEntries.add(entry);
      return this;
   }

   public int getEntryRelativeX() {
      return this.entryRelativeX;
   }

   public int getEntryRelativeY() {
      return this.entryRelativeY;
   }
}
