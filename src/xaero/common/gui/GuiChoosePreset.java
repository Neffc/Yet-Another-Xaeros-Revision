package xaero.common.gui;

import java.util.Iterator;
import net.minecraft.class_2561;
import net.minecraft.class_332;
import net.minecraft.class_4185;
import net.minecraft.class_437;
import xaero.common.IXaeroMinimap;
import xaero.common.interfaces.Preset;

public class GuiChoosePreset extends ScreenBase {
   protected String screenTitle;

   public GuiChoosePreset(IXaeroMinimap modMain, class_437 par1GuiScreen, class_437 escape) {
      super(modMain, par1GuiScreen, escape, class_2561.method_43471("gui.xaero_choose_a_preset"));
   }

   @Override
   public void method_25426() {
      super.method_25426();
      this.modMain.getInterfaces().setSelectedId(-1);
      this.modMain.getInterfaces().setDraggingId(-1);
      this.method_37063(
         class_4185.method_46430(class_2561.method_43469("gui.xaero_cancel", new Object[0]), b -> this.goBack())
            .method_46434(this.field_22789 / 2 - 100, this.field_22790 / 6 + 168, 200, 20)
            .method_46431()
      );
      Iterator<Preset> iterator = this.modMain.getInterfaces().getPresetsIterator();

      for (int i = 0; iterator.hasNext(); i++) {
         Preset var7 = iterator.next();
         int currentI = i;
         this.method_37063(
            new MySmallButton(
               i, this.field_22789 / 2 - 155 + i % 2 * 160, this.field_22790 / 7 + 24 * (i >> 1), class_2561.method_43470(var7.getName()), b -> {
                  ((GuiEditMode)this.parent).applyPreset(currentI);
                  this.goBack();
               }
            )
         );
      }
   }

   @Override
   public void method_25394(class_332 guiGraphics, int par1, int par2, float par3) {
      this.method_25420(guiGraphics);
      guiGraphics.method_27534(this.field_22793, this.field_22785, this.field_22789 / 2, 20, 16777215);
      super.method_25394(guiGraphics, par1, par2, par3);
   }
}
