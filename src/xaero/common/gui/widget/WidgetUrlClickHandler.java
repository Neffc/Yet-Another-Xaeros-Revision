package xaero.common.gui.widget;

import java.net.URI;
import java.net.URISyntaxException;
import net.minecraft.class_156;
import net.minecraft.class_310;
import net.minecraft.class_407;
import net.minecraft.class_437;
import xaero.hud.minimap.MinimapLogs;

public class WidgetUrlClickHandler implements WidgetClickHandler {
   private class_437 clickedScreen;
   private String clickedURL;

   @Override
   public void onClick(class_437 screen, Widget widget) {
      this.clickedScreen = screen;
      this.clickedURL = widget.getUrl();
      class_310.method_1551().method_1507(new class_407(this::confirmLink, this.clickedURL, true));
   }

   private void confirmLink(boolean confirmed) {
      if (confirmed) {
         try {
            class_156.method_668().method_673(new URI(this.clickedURL));
         } catch (URISyntaxException var3) {
            MinimapLogs.LOGGER.error("suppressed exception", var3);
         }
      }

      class_310.method_1551().method_1507(this.clickedScreen);
   }
}
