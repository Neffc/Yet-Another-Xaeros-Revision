package xaero.common.controls;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import net.minecraft.class_304;
import xaero.common.settings.ModSettings;

public abstract class ControlsRegister {
   public final List<class_304> keybindings = Lists.newArrayList(
      new class_304[]{
         ModSettings.keySwitchSet,
         ModSettings.keyInstantWaypoint,
         ModSettings.keyToggleSlimes,
         ModSettings.keyToggleGrid,
         ModSettings.keyToggleWaypoints,
         ModSettings.keyToggleMapWaypoints,
         ModSettings.keyToggleMap,
         ModSettings.keyLargeMap,
         ModSettings.keyWaypoints,
         ModSettings.keyBindZoom,
         ModSettings.keyBindZoom1,
         ModSettings.newWaypoint,
         ModSettings.keyAllSets,
         ModSettings.keyLightOverlay,
         ModSettings.keyToggleRadar,
         ModSettings.keyReverseEntityRadar,
         ModSettings.keyManualCaveMode,
         ModSettings.keyAlternativeListPlayers,
         ModSettings.keyToggleTrackedPlayersOnMap,
         ModSettings.keyToggleTrackedPlayersInWorld,
         ModSettings.keyTogglePacChunkClaims
      }
   );
   public final List<class_304> vanillaKeyBindings = new ArrayList<>();

   public void registerKeybindings(Consumer<class_304> registry) {
      for (class_304 kb : this.keybindings) {
         registry.accept(kb);
      }
   }

   public abstract void onStage2();
}
