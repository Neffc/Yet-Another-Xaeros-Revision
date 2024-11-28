package xaero.common.gui.widget.loader;

import java.io.IOException;
import java.util.Map;
import xaero.common.gui.widget.Alignment;
import xaero.common.gui.widget.TextWidgetBuilder;
import xaero.common.gui.widget.Widget;

public class TextWidgetLoader extends ScalableWidgetLoader {
   @Override
   public Widget load(Map<String, String> parsedArgs) throws IOException {
      TextWidgetBuilder builder = new TextWidgetBuilder();
      this.commonLoad(builder, parsedArgs);
      String text = parsedArgs.get("text");
      String alignment = parsedArgs.get("alignment");
      if (text != null) {
         builder.setText(text.replace("%semi%", ";"));
      }

      if (alignment != null) {
         builder.setAlignment(Alignment.valueOf(alignment));
      }

      return builder.validate() ? builder.build() : null;
   }
}
