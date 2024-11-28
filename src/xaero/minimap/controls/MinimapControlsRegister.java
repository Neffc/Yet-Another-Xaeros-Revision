package xaero.minimap.controls;

import net.minecraft.class_304;
import xaero.common.controls.ControlsRegister;

public class MinimapControlsRegister extends ControlsRegister {
   public static class_304 keyBindSettings = new class_304("gui.xaero_minimap_settings", 89, "Xaero's Minimap");

   @Override
   protected void registerKeybindings() {
      this.keybindings.add(keyBindSettings);
      super.registerKeybindings();
   }
}
