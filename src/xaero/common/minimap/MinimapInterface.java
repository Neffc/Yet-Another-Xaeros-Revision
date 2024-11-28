package xaero.common.minimap;

import java.io.IOException;
import xaero.common.IXaeroMinimap;
import xaero.hud.minimap.Minimap;

@Deprecated
public class MinimapInterface extends Minimap {
   public MinimapInterface(IXaeroMinimap modMain) throws IOException {
      super(modMain);
   }
}
