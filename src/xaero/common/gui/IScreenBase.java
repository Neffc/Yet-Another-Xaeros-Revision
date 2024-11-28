package xaero.common.gui;

import net.minecraft.class_437;
import xaero.common.gui.dropdown.IDropDownContainer;

public interface IScreenBase extends IDropDownContainer {
   boolean shouldSkipWorldRender();

   class_437 getEscape();
}
