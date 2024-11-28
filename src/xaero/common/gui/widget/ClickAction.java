package xaero.common.gui.widget;

public enum ClickAction {
   NOTHING(null),
   URL(new WidgetUrlClickHandler());

   public final WidgetClickHandler clickHandler;

   private ClickAction(WidgetClickHandler clickHandler) {
      this.clickHandler = clickHandler;
   }
}
