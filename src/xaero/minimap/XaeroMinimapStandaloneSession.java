package xaero.minimap;

import java.io.IOException;
import net.minecraft.class_634;
import xaero.common.HudMod;
import xaero.common.XaeroMinimapSession;
import xaero.minimap.controls.MinimapControlsHandler;

public class XaeroMinimapStandaloneSession extends XaeroMinimapSession {
   public XaeroMinimapStandaloneSession(HudMod modMain) {
      super(modMain);
   }

   @Override
   public void init(class_634 connection) throws IOException {
      super.init(connection);
      this.controls = new MinimapControlsHandler(this.modMain, this);
   }
}
