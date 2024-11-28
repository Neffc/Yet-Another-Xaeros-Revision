package xaero.common.gui;

import net.minecraft.class_2561;
import net.minecraft.class_4185;
import net.minecraft.class_4185.class_4241;
import xaero.common.settings.ModOptions;

public class MySmallButton extends class_4185 {
   private final ModOptions modOptions;
   protected int id;

   public MySmallButton(int id, int par1, int par2, class_2561 par4Str, class_4241 onPress) {
      this(id, par1, par2, (ModOptions)null, par4Str, onPress);
   }

   public MySmallButton(int id, int par1, int par2, int par3, int par4, int par5, class_2561 par6Str, class_4241 onPress) {
      super(par1, par2, par3, par4, par6Str, onPress, field_40754);
      this.modOptions = null;
      this.id = id;
   }

   public MySmallButton(int id, int par1, int par2, ModOptions par4EnumOptions, class_2561 par5Str, class_4241 onPress) {
      super(par1, par2, 150, 20, par5Str, onPress, field_40754);
      this.modOptions = par4EnumOptions;
      this.id = id;
   }

   public ModOptions returnModOptions() {
      return this.modOptions;
   }

   public int getId() {
      return this.id;
   }
}
