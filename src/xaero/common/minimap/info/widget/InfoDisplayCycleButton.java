package xaero.common.minimap.info.widget;

import java.io.IOException;
import java.util.List;
import net.minecraft.class_2561;
import net.minecraft.class_4185;
import net.minecraft.class_437;
import net.minecraft.class_4185.class_4241;
import xaero.common.minimap.info.InfoDisplay;
import xaero.common.settings.ModSettings;

public class InfoDisplayCycleButton extends class_4185 {
   private int currentIndex;

   private InfoDisplayCycleButton(int currentIndex, int x, int y, int w, int h, class_2561 component, class_4241 onPress) {
      super(x, y, w, h, component, onPress, field_40754);
      this.currentIndex = currentIndex;
   }

   public static final class Builder<T> {
      private int x;
      private int y;
      private int w;
      private int h;
      private List<T> values;
      private List<class_2561> valueNames;
      private InfoDisplay<T> infoDisplay;
      private ModSettings settings;

      private Builder() {
      }

      public InfoDisplayCycleButton.Builder<T> setDefault() {
         this.setBounds(0, 0, 0, 0);
         this.setValues(null, null);
         this.setInfoDisplay(null);
         return this;
      }

      public InfoDisplayCycleButton.Builder<T> setBounds(int x, int y, int w, int h) {
         this.x = x;
         this.y = y;
         this.w = w;
         this.h = h;
         return this;
      }

      public InfoDisplayCycleButton.Builder<T> setValues(List<T> values, List<class_2561> valueNames) {
         if (values == null != (valueNames == null)) {
            throw new IllegalArgumentException();
         } else if (values != null && values.size() != valueNames.size()) {
            throw new IllegalArgumentException();
         } else {
            this.values = values;
            this.valueNames = valueNames;
            return this;
         }
      }

      public InfoDisplayCycleButton.Builder<T> setInfoDisplay(InfoDisplay<T> infoDisplay) {
         this.infoDisplay = infoDisplay;
         return this;
      }

      public InfoDisplayCycleButton.Builder<T> setSettings(ModSettings settings) {
         this.settings = settings;
         return this;
      }

      public InfoDisplayCycleButton build() {
         if (this.w != 0 && this.h != 0 && this.values != null && this.infoDisplay != null && this.settings != null) {
            int currentStateIndex = this.values.indexOf(this.infoDisplay.getState());
            if (currentStateIndex < 0) {
               this.infoDisplay.setState(this.values.get(0));
               currentStateIndex = 0;
            }

            class_4241 action = b -> {
               InfoDisplayCycleButton cycleButton = (InfoDisplayCycleButton)b;
               if (class_437.method_25442()) {
                  cycleButton.currentIndex--;
                  if (cycleButton.currentIndex < 0) {
                     cycleButton.currentIndex = this.values.size() - 1;
                  }
               } else {
                  cycleButton.currentIndex = (cycleButton.currentIndex + 1) % this.values.size();
               }

               this.infoDisplay.setState(this.values.get(cycleButton.currentIndex));
               cycleButton.method_25355(this.valueNames.get(cycleButton.currentIndex));

               try {
                  this.settings.saveSettings();
               } catch (IOException var4) {
                  var4.printStackTrace();
               }
            };
            return new InfoDisplayCycleButton(currentStateIndex, this.x, this.y, this.w, this.h, this.valueNames.get(currentStateIndex), action);
         } else {
            throw new IllegalStateException();
         }
      }

      public static <T> InfoDisplayCycleButton.Builder<T> begin() {
         return new InfoDisplayCycleButton.Builder<T>().setDefault();
      }
   }
}
