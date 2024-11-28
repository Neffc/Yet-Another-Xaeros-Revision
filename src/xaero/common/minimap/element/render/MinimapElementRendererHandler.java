package xaero.common.minimap.element.render;

import java.util.List;
import xaero.common.HudMod;

@Deprecated
public abstract class MinimapElementRendererHandler extends xaero.hud.minimap.element.render.MinimapElementRendererHandler {
   protected MinimapElementRendererHandler(
      HudMod modMain,
      List<xaero.hud.minimap.element.render.MinimapElementRenderer<?, ?>> renderers,
      xaero.hud.minimap.element.render.MinimapElementRenderLocation location,
      int indexLimit
   ) {
      super(modMain, renderers, location, indexLimit);
   }

   @Deprecated
   public void add(MinimapElementRenderer<?, ?> renderer) {
      super.add(renderer);
   }
}
