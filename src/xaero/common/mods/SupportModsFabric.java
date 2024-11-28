package xaero.common.mods;

import xaero.common.IXaeroMinimap;
import xaero.hud.minimap.MinimapLogs;

public class SupportModsFabric extends SupportMods {
   public SupportAmecs amecs = null;

   public SupportModsFabric(IXaeroMinimap modMain) {
      super(modMain);

      try {
         Class mmClassTest = Class.forName("de.siphalor.amecs.api.KeyModifiers");
         this.amecs = new SupportAmecs(MinimapLogs.LOGGER);
      } catch (ClassNotFoundException var3) {
      }
   }

   public boolean amecs() {
      return this.amecs != null;
   }
}
