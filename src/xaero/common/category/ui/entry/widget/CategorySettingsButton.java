package xaero.common.category.ui.entry.widget;

import java.util.function.Supplier;
import net.minecraft.class_2561;
import net.minecraft.class_4185;
import net.minecraft.class_5250;
import net.minecraft.class_4185.class_4241;
import xaero.common.category.ui.GuiCategorySettings;
import xaero.common.category.ui.data.GuiCategoryUIEditorExpandableData;
import xaero.common.gui.IXaeroNarratableWidget;

public class CategorySettingsButton extends class_4185 implements IXaeroNarratableWidget {
   protected Supplier<String> messageSupplier;
   private GuiCategoryUIEditorExpandableData<?> parent;
   private GuiCategorySettings<?, ?, ?, ?, ?, ?>.SettingRowList rowList;

   public CategorySettingsButton(
      GuiCategoryUIEditorExpandableData<?> parent,
      Supplier<String> messageSupplier,
      boolean active,
      int w,
      int h,
      class_4241 onPress,
      GuiCategorySettings<?, ?, ?, ?, ?, ?>.SettingRowList rowList
   ) {
      super(2, 2, w, h, class_2561.method_43470(""), onPress, field_40754);
      this.field_22763 = active;
      this.messageSupplier = messageSupplier;
      this.rowList = rowList;
      this.parent = parent;
      this.updateMessage();
   }

   protected void updateMessage() {
      this.method_25355(class_2561.method_43470(this.messageSupplier.get()));
   }

   @Override
   public class_5250 method_25360() {
      return class_2561.method_43470("");
   }

   public abstract static class PressActionWithContext implements class_4241 {
      public void onPress(class_4185 button) {
         this.onPress((CategorySettingsButton)button, ((CategorySettingsButton)button).parent, ((CategorySettingsButton)button).rowList);
      }

      public abstract void onPress(
         CategorySettingsButton var1, GuiCategoryUIEditorExpandableData<?> var2, GuiCategorySettings<?, ?, ?, ?, ?, ?>.SettingRowList var3
      );
   }
}
