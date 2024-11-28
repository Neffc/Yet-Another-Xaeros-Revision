package xaero.common.gui;

import java.io.IOException;
import net.minecraft.class_1074;
import net.minecraft.class_2561;
import net.minecraft.class_332;
import net.minecraft.class_342;
import net.minecraft.class_437;
import org.apache.commons.lang3.StringUtils;
import xaero.common.IXaeroMinimap;
import xaero.common.minimap.waypoints.WaypointsManager;
import xaero.common.settings.ModOptions;
import xaero.hud.minimap.MinimapLogs;

public class GuiSlimeSeed extends GuiSettings {
   public class_342 seedTextField;
   private final String fullWorldID;

   public GuiSlimeSeed(IXaeroMinimap modMain, WaypointsManager waypointsManager, class_437 parent, class_437 escape) {
      super(modMain, class_2561.method_43471("gui.xaero_slime_chunks"), parent, escape);
      this.entries = entriesFromOptions(new ModOptions[]{ModOptions.SLIME_CHUNKS, ModOptions.OPEN_SLIME_SETTINGS});
      this.fullWorldID = waypointsManager.getCurrentContainerAndWorldID();
   }

   @Override
   public void method_25426() {
      super.method_25426();
      this.seedTextField = new class_342(
         this.field_22793, this.field_22789 / 2 - 100, this.field_22790 / 7 + 68, 200, 20, class_2561.method_43471("gui.xaero_used_seed")
      );
      this.seedTextField
         .method_1852(
            (this.modMain.getSettings().getSlimeChunksSeed(this.fullWorldID) == null ? "" : this.modMain.getSettings().getSlimeChunksSeed(this.fullWorldID))
               + ""
         );
      this.method_37063(this.seedTextField);
   }

   @Override
   public void method_25394(class_332 guiGraphics, int mouseX, int mouseY, float partial) {
      super.method_25394(guiGraphics, mouseX, mouseY, partial);
      this.seedTextField.method_25394(guiGraphics, mouseX, mouseY, partial);
      guiGraphics.method_25300(
         this.field_22793, class_1074.method_4662("gui.xaero_used_seed", new Object[0]), this.field_22789 / 2, this.field_22790 / 7 + 55, 16777215
      );
   }

   @Override
   public void method_25393() {
      this.seedTextField.method_1865();
   }

   @Override
   public boolean method_25404(int par1, int par2, int par3) {
      boolean result = super.method_25404(par1, par2, par3);
      if (par1 == 257) {
         this.goBack();
      }

      this.updateSlimeSeed();
      return result;
   }

   @Override
   public boolean method_25400(char par1, int par2) {
      boolean result = super.method_25400(par1, par2);
      this.updateSlimeSeed();
      return result;
   }

   private void updateSlimeSeed() {
      String s = this.seedTextField.method_1882();
      if (!StringUtils.isEmpty(s)) {
         try {
            long j = Long.parseLong(s);
            this.modMain.getSettings().setSlimeChunksSeed(j, this.fullWorldID);
         } catch (NumberFormatException var5) {
            this.modMain.getSettings().setSlimeChunksSeed((long)s.hashCode(), this.fullWorldID);
         }
      }

      try {
         this.modMain.getSettings().saveSettings();
      } catch (IOException var4) {
         MinimapLogs.LOGGER.error("suppressed exception", var4);
      }
   }
}
