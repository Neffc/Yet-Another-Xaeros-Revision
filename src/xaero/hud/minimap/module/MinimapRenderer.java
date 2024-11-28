package xaero.hud.minimap.module;

import net.minecraft.class_310;
import net.minecraft.class_332;
import net.minecraft.class_408;
import net.minecraft.class_418;
import xaero.common.HudMod;
import xaero.common.effect.Effects;
import xaero.common.gui.IScreenBase;
import xaero.common.minimap.render.MinimapRendererHelper;
import xaero.common.misc.Misc;
import xaero.hud.render.module.IModuleRenderer;
import xaero.hud.render.module.ModuleRenderContext;

public class MinimapRenderer implements IModuleRenderer<MinimapSession> {
   public void render(MinimapSession session, ModuleRenderContext c, class_332 guiGraphics, float partialTicks) {
      class_310 mc = class_310.method_1551();
      if (!Misc.hasEffect(mc.field_1724, Effects.NO_MINIMAP)
         && !Misc.hasEffect(mc.field_1724, Effects.NO_MINIMAP_HARMFUL)
         && !session.getProcessor().getNoMinimapMessageReceived()) {
         if ((
               !session.getHideMinimapUnderScreen()
                  || mc.field_1755 == null
                  || mc.field_1755 instanceof IScreenBase
                  || mc.field_1755 instanceof class_408
                  || mc.field_1755 instanceof class_418
            )
            && (!session.getHideMinimapUnderF3() || !mc.field_1690.field_1866)) {
            MinimapRendererHelper.restoreDefaultShaderBlendState();
            session.getProcessor()
               .onRender(
                  guiGraphics,
                  c.x,
                  c.y,
                  c.screenWidth,
                  c.screenHeight,
                  c.screenScale,
                  session.getConfiguredWidth(),
                  c.w,
                  partialTicks,
                  HudMod.INSTANCE.getHudRenderer().getCustomVertexConsumers()
               );
            MinimapRendererHelper.restoreDefaultShaderBlendState();
         }
      }
   }
}
