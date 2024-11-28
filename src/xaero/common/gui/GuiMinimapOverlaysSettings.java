package xaero.common.gui;

import net.minecraft.class_2561;
import net.minecraft.class_437;
import xaero.common.AXaeroMinimap;
import xaero.common.XaeroMinimapSession;
import xaero.common.settings.ModOptions;

public class GuiMinimapOverlaysSettings extends GuiMinimapSettings {
   public GuiMinimapOverlaysSettings(AXaeroMinimap modMain, class_437 backScreen, class_437 escScreen) {
      super(modMain, class_2561.method_43471("gui.xaero_overlay_settings"), backScreen, escScreen);
      ScreenSwitchSettingEntry lightOverlayEntry = new ScreenSwitchSettingEntry(
         "gui.xaero_light_overlay", (current, escape) -> new GuiLightOverlay(modMain, current, escape), null, true
      );
      ScreenSwitchSettingEntry slimeChunksMultiplayerEntry = null;
      XaeroMinimapSession minimapSession = XaeroMinimapSession.getCurrentSession();
      if (modMain.getSettings().customSlimeSeedNeeded(minimapSession)) {
         slimeChunksMultiplayerEntry = new ScreenSwitchSettingEntry(
            "gui.xaero_slime_chunks", (current, escape) -> new GuiSlimeSeed(modMain, minimapSession.getWaypointsManager(), current, escape), null, true
         );
      }

      this.entries = new ISettingEntry[]{
         new ConfigSettingEntry(ModOptions.CHUNK_GRID),
         new ConfigSettingEntry(ModOptions.CHUNK_GRID_LINE_WIDTH),
         lightOverlayEntry,
         (ISettingEntry)(slimeChunksMultiplayerEntry == null ? new ConfigSettingEntry(ModOptions.SLIME_CHUNKS) : slimeChunksMultiplayerEntry),
         new ConfigSettingEntry(ModOptions.PAC_CLAIMS),
         new ConfigSettingEntry(ModOptions.PAC_CLAIMS_BORDER_OPACITY),
         new ConfigSettingEntry(ModOptions.PAC_CLAIMS_FILL_OPACITY)
      };
   }
}
