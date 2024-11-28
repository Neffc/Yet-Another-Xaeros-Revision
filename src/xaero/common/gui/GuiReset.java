package xaero.common.gui;

import net.minecraft.class_2561;
import net.minecraft.class_410;
import net.minecraft.class_437;

public class GuiReset extends class_410 {
   public GuiReset(IXaeroConfirmScreenCallback callback, class_437 parent, class_437 escScreen) {
      super(r -> callback.accept(r, parent, escScreen), class_2561.method_43471("gui.xaero_reset_message"), class_2561.method_43471("gui.xaero_reset_message2"));
   }
}
