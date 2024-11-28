package xaero.common.interfaces;

import java.io.IOException;
import xaero.common.IXaeroMinimap;
import xaero.common.minimap.MinimapInterface;

@Deprecated
public class InterfaceManager {
   private IXaeroMinimap modMain;

   public InterfaceManager(IXaeroMinimap modMain) throws IOException {
      this.modMain = modMain;
   }

   public MinimapInterface getMinimapInterface() {
      return (MinimapInterface)this.modMain.getMinimap();
   }
}
