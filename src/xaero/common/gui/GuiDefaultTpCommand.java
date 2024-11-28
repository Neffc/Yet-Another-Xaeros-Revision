package xaero.common.gui;

import java.io.IOException;
import net.minecraft.class_2561;
import net.minecraft.class_332;
import net.minecraft.class_342;
import net.minecraft.class_437;
import xaero.common.AXaeroMinimap;
import xaero.common.MinimapLogs;

public class GuiDefaultTpCommand extends ScreenBase {
   private MySmallButton confirmButton;
   private class_342 commandFormatTextField;
   private class_342 rotationCommandFormatTextField;
   private String commandFormat;
   private String rotationCommandFormat;

   public GuiDefaultTpCommand(AXaeroMinimap modMain, class_437 parent, class_437 escape) {
      super(modMain, parent, escape, class_2561.method_43471("gui.xaero_teleport_default_command"));
   }

   @Override
   public void method_25426() {
      super.method_25426();
      if (this.commandFormat == null) {
         this.commandFormat = this.modMain.getSettings().defaultWaypointTPCommandFormat;
      }

      if (this.rotationCommandFormat == null) {
         this.rotationCommandFormat = this.modMain.getSettings().defaultWaypointTPCommandRotationFormat;
      }

      this.commandFormatTextField = new class_342(
         this.field_22793, this.field_22789 / 2 - 100, this.field_22790 / 7 + 50, 200, 20, class_2561.method_43471("gui.xaero_teleport_default_command")
      );
      this.commandFormatTextField.method_1852(this.commandFormat);
      this.commandFormatTextField.method_1880(128);
      this.rotationCommandFormatTextField = new class_342(
         this.field_22793, this.field_22789 / 2 - 100, this.field_22790 / 7 + 98, 200, 20, class_2561.method_43471("gui.xaero_teleport_default_command")
      );
      this.rotationCommandFormatTextField.method_1852(this.rotationCommandFormat);
      this.rotationCommandFormatTextField.method_1880(128);
      this.method_25429(this.commandFormatTextField);
      this.method_25429(this.rotationCommandFormatTextField);
      this.method_37063(
         this.confirmButton = new MySmallButton(
            200, this.field_22789 / 2 - 155, this.field_22790 / 6 + 168, class_2561.method_43469("gui.xaero_confirm", new Object[0]), b -> {
               this.modMain.getSettings().defaultWaypointTPCommandFormat = this.commandFormat;
               this.modMain.getSettings().defaultWaypointTPCommandRotationFormat = this.rotationCommandFormat;

               try {
                  this.modMain.getSettings().saveSettings();
               } catch (IOException var3) {
                  MinimapLogs.LOGGER.error("suppressed exception", var3);
               }

               this.goBack();
            }
         )
      );
      this.method_37063(
         new MySmallButton(
            201, this.field_22789 / 2 + 5, this.field_22790 / 6 + 168, class_2561.method_43469("gui.xaero_cancel", new Object[0]), b -> this.goBack()
         )
      );
   }

   @Override
   public void method_25394(class_332 guiGraphics, int mouseX, int mouseY, float partial) {
      this.method_25420(guiGraphics);
      guiGraphics.method_27534(this.field_22793, this.field_22785, this.field_22789 / 2, 20, 16777215);
      guiGraphics.method_25300(this.field_22793, "{x} {y} {z} {name}", this.field_22789 / 2, this.field_22790 / 7 + 36, -5592406);
      guiGraphics.method_25300(this.field_22793, "{x} {y} {z} {name} {yaw}", this.field_22789 / 2, this.field_22790 / 7 + 84, -5592406);
      super.method_25394(guiGraphics, mouseX, mouseY, partial);
      this.commandFormatTextField.method_25394(guiGraphics, mouseX, mouseY, partial);
      this.rotationCommandFormatTextField.method_25394(guiGraphics, mouseX, mouseY, partial);
   }

   public void method_25393() {
      this.commandFormatTextField.method_1865();
      this.rotationCommandFormatTextField.method_1865();
      this.commandFormat = this.commandFormatTextField.method_1882();
      this.rotationCommandFormat = this.rotationCommandFormatTextField.method_1882();
      this.confirmButton.field_22763 = this.commandFormat != null
         && this.commandFormat.length() > 0
         && this.rotationCommandFormat != null
         && this.rotationCommandFormat.length() > 0;
   }

   public boolean method_25404(int par1, int par2, int par3) {
      if (par1 == 257
         && this.commandFormat != null
         && this.commandFormat.length() > 0
         && this.rotationCommandFormat != null
         && this.rotationCommandFormat.length() > 0) {
         this.confirmButton.method_25348(0.0, 0.0);
      }

      return super.method_25404(par1, par2, par3);
   }
}
