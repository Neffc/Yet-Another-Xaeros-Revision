package xaero.common.gui.widget;

public abstract class ScalableWidgetBuilder extends WidgetBuilder {
   protected double scale = 1.0;
   protected int scaledOffsetX;
   protected int scaledOffsetY;
   protected boolean noGuiScale;

   public void setScale(double scale) {
      this.scale = scale;
   }

   public void setScaledOffsetX(int scaledOffsetX) {
      this.scaledOffsetX = scaledOffsetX;
   }

   public void setScaledOffsetY(int scaledOffsetY) {
      this.scaledOffsetY = scaledOffsetY;
   }

   public void setNoGuiScale(boolean noGuiScale) {
      this.noGuiScale = noGuiScale;
   }

   @Override
   public boolean validate() {
      return super.validate() && this.scale != 0.0;
   }
}
