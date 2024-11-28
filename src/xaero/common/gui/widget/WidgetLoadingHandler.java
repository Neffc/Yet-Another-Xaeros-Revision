package xaero.common.gui.widget;

import java.util.HashMap;
import java.util.Map;
import xaero.common.gui.widget.loader.WidgetLoader;
import xaero.common.patreon.Patreon;
import xaero.hud.minimap.MinimapLogs;

public class WidgetLoadingHandler {
   private static int CURRENT_VERSION = 1;
   private WidgetScreenHandler handler;

   public WidgetLoadingHandler(WidgetScreenHandler destination) {
      this.handler = destination;
   }

   public void loadWidget(String serialized) {
      Widget widget = null;
      String[] args = serialized.split(";");
      Map<String, String> parsedArgs = new HashMap<>();

      for (String arg : args) {
         int splitIndex = arg.indexOf(58);
         if (splitIndex != -1) {
            String parameter = arg.substring(0, splitIndex);
            String value = arg.substring(splitIndex + 1);
            parsedArgs.put(parameter, value);
         }
      }

      try {
         String min_version = parsedArgs.remove("min_version");
         String max_version = parsedArgs.remove("max_version");
         if (min_version != null && CURRENT_VERSION < Integer.parseInt(min_version) || max_version != null && CURRENT_VERSION > Integer.parseInt(max_version)) {
            return;
         }

         String min_patronage = parsedArgs.remove("min_patronage");
         String max_patronage = parsedArgs.remove("max_patronage");
         if (min_patronage != null && Patreon.getOnlineWidgetLevel() < Integer.parseInt(min_patronage)
            || max_patronage != null && Patreon.getOnlineWidgetLevel() > Integer.parseInt(max_patronage)) {
            return;
         }

         WidgetType type = WidgetType.valueOf(parsedArgs.remove("type"));
         WidgetLoader loader = type.widgetLoader;
         widget = loader.load(parsedArgs);
         this.handler.addWidget(widget);
      } catch (Throwable var12) {
         MinimapLogs.LOGGER.error("suppressed exception", var12);
      }
   }
}
