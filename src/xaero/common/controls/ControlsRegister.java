package xaero.common.controls;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.List;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.class_304;
import xaero.common.settings.ModSettings;

public class ControlsRegister {
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
         ModSettings.keyToggleTrackedPlayers,
         ModSettings.keyTogglePacChunkClaims
      }
   );
   public final List<class_304> vanillaKeyBindings = new ArrayList<>();

   public ControlsRegister() {
      this.registerKeybindings();
   }

   protected void registerKeybindings() {
      for (class_304 kb : this.keybindings) {
         KeyBindingHelper.registerKeyBinding(kb);
      }
   }
}
