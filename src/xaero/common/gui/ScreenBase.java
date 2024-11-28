package xaero.common.gui;

import com.mojang.blaze3d.platform.GlStateManager;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.class_2561;
import net.minecraft.class_310;
import net.minecraft.class_332;
import net.minecraft.class_339;
import net.minecraft.class_357;
import net.minecraft.class_364;
import net.minecraft.class_437;
import net.minecraft.class_4587;
import net.minecraft.class_6379;
import org.lwjgl.glfw.GLFW;
import xaero.common.IXaeroMinimap;
import xaero.common.graphics.CursorBox;
import xaero.common.gui.dropdown.DropDownWidget;
import xaero.common.misc.Misc;

public class ScreenBase extends class_437 implements IScreenBase {
   protected IXaeroMinimap modMain;
   public class_437 parent;
   public class_437 escape;
   protected boolean canSkipWorldRender;
   protected DropDownWidget openDropdown;
   private List<DropDownWidget> dropdowns;
   private static final CursorBox worldmapBox = new CursorBox("gui.xaero_uses_worldmap_value");

   protected ScreenBase(IXaeroMinimap modMain, class_437 parent, class_437 escape, class_2561 titleIn) {
      super(titleIn);
      this.modMain = modMain;
      this.parent = parent;
      this.escape = escape;
      this.dropdowns = new ArrayList<>();
   }

   protected void onExit(class_437 screen) {
      this.field_22787.method_1507(screen);
   }

   protected void goBack() {
      this.onExit(this.parent);
   }

   public void method_25419() {
      this.onExit(this.escape);
   }

   public void renderEscapeScreen(class_332 guiGraphics, int p_230430_2_, int p_230430_3_, float p_230430_4_) {
      if (this.escape != null) {
         this.escape.method_25394(guiGraphics, p_230430_2_, p_230430_3_, p_230430_4_);
      }

      GlStateManager._clear(256, class_310.field_1703);
   }

   public void method_25394(class_332 guiGraphics, int mouseX, int mouseY, float partial) {
      class_4587 poseStack = guiGraphics.method_51448();
      super.method_25394(guiGraphics, mouseX, mouseY, partial);
      this.renderPreDropdown(guiGraphics, mouseX, mouseY, partial);

      for (DropDownWidget dropdown : this.dropdowns) {
         dropdown.method_25394(guiGraphics, mouseX, mouseY, partial);
      }

      if (this.openDropdown != null) {
         poseStack.method_22903();
         poseStack.method_46416(0.0F, 0.0F, 2.0F);
         this.openDropdown.render(guiGraphics, mouseX, mouseY, this.field_22790, false);
         poseStack.method_22909();
      }
   }

   protected void renderPreDropdown(class_332 guiGraphics, int mouseX, int mouseY, float partial) {
   }

   protected void method_25426() {
      super.method_25426();
      this.dropdowns.clear();
      this.openDropdown = null;
      if (this.escape != null) {
         this.escape.method_25423(this.field_22787, this.field_22789, this.field_22790);
      }
   }

   public boolean method_25402(double mouseX, double mouseY, int button) {
      if (this.openDropdown != null) {
         if (this.openDropdown.onDropDown((int)mouseX, (int)mouseY, this.field_22790)) {
            this.openDropdown.method_25402(mouseX, mouseY, button);
            return true;
         }

         this.openDropdown.setClosed(true);
         this.openDropdown = null;
      }

      return super.method_25402(mouseX, mouseY, button);
   }

   public boolean method_25401(double mouseX, double mouseY, double wheel) {
      if (this.openDropdown != null) {
         return this.openDropdown.onDropDown((int)mouseX, (int)mouseY, this.field_22790) ? this.openDropdown.method_25401(mouseX, mouseY, wheel) : true;
      } else {
         return super.method_25401(mouseX, mouseY, wheel);
      }
   }

   public boolean method_25406(double mouseX, double mouseY, int button) {
      return this.openDropdown != null && this.openDropdown.method_25406(mouseX, mouseY, button) ? true : super.method_25406(mouseX, mouseY, button);
   }

   @Override
   public boolean shouldSkipWorldRender() {
      return this.canSkipWorldRender && Misc.screenShouldSkipWorldRender(this.modMain, this.escape, true);
   }

   protected boolean renderTooltips(class_332 guiGraphics, int par1, int par2, float par3) {
      class_4587 matrixStack = guiGraphics.method_51448();
      matrixStack.method_22903();
      matrixStack.method_22904(0.0, 0.0, 2.1);
      boolean result = this.modMain
         .getWidgetScreenHandler()
         .renderTooltips(guiGraphics, this, this.field_22789, this.field_22790, par1, par2, this.field_22787.method_22683().method_4495());
      boolean mousePressed = GLFW.glfwGetMouseButton(this.field_22787.method_22683().method_4490(), 0) == 1;

      for (class_364 el : this.method_25396()) {
         if (el instanceof class_339) {
            class_339 b = (class_339)el;
            if (b instanceof ICanTooltip && (!(b instanceof class_357) || !mousePressed)) {
               ICanTooltip optionWidget = (ICanTooltip)b;
               if (par1 >= b.method_46426()
                  && par2 >= b.method_46427()
                  && par1 < b.method_46426() + b.method_25368()
                  && par2 < b.method_46427() + b.method_25364()
                  && optionWidget.getXaero_tooltip() != null) {
                  CursorBox tooltip = optionWidget.getXaero_tooltip().get();
                  if (tooltip != null) {
                     tooltip.drawBox(guiGraphics, par1, par2, this.field_22789, this.field_22790);
                     result = true;
                     break;
                  }
               }
            }
         }
      }

      matrixStack.method_22909();
      return result;
   }

   @Override
   public void onDropdownOpen(DropDownWidget menu) {
      if (this.openDropdown != null && this.openDropdown != menu) {
         this.openDropdown.setClosed(true);
      }

      this.openDropdown = menu;
   }

   @Override
   public void onDropdownClosed(DropDownWidget menu) {
      if (menu != this.openDropdown && this.openDropdown != null) {
         this.openDropdown.setClosed(true);
      }

      this.openDropdown = null;
   }

   protected <T extends class_364 & class_6379> T method_25429(T guiEventListener) {
      if (guiEventListener instanceof DropDownWidget) {
         this.dropdowns.add((DropDownWidget)guiEventListener);
      }

      return (T)super.method_25429(guiEventListener);
   }

   private void handleDropdownReplacement(class_339 current, class_339 replacement) {
      int dropdownIndex = this.dropdowns.indexOf(current);
      if (dropdownIndex != -1) {
         this.dropdowns.set(dropdownIndex, (DropDownWidget)replacement);
      }

      if (this.method_25399() == current) {
         this.method_25395(replacement);
      }
   }

   private void replaceWidget(class_339 current, class_339 replacement, boolean renderable) {
      int childIndex = this.method_25396().indexOf(current);
      if (childIndex != -1) {
         super.method_37066(current);
         if (renderable) {
            super.method_37063(replacement);
         } else {
            super.method_25429(replacement);
         }

         this.method_25396().remove(replacement);
         this.method_25396().add(childIndex, replacement);
      }

      this.handleDropdownReplacement(current, replacement);
   }

   public void replaceWidget(class_339 current, class_339 replacement) {
      this.replaceWidget(current, replacement, false);
   }

   public void replaceRenderableWidget(class_339 current, class_339 replacement) {
      this.replaceWidget(current, replacement, true);
   }

   protected void method_37066(class_364 current) {
      this.dropdowns.remove(current);
      super.method_37066(current);
   }

   @Override
   public class_437 getEscape() {
      return this.escape;
   }

   public static class_437 tryToGetEscape(class_437 screen) {
      return screen instanceof IScreenBase screenBase ? screenBase.getEscape() : null;
   }
}
