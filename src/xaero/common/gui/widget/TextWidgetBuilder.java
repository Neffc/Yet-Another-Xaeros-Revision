package xaero.common.gui.widget;

public class TextWidgetBuilder extends ScalableWidgetBuilder {
   private String text;
   private Alignment alignment = Alignment.LEFT;

   public void setText(String text) {
      this.text = text;
   }

   public void setAlignment(Alignment alignment) {
      this.alignment = alignment;
   }

   @Override
   public boolean validate() {
      return super.validate() && this.text != null && this.alignment != null;
   }

   @Override
   public Widget build() {
      return new TextWidget(
         this.location,
         this.horizontalAnchor,
         this.verticalAnchor,
         this.onClick,
         this.onHover,
         this.x,
         this.y,
         this.scaledOffsetX,
         this.scaledOffsetY,
         this.url,
         this.tooltip,
         this.text,
         this.alignment,
         this.noGuiScale,
         this.scale
      );
   }
}
