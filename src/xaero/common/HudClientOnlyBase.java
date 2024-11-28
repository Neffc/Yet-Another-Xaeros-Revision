package xaero.common;

import xaero.hud.Hud;
import xaero.hud.io.HudIO;
import xaero.hud.minimap.BuiltInHudModules;
import xaero.hud.pushbox.BuiltInPushBoxes;
import xaero.hud.pushbox.PushboxHandler;
import xaero.hud.render.HudRenderer;

public abstract class HudClientOnlyBase {
   public void preInit(String modId, HudMod modMain) {
      modMain.hud = Hud.Builder.begin().build();
      modMain.hudRenderer = new HudRenderer(new PushboxHandler());
      modMain.hudIO = HudIO.Builder.begin().setHud(modMain.hud).build();
      BuiltInPushBoxes.addAll(modMain.hud.getPushboxManager());
      modMain.getPlatformContext().getLoaderClientOnly().preInit(modId, modMain);
   }

   public void preLoadLater(HudMod modMain) {
      BuiltInHudModules.addAll(modMain.getHud().getModuleManager());
   }
}
