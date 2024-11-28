package xaero.common.gui.widget;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.class_332;
import net.minecraft.class_437;
import xaero.common.gui.widget.init.WidgetInitializer;
import xaero.common.gui.widget.render.WidgetRenderer;

public class WidgetScreenHandler {
   private List<Widget> widgets = new ArrayList<>();

   void addWidget(Widget widget) {
      if (widget != null) {
         this.widgets.add(widget);
      }
   }

   public void initialize(WidgetScreen screen, int width, int height) {
      for (Widget w : this.widgets) {
         if (w.getLocation().isAssignableFrom(screen.getClass())) {
            WidgetInitializer widgetInit = w.getType().widgetInit;
            if (widgetInit != null) {
               widgetInit.init(screen, width, height, w);
            }
         }
      }
   }

   public void render(class_332 guiGraphics, WidgetScreen screen, int width, int height, int mouseX, int mouseY, double guiScale) {
      for (Widget w : this.widgets) {
         if (w.getLocation().isAssignableFrom(screen.getClass())) {
            WidgetRenderer renderer = w.getType().widgetRenderer;
            if (renderer != null) {
               renderer.render(guiGraphics, width, height, mouseX, mouseY, guiScale, w);
            }
         }
      }
   }

   public boolean renderTooltips(class_332 guiGraphics, class_437 screen, int width, int height, int mouseX, int mouseY, double guiScale) {
      boolean result = false;

      for (Widget w : this.widgets) {
         if (w.getLocation().isAssignableFrom(screen.getClass()) && this.renderTooltip(guiGraphics, width, height, mouseX, mouseY, guiScale, w)) {
            result = true;
         }
      }

      return result;
   }

   private boolean renderTooltip(class_332 guiGraphics, int width, int height, int mouseX, int mouseY, double guiScale, Widget widget) {
      if (widget.getOnHover() == HoverAction.TOOLTIP && widget.getTooltip() != null) {
         int x = widget.getBoxX(width, guiScale);
         int y = widget.getBoxY(height, guiScale);
         int w = widget.getBoxW(guiScale);
         int h = widget.getBoxH(guiScale);
         if (mouseX >= x && mouseY >= y && mouseX < x + w && mouseY < y + h) {
            widget.getCursorBox().drawBox(guiGraphics, mouseX, mouseY, width, height);
            return true;
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   public void handleClick(class_437 screen, int width, int height, int mouseX, int mouseY, double guiScale) {
      for (Widget w : this.widgets) {
         if (w.getLocation().isAssignableFrom(screen.getClass())) {
            this.handleWidgetClick(screen, width, height, mouseX, mouseY, guiScale, w);
         }
      }
   }

   private void handleWidgetClick(class_437 screen, int width, int height, int mouseX, int mouseY, double guiScale, Widget widget) {
      if (widget.getOnClick() != ClickAction.NOTHING && widget.getType() != WidgetType.BUTTON) {
         int x = widget.getBoxX(width, guiScale);
         int y = widget.getBoxY(height, guiScale);
         int w = widget.getBoxW(guiScale);
         int h = widget.getBoxH(guiScale);
         if (mouseX >= x && mouseY >= y && mouseX < x + w && mouseY < y + h) {
            WidgetClickHandler clickHandler = widget.getOnClick().clickHandler;
            if (clickHandler != null) {
               clickHandler.onClick(screen, widget);
            }
         }
      }
   }
}
