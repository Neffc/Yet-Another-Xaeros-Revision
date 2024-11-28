package xaero.common.effect;

import java.util.function.Consumer;

public class EffectsRegister {
   public void registerEffects(Consumer<MinimapStatusEffect> registrar) {
      Effects.init();
      registrar.accept((MinimapStatusEffect)Effects.NO_MINIMAP);
      registrar.accept((MinimapStatusEffect)Effects.NO_MINIMAP_HARMFUL);
      registrar.accept((MinimapStatusEffect)Effects.NO_RADAR);
      registrar.accept((MinimapStatusEffect)Effects.NO_RADAR_HARMFUL);
      registrar.accept((MinimapStatusEffect)Effects.NO_WAYPOINTS);
      registrar.accept((MinimapStatusEffect)Effects.NO_WAYPOINTS_HARMFUL);
      registrar.accept((MinimapStatusEffect)Effects.NO_CAVE_MAPS);
      registrar.accept((MinimapStatusEffect)Effects.NO_CAVE_MAPS_HARMFUL);
   }
}
