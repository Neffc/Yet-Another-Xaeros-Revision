package xaero.minimap.controls;

import net.minecraft.class_304;
import net.minecraft.class_310;
import net.minecraft.class_437;
import xaero.common.AXaeroMinimap;
import xaero.common.XaeroMinimapSession;
import xaero.common.controls.ControlsHandler;
import xaero.common.gui.GuiMinimapMain;
import xaero.common.gui.ScreenBase;

public class MinimapControlsHandler extends ControlsHandler {
   public MinimapControlsHandler(AXaeroMinimap modMain, XaeroMinimapSession minimapSession) {
      super(modMain, minimapSession);
   }

   @Override
   public void keyDownPre(class_304 kb) {
      if (kb == MinimapControlsRegister.keyBindSettings) {
         class_437 current = class_310.method_1551().field_1755;
         class_437 currentEscScreen = current instanceof ScreenBase ? ((ScreenBase)current).escape : null;
         class_310.method_1551().method_1507(new GuiMinimapMain(this.modMain, current, currentEscScreen));
      }
   }
}
