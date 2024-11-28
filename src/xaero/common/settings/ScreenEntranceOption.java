package xaero.common.settings;

import net.minecraft.class_339;
import net.minecraft.class_4185;
import xaero.common.AXaeroMinimap;

public class ScreenEntranceOption extends Option {
   private AXaeroMinimap modMain;

   public ScreenEntranceOption(String key, AXaeroMinimap modMain, ModOptions option) {
      super(option);
      this.modMain = modMain;
   }

   @Override
   public class_339 createButton(int x, int y, int width) {
      return class_4185.method_46430(this.getCaption(), b -> this.modMain.getGuiHelper().openSettingsGui(this.option))
         .method_46434(x, y, width, 20)
         .method_46431();
   }
}
