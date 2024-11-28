package xaero.common.minimap.element.render.map;

import java.util.List;
import xaero.common.HudMod;
import xaero.hud.minimap.element.render.MinimapElementRenderer;

@Deprecated
public final class MinimapElementMapRendererHandler extends xaero.hud.minimap.element.render.map.MinimapElementMapRendererHandler {
   public MinimapElementMapRendererHandler(HudMod modMain, List<MinimapElementRenderer<?, ?>> renderers) {
      super(modMain, renderers);
   }
}
