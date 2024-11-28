package xaero.common.gui;

import java.util.function.Supplier;
import net.minecraft.class_315;
import net.minecraft.client.ProgressOption;
import net.minecraft.client.gui.components.SliderButton;
import xaero.common.graphics.CursorBox;
import xaero.common.settings.ModOptions;

public class ModOptionSlider extends SliderButton implements IXaeroClickableWidget {
   private final Supplier<CursorBox> xaero_tooltip;

   public ModOptionSlider(ModOptions modOption, class_315 settings, int xIn, int yIn, int widthIn, int heightIn, ProgressOption p_i51129_6_) {
      super(settings, xIn, yIn, widthIn, heightIn, p_i51129_6_);
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
