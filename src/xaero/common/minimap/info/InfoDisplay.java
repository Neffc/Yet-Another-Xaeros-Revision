package xaero.common.minimap.info;

import java.util.List;
import net.minecraft.class_2561;
import xaero.common.minimap.info.codec.InfoDisplayStateCodec;
import xaero.common.minimap.info.render.compile.InfoDisplayOnCompile;
import xaero.common.minimap.info.widget.InfoDisplayWidgetFactory;

public class InfoDisplay<T> {
   private final String id;
   private final class_2561 name;
   private int backgroundColor;
   private int textColor;
   private final T defaultState;
   private T state;
   private final InfoDisplayStateCodec<T> codec;
   private final InfoDisplayWidgetFactory<T> widgetFactory;
   private final InfoDisplayOnCompile<T> onCompile;

   public InfoDisplay(
      String id,
      class_2561 name,
      T defaultState,
      InfoDisplayStateCodec<T> codec,
      InfoDisplayWidgetFactory<T> widgetFactory,
      InfoDisplayOnCompile<T> onCompile,
      List<InfoDisplay<?>> destination
   ) {
      this.id = id;
      this.name = name;
      this.defaultState = defaultState;
      this.codec = codec;
      this.widgetFactory = widgetFactory;
      this.onCompile = onCompile;
      this.reset();
      if (destination != null) {
         destination.add(this);
      }
   }

   public void reset() {
      this.state = this.defaultState;
      this.textColor = 15;
      this.backgroundColor = -1;
   }

   public String getId() {
      return this.id;
   }

   public class_2561 getName() {
      return this.name;
   }

   public int getBackgroundColor() {
      return this.backgroundColor;
   }

   public int getTextColor() {
      return this.textColor;
   }

   public T getDefaultState() {
      return this.defaultState;
   }

   public T getState() {
      return this.state;
   }

   public InfoDisplayStateCodec<T> getCodec() {
      return this.codec;
   }

   public void setBackgroundColor(int backgroundColor) {
      this.backgroundColor = backgroundColor;
   }

   public void setTextColor(int textColor) {
      this.textColor = textColor;
   }

   public void setState(T state) {
      this.state = state;
   }

   public InfoDisplayWidgetFactory<T> getWidgetFactory() {
      return this.widgetFactory;
   }

   public InfoDisplayOnCompile<T> getOnCompile() {
      return this.onCompile;
   }
}
