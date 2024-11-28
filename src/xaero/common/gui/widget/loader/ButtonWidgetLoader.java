package xaero.common.gui.widget.loader;

import java.util.Map;
import xaero.common.gui.widget.ButtonWidgetBuilder;
import xaero.common.gui.widget.Widget;

public class ButtonWidgetLoader extends WidgetLoader {
   @Override
   public Widget load(Map<String, String> parsedArgs) {
      ButtonWidgetBuilder builder = new ButtonWidgetBuilder();
      this.commonLoad(builder, parsedArgs);
      String button_text = parsedArgs.get("button_text");
      String button_w = parsedArgs.get("button_w");
      String button_h = parsedArgs.get("button_h");
      if (button_text != null) {
         builder.setButtonText(button_text.replace("%semi%", ";"));
      }

      if (button_w != null) {
         builder.setButtonW(Integer.parseInt(button_w));
      }

      if (button_h != null) {
         builder.setButtonH(Integer.parseInt(button_h));
      }

      return builder.validate() ? builder.build() : null;
   }
}
