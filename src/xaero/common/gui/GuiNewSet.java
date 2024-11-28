package xaero.common.gui;

import java.io.IOException;
import net.minecraft.class_2561;
import net.minecraft.class_332;
import net.minecraft.class_342;
import net.minecraft.class_4185;
import net.minecraft.class_437;
import xaero.common.IXaeroMinimap;
import xaero.common.MinimapLogs;
import xaero.common.XaeroMinimapSession;
import xaero.common.minimap.waypoints.WaypointWorld;
import xaero.common.minimap.waypoints.WaypointsManager;

public class GuiNewSet extends ScreenBase {
   private class_342 nameTextField;
   private XaeroMinimapSession minimapSession;
   private WaypointsManager waypointsManager;
   private WaypointWorld waypointWorld;
   private class_4185 confirmButton;

   public GuiNewSet(IXaeroMinimap modMain, XaeroMinimapSession minimapSession, class_437 par1GuiScreen, WaypointWorld waypointWorld) {
      this(modMain, minimapSession, par1GuiScreen, null, waypointWorld);
   }

   public GuiNewSet(IXaeroMinimap modMain, XaeroMinimapSession minimapSession, class_437 par1GuiScreen, class_437 escapeScreen, WaypointWorld waypointWorld) {
      super(modMain, par1GuiScreen, escapeScreen, class_2561.method_43471("gui.xaero_create_set"));
      this.minimapSession = minimapSession;
      this.waypointsManager = minimapSession.getWaypointsManager();
      this.waypointWorld = waypointWorld;
      this.canSkipWorldRender = true;
   }

   @Override
   public void method_25426() {
      super.method_25426();
      this.nameTextField = new class_342(this.field_22793, this.field_22789 / 2 - 100, 60, 200, 20, class_2561.method_43471("gui.xaero_set_name"));
      this.method_25395(this.nameTextField);
      this.nameTextField.method_25365(true);
      this.method_37063(this.nameTextField);
      this.method_37063(
         this.confirmButton = new MySmallButton(
            200, this.field_22789 / 2 - 155, this.field_22790 / 6 + 168, class_2561.method_43469("gui.xaero_confirm", new Object[0]), b -> {
               if (this.canConfirm()) {
                  String setName = this.nameTextField.method_1882().replace(":", "§§");
                  this.waypointWorld.setCurrent(setName);
                  this.waypointWorld.addSet(setName);
                  this.waypointsManager.updateWaypoints();

                  try {
                     this.modMain.getSettings().saveWaypoints(this.waypointWorld);
                  } catch (IOException var4) {
                     MinimapLogs.LOGGER.error("suppressed exception", var4);
                  }

                  this.goBack();
               }
            }
         )
      );
      this.method_37063(
         new MySmallButton(
            201, this.field_22789 / 2 + 5, this.field_22790 / 6 + 168, class_2561.method_43469("gui.xaero_cancel", new Object[0]), b -> this.goBack()
         )
      );
      this.updateConfirmButton();
   }

   private boolean canConfirm() {
      return this.nameTextField.method_1882().length() > 0 && this.waypointWorld.getSets().get(this.nameTextField.method_1882()) == null;
   }

   private void updateConfirmButton() {
      this.confirmButton.field_22763 = this.canConfirm();
   }

   public boolean method_25404(int par1, int par2, int par3) {
      boolean result = super.method_25404(par1, par2, par3);
      if (par1 == 257 && this.canConfirm()) {
         this.confirmButton.method_25348(0.0, 0.0);
         return true;
      } else {
         return result;
      }
   }

   public void method_25393() {
      this.updateConfirmButton();
      this.nameTextField.method_1865();
   }

   @Override
   public void method_25394(class_332 guiGraphics, int par1, int par2, float par3) {
      this.renderEscapeScreen(guiGraphics, par1, par2, par3);
      this.method_25420(guiGraphics);
      guiGraphics.method_27534(this.field_22793, this.field_22785, this.field_22789 / 2, 20, 16777215);
      this.nameTextField.method_25394(guiGraphics, par1, par2, par3);
      super.method_25394(guiGraphics, par1, par2, par3);
   }
}
