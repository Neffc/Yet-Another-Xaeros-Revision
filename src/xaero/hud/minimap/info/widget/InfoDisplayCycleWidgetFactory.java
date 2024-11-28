package xaero.hud.minimap.info.widget;

import java.util.List;
import net.minecraft.class_2561;
import net.minecraft.class_339;
import xaero.hud.minimap.info.InfoDisplay;

public class InfoDisplayCycleWidgetFactory<T> implements InfoDisplayWidgetFactory<T> {
   private final List<T> values;
   private final List<class_2561> valueNames;

   public InfoDisplayCycleWidgetFactory(List<T> values, List<class_2561> valueNames) {
      this.values = values;
      this.valueNames = valueNames;
   }

   @Override
   public class_339 create(int x, int y, int w, int h, InfoDisplay<T> infoDisplay) {
      return InfoDisplayCycleButton.Builder.<T>begin().setBounds(x, y, w, h).setInfoDisplay(infoDisplay).setValues(this.values, this.valueNames).build();
   }
}
