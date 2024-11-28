package xaero.common.effect;

import net.minecraft.class_2378;
import net.minecraft.class_7923;

public class EffectsRegister {
   public void registerEffects() {
      Effects.init();
      this.registerEffect((MinimapStatusEffect)Effects.NO_MINIMAP);
      this.registerEffect((MinimapStatusEffect)Effects.NO_MINIMAP_HARMFUL);
      this.registerEffect((MinimapStatusEffect)Effects.NO_RADAR);
      this.registerEffect((MinimapStatusEffect)Effects.NO_RADAR_HARMFUL);
      this.registerEffect((MinimapStatusEffect)Effects.NO_WAYPOINTS);
      this.registerEffect((MinimapStatusEffect)Effects.NO_WAYPOINTS_HARMFUL);
      this.registerEffect((MinimapStatusEffect)Effects.NO_CAVE_MAPS);
      this.registerEffect((MinimapStatusEffect)Effects.NO_CAVE_MAPS_HARMFUL);
   }

   private void registerEffect(MinimapStatusEffect effect) {
      class_2378.method_10230(class_7923.field_41174, effect.getRegistryName(), effect);
   }
}
