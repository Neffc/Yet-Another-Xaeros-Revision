package xaero.common.mods.pac.party;

import java.util.UUID;
import net.minecraft.class_2960;
import xaero.common.minimap.radar.tracker.system.ITrackedPlayerReader;
import xaero.pac.common.parties.party.api.IPartyMemberDynamicInfoSyncableAPI;

public class OPACTrackedPlayerReader implements ITrackedPlayerReader<IPartyMemberDynamicInfoSyncableAPI> {
   public UUID getId(IPartyMemberDynamicInfoSyncableAPI player) {
      return player.getPlayerId();
   }

   public double getX(IPartyMemberDynamicInfoSyncableAPI player) {
      return player.getX();
   }

   public double getY(IPartyMemberDynamicInfoSyncableAPI player) {
      return player.getY();
   }

   public double getZ(IPartyMemberDynamicInfoSyncableAPI player) {
      return player.getZ();
   }

   public class_2960 getDimension(IPartyMemberDynamicInfoSyncableAPI player) {
      return player.getDimension();
   }
}
