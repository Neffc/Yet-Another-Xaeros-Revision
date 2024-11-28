package xaero.common.gui;

import net.minecraft.class_2561;
import net.minecraft.class_332;
import net.minecraft.class_437;
import xaero.common.IXaeroMinimap;
import xaero.common.minimap.MinimapInterface;

public abstract class GuiMinimapSettings extends GuiSettings {
   public GuiMinimapSettings(IXaeroMinimap modMain, class_2561 title, class_437 par1Screen, class_437 escScreen) {
      super(modMain, title, par1Screen, escScreen);
   }

   @Override
   public void method_25394(class_332 guiGraphics, int par1, int par2, float par3) {
      super.method_25394(guiGraphics, par1, par2, par3);
      MinimapInterface minimapInterface = this.modMain.getInterfaces().getMinimapInterface();
      if (!this.modMain.getSettings().mapSafeMode
         && minimapInterface.getMinimapFBORenderer().isTriedFBO()
         && !minimapInterface.getMinimapFBORenderer().isLoadedFBO()) {
         guiGraphics.method_25300(this.field_22793, "§4You've been forced into safe mode! :(", this.field_22789 / 2, 11, 16777215);
      }
   }
}
