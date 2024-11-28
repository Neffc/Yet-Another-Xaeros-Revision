package xaero.common.gui.dropdown;

import javax.annotation.Nonnull;
import net.minecraft.class_1074;
import net.minecraft.class_1109;
import net.minecraft.class_2561;
import net.minecraft.class_310;
import net.minecraft.class_332;
import net.minecraft.class_339;
import net.minecraft.class_3417;
import net.minecraft.class_437;
import net.minecraft.class_5250;
import net.minecraft.class_6381;
import net.minecraft.class_6382;

public class DropDownWidget extends class_339 {
   public static final int DEFAULT_BACKGROUND = -939524096;
   public static final int SELECTED_DEFAULT_BACKGROUND = -922757376;
   public static final int SELECTED_DEFAULT_HOVERED_BACKGROUND = -10496;
   public static final int TRIM = -6250336;
   public static final int TRIM_OPEN = -1;
   public static final int TRIM_INSIDE = -13487566;
   public static final int LINE_HEIGHT = 11;
   private int xOffset = 0;
   private int yOffset = 0;
   private String[] realOptions = new String[0];
   private String[] options = new String[0];
   private int selected = 0;
   private boolean closed = true;
   private int scroll;
   private long scrollTime;
   private int autoScrolling;
   protected boolean openingUp;
   private final IDropDownWidgetCallback callback;
   private final IDropDownContainer container;
   private final boolean hasEmptyOption;
   protected int selectedBackground;
   protected int selectedHoveredBackground;
   protected boolean shortenFromTheRight;
   private boolean wasHovered;

   protected DropDownWidget(
      String[] options,
      int x,
      int y,
      int w,
      Integer selected,
      boolean openingUp,
      IDropDownWidgetCallback callback,
      IDropDownContainer container,
      boolean hasEmptyOption,
      class_2561 narrationTitle
   ) {
      super(x, y + (openingUp ? 11 : 0), w, 11, narrationTitle);
      this.realOptions = options;
      this.callback = callback;
      this.container = container;
      int emptyOptionCount = hasEmptyOption ? 1 : 0;
      this.options = new String[this.realOptions.length + emptyOptionCount];
      System.arraycopy(this.realOptions, 0, this.options, emptyOptionCount, this.realOptions.length);
      this.selectId(selected, false);
      this.openingUp = openingUp;
      this.hasEmptyOption = hasEmptyOption;
      this.selectedBackground = -922757376;
      this.selectedHoveredBackground = -10496;
      this.field_22763 = true;
   }

   public int size() {
      return this.realOptions.length;
   }

   public int getXWithOffset() {
      return this.method_46426() + this.xOffset;
   }

   public int getYWithOffset() {
      return this.method_46427() + this.yOffset;
   }

   private void drawSlot(
      class_332 guiGraphics, String text, int slotIndex, int pos, int mouseX, int mouseY, boolean scrolling, int optionLimit, int xWithOffset, int yWithOffset
   ) {
      int emptyOptionCount = this.hasEmptyOption ? 1 : 0;
      int slotBackground;
      if (this.closed && this.method_25367() || !this.closed && this.onDropDownSlot(mouseX, mouseY, slotIndex, scrolling, optionLimit)) {
         slotBackground = slotIndex - emptyOptionCount == this.selected ? this.selectedHoveredBackground : -13487566;
      } else {
         slotBackground = slotIndex - emptyOptionCount == this.selected ? this.selectedBackground : -939524096;
      }

      if (this.openingUp) {
         pos = -pos - 1;
      }

      guiGraphics.method_25294(xWithOffset, yWithOffset + 11 * pos, xWithOffset + this.field_22758, yWithOffset + 11 + 11 * pos, slotBackground);
      guiGraphics.method_25292(xWithOffset + 1, xWithOffset + this.field_22758 - 1, yWithOffset + 11 * pos, -13487566);
      int textWidth = class_310.method_1551().field_1772.method_1727(text);

      boolean shortened;
      for (shortened = false; textWidth > this.field_22758 - 2; shortened = true) {
         text = this.shortenFromTheRight ? text.substring(0, text.length() - 1) : text.substring(1);
         textWidth = class_310.method_1551().field_1772.method_1727("..." + text);
      }

      if (shortened) {
         if (this.shortenFromTheRight) {
            text = text + "...";
         } else {
            text = "..." + text;
         }
      }

      int textColor = 16777215;
      int var10003 = xWithOffset + 1 + this.field_22758 / 2;
      guiGraphics.method_25300(class_310.method_1551().field_1772, text, var10003, yWithOffset + 2 + 11 * pos, textColor);
   }

   private void drawMenu(class_332 guiGraphics, int amount, int mouseX, int mouseY, int scaledHeight, int optionLimit) {
      boolean scrolling = this.scrolling(optionLimit);
      int totalH = 11 * (amount + (scrolling ? 2 : 0));
      if (!this.openingUp && this.method_46427() + totalH + 1 > scaledHeight) {
         this.yOffset = scaledHeight - this.method_46427() - totalH - 1;
      } else if (this.openingUp && this.method_46427() - totalH < 0) {
         this.yOffset = totalH - this.method_46427();
      } else {
         this.yOffset = 0;
      }

      int xWithOffset = this.getXWithOffset();
      int yWithOffset = this.getYWithOffset();
      int first = this.closed ? 0 : this.scroll;
      if (scrolling) {
         this.drawSlot(
            guiGraphics,
            (this.scroll == 0 ? "§8" : "§7") + class_1074.method_4662(this.openingUp ? "gui.xaero_down" : "gui.xaero_up", new Object[0]),
            -1,
            0,
            mouseX,
            mouseY,
            scrolling,
            optionLimit,
            xWithOffset,
            yWithOffset
         );
         this.drawSlot(
            guiGraphics,
            (this.scroll + optionLimit >= this.options.length ? "§8" : "§7")
               + class_1074.method_4662(this.openingUp ? "gui.xaero_up" : "gui.xaero_down", new Object[0]),
            -2,
            amount + 1,
            mouseX,
            mouseY,
            scrolling,
            optionLimit,
            xWithOffset,
            yWithOffset
         );
      }

      for (int i = first; i < first + amount; i++) {
         String slotText;
         if (this.hasEmptyOption && i == 0) {
            slotText = !this.closed ? "-" : class_1074.method_4662(this.realOptions[this.selected], new Object[0]).replace("§§", ":");
         } else {
            slotText = class_1074.method_4662(this.options[i], new Object[0]).replace("§§", ":");
         }

         this.drawSlot(guiGraphics, slotText, i, i - first + (scrolling ? 1 : 0), mouseX, mouseY, scrolling, optionLimit, xWithOffset, yWithOffset);
      }

      int trimPosY = yWithOffset - (this.openingUp ? totalH : 0);
      int trim = this.closed ? -6250336 : -1;
      guiGraphics.method_25301(xWithOffset, trimPosY, trimPosY + totalH, trim);
      guiGraphics.method_25301(xWithOffset + this.field_22758, trimPosY, trimPosY + totalH, trim);
      guiGraphics.method_25292(xWithOffset, xWithOffset + this.field_22758, trimPosY, trim);
      guiGraphics.method_25292(xWithOffset, xWithOffset + this.field_22758, trimPosY + totalH, trim);
   }

   private boolean scrolling(int optionLimit) {
      return this.options.length > optionLimit && !this.closed;
   }

   public boolean mouseClicked(int mouseX, int mouseY, int mouseButton, int scaledHeight) {
      if (mouseButton != 0) {
         return false;
      } else {
         if (!this.closed) {
            int optionLimit = this.optionLimit(scaledHeight);
            int clickedId = this.getHoveredId(mouseX, mouseY, this.scrolling(optionLimit), optionLimit);
            if (clickedId >= 0) {
               this.selectId(clickedId - (this.hasEmptyOption ? 1 : 0), true);
            } else {
               this.autoScrolling = clickedId == -1 ? 1 : -1;
               this.scrollTime = System.currentTimeMillis();
               this.mouseScrolledInternal(this.autoScrolling, mouseX, mouseY, optionLimit);
            }

            class_310.method_1551().method_1483().method_4873(class_1109.method_47978(class_3417.field_15015, 1.0F));
         } else if (this.options.length > 1 && this.field_22763) {
            this.setClosed(false);
            this.scroll = 0;
         }

         return true;
      }
   }

   public void mouseReleased(int mouseX, int mouseY, int mouseButton, int scaledHeight) {
      this.autoScrolling = 0;
   }

   private int getHoveredId(int mouseX, int mouseY, boolean scrolling, int optionLimit) {
      int yOnMenu = mouseY - this.getYWithOffset();
      int visibleSlotIndex = (this.openingUp ? -yOnMenu - 1 : yOnMenu) / 11;
      if (scrolling && visibleSlotIndex == 0) {
         return -1;
      } else if (visibleSlotIndex >= optionLimit + (scrolling ? 1 : 0)) {
         return -2;
      } else {
         int slot = this.scroll + visibleSlotIndex - (scrolling ? 1 : 0);
         if (slot >= this.options.length) {
            slot = this.options.length - 1;
         }

         return slot;
      }
   }

   public boolean onDropDown(int mouseX, int mouseY, int scaledHeight) {
      int optionLimit = this.optionLimit(scaledHeight);
      return this.onDropDown(mouseX, mouseY, this.scrolling(optionLimit), optionLimit);
   }

   public boolean onDropDown(int mouseX, int mouseY, boolean scrolling, int optionLimit) {
      int menuTop = this.getYWithOffset();
      int menuHeight = this.closed ? 11 : (Math.min(this.options.length, optionLimit) + (scrolling ? 2 : 0)) * 11;
      if (this.openingUp) {
         menuTop -= menuHeight;
      }

      int xOnMenu = mouseX - this.getXWithOffset();
      int yOnMenu = mouseY - menuTop;
      return xOnMenu >= 0 && yOnMenu >= 0 && xOnMenu <= this.field_22758 && yOnMenu < menuHeight;
   }

   private boolean onDropDownSlot(int mouseX, int mouseY, int id, boolean scrolling, int optionLimit) {
      if (!this.onDropDown(mouseX, mouseY, scrolling, optionLimit)) {
         return false;
      } else {
         int hoveredSlot = this.getHoveredId(mouseX, mouseY, scrolling, optionLimit);
         return hoveredSlot == id;
      }
   }

   public void selectId(int id, boolean callCallback) {
      if (id == -1) {
         this.setClosed(true);
      } else {
         if (id < 0 || id >= this.realOptions.length) {
            id = 0;
         }

         boolean newId = id != this.selected;
         if (newId && (!callCallback || this.callback.onSelected(this, id))) {
            this.selected = id;
         }

         this.setClosed(true);
      }
   }

   public void method_25394(@Nonnull class_332 guiGraphics, int mouseX, int mouseY, float partial) {
      int scaledHeight = class_310.method_1551().field_1755.field_22790;
      this.field_22762 = this.field_22764 && this.onDropDown(mouseX, mouseY, scaledHeight);
      if (this.field_22764) {
         this.render(guiGraphics, mouseX, mouseY, class_310.method_1551().field_1755.field_22790, true);
      }
   }

   public void method_48579(class_332 guiGraphics, int var2, int var3, float var4) {
   }

   public void render(class_332 guiGraphics, int mouseX, int mouseY, int scaledHeight, boolean closedOnly) {
      if (this.closed || !closedOnly) {
         int optionLimit = this.optionLimit(scaledHeight);
         if (this.autoScrolling != 0 && System.currentTimeMillis() - this.scrollTime > 100L) {
            this.scrollTime = System.currentTimeMillis();
            this.mouseScrolledInternal(this.autoScrolling, mouseX, mouseY, optionLimit);
         }

         this.drawMenu(guiGraphics, this.closed ? 1 : Math.min(optionLimit, this.options.length), mouseX, mouseY, scaledHeight, optionLimit);
      }
   }

   public boolean isClosed() {
      return this.closed;
   }

   public void setClosed(boolean closed) {
      if (this.closed != closed) {
         if (!closed) {
            class_310.method_1551().method_1483().method_4873(class_1109.method_47978(class_3417.field_15015, 1.0F));
            this.container.onDropdownOpen(this);
         } else {
            this.container.onDropdownClosed(this);
         }
      }

      this.closed = closed;
   }

   public void mouseScrolled(int wheel, int mouseXScaled, int mouseYScaled, int scaledHeight) {
      this.mouseScrolledInternal(wheel * (this.openingUp ? -1 : 1), mouseXScaled, mouseYScaled, this.optionLimit(scaledHeight));
   }

   private void mouseScrolledInternal(int wheel, int mouseXScaled, int mouseYScaled, int optionLimit) {
      int newScroll = this.scroll - wheel;
      if (newScroll + optionLimit > this.options.length) {
         newScroll = this.options.length - optionLimit;
      }

      if (newScroll < 0) {
         newScroll = 0;
      }

      this.scroll = newScroll;
   }

   private int optionLimit(int scaledHeight) {
      return Math.max(1, scaledHeight / 11 - 2);
   }

   public int getSelected() {
      return this.selected;
   }

   public void method_47399(class_6382 narrationElementOutput) {
      if (this.method_25369() != null) {
         narrationElementOutput.method_37034(class_6381.field_33788, this.method_25360());
      }
   }

   protected class_5250 method_25360() {
      class_5250 narrationMessage = class_2561.method_43471("");
      narrationMessage.method_10855().add(this.method_25369());
      narrationMessage.method_10855().add(class_2561.method_43470(". "));
      narrationMessage.method_10855()
         .add(class_2561.method_43469("gui.xaero_dropdown_selected_narration", new Object[]{this.realOptions[this.selected].replaceAll("(§[0-9a-gr])+", "")}));
      return narrationMessage;
   }

   public void method_16014(double $$0, double $$1) {
      super.method_16014($$0, $$1);
   }

   public boolean method_25401(double mouseXScaled, double mouseYScaled, double wheel) {
      if (!this.isClosed()) {
         this.mouseScrolled((int)wheel, (int)mouseXScaled, (int)mouseYScaled, class_310.method_1551().field_1755.field_22790);
         return true;
      } else {
         return super.method_25401(mouseXScaled, mouseYScaled, wheel);
      }
   }

   public boolean method_25404(int $$0, int $$1, int $$2) {
      if ($$0 != 257 && $$0 != 32) {
         return super.method_25404($$0, $$1, $$2);
      } else {
         int nextSelection;
         if (class_437.method_25442()) {
            nextSelection = this.getSelected() - 1;
            if (nextSelection < 0) {
               nextSelection = this.realOptions.length - 1;
            }
         } else {
            nextSelection = (this.getSelected() + 1) % this.realOptions.length;
         }

         this.selectId(nextSelection, true);
         return true;
      }
   }

   public boolean method_16803(int $$0, int $$1, int $$2) {
      return super.method_16803($$0, $$1, $$2);
   }

   public boolean method_25400(char $$0, int $$1) {
      return super.method_25400($$0, $$1);
   }

   public boolean method_25402(double mouseX, double mouseY, int button) {
      if (this.field_22762) {
         int scaledHeight = class_310.method_1551().field_1755.field_22790;
         return this.mouseClicked((int)mouseX, (int)mouseY, button, scaledHeight);
      } else {
         return super.method_25402(mouseX, mouseY, button);
      }
   }

   public boolean method_25406(double mouseX, double mouseY, int button) {
      int scaledHeight = class_310.method_1551().field_1755.field_22790;
      this.mouseReleased((int)mouseX, (int)mouseY, button, scaledHeight);
      return false;
   }

   public void setActive(boolean b) {
      this.field_22763 = b;
   }

   public static final class Builder {
      private String[] options;
      private int x;
      private int y;
      private int w;
      private Integer selected;
      private boolean openingUp;
      private IDropDownWidgetCallback callback;
      private IDropDownContainer container;
      private boolean hasEmptyOption;
      private class_2561 narrationTitle;

      private Builder() {
      }

      public DropDownWidget.Builder setDefault() {
         this.setOptions(null);
         this.setX(0);
         this.setY(0);
         this.setW(0);
         this.setSelected(null);
         this.setOpeningUp(false);
         this.setCallback(null);
         this.setHasEmptyOption(true);
         this.setNarrationTitle(null);
         return this;
      }

      public DropDownWidget.Builder setOptions(String[] options) {
         this.options = options;
         return this;
      }

      public DropDownWidget.Builder setX(int x) {
         this.x = x;
         return this;
      }

      public DropDownWidget.Builder setY(int y) {
         this.y = y;
         return this;
      }

      public DropDownWidget.Builder setW(int w) {
         this.w = w;
         return this;
      }

      public DropDownWidget.Builder setSelected(Integer selected) {
         this.selected = selected;
         return this;
      }

      public DropDownWidget.Builder setOpeningUp(boolean openingUp) {
         this.openingUp = openingUp;
         return this;
      }

      public DropDownWidget.Builder setCallback(IDropDownWidgetCallback callback) {
         this.callback = callback;
         return this;
      }

      public DropDownWidget.Builder setContainer(IDropDownContainer container) {
         this.container = container;
         return this;
      }

      public DropDownWidget.Builder setHasEmptyOption(boolean hasEmptyOption) {
         this.hasEmptyOption = hasEmptyOption;
         return this;
      }

      public DropDownWidget.Builder setNarrationTitle(class_2561 narrationTitle) {
         this.narrationTitle = narrationTitle;
         return this;
      }

      public DropDownWidget build() {
         if (this.options != null && this.w != 0 && this.selected != null && this.callback != null && this.narrationTitle != null && this.container != null) {
            return new DropDownWidget(
               this.options, this.x, this.y, this.w, this.selected, this.openingUp, this.callback, this.container, this.hasEmptyOption, this.narrationTitle
            );
         } else {
            throw new IllegalStateException();
         }
      }

      public static DropDownWidget.Builder begin() {
         return new DropDownWidget.Builder().setDefault();
      }
   }
}
