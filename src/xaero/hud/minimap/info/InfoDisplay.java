package xaero.hud.minimap.info;

import java.util.function.Consumer;
import net.minecraft.class_2561;
import net.minecraft.class_339;
import xaero.hud.minimap.info.codec.InfoDisplayStateCodec;
import xaero.hud.minimap.info.render.compile.InfoDisplayOnCompile;
import xaero.hud.minimap.info.widget.InfoDisplayWidgetFactory;

public abstract class InfoDisplay<T> {
   private final String id;
   private final class_2561 name;
   private int backgroundColor;
   private int textColor;
   private final T defaultState;
   private T state;
   private final InfoDisplayStateCodec<T> codec;
   private final InfoDisplayWidgetFactory<T> widgetFactory;
   private final InfoDisplayOnCompile<T> compiler;

   protected InfoDisplay(
      String id, class_2561 name, T defaultState, InfoDisplayStateCodec<T> codec, InfoDisplayWidgetFactory<T> widgetFactory, InfoDisplayOnCompile<T> compiler
   ) {
      this(id, name, defaultState, codec, widgetFactory, compiler, null);
   }

   protected InfoDisplay(
      String id,
      class_2561 name,
      T defaultState,
      InfoDisplayStateCodec<T> codec,
      InfoDisplayWidgetFactory<T> widgetFactory,
      InfoDisplayOnCompile<T> compiler,
      Consumer<InfoDisplay<?>> destination
   ) {
      this.id = id;
      this.name = name;
      this.defaultState = defaultState;
      this.codec = codec;
      this.widgetFactory = widgetFactory;
      this.compiler = compiler;
      this.reset();
      if (destination != null) {
         destination.accept(this);
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

   public class_339 createWidget(int x, int y, int w, int h) {
      return this.widgetFactory.create(x, y, w, h, this);
   }

   public InfoDisplayOnCompile<T> getCompiler() {
      return this.compiler;
   }

   public static final class Builder<T> {
      private String id;
      private class_2561 name;
      private T defaultState;
      private InfoDisplayStateCodec<T> codec;
      private InfoDisplayWidgetFactory<T> widgetFactory;
      private InfoDisplayOnCompile<T> compiler;
      private Consumer<InfoDisplay<?>> destination;

      private Builder() {
      }

      public InfoDisplay.Builder<T> setDefault() {
         this.setId(null);
         this.setName(null);
         this.setDefaultState(null);
         this.setCodec(null);
         this.setWidgetFactory(null);
         this.setCompiler(null);
         this.setDestination(null);
         return this;
      }

      public InfoDisplay.Builder<T> setId(String id) {
         this.id = id;
         return this;
      }

      public InfoDisplay.Builder<T> setName(class_2561 name) {
         this.name = name;
         return this;
      }

      public InfoDisplay.Builder<T> setDefaultState(T defaultState) {
         this.defaultState = defaultState;
         return this;
      }

      public InfoDisplay.Builder<T> setCodec(InfoDisplayStateCodec<T> codec) {
         this.codec = codec;
         return this;
      }

      public InfoDisplay.Builder<T> setWidgetFactory(InfoDisplayWidgetFactory<T> widgetFactory) {
         this.widgetFactory = widgetFactory;
         return this;
      }

      public InfoDisplay.Builder<T> setCompiler(InfoDisplayOnCompile<T> compiler) {
         this.compiler = compiler;
         return this;
      }

      public InfoDisplay.Builder<T> setDestination(Consumer<InfoDisplay<?>> destination) {
         this.destination = destination;
         return this;
      }

      public InfoDisplay<T> build() {
         if (this.id != null && this.name != null && this.defaultState != null && this.codec != null && this.widgetFactory != null && this.compiler != null) {
            return new xaero.common.minimap.info.InfoDisplay<>(
               this.id, this.name, this.defaultState, this.codec, this.widgetFactory, this.compiler, this.destination
            );
         } else {
            throw new IllegalStateException();
         }
      }

      public static <T> InfoDisplay.Builder<T> begin() {
         return new InfoDisplay.Builder<T>().setDefault();
      }
   }
}
