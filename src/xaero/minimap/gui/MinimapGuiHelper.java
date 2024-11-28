package xaero.minimap.gui;

import net.minecraft.class_310;
import net.minecraft.class_437;
import xaero.common.AXaeroMinimap;
import xaero.common.gui.GuiEditMode;
import xaero.common.gui.GuiHelper;
import xaero.common.gui.GuiMinimapMain;
import xaero.common.gui.GuiSettings;
import xaero.common.gui.MyOptions;
import xaero.common.gui.ScreenBase;

public class MinimapGuiHelper extends GuiHelper {
   public MinimapGuiHelper(AXaeroMinimap modMain) {
      super(modMain);
   }

   @Override
   public void openInterfaceSettings(int i) {
      class_437 current = class_310.method_1551().field_1755;
      class_437 currentEscScreen = current instanceof ScreenBase ? ((ScreenBase)current).escape : null;
      switch (i) {
         case 4:
            if (current instanceof GuiEditMode currentGuiSettings) {
               class_437 parent = currentGuiSettings.parent;
               class_310.method_1551()
                  .method_1507((class_437)(parent instanceof GuiMinimapMain ? parent : new GuiMinimapMain(this.modMain, current, currentEscScreen)));
            }
      }
   }

   @Override
   public GuiSettings getMainSettingsScreen(class_437 parent) {
      return new GuiMinimapMain(this.modMain, parent, null);
   }

   @Override
   public void onResetCancel(class_437 parent, class_437 escScreen) {
      class_310.method_1551().method_1507(new GuiMinimapMain(this.modMain, parent, escScreen));
   }

   @Override
   public MyOptions getMyOptions() {
      return new MyOptions(
         "gui.xaero_minimap_settings", this.getMainSettingsScreen(class_310.method_1551().field_1755), null, class_310.method_1551().field_1690
      );
   }

   @Override
   public void openMainSettingsFromScreen(class_437 parent, class_437 escScreen) {
      this.openMinimapSettingsFromScreen(parent, escScreen);
   }
}
