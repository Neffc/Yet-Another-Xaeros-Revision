package xaero.minimap.interfaces;

import java.io.IOException;
import xaero.common.AXaeroMinimap;
import xaero.common.interfaces.IInterfaceLoader;
import xaero.common.interfaces.Interface;
import xaero.common.interfaces.InterfaceManager;
import xaero.common.interfaces.Preset;
import xaero.common.minimap.MinimapInterface;
import xaero.common.settings.ModOptions;

public class MinimapInterfaceLoader implements IInterfaceLoader {
   @Override
   public void loadPresets(InterfaceManager interfaces) {
      interfaces.addPreset(
         new Preset(
            "gui.xaero_preset_topleft",
            new int[][]{{0, 0}, {0, 10000}, {0, 0}, {0, 36}, {0, 0}},
            new boolean[][]{{true, false, false}, {true, false, false}, {false, true, false}, {true, false, false}, {false, false, false}}
         )
      );
      interfaces.addPreset(
         new Preset(
            "gui.xaero_preset_topright",
            new int[][]{{0, 0}, {0, 135}, {120, 0}, {0, 50}, {0, 0}},
            new boolean[][]{{false, true, false}, {false, false, false}, {true, false, false}, {true, false, false}, {false, true, false}}
         )
      );
      interfaces.addPreset(
         new Preset(
            "gui.xaero_preset_bottom_left",
            new int[][]{{0, 0}, {0, 135}, {120, 0}, {0, 50}, {0, 10000}},
            new boolean[][]{{false, true, false}, {false, false, false}, {true, false, false}, {true, false, false}, {false, false, false}}
         )
      );
      interfaces.addPreset(
         new Preset(
            "gui.xaero_preset_bottom_right",
            new int[][]{{0, 0}, {0, 135}, {120, 0}, {0, 50}, {0, 10000}},
            new boolean[][]{{false, true, false}, {false, false, false}, {true, false, false}, {true, false, false}, {false, true, false}}
         )
      );
   }

   @Override
   public void load(AXaeroMinimap modMain, InterfaceManager interfaces) throws IOException {
      for (int i = 0; i < 4; i++) {
         interfaces.add(new Interface(interfaces, "dummy", interfaces.getNextId(), 0, 0, ModOptions.DEFAULT) {
         });
      }

      interfaces.add(new MinimapInterface(modMain, interfaces.getNextId(), interfaces));
   }
}
