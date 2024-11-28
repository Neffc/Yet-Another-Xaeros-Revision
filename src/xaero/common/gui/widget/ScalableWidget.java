package xaero.common.gui.widget;

import net.minecraft.class_437;

public class ScalableWidget extends Widget {
   private double scale;
   private int scaledOffsetX;
   private int scaledOffsetY;
   private boolean noGuiScale;

   public ScalableWidget(
      WidgetType type,
      Class<? extends class_437> location,
      float horizontalAnchor,
      float verticalAnchor,
      ClickAction onClick,
      HoverAction onHover,
      int x,
      int y,
      int scaledOffsetX,
      int scaledOffsetY,
      String url,
      String tooltip,
      boolean noGuiScale,
      double scale
   ) {
      super(type, location, horizontalAnchor, verticalAnchor, onClick, onHover, x, y, url, tooltip);
      this.scale = scale;
      this.scaledOffsetX = scaledOffsetX;
      this.scaledOffsetY = scaledOffsetY;
      this.noGuiScale = noGuiScale;
   }

   public double getScale() {
      return this.scale;
   }

   public int getScaledOffsetX() {
      return this.scaledOffsetX;
   }

   public int getScaledOffsetY() {
      return this.scaledOffsetY;
   }

   public boolean isNoGuiScale() {
      return this.noGuiScale;
   }

   @Override
   public int getBoxX(int width, double guiScale) {
      int originX = this.getX(width);
      double combinedScale = this.getScale() / (this.isNoGuiScale() ? guiScale : 1.0);
      return (int)((double)originX + (double)this.getScaledOffsetX() * combinedScale);
   }

   @Override
   public int getBoxY(int height, double guiScale) {
      int originY = this.getY(height);
      double combinedScale = this.getScale() / (this.isNoGuiScale() ? guiScale : 1.0);
      return (int)((double)originY + (double)this.getScaledOffsetY() * combinedScale);
   }

   @Override
   public int getBoxW(double guiScale) {
      double combinedScale = this.getScale() / (this.isNoGuiScale() ? guiScale : 1.0);
      return (int)((double)this.getW() * combinedScale);
   }

   @Override
   public int getBoxH(double guiScale) {
      double combinedScale = this.getScale() / (this.isNoGuiScale() ? guiScale : 1.0);
      return (int)((double)this.getH() * combinedScale);
   }
}
