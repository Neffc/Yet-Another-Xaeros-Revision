package xaero.common.gui;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.class_2561;
import net.minecraft.class_437;
import xaero.common.IXaeroMinimap;
import xaero.hud.preset.HudPreset;

public class GuiChoosePreset extends GuiSettings {
   public GuiChoosePreset(IXaeroMinimap modMain, class_437 back, class_437 escape) {
      super(modMain, class_2561.method_43471("gui.xaero_choose_a_preset"), back, escape);
      List<ISettingEntry> entryList = new ArrayList<>();

      for (HudPreset preset : modMain.getHud().getPresetManager().getPresets()) {
         entryList.add(new HudPresetSettingEntry(modMain.getHud().getPresetManager(), preset));
      }

      this.entries = entryList.toArray(new ISettingEntry[0]);
   }
}
