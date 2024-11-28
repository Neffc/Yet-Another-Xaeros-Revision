package xaero.common.gui;

import java.util.function.Supplier;
import net.minecraft.class_357;
import xaero.common.graphics.CursorBox;
import xaero.common.settings.XaeroDoubleOption;

public class XaeroDoubleOptionWidget extends class_357 implements IXaeroClickableWidget {
   private XaeroDoubleOption option;

   public XaeroDoubleOptionWidget(XaeroDoubleOption option, int x, int y, int width, int height) {
      super(x, y, width, height, option.getMessage(), option.getOption().normalizeValue(option.getGetter().get()));
      this.option = option;
   }

   @Override
   public Supplier<CursorBox> getXaero_tooltip() {
      return () -> this.option.getOption().getTooltip();
   }

   @Override
   public void setXaero_tooltip(Supplier<CursorBox> tooltip) {
   }

   protected void method_25346() {
      this.method_25355(this.option.getMessage());
   }

   protected void method_25344() {
      this.option.getSetter().accept(this.option.getOption().denormalizeValue(this.field_22753));
   }
}
