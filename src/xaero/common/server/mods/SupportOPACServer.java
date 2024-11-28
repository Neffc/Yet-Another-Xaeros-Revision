package xaero.common.server.mods;

import net.minecraft.class_3222;
import xaero.pac.common.server.api.OpenPACServerAPI;
import xaero.pac.common.server.player.config.api.IPlayerConfigAPI;
import xaero.pac.common.server.player.config.api.IPlayerConfigManagerAPI;
import xaero.pac.common.server.player.config.api.PlayerConfigOptions;

public class SupportOPACServer {
   public boolean isPositionSyncAllowed(int relationship, class_3222 toPlayer, class_3222 fromPlayer) {
      if (relationship <= 0) {
         return false;
      } else {
         IPlayerConfigManagerAPI configAPI = OpenPACServerAPI.get(toPlayer.method_5682()).getPlayerConfigs();
         IPlayerConfigAPI fromPlayerConfig = configAPI.getLoadedConfig(fromPlayer.method_5667());
         if (relationship == 1 && !(Boolean)fromPlayerConfig.getEffective(PlayerConfigOptions.SHARE_LOCATION_WITH_PARTY_MUTUAL_ALLIES)) {
            return false;
         } else if (relationship > 1 && !(Boolean)fromPlayerConfig.getEffective(PlayerConfigOptions.SHARE_LOCATION_WITH_PARTY)) {
            return false;
         } else {
            IPlayerConfigAPI toPlayerConfig = configAPI.getLoadedConfig(toPlayer.method_5667());
            return relationship == 1 && !toPlayerConfig.getEffective(PlayerConfigOptions.RECEIVE_LOCATIONS_FROM_PARTY_MUTUAL_ALLIES)
               ? false
               : relationship <= 1 || (Boolean)toPlayerConfig.getEffective(PlayerConfigOptions.RECEIVE_LOCATIONS_FROM_PARTY);
         }
      }
   }
}
