package xaero.common.mixin;

import net.minecraft.class_2535;
import net.minecraft.class_3218;
import net.minecraft.class_3222;
import net.minecraft.class_3324;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xaero.common.HudMod;
import xaero.common.server.core.XaeroMinimapServerCore;

@Mixin({class_3324.class})
public class MixinPlayerList {
   @Inject(
      at = {@At("TAIL")},
      method = {"placeNewPlayer"}
   )
   public void onPlaceNewPlayer(class_2535 connection, class_3222 serverPlayer, CallbackInfo info) {
      if (XaeroMinimapServerCore.isModLoaded()) {
         HudMod.INSTANCE.getCommonEvents().onPlayerLogIn(serverPlayer);
      }
   }

   @Inject(
      at = {@At("HEAD")},
      method = {"sendLevelInfo"}
   )
   public void onSendWorldInfo(class_3222 player, class_3218 world, CallbackInfo info) {
      XaeroMinimapServerCore.onServerWorldInfo(player);
   }
}
