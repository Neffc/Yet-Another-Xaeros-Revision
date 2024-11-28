package xaero.common;

public abstract class HudClientOnlyBase {
   public void preInit(String modId, IXaeroMinimap modMain) {
      modMain.getPlatformContext().getLoaderClientOnly().preInit(modId, modMain);
   }
}
