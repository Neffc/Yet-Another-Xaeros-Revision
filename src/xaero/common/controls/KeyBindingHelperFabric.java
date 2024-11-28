package xaero.common.controls;

import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.class_304;
import net.minecraft.class_3675.class_306;
import xaero.common.HudMod;
import xaero.common.mods.SupportModsFabric;

public class KeyBindingHelperFabric implements IKeyBindingHelper {
   @Override
   public class_306 getBoundKeyOf(class_304 kb) {
      return KeyBindingHelper.getBoundKeyOf(kb);
   }

   @Override
   public boolean modifiersAreActive(class_304 kb, int keyConflictContext) {
      return !((SupportModsFabric)HudMod.INSTANCE.getSupportMods()).amecs()
         || ((SupportModsFabric)HudMod.INSTANCE.getSupportMods()).amecs.modifiersArePressed(kb);
   }
}
