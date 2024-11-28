package xaero.common;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import xaero.common.gui.GuiSettings;

public class XaeroMinimapModMenu implements ModMenuApi {
   public ConfigScreenFactory<GuiSettings> getModConfigScreenFactory() {
      return screen -> AXaeroMinimap.INSTANCE.getGuiHelper().getMainSettingsScreen(screen);
   }
}
