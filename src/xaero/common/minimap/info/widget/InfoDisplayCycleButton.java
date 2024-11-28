package xaero.common.minimap.info.widget;

import java.util.List;
import net.minecraft.class_2561;
import net.minecraft.class_4185.class_4241;
import xaero.common.minimap.info.InfoDisplay;
import xaero.common.settings.ModSettings;

@Deprecated
public class InfoDisplayCycleButton extends xaero.hud.minimap.info.widget.InfoDisplayCycleButton {
   @Deprecated
   public InfoDisplayCycleButton(int currentIndex, int x, int y, int w, int h, class_2561 component, class_4241 onPress) {
      super(currentIndex, x, y, w, h, component, onPress);
   }

   @Deprecated
   public static final class Builder<T> extends xaero.hud.minimap.info.widget.InfoDisplayCycleButton.Builder<T> {
      private Builder() {
      }

      @Deprecated
      public InfoDisplayCycleButton.Builder<T> setDefault() {
         return (InfoDisplayCycleButton.Builder<T>)super.setDefault();
      }

      @Deprecated
      public InfoDisplayCycleButton.Builder<T> setBounds(int x, int y, int w, int h) {
         return (InfoDisplayCycleButton.Builder<T>)super.setBounds(x, y, w, h);
      }

      @Deprecated
      public InfoDisplayCycleButton.Builder<T> setValues(List<T> values, List<class_2561> valueNames) {
         return (InfoDisplayCycleButton.Builder<T>)super.setValues(values, valueNames);
      }

      @Deprecated
      public InfoDisplayCycleButton.Builder<T> setInfoDisplay(InfoDisplay<T> infoDisplay) {
         return (InfoDisplayCycleButton.Builder<T>)super.setInfoDisplay(infoDisplay);
      }

      @Deprecated
      public InfoDisplayCycleButton.Builder<T> setSettings(ModSettings settings) {
         return this;
      }

      @Deprecated
      public InfoDisplayCycleButton build() {
         return (InfoDisplayCycleButton)super.build();
      }

      @Deprecated
      public static <T> InfoDisplayCycleButton.Builder<T> begin() {
         return new InfoDisplayCycleButton.Builder<T>().setDefault();
      }
   }
}
