package xaero.common.gui;

import java.io.IOException;
import java.util.Iterator;
import net.minecraft.class_1074;
import net.minecraft.class_2561;
import net.minecraft.class_332;
import net.minecraft.class_4185;
import net.minecraft.class_437;
import net.minecraft.class_4587;
import xaero.common.AXaeroMinimap;
import xaero.common.MinimapLogs;
import xaero.common.interfaces.Interface;
import xaero.common.interfaces.InterfaceManager;

public class GuiEditMode extends ScreenBase {
   private String message;
   private boolean instructions;
   public boolean mouseDown;

   public GuiEditMode(AXaeroMinimap modMain, class_437 par1GuiScreen, class_437 escScreen, String message, boolean instructions) {
      super(modMain, par1GuiScreen, escScreen, class_2561.method_43471("gui.xaero_edit_mode"));
      this.message = message;
      this.instructions = instructions;
   }

   @Override
   public void method_25426() {
      super.method_25426();
      this.modMain.getInterfaces().setSelectedId(-1);
      this.modMain.getInterfaces().setDraggingId(-1);
      this.method_37063(
         new MySmallButton(200, this.field_22789 / 2 - 155, this.field_22790 / 6 + 143, class_2561.method_43469("gui.xaero_confirm", new Object[0]), b -> {
            try {
               this.confirm();
               this.modMain.getSettings().saveSettings();
            } catch (IOException var3) {
               MinimapLogs.LOGGER.error("suppressed exception", var3);
            }

            this.goBack();
         })
      );
      this.method_37063(
         new MySmallButton(
            202,
            this.field_22789 / 2 + 5,
            this.field_22790 / 6 + 143,
            class_2561.method_43469("gui.xaero_choose_a_preset", new Object[0]),
            b -> this.field_22787.method_1507(new GuiChoosePreset(this.modMain, this, this.escape))
         )
      );
      if (this.instructions) {
         this.method_37063(
            new MySmallButton(201, this.field_22789 / 2 + 5, this.field_22790 / 6 + 168, class_2561.method_43469("gui.xaero_cancel", new Object[0]), b -> {
               cancel(this.modMain.getInterfaces());
               this.goBack();
            })
         );
         this.method_37063(
            new MySmallButton(
               203,
               this.field_22789 / 2 - 155,
               this.field_22790 / 6 + 168,
               class_2561.method_43469("gui.xaero_instructions", new Object[0]),
               b -> this.field_22787.method_1507(new GuiInstructions(this.modMain, this, this.escape))
            )
         );
      } else {
         this.method_37063(class_4185.method_46430(class_2561.method_43469("gui.xaero_cancel", new Object[0]), b -> {
            cancel(this.modMain.getInterfaces());
            this.goBack();
         }).method_46434(this.field_22789 / 2 - 100, this.field_22790 / 6 + 168, 200, 20).method_46431());
      }
   }

   @Override
   public boolean method_25402(double par1, double par2, int par3) {
      if (par3 == 0) {
         this.mouseDown = true;
      }

      return super.method_25402(par1, par2, par3);
   }

   @Override
   public boolean method_25406(double par1, double par2, int par3) {
      if (par3 == 0) {
         this.mouseDown = false;
      }

      return super.method_25406(par1, par2, par3);
   }

   public void method_25432() {
      super.method_25432();
      this.mouseDown = false;
   }

   @Override
   public void method_25394(class_332 guiGraphics, int par1, int par2, float par3) {
      class_4587 matrixStack = guiGraphics.method_51448();
      if (this.modMain.getInterfaces().getDraggingId() == -1) {
         if (this.field_22787.field_1724 == null) {
            this.method_25420(guiGraphics);
            guiGraphics.method_25300(
               this.field_22793, class_1074.method_4662("gui.xaero_not_ingame", new Object[0]), this.field_22789 / 2, this.field_22790 / 6 + 128, 16777215
            );
         } else {
            guiGraphics.method_25300(
               this.field_22793, class_1074.method_4662(this.message, new Object[0]), this.field_22789 / 2, this.field_22790 / 6 + 128, 16777215
            );
         }

         super.method_25394(guiGraphics, par1, par2, par3);
      }

      if (this.field_22787.field_1724 != null) {
         matrixStack.method_46416(0.0F, 0.0F, 1.0F);
         this.modMain
            .getInterfaceRenderer()
            .renderBoxes(
               guiGraphics,
               par1,
               par2,
               this.field_22787.method_22683().method_4486(),
               this.field_22787.method_22683().method_4502(),
               this.field_22787.method_22683().method_4495()
            );
         matrixStack.method_46416(0.0F, 0.0F, -1.0F);
      }
   }

   public void confirm() {
      Iterator<Interface> iter = this.modMain.getInterfaces().getInterfaceIterator();

      while (iter.hasNext()) {
         iter.next().backup();
      }
   }

   public static void cancel(InterfaceManager interfaces) {
      Iterator<Interface> iter = interfaces.getInterfaceIterator();

      while (iter.hasNext()) {
         iter.next().restore();
      }
   }

   public void applyPreset(int id) {
      Iterator<Interface> iter = this.modMain.getInterfaces().getInterfaceIterator();
      this.modMain.getInterfaces().setActionTimer(10);

      while (iter.hasNext()) {
         iter.next().applyPreset(this.modMain.getInterfaces().getPreset(id));
      }
   }
}
