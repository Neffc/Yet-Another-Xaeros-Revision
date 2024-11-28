package xaero.common.mods.pac.party;

import java.util.Iterator;
import xaero.common.minimap.radar.tracker.system.IPlayerTrackerSystem;
import xaero.common.minimap.radar.tracker.system.ITrackedPlayerReader;
import xaero.common.mods.pac.SupportOpenPartiesAndClaims;
import xaero.pac.common.parties.party.api.IPartyMemberDynamicInfoSyncableAPI;

public class OPACPlayerTrackerSystem implements IPlayerTrackerSystem<IPartyMemberDynamicInfoSyncableAPI> {
   private final SupportOpenPartiesAndClaims opac;
   private final OPACTrackedPlayerReader reader;

   public OPACPlayerTrackerSystem(SupportOpenPartiesAndClaims opac) {
      this.opac = opac;
      this.reader = new OPACTrackedPlayerReader();
   }

   @Override
   public ITrackedPlayerReader<IPartyMemberDynamicInfoSyncableAPI> getReader() {
      return this.reader;
   }

   @Override
   public Iterator<IPartyMemberDynamicInfoSyncableAPI> getTrackedPlayerIterator() {
      return this.opac.getAllyIterator();
   }
}
