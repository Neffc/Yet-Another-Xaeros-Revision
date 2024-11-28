package xaero.common.settings;

import net.minecraft.class_339;
import net.minecraft.class_4185;
import xaero.common.IXaeroMinimap;

public class ScreenEntranceOption extends Option {
   private IXaeroMinimap modMain;

   public ScreenEntranceOption(String key, IXaeroMinimap modMain, ModOptions option) {
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
