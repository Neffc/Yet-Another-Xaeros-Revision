package xaero.common.gui;

import java.util.Optional;
import net.minecraft.class_310;
import net.minecraft.class_339;
import net.minecraft.class_4185;
import xaero.hud.preset.HudPreset;
import xaero.hud.preset.HudPresetManager;

public class HudPresetSettingEntry implements ISettingEntry {
   private final HudPresetManager manager;
   private final HudPreset preset;
   private final String searchString;

   public HudPresetSettingEntry(HudPresetManager manager, HudPreset preset) {
      this.manager = manager;
      this.preset = preset;
      StringBuilder searchStringBuilder = new StringBuilder();
      searchStringBuilder.append(preset.getId()).append(" ");
      preset.getName().method_27657(s -> {
         searchStringBuilder.append(s);
         return Optional.empty();
      });
      this.searchString = searchStringBuilder.toString();
   }

   @Override
   public String getStringForSearch() {
      return this.searchString;
   }

   @Override
   public class_339 createWidget(int x, int y, int w, boolean canEditIngameSettings) {
      return class_4185.method_46430(this.preset.getName(), b -> {
         for (HudPreset preset : this.manager.getPresets()) {
            preset.cancel();
         }

         this.preset.apply();
         if (class_310.method_1551().field_1755 instanceof GuiChoosePreset gui) {
            gui.goBack();
         }
      }).method_46434(x, y, w, 20).method_46431();
   }
}
