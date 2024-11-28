package xaero.common.gui;

import net.minecraft.class_310;
import net.minecraft.class_437;
import xaero.common.AXaeroMinimap;
import xaero.common.settings.ModOptions;

public abstract class GuiHelper {
   protected AXaeroMinimap modMain;

   public GuiHelper(AXaeroMinimap modMain) {
      this.modMain = modMain;
   }

   public void openSettingsGui(ModOptions returnModOptions) {
      class_437 current = class_310.method_1551().field_1755;
      if (!(current instanceof ScreenBase)) {
         Object var10000 = null;
      }
   }

   public void openMinimapSettingsFromScreen(class_437 parent, class_437 escScreen) {
      class_310.method_1551().method_1507(new GuiMinimapMain(this.modMain, parent, escScreen));
   }

   public abstract void openInterfaceSettings(int var1);

   public abstract GuiSettings getMainSettingsScreen(class_437 var1);

   public abstract void onResetCancel(class_437 var1, class_437 var2);

   public abstract MyOptions getMyOptions();

   public abstract void openMainSettingsFromScreen(class_437 var1, class_437 var2);
}
