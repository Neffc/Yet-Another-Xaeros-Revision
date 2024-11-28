package xaero.common.minimap.element.render.over;

import java.util.List;
import xaero.common.HudMod;
import xaero.hud.minimap.element.render.MinimapElementRenderer;

@Deprecated
public class MinimapElementOverMapRendererHandler extends xaero.hud.minimap.element.render.over.MinimapElementOverMapRendererHandler {
   public MinimapElementOverMapRendererHandler(HudMod modMain, List<MinimapElementRenderer<?, ?>> renderers, double[] partialTranslate) {
      super(modMain, renderers, partialTranslate);
   }
}
