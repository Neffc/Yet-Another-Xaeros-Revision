package xaero.common.gui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.class_1074;
import net.minecraft.class_2561;
import net.minecraft.class_332;
import net.minecraft.class_339;
import net.minecraft.class_364;
import net.minecraft.class_4185;
import net.minecraft.class_4280;
import net.minecraft.class_437;
import net.minecraft.class_6381;
import net.minecraft.class_6382;
import net.minecraft.class_4280.class_4281;
import xaero.common.IXaeroMinimap;
import xaero.common.gui.dropdown.DropDownWidget;
import xaero.common.minimap.info.InfoDisplay;
import xaero.common.minimap.info.InfoDisplayManager;
import xaero.common.settings.ModSettings;

public class GuiInfoDisplayEdit extends ScreenBase {
   private static final int FRAME_TOP_SIZE = 30;
   private static final int FRAME_BOTTOM_SIZE = 61;
   private static final int SELECTION_ITEM_HEIGHT = 24;
   private static final class_2561 HELP_COMPONENT = class_2561.method_43471("gui.xaero_minimap_info_display_manager_help");
   private GuiInfoDisplayEdit.SelectionList selectionList;
   private final InfoDisplayManager manager;
   private List<String> currentOrder;
   private int selected;
   private int subSelected;
   private final Map<String, GuiInfoDisplayEdit.MoveableEntry> moveableEntries;

   protected GuiInfoDisplayEdit(IXaeroMinimap modMain, class_437 parent, class_437 escape) {
      super(modMain, parent, escape, class_2561.method_43471("gui.xaero_minimap_info_display_manager"));
      this.manager = modMain.getInterfaces().getMinimapInterface().getInfoDisplayManager();
      this.currentOrder = new ArrayList<>();
      this.manager.getStream().forEach(info -> this.currentOrder.add(info.getId()));
      this.moveableEntries = new HashMap<>();
      this.selected = -1;
      this.subSelected = -1;
   }

   @Override
   protected void method_25426() {
      super.method_25426();
      this.selectionList = new GuiInfoDisplayEdit.SelectionList();
      this.method_25429(this.selectionList);
      this.method_37063(
         class_4185.method_46430(class_2561.method_43469("gui.done", new Object[0]), b -> this.goBack())
            .method_46434(this.field_22789 / 2 - 100, this.field_22790 - 34, 200, 20)
            .method_46431()
      );
      this.moveableEntries.clear();

      for (String id : this.currentOrder) {
         InfoDisplay<?> infoDisplay = this.manager.get(id);
         GuiInfoDisplayEdit.MoveableEntry moveable = new GuiInfoDisplayEdit.MoveableEntry();
         this.addSubElements(moveable, infoDisplay);
         this.moveableEntries.put(id, moveable);
      }
   }

   public String[] createColorOptions(String symbol, boolean noneOption) {
      String[] options = new String[ModSettings.ENCHANT_COLOR_NAMES.length + (noneOption ? 1 : 0)];
      if (noneOption) {
         options[0] = "□□";
      }

      for (int i = 0; i < ModSettings.ENCHANT_COLOR_NAMES.length; i++) {
         options[i + (noneOption ? 1 : 0)] = "§" + ModSettings.ENCHANT_COLORS[i] + symbol;
      }

      return options;
   }

   private <T> void addSubElements(GuiInfoDisplayEdit.MoveableEntry moveable, InfoDisplay<T> infoDisplay) {
      class_339 stateWidget = infoDisplay.getWidgetFactory().create(this.field_22789 / 2 + 150 - 102, 0, 100, 20, infoDisplay, this.modMain.getSettings());
      if (stateWidget != null) {
         moveable.addSubElement(stateWidget);
      }

      DropDownWidget textColorWidget = DropDownWidget.Builder.begin()
         .setOptions(this.createColorOptions("Aa", false))
         .setX(this.field_22789 / 2 - 147)
         .setW(20)
         .setSelected(infoDisplay.getTextColor() % ModSettings.ENCHANT_COLOR_NAMES.length)
         .setContainer(this)
         .setCallback((menu, index) -> {
            infoDisplay.setTextColor(index);

            try {
               this.modMain.getSettings().saveSettings();
            } catch (IOException var5x) {
               var5x.printStackTrace();
            }

            return true;
         })
         .setNarrationTitle(class_2561.method_43471("gui.xaero_dropdown_info_display_text_color"))
         .build();
      moveable.addSubElement(textColorWidget);
      int currentBackgroundIndex = infoDisplay.getBackgroundColor();
      DropDownWidget backgroundColorWidget = DropDownWidget.Builder.begin()
         .setOptions(this.createColorOptions("■■", true))
         .setX(this.field_22789 / 2 - 124)
         .setW(20)
         .setSelected(currentBackgroundIndex < 0 ? 0 : currentBackgroundIndex % ModSettings.ENCHANT_COLOR_NAMES.length + 1)
         .setContainer(this)
         .setCallback((menu, index) -> {
            infoDisplay.setBackgroundColor(index - 1);

            try {
               this.modMain.getSettings().saveSettings();
            } catch (IOException var5x) {
               var5x.printStackTrace();
            }

            return true;
         })
         .setNarrationTitle(class_2561.method_43471("gui.xaero_dropdown_info_display_background_color"))
         .build();
      moveable.addSubElement(backgroundColorWidget);
   }

   @Override
   public boolean method_25406(double d, double e, int i) {
      if (this.selectionList != null) {
         this.selectionList.releaseDrag();
      }

      return super.method_25406(d, e, i);
   }

   public void method_25395(class_364 guiEventListener) {
      super.method_25395(guiEventListener);
   }

   @Override
   public void method_25394(class_332 guiGraphics, int mouseX, int mouseY, float partialTicks) {
      this.selectionList.method_25394(guiGraphics, mouseX, mouseY, partialTicks);
      guiGraphics.method_27534(this.field_22793, this.field_22785, this.field_22789 / 2, 5, -1);
      guiGraphics.method_27534(this.field_22793, HELP_COMPONENT, this.field_22789 / 2, this.field_22790 - 52, -1);
      super.method_25394(guiGraphics, mouseX, mouseY, partialTicks);
   }

   class MoveableEntry {
      private final List<class_339> subElements = new ArrayList<>();

      public MoveableEntry() {
      }

      public void addSubElement(class_339 widget) {
         this.subElements.add(widget);
      }
   }

   class SelectionList extends class_4280<GuiInfoDisplayEdit.SelectionList.Entry> {
      private static final class_2561 USAGE_NARRATION = class_2561.method_43471("narration.selection.usage");
      private static final class_2561 LEFT_RIGHT_USAGE = class_2561.method_43471("narration.xaero_ui_list_left_right_usage");
      private boolean dragging;
      private int dragStartX;
      private int dragStartY;
      private int dragged;
      private int draggedOffsetX;
      private int draggedOffsetY;

      public SelectionList() {
         super(
            GuiInfoDisplayEdit.this.field_22787,
            GuiInfoDisplayEdit.this.field_22789,
            GuiInfoDisplayEdit.this.field_22790,
            30,
            GuiInfoDisplayEdit.this.field_22790 - 61,
            24
         );
         this.createEntries();
         if (GuiInfoDisplayEdit.this.selected != -1) {
            this.method_25395(this.method_25326(GuiInfoDisplayEdit.this.selected));
         }

         this.dragged = -1;
      }

      public boolean method_25370() {
         return GuiInfoDisplayEdit.this.method_25399() == this;
      }

      public void method_25395(class_364 guiEventListener) {
         if (guiEventListener instanceof GuiInfoDisplayEdit.SelectionList.Entry || guiEventListener == null) {
            GuiInfoDisplayEdit.SelectionList.Entry entry = (GuiInfoDisplayEdit.SelectionList.Entry)guiEventListener;
            if (GuiInfoDisplayEdit.this.subSelected != -1) {
               GuiInfoDisplayEdit.SelectionList.Entry oldSelected = (GuiInfoDisplayEdit.SelectionList.Entry)this.method_25334();
               if (oldSelected != null) {
                  GuiInfoDisplayEdit.MoveableEntry moveable = oldSelected.getMoveable();
                  moveable.subElements.get(GuiInfoDisplayEdit.this.subSelected).method_25365(false);
               }
            }

            GuiInfoDisplayEdit.this.selected = entry == null ? -1 : entry.index;
            GuiInfoDisplayEdit.this.subSelected = -1;
         }

         super.method_25395(guiEventListener);
         if (this.method_25336() == null) {
            this.setSelected(null);
         }
      }

      public void setSelected(GuiInfoDisplayEdit.SelectionList.Entry entry) {
         super.method_25313(entry);
      }

      public void method_37020(class_6382 narrationElementOutput) {
         super.method_37020(narrationElementOutput);
         if (this.method_25370()) {
            narrationElementOutput.method_37035(class_6381.field_33791, new class_2561[]{USAGE_NARRATION, LEFT_RIGHT_USAGE});
         }
      }

      private void createEntries() {
         for (int i = 0; i < GuiInfoDisplayEdit.this.manager.getCount(); i++) {
            GuiInfoDisplayEdit.SelectionList.Entry entry = new GuiInfoDisplayEdit.SelectionList.Entry(i);
            this.method_25321(entry);
         }
      }

      private void releaseDrag() {
         this.dragging = false;
         this.dragged = -1;
      }

      protected int method_25329() {
         return this.field_22742 / 2 + 164;
      }

      public int method_25322() {
         return 300;
      }

      public void method_25394(class_332 guiGraphics, int mouseX, int mouseY, float partialTicks) {
         super.method_25394(guiGraphics, mouseX, mouseY, partialTicks);
         if (this.dragging) {
            GuiInfoDisplayEdit.SelectionList.Entry draggedEntry = (GuiInfoDisplayEdit.SelectionList.Entry)this.method_25326(this.dragged);
            draggedEntry.renderNonInteractable(guiGraphics, mouseX + this.draggedOffsetX, mouseY + this.draggedOffsetY);
            GuiInfoDisplayEdit.SelectionList.Entry hoveredEntry = (GuiInfoDisplayEdit.SelectionList.Entry)this.method_25308((double)mouseX, (double)mouseY);
            int hoveredIndex = hoveredEntry == null ? -1 : hoveredEntry.index;
            if (hoveredIndex != -1 && hoveredIndex != this.dragged) {
               String draggedId = GuiInfoDisplayEdit.this.currentOrder.get(this.dragged);
               int slideDirection = hoveredIndex < this.dragged ? 1 : -1;

               for (int i = this.dragged; i != hoveredIndex; i -= slideDirection) {
                  GuiInfoDisplayEdit.this.currentOrder.set(i, GuiInfoDisplayEdit.this.currentOrder.get(i - slideDirection));
               }

               GuiInfoDisplayEdit.this.currentOrder.set(hoveredIndex, draggedId);
               GuiInfoDisplayEdit.this.manager.setOrder(GuiInfoDisplayEdit.this.currentOrder);
               this.dragged = hoveredIndex;

               try {
                  GuiInfoDisplayEdit.this.modMain.getSettings().saveSettings();
               } catch (IOException var11) {
                  var11.printStackTrace();
               }
            }
         } else if (this.dragged != -1 && (Math.abs(mouseX - this.dragStartX) > 5 || Math.abs(mouseY - this.dragStartY) > 5)) {
            this.dragging = true;
            this.method_25395(null);
         }
      }

      public class Entry extends class_4281<GuiInfoDisplayEdit.SelectionList.Entry> {
         private final int index;
         private int lastRenderX;
         private int lastRenderY;
         private int lastMouseX;
         private int lastMouseY;

         public Entry(int index) {
            this.index = index;
         }

         private void renderNonInteractable(class_332 guiGraphics, int x, int y) {
            String infoDisplayId = GuiInfoDisplayEdit.this.currentOrder.get(this.index);
            InfoDisplay<?> infoDisplay = GuiInfoDisplayEdit.this.manager.get(infoDisplayId);
            guiGraphics.method_27535(GuiInfoDisplayEdit.this.field_22793, infoDisplay.getName(), x + 48, y + 6, -1);
         }

         private GuiInfoDisplayEdit.MoveableEntry getMoveable() {
            String infoDisplayId = GuiInfoDisplayEdit.this.currentOrder.get(this.index);
            return GuiInfoDisplayEdit.this.moveableEntries.get(infoDisplayId);
         }

         public void method_25343(class_332 guiGraphics, int index, int y, int x, int l, int m, int mouseX, int mouseY, boolean bl, float partialTicks) {
            this.lastRenderX = x;
            this.lastRenderY = y;
            this.lastMouseX = mouseX;
            this.lastMouseY = mouseY;
            if (!SelectionList.this.dragging || SelectionList.this.dragged != index) {
               this.renderNonInteractable(guiGraphics, x, y);
               GuiInfoDisplayEdit.MoveableEntry moveableEntry = this.getMoveable();

               for (class_339 subElement : moveableEntry.subElements) {
                  subElement.method_46419(y - 2 + 12 - subElement.method_25364() / 2);
                  if (subElement instanceof DropDownWidget) {
                     subElement.method_46419(subElement.method_46427() - 1);
                  }

                  subElement.method_25394(guiGraphics, mouseX, mouseY, partialTicks);
               }
            }
         }

         public boolean method_25402(double d, double e, int i) {
            GuiInfoDisplayEdit.MoveableEntry moveableEntry = this.getMoveable();

            for (class_339 subElement : moveableEntry.subElements) {
               if (subElement.method_25405(d, e) && subElement.method_25402(d, e, i)) {
                  return true;
               }
            }

            if (i == 0) {
               SelectionList.this.dragging = false;
               SelectionList.this.dragged = this.index;
               SelectionList.this.draggedOffsetX = (int)((double)this.lastRenderX - d);
               SelectionList.this.draggedOffsetY = (int)((double)this.lastRenderY - e);
               SelectionList.this.dragStartX = (int)d;
               SelectionList.this.dragStartY = (int)e;
               if (SelectionList.this.method_25334() != this) {
                  return true;
               }

               SelectionList.this.method_25395(null);
            } else {
               SelectionList.this.method_25395(null);
            }

            return super.method_25402(d, e, i);
         }

         public boolean method_25406(double d, double e, int i) {
            GuiInfoDisplayEdit.MoveableEntry moveableEntry = this.getMoveable();

            for (class_339 subElement : moveableEntry.subElements) {
               subElement.method_25406(d, e, i);
            }

            return super.method_25406(d, e, i);
         }

         public void method_16014(double d, double e) {
            this.lastMouseX = (int)d;
            this.lastMouseY = (int)e;
            GuiInfoDisplayEdit.MoveableEntry moveableEntry = this.getMoveable();

            for (class_339 subElement : moveableEntry.subElements) {
               if (subElement.method_25405(d, e)) {
                  subElement.method_16014(d, e);
               }
            }

            super.method_16014(d, e);
         }

         public boolean method_25403(double d, double e, int i, double f, double g) {
            this.lastMouseX = (int)d;
            this.lastMouseY = (int)e;
            GuiInfoDisplayEdit.MoveableEntry moveableEntry = this.getMoveable();

            for (class_339 subElement : moveableEntry.subElements) {
               if (subElement.method_25405(d, e) && subElement.method_25403(d, e, i, f, g)) {
                  return true;
               }
            }

            return super.method_25403(d, e, i, f, g);
         }

         public boolean method_25401(double d, double e, double f) {
            GuiInfoDisplayEdit.MoveableEntry moveableEntry = this.getMoveable();

            for (class_339 subElement : moveableEntry.subElements) {
               if (subElement.method_25405(d, e) && subElement.method_25401(d, e, f)) {
                  return true;
               }
            }

            return super.method_25401(d, e, f);
         }

         public boolean method_25404(int i, int j, int k) {
            GuiInfoDisplayEdit.MoveableEntry moveableEntry = this.getMoveable();
            if (i != 262 && i != 263) {
               if (GuiInfoDisplayEdit.this.subSelected != -1 && moveableEntry.subElements.get(GuiInfoDisplayEdit.this.subSelected).method_25404(i, j, k)) {
                  return true;
               }
            } else {
               if (GuiInfoDisplayEdit.this.subSelected != -1) {
                  moveableEntry.subElements.get(GuiInfoDisplayEdit.this.subSelected).method_25365(false);
               }

               if (i == 262) {
                  GuiInfoDisplayEdit.this.subSelected++;
                  if (GuiInfoDisplayEdit.this.subSelected == moveableEntry.subElements.size()) {
                     GuiInfoDisplayEdit.this.subSelected = -1;
                  }
               } else {
                  GuiInfoDisplayEdit.this.subSelected--;
                  if (GuiInfoDisplayEdit.this.subSelected < -1) {
                     GuiInfoDisplayEdit.this.subSelected = moveableEntry.subElements.size() - 1;
                  }
               }

               if (GuiInfoDisplayEdit.this.subSelected != -1) {
                  moveableEntry.subElements.get(GuiInfoDisplayEdit.this.subSelected).method_25365(true);
               }
            }

            return super.method_25404(i, j, k);
         }

         public boolean method_16803(int i, int j, int k) {
            GuiInfoDisplayEdit.MoveableEntry moveableEntry = this.getMoveable();
            return GuiInfoDisplayEdit.this.subSelected != -1 && moveableEntry.subElements.get(GuiInfoDisplayEdit.this.subSelected).method_16803(i, j, k)
               ? true
               : super.method_16803(i, j, k);
         }

         public boolean method_25400(char c, int i) {
            GuiInfoDisplayEdit.MoveableEntry moveableEntry = this.getMoveable();
            return GuiInfoDisplayEdit.this.subSelected != -1 && moveableEntry.subElements.get(GuiInfoDisplayEdit.this.subSelected).method_25400(c, i)
               ? true
               : super.method_25400(c, i);
         }

         public void method_37020(class_6382 narrationElementOutput) {
            GuiInfoDisplayEdit.MoveableEntry moveableEntry = this.getMoveable();
            int sub = -1;
            if (GuiInfoDisplayEdit.this.selected == this.index && GuiInfoDisplayEdit.this.subSelected >= 0) {
               sub = GuiInfoDisplayEdit.this.subSelected;
            } else {
               for (int i = 0; i < moveableEntry.subElements.size(); i++) {
                  if (moveableEntry.subElements.get(i).method_25405((double)this.lastMouseX, (double)this.lastMouseY)) {
                     sub = i;
                  }
               }
            }

            if (sub >= 0) {
               moveableEntry.subElements.get(sub).method_37020(narrationElementOutput);
            } else {
               super.method_37020(narrationElementOutput);
            }
         }

         public class_2561 method_37006() {
            String infoDisplayId = GuiInfoDisplayEdit.this.currentOrder.get(this.index);
            InfoDisplay<?> infoDisplay = GuiInfoDisplayEdit.this.manager.get(infoDisplayId);
            String narration = infoDisplay.getName().getString();
            return class_2561.method_43470(class_1074.method_4662("narrator.select", new Object[]{narration}));
         }
      }
   }
}
