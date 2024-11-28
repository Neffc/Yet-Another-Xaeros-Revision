package xaero.common.gui;

import java.util.function.Supplier;
import net.minecraft.class_2561;
import net.minecraft.class_4185.class_4241;
import net.minecraft.client.Option;
import net.minecraft.client.gui.components.OptionButton;
import xaero.common.graphics.CursorBox;
import xaero.common.settings.ModOptions;

public class ModOptionButton extends OptionButton implements IXaeroClickableWidget {
   private final Supplier<CursorBox> xaero_tooltip;

   public ModOptionButton(
      ModOptions modOption,
      int p_i51132_1_,
      int p_i51132_2_,
      int p_i51132_3_,
      int p_i51132_4_,
      Option p_i51132_5_,
      class_2561 p_i51132_6_,
      class_4241 p_i51132_7_
   ) {
      super(p_i51132_1_, p_i51132_2_, p_i51132_3_, p_i51132_4_, p_i51132_5_, p_i51132_6_, p_i51132_7_);
      this.xaero_tooltip = () -> modOption.getTooltip();
   }

   @Override
   public Supplier<CursorBox> getXaero_tooltip() {
      return this.xaero_tooltip;
   }

   @Override
   public void setXaero_tooltip(Supplier<CursorBox> tooltip) {
   }
}
