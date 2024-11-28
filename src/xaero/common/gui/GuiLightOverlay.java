package xaero.common.gui;

import net.minecraft.class_2561;
import net.minecraft.class_437;
import xaero.common.IXaeroMinimap;
import xaero.common.settings.ModOptions;

public class GuiLightOverlay extends GuiSettings {
   public GuiLightOverlay(IXaeroMinimap modMain, class_437 par1Screen, class_437 escScreen) {
      super(modMain, class_2561.method_43471("gui.xaero_light_overlay"), par1Screen, escScreen);
      this.entries = entriesFromOptions(
         new ModOptions[]{
            ModOptions.LIGHT_OVERLAY_TYPE, ModOptions.LIGHT_OVERLAY_MAX_LIGHT, ModOptions.LIGHT_OVERLAY_COLOR, ModOptions.LIGHT_OVERLAY_MIN_LIGHT
         }
      );
   }
}
