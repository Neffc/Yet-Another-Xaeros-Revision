package xaero.common.gui.widget;

import net.minecraft.class_437;

public class ButtonWidget extends Widget {
   private String buttonText;
   private int buttonW;
   private int buttonH;

   public ButtonWidget(
      Class<? extends class_437> location,
      float horizontalAnchor,
      float verticalAnchor,
      ClickAction onClick,
      HoverAction onHover,
      int x,
      int y,
      String url,
      String tooltip,
      String buttonText,
      int buttonW,
      int buttonH
   ) {
      super(WidgetType.BUTTON, location, horizontalAnchor, verticalAnchor, onClick, onHover, x, y, url, tooltip);
      this.buttonText = buttonText;
      this.buttonW = buttonW;
      this.buttonH = buttonH;
   }

   public String getButtonText() {
      return this.buttonText;
   }

   @Override
   public int getW() {
      return this.buttonW;
   }

   @Override
   public int getH() {
      return this.buttonH;
   }
}
