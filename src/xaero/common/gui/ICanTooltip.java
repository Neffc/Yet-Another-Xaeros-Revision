package xaero.common.gui;

import java.util.function.Supplier;
import xaero.common.graphics.CursorBox;

public interface ICanTooltip {
   Supplier<CursorBox> getXaero_tooltip();
}
