package xaero.common.gui;

import net.minecraft.class_339;
import xaero.common.IXaeroMinimap;
import xaero.common.graphics.CursorBox;
import xaero.common.settings.ModOptions;
import xaero.common.settings.Option;
import xaero.common.settings.XaeroCyclingOption;
import xaero.common.settings.XaeroDoubleOption;

public class ConfigSettingEntry implements ISettingEntry {
   private ModOptions option;

   public ConfigSettingEntry(ModOptions option) {
      this.option = option;
   }

   @Override
   public class_339 createWidget(int x, int y, int w, boolean canEditIngameSettings) {
      class_339 widget = this.option.getXOption().createButton(x, y, w);
      widget.field_22763 = !this.option.isIngameOnly() || canEditIngameSettings;
      return widget;
   }

   @Override
   public String getStringForSearch() {
      Option mcOption = this.option.getXOption();
      CursorBox optionTooltip = this.option.getTooltip();
      String mainText = mcOption instanceof XaeroCyclingOption
         ? ((XaeroCyclingOption)mcOption).getSearchText()
         : (mcOption instanceof XaeroDoubleOption ? ((XaeroDoubleOption)mcOption).getMessage().getString() : "");
      String tooltipPart;
      if (optionTooltip != null) {
         tooltipPart = " " + optionTooltip.getPlainText();
         if (optionTooltip.getFullCode() != null) {
            tooltipPart = tooltipPart + " " + optionTooltip.getFullCode().replace("gui.xaero", "");
         }
      } else {
         tooltipPart = "";
      }

      return mainText + " " + this.option.getEnumStringRaw().replace("gui.xaero", "") + tooltipPart;
   }

   @Override
   public int hashCode() {
      return this.option.hashCode();
   }

   @Override
   public boolean equals(Object obj) {
      return obj instanceof ConfigSettingEntry && ((ConfigSettingEntry)obj).option == this.option;
   }

   public boolean usesWorldMapHardValue(IXaeroMinimap modMain) {
      return modMain.getSettings().usesWorldMapHardValue(this.option);
   }
}
