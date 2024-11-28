package xaero.common.interfaces;

import java.io.IOException;
import net.minecraft.class_2960;
import xaero.common.IXaeroMinimap;

public interface IInterfaceLoader {
   class_2960 VANILLA_GUI_ICONS_LOCATION = new class_2960("textures/gui/icons.png");

   void loadPresets(InterfaceManager var1);

   void load(IXaeroMinimap var1, InterfaceManager var2) throws IOException;
}
