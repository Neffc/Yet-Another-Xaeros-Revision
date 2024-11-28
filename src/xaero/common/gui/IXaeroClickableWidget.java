package xaero.common.gui;

import java.util.function.Supplier;
import xaero.common.graphics.CursorBox;

public interface IXaeroClickableWidget extends ICanTooltip {
   void setXaero_tooltip(Supplier<CursorBox> var1);
}
