package xaero.common.category.ui.entry;

import java.util.function.Supplier;
import net.minecraft.class_1074;
import net.minecraft.class_1109;
import net.minecraft.class_310;
import net.minecraft.class_327;
import net.minecraft.class_332;
import net.minecraft.class_339;
import net.minecraft.class_3417;
import net.minecraft.class_342;
import net.minecraft.class_357;
import net.minecraft.class_4264;
import net.minecraft.class_4587;
import org.joml.Vector4f;
import xaero.common.category.ui.GuiCategorySettings;
import xaero.common.category.ui.data.GuiCategoryUIEditorExpandableData;
import xaero.common.category.ui.entry.widget.CategorySettingsTextField;
import xaero.common.graphics.CursorBox;

public class CategorySettingsListEntryWidget<D extends GuiCategoryUIEditorExpandableData<?>> extends CategorySettingsListEntryWithRootReference<D> {
   protected class_339 widget;
   private boolean widgetPressed;

   public CategorySettingsListEntryWidget(
      int entryX,
      int entryY,
      int entryW,
      int entryH,
      int index,
      GuiCategorySettings<?, ?, ?, ?, ?, ?>.SettingRowList rowList,
      CategorySettingsListMainEntry<D> root,
      class_339 widget,
      Supplier<CursorBox> tooltipSupplier
   ) {
      super(entryX, entryY, entryW, entryH, index, rowList, root, tooltipSupplier);
      this.widget = widget;
   }

   @Override
   public boolean mouseClicked(GuiCategorySettings<?, ?, ?, ?, ?, ?>.SettingRowList.Entry entry, double relativeMouseX, double relativeMouseY, int i) {
      boolean result = super.mouseClicked(entry, relativeMouseX, relativeMouseY, i);
      if (!result && !(this.widget instanceof class_4264) && this.widget.method_25405(relativeMouseX, relativeMouseY)) {
         this.widgetPressed = true;
         return this.widget.method_25402(relativeMouseX, relativeMouseY, i);
      } else {
         return result;
      }
   }

   @Override
   public boolean mouseReleased(double relativeMouseX, double relativeMouseY, int i) {
      if (this.widgetPressed) {
         this.widget.method_25406(relativeMouseX, relativeMouseY, i);
      }

      this.widgetPressed = false;
      super.mouseReleased(relativeMouseX, relativeMouseY, i);
      return false;
   }

   @Override
   public boolean mouseDragged(double relativeMouseX, double relativeMouseY, int i, double f, double g) {
      return this.widgetPressed && this.widget.method_25403(relativeMouseX, relativeMouseY, i, f, g)
         ? true
         : super.mouseDragged(relativeMouseX, relativeMouseY, i, f, g);
   }

   @Override
   public boolean mouseScrolled(double relativeMouseX, double relativeMouseY, double f) {
      return this.widget.method_25405(relativeMouseX, relativeMouseY) && this.widget.method_25401(relativeMouseX, relativeMouseY, f)
         ? true
         : super.mouseScrolled(relativeMouseX, relativeMouseY, f);
   }

   @Override
   public void mouseMoved(double relativeMouseX, double relativeMouseY) {
      this.widget.method_16014(relativeMouseX, relativeMouseY);
      super.mouseMoved(relativeMouseX, relativeMouseY);
   }

   @Override
   public boolean keyPressed(int i, int j, int k, boolean isRoot) {
      return this.widget.method_25404(i, j, k) ? true : super.keyPressed(i, j, k, isRoot);
   }

   @Override
   public boolean keyReleased(int i, int j, int k) {
      return this.widget.method_16803(i, j, k) ? true : super.keyReleased(i, j, k);
   }

   @Override
   public boolean charTyped(char c, int i) {
      return this.widget.method_25400(c, i) ? true : super.charTyped(c, i);
   }

   @Override
   public void tick() {
      if (this.widget instanceof class_342) {
         ((class_342)this.widget).method_1865();
      }

      super.tick();
   }

   @Override
   public String getNarration() {
      return super.getNarration();
   }

   @Override
   public String getHoverNarration() {
      return this.getNarration();
   }

   @Override
   public String getMessage() {
      return this.widget.method_25369().getString();
   }

   @Override
   public String getNarrationMessage() {
      if (this.widget instanceof CategorySettingsTextField) {
         return ((CategorySettingsTextField)this.widget).method_25360().getString();
      } else {
         return this.widget instanceof class_357
            ? class_1074.method_4662("gui.narrate.slider", new Object[]{this.getMessage()})
            : class_1074.method_4662("gui.narrate.button", new Object[]{this.getMessage()});
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
      class_4587 poseStack = guiGraphics.method_51448();
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
      Vector4f widgetPos = new Vector4f((float)this.widget.method_46426(), (float)this.widget.method_46427(), 0.0F, 1.0F);
      widgetPos.mul(poseStack.method_23760().method_23761());
      int xBU = this.widget.method_46426();
      int yBU = this.widget.method_46427();
      this.widget.method_46421((int)widgetPos.x());
      this.widget.method_46419((int)widgetPos.y());
      poseStack.method_22903();
      poseStack.method_34426();
      this.widget.method_25394(guiGraphics, globalMouseX, globalMouseY, partialTicks);
      poseStack.method_22909();
      this.widget.method_46421(xBU);
      this.widget.method_46419(yBU);
      return this.widgetPressed ? null : result;
   }

   @Override
   public void setFocused(boolean bl) {
      if (this.widget.field_22763 && this.widget.field_22764 && this.widget.method_25370() != bl) {
         this.widget.method_25365(bl);
      }

      super.setFocused(bl);
   }

   @Override
   protected boolean selectAction() {
      if (this.widget instanceof class_4264 && this.widget.field_22763) {
         ((class_4264)this.widget).method_25306();
         class_310.method_1551().method_1483().method_4873(class_1109.method_47978(class_3417.field_15015, 1.0F));
      }

      return false;
   }
}
