package xaero.common.minimap.info;

import java.util.List;
import java.util.function.Consumer;
import net.minecraft.class_1041;
import net.minecraft.class_2561;
import net.minecraft.class_310;
import xaero.common.HudMod;
import xaero.common.XaeroMinimapSession;
import xaero.common.minimap.info.codec.InfoDisplayStateCodec;
import xaero.common.minimap.info.render.compile.InfoDisplayCompiler;
import xaero.common.minimap.info.render.compile.InfoDisplayOnCompile;
import xaero.common.minimap.info.widget.InfoDisplayWidgetFactory;
import xaero.hud.minimap.BuiltInHudModules;

@Deprecated
public class InfoDisplay<T> extends xaero.hud.minimap.info.InfoDisplay<T> {
   private final InfoDisplayWidgetFactory<T> oldWidgetFactory;
   private final InfoDisplayOnCompile<T> oldOnCompile;
   private final InfoDisplayStateCodec<T> oldCodec;

   @Deprecated
   public InfoDisplay(
      String id,
      class_2561 name,
      T defaultState,
      InfoDisplayStateCodec<T> codec,
      InfoDisplayWidgetFactory<T> widgetFactory,
      InfoDisplayOnCompile<T> onCompile,
      List<InfoDisplay<?>> destination
   ) {
      super(id, name, defaultState, codec, convertWidgetFactory(widgetFactory), convertOnCompile(onCompile));
      this.oldWidgetFactory = widgetFactory;
      this.oldOnCompile = onCompile;
      this.oldCodec = codec;
      if (destination != null) {
         destination.add(this);
      }
   }

   @Deprecated
   public InfoDisplay(
      String id,
      class_2561 name,
      T defaultState,
      xaero.hud.minimap.info.codec.InfoDisplayStateCodec<T> codec,
      xaero.hud.minimap.info.widget.InfoDisplayWidgetFactory<T> widgetFactory,
      xaero.hud.minimap.info.render.compile.InfoDisplayOnCompile<T> onCompile,
      Consumer<xaero.hud.minimap.info.InfoDisplay<?>> destination
   ) {
      super(id, name, defaultState, codec, widgetFactory, onCompile, destination);
      this.oldWidgetFactory = unconvertWidgetFactory(widgetFactory);
      this.oldOnCompile = unconvertOnCompile(onCompile);
      this.oldCodec = new InfoDisplayStateCodec<>(codec::decode, codec::encode);
   }

   private static <T> xaero.hud.minimap.info.widget.InfoDisplayWidgetFactory<T> convertWidgetFactory(InfoDisplayWidgetFactory<T> oldFactory) {
      return (x, y, w, h, infoDisplay) -> oldFactory.create(x, y, w, h, (InfoDisplay<T>)infoDisplay, HudMod.INSTANCE.getSettings());
   }

   private static <T> xaero.hud.minimap.info.render.compile.InfoDisplayOnCompile<T> convertOnCompile(InfoDisplayOnCompile<T> old) {
      return (infoDisplay, compiler, session, availableWidth, playerPos) -> {
         XaeroMinimapSession minimapSession = XaeroMinimapSession.getCurrentSession();
         class_1041 window = class_310.method_1551().method_22683();
         int screenWidth = window.method_4486();
         int screenHeight = window.method_4502();
         double screenScale = window.method_4495();
         old.onCompile(
            (InfoDisplay<T>)infoDisplay,
            (InfoDisplayCompiler)compiler,
            minimapSession,
            session.getProcessor(),
            0,
            0,
            screenWidth,
            screenHeight,
            screenScale,
            availableWidth,
            playerPos.method_10263(),
            playerPos.method_10264(),
            playerPos.method_10260(),
            playerPos
         );
      };
   }

   private static <T> InfoDisplayWidgetFactory<T> unconvertWidgetFactory(xaero.hud.minimap.info.widget.InfoDisplayWidgetFactory<T> newFactory) {
      return (x, y, w, h, infoDisplay, settings) -> newFactory.create(x, y, w, h, infoDisplay);
   }

   private static <T> InfoDisplayOnCompile<T> unconvertOnCompile(xaero.hud.minimap.info.render.compile.InfoDisplayOnCompile<T> newOnCompile) {
      return (infoDisplay, compiler, minimapSession, minimap, x, y, width, height, scale, size, playerBlockX, playerBlockY, playerBlockZ, playerPos) -> newOnCompile.onCompile(
            infoDisplay, compiler, minimapSession.getSession(BuiltInHudModules.MINIMAP), size, playerPos
         );
   }

   @Deprecated
   @Override
   public void reset() {
      super.reset();
   }

   @Deprecated
   @Override
   public String getId() {
      return super.getId();
   }

   @Deprecated
   @Override
   public class_2561 getName() {
      return super.getName();
   }

   @Deprecated
   @Override
   public int getBackgroundColor() {
      return super.getBackgroundColor();
   }

   @Deprecated
   @Override
   public int getTextColor() {
      return super.getTextColor();
   }

   @Deprecated
   @Override
   public T getDefaultState() {
      return super.getDefaultState();
   }

   @Deprecated
   @Override
   public T getState() {
      return super.getState();
   }

   @Deprecated
   public InfoDisplayStateCodec<T> getCodec() {
      return this.oldCodec;
   }

   @Deprecated
   @Override
   public void setBackgroundColor(int backgroundColor) {
      super.setBackgroundColor(backgroundColor);
   }

   @Deprecated
   @Override
   public void setTextColor(int textColor) {
      super.setTextColor(textColor);
   }

   @Deprecated
   @Override
   public void setState(T state) {
      super.setState(state);
   }

   @Deprecated
   public InfoDisplayWidgetFactory<T> getWidgetFactory() {
      return this.oldWidgetFactory;
   }

   @Deprecated
   public InfoDisplayOnCompile<T> getOnCompile() {
      return this.oldOnCompile;
   }
}
