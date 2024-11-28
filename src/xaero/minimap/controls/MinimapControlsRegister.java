package xaero.minimap.controls;

import java.util.function.Consumer;
import net.minecraft.class_304;
import xaero.common.controls.ControlsRegister;

public class MinimapControlsRegister extends ControlsRegister {
   public static class_304 keyBindSettings = new class_304("gui.xaero_minimap_settings", 89, "Xaero's Minimap");

   @Override
   public void registerKeybindings(Consumer<class_304> registry) {
      this.keybindings.add(keyBindSettings);
      super.registerKeybindings(registry);
   }

   @Override
   public void onStage2() {
   }
}
